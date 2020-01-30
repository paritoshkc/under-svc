package ie.tcd.cs7cs3.undersvc;

import io.dropwizard.Configuration;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.db.DataSourceFactory;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * underSvcConfiguration is a class to hold runtime configuration variables for undersvc.
 */
public class undersvcConfiguration extends Configuration {
    @Valid
    @NotNull
    private DataSourceFactory database;

    /**
     * getDatabase returns the DataSourceFactory defined by the on-disk configuration.
     * @return a DataSourceFactory
     */
    @JsonProperty("database")
    public DataSourceFactory getDatabase() {
        return database;
    }
}
