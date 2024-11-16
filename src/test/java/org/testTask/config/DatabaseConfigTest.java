package org.testTask.config;

import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;

import javax.sql.DataSource;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {DatabaseConfigTest.TestConfig.class})
class DatabaseConfigTest {

    @Autowired
    private ApplicationContext context;

    @Test
    void testDb1DataSourceBean() {
        DataSource db1DataSource = (DataSource) context.getBean("db1DataSource");
        assertNotNull(db1DataSource);
        assertTrue(db1DataSource instanceof DriverManagerDataSource);
        DriverManagerDataSource driverManagerDataSource = (DriverManagerDataSource) db1DataSource;
        assertEquals("jdbc:postgresql://localhost:5433/testdb", driverManagerDataSource.getUrl());
        assertEquals("testuser", driverManagerDataSource.getUsername());
    }

    @Test
    void testDb2DataSourceBean() {
        DataSource db2DataSource = (DataSource) context.getBean("db2DataSource");
        assertNotNull(db2DataSource);
        assertTrue(db2DataSource instanceof DriverManagerDataSource);
        DriverManagerDataSource driverManagerDataSource = (DriverManagerDataSource) db2DataSource;
        assertEquals("jdbc:postgresql://localhost:5434/testdb", driverManagerDataSource.getUrl());
        assertEquals("testuser", driverManagerDataSource.getUsername());
    }

    @Test
    void testDb1EntityManagerFactoryBean() {
        EntityManagerFactory db1EntityManagerFactory = (EntityManagerFactory) context.getBean("db1EntityManagerFactory");
        assertNotNull(db1EntityManagerFactory);
    }

    @Test
    void testDb2EntityManagerFactoryBean() {
        EntityManagerFactory db2EntityManagerFactory = (EntityManagerFactory) context.getBean("db2EntityManagerFactory");
        assertNotNull(db2EntityManagerFactory);
    }

    @Test
    void testDb1TransactionManagerBean() {
        JpaTransactionManager db1TransactionManager = (JpaTransactionManager) context.getBean("db1TransactionManager");
        assertNotNull(db1TransactionManager);
    }

    @Test
    void testDb2TransactionManagerBean() {
        JpaTransactionManager db2TransactionManager = (JpaTransactionManager) context.getBean("db2TransactionManager");
        assertNotNull(db2TransactionManager);
    }

    @Configuration
    static class TestConfig extends DatabaseConfig {

        @Bean
        public Environment env() {
            Environment mockEnv = mock(Environment.class);
            when(mockEnv.getProperty("spring.first-datasource.url")).thenReturn("jdbc:postgresql://localhost:5433/testdb");
            when(mockEnv.getProperty("spring.first-datasource.username")).thenReturn("testuser");
            when(mockEnv.getProperty("spring.first-datasource.password")).thenReturn("testpass");
            when(mockEnv.getProperty("spring.first-datasource.driver-class-name")).thenReturn("org.postgresql.Driver");
            when(mockEnv.getProperty("spring.second-datasource.url")).thenReturn("jdbc:postgresql://localhost:5434/testdb");
            when(mockEnv.getProperty("spring.second-datasource.username")).thenReturn("testuser");
            when(mockEnv.getProperty("spring.second-datasource.password")).thenReturn("testpass");
            when(mockEnv.getProperty("spring.second-datasource.driver-class-name")).thenReturn("org.postgresql.Driver");
            return mockEnv;
        }
    }
}
