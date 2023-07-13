package com.mk.swordfish.ports.primary.rs;


import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.mk.swordfish.core.exceptions.BusinessErrorException;
import com.mk.swordfish.core.exceptions.ResourceNotFoundException;
import java.net.URL;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.util.CollectionUtils;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.API.Match;
import static io.vavr.Predicates.instanceOf;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class RsControllerAdvice {

  public static final String HTTP_MESSAGE_NOT_READABLE = "http.message.not.readable.exception";
  public static final String INVALID_FORMAT = "invalid.format.exception";
  public static final String JSON_MAPPING = "json.mapping.exception";
  public static final String JSON_PARSE = "json.parse.exception";
  public static final String HTTP_MEDIA_TYPE_NOT_SUPPORTED
      = "http.media.type.not.supported.exception";
  public static final String CONSTRAINT_VIOLATION = "constraint.violation.exception";
  public static final String METHOD_ARGUMENT_NOT_VALID = "method.argument.not.valid.exception";

  private final MessageSource messageSource;

  record LocationResponse(String field, String error) {

  }

  @Builder
  record ErrorResponse(OffsetDateTime timestamp, int status, String error, String message,
                       String path,
                       List<LocationResponse> details, String type) {

  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  ErrorResponse onHttpMessageNotReadableException(HttpMessageNotReadableException e,
      HttpServletRequest request) {
    String message = Match(e.getCause()).of(
        Case($(instanceOf(HttpMessageNotReadableException.class)),
            cause -> getLocalizedMessage(HTTP_MESSAGE_NOT_READABLE)),
        // Eg. Could not parse a date
        Case($(instanceOf(InvalidFormatException.class)),
            (Function<? super InvalidFormatException, String>)
                cause -> getLocalizedMessage(INVALID_FORMAT, getPath(cause.getPath()),
                    cause.getLocation().getLineNr(), cause.getLocation().getColumnNr())),
        // Error in body missing , or ' instead "
        Case($(instanceOf(JsonMappingException.class)),
            (Function<? super JsonMappingException, String>) cause ->
                getLocalizedMessage(JSON_MAPPING, getPath(cause.getPath()),
                    cause.getLocation().getLineNr(), cause.getLocation().getColumnNr())
        ),
        //Error  or ' instead "
        Case($(instanceOf(JsonParseException.class)),
            (Function<? super JsonParseException, String>) cause ->
                getLocalizedMessage(JSON_PARSE, cause.getLocation().getLineNr(),
                    cause.getLocation().getColumnNr())
        ),
        Case($(), cause -> getLocalizedMessage(HTTP_MESSAGE_NOT_READABLE)));
    if (e.getCause() != null) {
      String errorName = e.getCause().getClass().getName();
      String errorMessage = e.getCause().getMessage();
      log.info("HttpMessageNotReadableException cause:[{}] message:[{}]", errorName, errorMessage);
    }

    return defaultErrorResponseBuilder(request, HttpStatus.BAD_REQUEST)
        .message(message)
        .build();
  }

  private String getPath(List<JsonMappingException.Reference> references) {
    if (CollectionUtils.isEmpty(references)) {
      return "";
    } else {
      return references.stream().map(ref ->
          ref.getIndex() != -1 ? "[" + ref.getIndex() + "]" : "." + ref.getFieldName()
      ).collect(Collectors.joining()).substring(1);
    }
  }

  @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  ErrorResponse onHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException e,
      HttpServletRequest request) {
    return defaultErrorResponseBuilder(request, HttpStatus.BAD_REQUEST)
        .message(getLocalizedMessage(HTTP_MEDIA_TYPE_NOT_SUPPORTED))
        .build();
  }

  @ExceptionHandler(ConstraintViolationException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  ErrorResponse onConstraintValidationException(ConstraintViolationException e,
      HttpServletRequest request) {
    // On validating requests with @Valid
    List<LocationResponse> details = e.getConstraintViolations().stream()
        .map(violation -> new LocationResponse(violation.getPropertyPath().toString(),
            violation.getMessage()))
        .collect(Collectors.toList());

    return defaultErrorResponseBuilder(request, HttpStatus.BAD_REQUEST)
        .message(getLocalizedMessage(CONSTRAINT_VIOLATION))
        .details(details)
        .build();
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  ErrorResponse onMethodArgumentNotValidException(MethodArgumentNotValidException e,
      HttpServletRequest request) {
    // On validating methods with @Validate
    List<LocationResponse> details = e.getBindingResult().getFieldErrors().stream()
        .map(fieldError -> new LocationResponse(fieldError.getField(),
            fieldError.getDefaultMessage()))
        .collect(Collectors.toList());

    return defaultErrorResponseBuilder(request, HttpStatus.BAD_REQUEST)
        .message(getLocalizedMessage(METHOD_ARGUMENT_NOT_VALID))
        .details(details)
        .build();
  }

  @ExceptionHandler(ResourceNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  ErrorResponse onResourceNotFoundException(ResourceNotFoundException e,
      HttpServletRequest request) {
    return defaultErrorResponseBuilder(request, HttpStatus.NOT_FOUND)
        .message(getLocalizedMessage(e.getCode()))
        .build();
  }

  @ExceptionHandler(BusinessErrorException.class)
  @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
  ErrorResponse onBusinessErrorException(BusinessErrorException e,
      HttpServletRequest request) {
    return defaultErrorResponseBuilder(request, HttpStatus.UNPROCESSABLE_ENTITY)
        .message(getLocalizedMessage(e.getCode()))
        .type(e.getCode())
        .build();
  }

  ErrorResponse.ErrorResponseBuilder defaultErrorResponseBuilder(HttpServletRequest request,
      HttpStatus status) {
    return ErrorResponse.builder().timestamp(OffsetDateTime.now())
        .path(request.getServletPath())
        .status(status.value())
        .error(status.getReasonPhrase());
  }

  String getLocalizedMessage(String code, Object... args) {
    Locale local = LocaleContextHolder.getLocale();
    return messageSource.getMessage(code, args, local);
  }
}
