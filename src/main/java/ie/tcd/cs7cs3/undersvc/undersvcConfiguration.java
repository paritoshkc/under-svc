package ie.tcd.cs7cs3.undersvc;

import io.dropwizard.Configuration;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.db.DataSourceFactory;
import javax.validation.Valid;
import javax.validation.constraints.*;

public class undersvcConfiguration extends Configuration {
    @Valid
    @NotNull
    private DataSourceFactory database;

    @JsonProperty("database")
    public DataSourceFactory getDatabase() {
        return database;
    }
}
