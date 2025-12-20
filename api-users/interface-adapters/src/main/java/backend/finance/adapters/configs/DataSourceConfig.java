package backend.finance.adapters.configs;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.autoconfigure.DataSourceProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class DataSourceConfig {

    @Bean
    @Primary // Define este DataSource como o principal na aplicação
    @RefreshScope // Permite recarregar as propriedades do DataSource em tempo de execução
    @ConfigurationProperties("spring.datasource.hikari")
    public HikariDataSource dataSource(DataSourceProperties properties) {
        // 1. Cria o builder usando as propriedades base (url, username, password)
        HikariDataSource dataSource = properties.initializeDataSourceBuilder()
                .type(HikariDataSource.class)
                .build();

        // 2. O Hikari exige 'jdbcUrl'. Se o Spring carregou como 'url',
        // nós setamos manualmente para garantir que não suba nulo.
        if (properties.getUrl() != null) {
            dataSource.setJdbcUrl(properties.getUrl());
        }

        return dataSource;
    }
}
