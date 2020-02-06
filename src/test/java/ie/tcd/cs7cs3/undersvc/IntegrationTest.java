package ie.tcd.cs7cs3.undersvc;


import com.google.common.collect.ImmutableList;
import ie.tcd.cs7cs3.undersvc.api.group;
import ie.tcd.cs7cs3.undersvc.api.restriction;
import io.dropwizard.testing.ConfigOverride;
import io.dropwizard.testing.DropwizardTestSupport;
import io.dropwizard.testing.ResourceHelpers;
import org.glassfish.jersey.client.JerseyClientBuilder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class IntegrationTest {
    private static final String TEMP_FILE = createTempFile();
    private static final String CONFIG_PATH = ResourceHelpers.resourceFilePath("test-undersvc.yml");
    private static final DropwizardTestSupport<undersvcConfiguration> SUPPORT = new DropwizardTestSupport<>(
            undersvcApplication.class,
            CONFIG_PATH,
            ConfigOverride.config("database.url", "jdbc:h2:" + TEMP_FILE)
    );

    @BeforeAll
    static void setUp() throws Exception {
        SUPPORT.before();
    }

    @AfterAll
    static void tearDown() throws Exception {
        SUPPORT.after();
    }

    @Test
    @Order(1)
    public void testPostGroups() throws Exception {
        final group g = new group(
            group.Forming,
            "MULTIPOINT((37.516455 126.721757))",
            ImmutableList.of("deadbeef-dead-beef-dead-deadbeefdead"),
            0L,
            1000L,
            ImmutableList.of(new restriction("MaxPeople", 3))
        );

        final Client client = new JerseyClientBuilder().build();
        final group newGroup = client.target("http://localhost:" + SUPPORT.getLocalPort() + "/groups")
                .request()
                .post(Entity.entity(g, MediaType.APPLICATION_JSON_TYPE))
                .readEntity(group.class);

        assertThat(newGroup).isNotNull();
        assertThat(newGroup.equals(g));
    }

    public static String createTempFile() {
        try {
            return File.createTempFile("test-undersvc", null).getAbsolutePath();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
