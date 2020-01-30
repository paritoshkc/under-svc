package ie.tcd.cs7cs3.undersvc.resources;

import com.google.common.collect.ImmutableList;
import ie.tcd.cs7cs3.undersvc.api.group;
import ie.tcd.cs7cs3.undersvc.api.point;
import ie.tcd.cs7cs3.undersvc.api.restriction;
import ie.tcd.cs7cs3.undersvc.db.GroupDAO;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import io.dropwizard.testing.junit5.ResourceExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link GroupsResource}
 */
@ExtendWith(DropwizardExtensionsSupport.class)
public class GroupsResourceTest {
    private static final GroupDAO GROUP_DAO = mock(GroupDAO.class);
    private static final ResourceExtension RESOURCES = ResourceExtension.builder()
            .addResource(new GroupsResource(GROUP_DAO))
            .build();
    private static final List<group> dummyGroups = ImmutableList.of(
            new group(
                    group.Forming,
                    ImmutableList.of(new point(1.0, 2.0)),
                    ImmutableList.of(UUID.fromString("783575bf-5629-44af-9a5e-c22174d722e3")),
                    1234567,
                    ImmutableList.of(new restriction(restriction.MaxPeople, 3))
            )
    );

    @BeforeEach
    public void setUp() {

    }

    @AfterEach
    public void tearDown() {
        reset(GROUP_DAO);
    }

    @Test
    public void testHandleGroupsGet() {
        when(GROUP_DAO.listGroups()).thenReturn(dummyGroups);
        final WebTarget tgt = RESOURCES.target("/groups");
        final List<group> resp = tgt.request().get(new GenericType<List<group>>() {});
        verify(GROUP_DAO).listGroups();
        assertThat(resp).isEqualTo(dummyGroups);
    }
}
