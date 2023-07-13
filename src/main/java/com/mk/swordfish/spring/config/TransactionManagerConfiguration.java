package com.mk.swordfish.spring.config;

import com.mk.swordfish.core.annotations.Behavior;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.interceptor.BeanFactoryTransactionAttributeSourceAdvisor;
import org.springframework.transaction.interceptor.NameMatchTransactionAttributeSource;
import org.springframework.transaction.interceptor.RuleBasedTransactionAttribute;
import org.springframework.transaction.interceptor.TransactionInterceptor;

@Configuration
public class TransactionManagerConfiguration {

  @Bean
  BeanFactoryTransactionAttributeSourceAdvisor transactionAdvisor(
      TransactionManager transactionManager) {

    RuleBasedTransactionAttribute readOnlyAttribute = new RuleBasedTransactionAttribute();
    readOnlyAttribute.setReadOnly(true);

    NameMatchTransactionAttributeSource source = new NameMatchTransactionAttributeSource();
    source.addTransactionalMethod("get*", readOnlyAttribute);
    source.addTransactionalMethod("find*", readOnlyAttribute);
    source.addTransactionalMethod("is*", readOnlyAttribute);
    source.addTransactionalMethod("load*", readOnlyAttribute);
    source.addTransactionalMethod("*", new RuleBasedTransactionAttribute());

    BeanFactoryTransactionAttributeSourceAdvisor advisor =
        new BeanFactoryTransactionAttributeSourceAdvisor();
    advisor.setTransactionAttributeSource(source);
    advisor.setAdvice(new TransactionInterceptor(transactionManager, source));
    advisor.setClassFilter(clazz -> AnnotationUtils.findAnnotation(clazz, Behavior.class) != null);
    return advisor;
  }

}
