package ie.tcd.cs7cs3.undersvc;


import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import ie.tcd.cs7cs3.undersvc.api.GroupResponse;
import ie.tcd.cs7cs3.undersvc.client.UnderHTTPClient;
import io.dropwizard.testing.ConfigOverride;
import io.dropwizard.testing.DropwizardTestSupport;
import io.dropwizard.testing.ResourceHelpers;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

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
    public void doTest() throws Exception {
        final GroupResponse g = new GroupResponse(
                9999, // very improbable in brand-new in-memory H2 DB
                GroupResponse.Forming,
                "MULTIPOINT((37.516455 126.721757))",
                ImmutableList.of("deadbeef-dead-beef-dead-deadbeefdead"),
                0L,
                1000L,
                new HashMap<>(ImmutableMap.of("MaxPeople", 3))
        );

        final UnderHTTPClient client = new UnderHTTPClient()
                .insecure()
                .withHost("localhost")
                .withPort(SUPPORT.getLocalPort());

        // There should initially be no groups
        List<GroupResponse> groups = client.listGroups();
        assertThat(groups).isEmpty();

        // Then creating a group should work as expected
        GroupResponse newGroup = client.createGroup(g);
        assertThat(newGroup).isNotNull();
        assertThat(newGroup.equals(g));

        // And retrieving the list of groups should include the newly created group
        groups = client.listGroups();
        assertThat(groups.size() == 1);
        assertThat(groups.get(0).equals(g));
        // we would expect the group ID to be different
        assertThat(groups.get(0).getGroupId() != (g.getGroupId()));
    }

    public static String createTempFile() {
        try {
            return File.createTempFile("test-undersvc", null).getAbsolutePath();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
