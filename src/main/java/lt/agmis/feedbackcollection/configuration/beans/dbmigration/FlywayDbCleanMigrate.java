package lt.agmis.feedbackcollection.configuration.beans.dbmigration;

import com.googlecode.flyway.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

/**
 * Be careful not to include this bean in production profile, because it will destroy the database schema
 * @author ignas
 *
 */

@Configuration
@Profile({"db-integration-test","system-integration-test"})
public class FlywayDbCleanMigrate {

    enum Database {MYSQL, POSTGRES, UNSPECIFIED}

	private static Logger logger = LoggerFactory.getLogger(FlywayDbCleanMigrate.class.getName());
	
	@Autowired
	private DataSource dataSource;
	
	@Bean(name="flywayMigrationsOfCompanies")
	public Flyway flyway() {

		
		JdbcTemplate jdbc = new JdbcTemplate(dataSource);

        switch(Database.MYSQL) {
            case MYSQL: break;
            case POSTGRES: jdbc.execute("DROP SCHEMA IF EXISTS public CASCADE "); break;
            default: throw new RuntimeException("Database type is not specified. Specify one in switch statement. Database Migration failed");
        }

        Flyway flyway = new Flyway();
		flyway.setDataSource(dataSource);
		flyway.setInitVersion("0.0.1");
		flyway.setSchemas("feedback_collection_test");
		flyway.clean();
		flyway.init();
		flyway.migrate();
		
		return flyway;
	}
}
