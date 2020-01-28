package ie.tcd.cs7cs3.undersvc;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class undersvcApplication extends Application<UndersvcConfiguration> {
    public static void main(final String[] args) throws Exception {
        new UndersvcApplication().run(args);
    }

    @Override
    public String getName() {
        return "undersvc";
    }

    @Override
    public void initialize(final Bootstrap<UndersvcConfiguration> bootstrap) {
        // TODO: application initialization
    }

    @Override
    public void run(final UndersvcConfiguration configuration,
                    final Environment environment) {
        // TODO: implement application
    }

}
