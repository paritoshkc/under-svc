package ie.tcd.cs7cs3.undersvc;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class undersvcApplication extends Application<undersvcConfiguration> {
    public static void main(final String[] args) throws Exception {
        new undersvcApplication().run(args);
    }

    @Override
    public String getName() {
        return "undersvc";
    }

    @Override
    public void initialize(final Bootstrap<undersvcConfiguration> bootstrap) {
        // TODO: application initialization
    }

    @Override
    public void run(final undersvcConfiguration configuration,
                    final Environment environment) {
        // TODO: implement application
    }

}
