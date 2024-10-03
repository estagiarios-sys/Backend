package com.systextil.relatorio.infra.persistence_jndi;
//
//import java.util.Properties;
//
//import javax.naming.NamingException;
//import javax.sql.DataSource;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.PropertySource;
//import org.springframework.core.env.Environment;
//import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
//import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
//import org.springframework.jndi.JndiTemplate;
//import org.springframework.orm.jpa.JpaTransactionManager;
//import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
//import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
//import org.springframework.transaction.PlatformTransactionManager;
//import org.springframework.transaction.annotation.EnableTransactionManagement;
//
//import jakarta.el.PropertyNotFoundException;
//import jakarta.persistence.EntityManagerFactory;
//
//@Configuration
//@EnableTransactionManagement
//@PropertySource("classpath:persistence-jndi.properties")
//@ComponentScan("com.baeldung.hibernate.cache")
//@EnableJpaRepositories(basePackages = "com.baeldung.hibernate.cache.dao")
public class PersistenceJndiConfig {
//
//    private Environment env;
//	
//	public PersistenceJndiConfig(Environment env) {
//		this.env = env;
//	}
//
//    @Bean
//    LocalContainerEntityManagerFactoryBean entityManagerFactory() throws NamingException {
//        final LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
//        em.setDataSource(dataSource());
//        em.setPackagesToScan("com.baeldung.hibernate.cache.model");
//        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
//        em.setJpaProperties(additionalProperties());
//        return em;
//    }
//
//    @Bean
//    DataSource dataSource() throws NamingException {
//    	String property = env.getProperty("jdbc.url");
//    	if (property != null) {
//    		return (DataSource) new JndiTemplate().lookup(property);
//    	} else {
//    		throw new PropertyNotFoundException();
//    	}
//    }
//
//    @Bean
//    PlatformTransactionManager transactionManager(final EntityManagerFactory emf) {
//        return new JpaTransactionManager(emf);
//    }
//
//    @Bean
//    PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
//        return new PersistenceExceptionTranslationPostProcessor();
//    }
//
//    final Properties additionalProperties() {
//        final Properties hibernateProperties = new Properties();
//        hibernateProperties.setProperty("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto"));
//        hibernateProperties.setProperty("hibernate.dialect", env.getProperty("hibernate.dialect"));
//        hibernateProperties.setProperty("hibernate.cache.use_second_level_cache", "false");
//        return hibernateProperties;
//    }
}