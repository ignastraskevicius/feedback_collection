package lt.agmis.feedbackcollection.configuration.beans.dbmigration;

import com.googlecode.flyway.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;

/**
 * dummy flyway beans setup. These beans are needed as dependencies for non-profile beans.
 * @author ignas
 *
 */

@Configuration
@Profile({"dev-hardcoded-db-config","production"})
public class DummyFlyway {

	private static Logger logger = LoggerFactory.getLogger(DummyFlyway.class.getName());

    @Value("#{dbMigrationActivated}")
    private Boolean dbMigrationActivated;

	@Autowired
	private DataSource dataSource;
	
	@Bean(name="flywayMigrationsOfCompanies")
	public Flyway flyway() {
        Flyway flyway = new Flyway();
        if(dbMigrationActivated) {
            logger.trace("Dummy Migration bean init");
            flyway.setDataSource(dataSource);
        }
		return flyway;
	}
}
