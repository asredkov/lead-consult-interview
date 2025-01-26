package eu.leadconsult.interview.component;

import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
@Profile("initial-data")
@AllArgsConstructor
public class DataInitializer implements CommandLineRunner {
    private final DataSource dataSource;

    @Override
    public void run(String... args) {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator(
                new ClassPathResource("initial-data.sql")
        );
        populator.execute(dataSource);
    }
}
