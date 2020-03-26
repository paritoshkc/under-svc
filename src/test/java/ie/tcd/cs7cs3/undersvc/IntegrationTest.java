package ie.tcd.cs7cs3.undersvc;


import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import ie.tcd.cs7cs3.undersvc.api.GroupResponse;
import ie.tcd.cs7cs3.undersvc.client.UnderHTTPClient;
import io.dropwizard.testing.ConfigOverride;
import io.dropwizard.testing.DropwizardTestSupport;
import io.dropwizard.testing.ResourceHelpers;
import org.hibernate.cfg.AvailableSettings;
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
                "MULTIPOINT ((37.516455 126.721757))",
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
        // we would expect the group ID to be different
        assertThat(newGroup.getGroupId()).isNotEqualTo(g.getGroupId());
        // everything else should be as specified
        assertThat(newGroup.getGroupState()).isEqualTo(g.getGroupState());
        assertThat(newGroup.getCreateTime()).isEqualTo(g.getCreateTime());
        assertThat(newGroup.getDepTime()).isEqualTo(g.getDepTime());
        assertThat(newGroup.getPoints()).isEqualTo(g.getPoints());
        assertThat(newGroup.getMemberUUIDs()).isEqualTo(g.getMemberUUIDs());
        assertThat(newGroup.getRestrictions()).isEqualTo(g.getRestrictions());

        // And retrieving the list of groups should include the newly created group
        groups = client.listGroups();
        assertThat(groups.size()).isEqualTo(1);
        assertThat(groups.get(0)).isEqualTo(g);

        // We should be able to fetch the group by ID
        final GroupResponse g1 = client.getGroup(newGroup.getGroupId());
        assertThat(g1).isNotNull();

        // Updating the group should also work
        final GroupResponse toUpdate = new GroupResponse(
                newGroup.getGroupId(),
                GroupResponse.Moving,
                "MULTIPOINT ((37.516455 126.721757), (38.516455 127.721757))",
                ImmutableList.of("cafed00d-cafe-d00d-cafe-d00dcafed00d"),
                1234L,
                5678L,
                new HashMap<>(ImmutableMap.of("MaxPeople", 2))
        );
        final GroupResponse updated = client.updateGroup(toUpdate);
        assertThat(updated.getGroupId()).isEqualTo(toUpdate.getGroupId());
        assertThat(updated.getGroupState()).isEqualTo(toUpdate.getGroupState());
        assertThat(updated.getCreateTime()).isEqualTo(toUpdate.getCreateTime());
        assertThat(updated.getDepTime()).isEqualTo(toUpdate.getDepTime());
        assertThat(updated.getPoints()).isEqualTo(toUpdate.getPoints());
        assertThat(updated.getMemberUUIDs()).isEqualTo(toUpdate.getMemberUUIDs());
        assertThat(updated.getRestrictions()).isEqualTo(toUpdate.getRestrictions());

        // The group should be updated also after issuing a subsequent GET
        final GroupResponse getAfterUpdate = client.getGroup(toUpdate.getGroupId());
        assertThat(getAfterUpdate.getGroupId()).isEqualTo(toUpdate.getGroupId());
        assertThat(getAfterUpdate.getGroupState()).isEqualTo(toUpdate.getGroupState());
        assertThat(getAfterUpdate.getCreateTime()).isEqualTo(toUpdate.getCreateTime());
        assertThat(getAfterUpdate.getDepTime()).isEqualTo(toUpdate.getDepTime());
        assertThat(getAfterUpdate.getPoints()).isEqualTo(toUpdate.getPoints());
        assertThat(getAfterUpdate.getMemberUUIDs()).isEqualTo(toUpdate.getMemberUUIDs());
        assertThat(getAfterUpdate.getRestrictions()).isEqualTo(toUpdate.getRestrictions());
    }

    public static String createTempFile() {
        try {
            return File.createTempFile("test-undersvc", null).getAbsolutePath();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
