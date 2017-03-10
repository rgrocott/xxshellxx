package com.xxshellxx.config;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;
import java.util.logging.Logger;

import javax.sql.DataSource;

import com.xxshellxx.Application;
import com.xxshellxx.account.UserService;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackageClasses = Application.class)
class JpaConfig implements TransactionManagementConfigurer {
    final org.slf4j.Logger logger = LoggerFactory.getLogger(JpaConfig.class);

    @Value("${dataSource.test}")
    private String test;

    @Value("${dataSource.driverClassName}")
    private String driver;
    @Value("${dataSource.url}")
    private String url;
    @Value("${dataSource.username}")
    private String username;
    @Value("${dataSource.password}")
    private String password;
    @Value("${hibernate.dialect}")
    private String dialect;
    @Value("${hibernate.hbm2ddl.auto}")
    private String hbm2ddlAuto;

    @Bean
    public DataSource configureDataSource() {
        UserService.Test = test;

        if (test.equals("true") && !url.equals("postgres")) {
            HikariConfig config = new HikariConfig();
            config.setDriverClassName(driver);
            config.setJdbcUrl(url);
            config.setUsername(username);
            config.setPassword(password);
            config.addDataSourceProperty("cachePrepStmts", "true");
            config.addDataSourceProperty("prepStmtCacheSize", "250");
            config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
            config.addDataSourceProperty("useServerPrepStmts", "true");

            return new HikariDataSource(config);
        }

        URI dbUri = null;
        String database_url = System.getenv("DATABASE_URL");
        
        try {
        	if (database_url == null) {
        		// local database for testing
        		database_url = "postgres://xxshellxx:password1@localhost/xxshellxx";
        	}
            dbUri = new URI(database_url);
        }
        catch (URISyntaxException ex) {
            logger.error("Database URI Syntax Error", ex);
            return null;
        }

        username = dbUri.getUserInfo().split(":")[0];
        password = dbUri.getUserInfo().split(":")[1];

        if (dbUri.getPort() == -1) {
            url = "jdbc:postgresql://" + dbUri.getHost() + dbUri.getPath();
        }
        else {
            url = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath();
        }
        
        if (dbUri.getQuery() != null) {
        	// This will be SSL flags to allow remote connection to Heroku
        	url += "?" + dbUri.getQuery();
        }

        HikariConfig config = new HikariConfig();
        config.setDriverClassName(driver);
        //config.setJdbcUrl(url + "?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory");
        config.setJdbcUrl(url);
        config.setUsername(username);
        config.setPassword(password);
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        config.addDataSourceProperty("useServerPrepStmts", "true");

        return new HikariDataSource(config);
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean configureEntityManagerFactory() {
        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setDataSource(configureDataSource());
        entityManagerFactoryBean.setPackagesToScan("com.xxshellxx");
        entityManagerFactoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());

        Properties jpaProperties = new Properties();
        jpaProperties.put(org.hibernate.cfg.Environment.DIALECT, dialect);
        jpaProperties.put(org.hibernate.cfg.Environment.HBM2DDL_AUTO, hbm2ddlAuto);
        entityManagerFactoryBean.setJpaProperties(jpaProperties);

        return entityManagerFactoryBean;
    }

    @Bean
    public PlatformTransactionManager annotationDrivenTransactionManager() {
        return new JpaTransactionManager();
    }
}
