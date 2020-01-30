package ie.tcd.cs7cs3.undersvc;

import ie.tcd.cs7cs3.undersvc.api.group;
import ie.tcd.cs7cs3.undersvc.db.GroupDAO;
import ie.tcd.cs7cs3.undersvc.resources.GroupsResource;
import io.dropwizard.Application;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.db.PooledDataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class undersvcApplication extends Application<undersvcConfiguration> {
    private final HibernateBundle<undersvcConfiguration> hibernate = new HibernateBundle<undersvcConfiguration>(group.class) {
        @Override
        public DataSourceFactory getDataSourceFactory(undersvcConfiguration config) {
            return config.getDatabase();
        }
    };

    @Override
    public String getName() {
        return "undersvc";
    }

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

    @Override
    public void run(final undersvcConfiguration configuration,
                    final Environment environment) {
        final GroupDAO groupDAO = new GroupDAO(hibernate.getSessionFactory());
        environment.jersey().register(new GroupsResource(groupDAO));
    }

    public static void main(final String[] args) throws Exception {
        new undersvcApplication().run(args);
    }
}
