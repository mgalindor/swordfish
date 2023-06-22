# swordfish
Is a hexagonal base project design as a template for future projects.


It include
- AOP Transaction Manager
- @Behavior annptation to use instead @Service
- Component Scan looking for @Behavior annotated classes
- Logging Aspect to log a blue print when a method is called and it gets the result
- RestTemplate RequestFactory configuration for
  - URLConnection
  - Apache HTTPClient
  - OkHttp Client
- Configuracion de Jackson Money to handle currency objects at controllers
- Enum validations for Request objects 
- Basic Controller Advice for mos common exceptions

Scafolding example

java/com/mk/swordfish
|  core/
|  |  annotations/
|  |  domain/
|  |  enums/
|  |  exceptions/
|  |  service/
|  ports/
|  |  primary/
|  |  |  rs/
|  |  |  | Controller.java
|  |  |  | Requests.java
|  |  |  | Response.java
|  |  |  |  ControllerAdvice.java
|  |  secondary/
|  |  | jpa/
|  |  | | AdapterJpaImpl.java
|  |  | | JpaRepository.java
|  |  | | Entity.java
|  |  | mongo/
|  |  | redis/
|  |  | rs/
|  |  PortInterface.java
|  spring/
|  |  aspect/
|  |  config/
|  |  properties/
|  Application.java
