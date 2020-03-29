package ie.tcd.cs7cs3.undersvc;

import ie.tcd.cs7cs3.undersvc.db.GroupDAO;
import ie.tcd.cs7cs3.undersvc.db.UserRatingDAO;
import ie.tcd.cs7cs3.undersvc.resources.GroupResource;
import ie.tcd.cs7cs3.undersvc.resources.GroupsResource;
import ie.tcd.cs7cs3.undersvc.resources.UserRatingResource;
import io.dropwizard.Application;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.db.PooledDataSourceFactory;
import io.dropwizard.hibernate.ScanningHibernateBundle;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

/**
 * undersvcApplication is the base application for undersvc.
 */
public class undersvcApplication extends Application<undersvcConfiguration> {
    private final ScanningHibernateBundle<undersvcConfiguration> hibernate = new ScanningHibernateBundle<undersvcConfiguration>("ie.tcd.cs7cs3.undersvc.core") {
        @Override
        public DataSourceFactory getDataSourceFactory(undersvcConfiguration config) {
            return config.getDatabase();
        }
    };

    /**
     * getName returns the name of the application.
     * @return the name of the application
     */
    @Override
    public String getName() {
        return "undersvc";
    }

    /**
     * initialize is called at startup to bootstrap required runtime dependencies.
     * @param bootstrap required configuration for bootstrapping
     */
    @Override
    public void initialize(final Bootstrap<undersvcConfiguration> bootstrap) {
        bootstrap.setConfigurationSourceProvider(
                new SubstitutingSourceProvider(
                        bootstrap.getConfigurationSourceProvider(),
                        new EnvironmentVariableSubstitutor(false)
                )
        );
        bootstrap.addBundle(new MigrationsBundle<undersvcConfiguration>() {
            @Override
            public PooledDataSourceFactory getDataSourceFactory(undersvcConfiguration configuration) {
                return configuration.getDatabase();
            }
        });
        bootstrap.addBundle(hibernate);
    }

    /**
     * run runs the application.
     * @param configuration the runtime configuration
     * @param environment environment variables etc.
     */
    @Override
    public void run(final undersvcConfiguration configuration,
                    final Environment environment) {
        final GroupDAO groupDAO = new GroupDAO(hibernate.getSessionFactory());
        final UserRatingDAO userRatingDAO = new UserRatingDAO(hibernate.getSessionFactory());
        environment.jersey().register(new GroupsResource(groupDAO));
        environment.jersey().register(new GroupResource(groupDAO));
        environment.jersey().register(new UserRatingResource(userRatingDAO));
//        environment.jersey().register(new GroupMemberResource(groupDAO));
    }

    public static void main(final String[] args) throws Exception {
        new undersvcApplication().run(args);
    }
}
