package ie.tcd.cs7cs3.undersvc.resources;

import com.google.common.collect.ImmutableList;
import ie.tcd.cs7cs3.undersvc.api.group;
import ie.tcd.cs7cs3.undersvc.api.point;
import ie.tcd.cs7cs3.undersvc.api.restriction;
import ie.tcd.cs7cs3.undersvc.core.Group;
import ie.tcd.cs7cs3.undersvc.core.GroupMember;
import ie.tcd.cs7cs3.undersvc.core.GroupRestriction;
import ie.tcd.cs7cs3.undersvc.db.GroupDAO;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import io.dropwizard.testing.junit5.ResourceExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static ie.tcd.cs7cs3.undersvc.utils.GeometryUtils.WKT2MultiPoint;
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

    private static List<Group> dummyGroupEntities;
    private static List<group> dummyGroups;

    @BeforeEach
    public void setUp() throws Exception {
        final MultiPoint p = WKT2MultiPoint("MULTIPOINT((37.516455 126.721757))");
        final Group g = new Group();
        final GroupRestriction r1 = new GroupRestriction();
        final GroupMember m1 = new GroupMember();
        g.setCreationTimestamp(0L);
        g.setDepartureTimestamp(1000L);
        g.setState("FORMING");
        g.setPoints(p);
        g.addGroupMember(m1);
        g.addGroupRestriction(r1);
        m1.setUuid(UUID.fromString("deadbeef-dead-beef-dead-deadbeefdead"));
        m1.setGroup(g);
        r1.setType("MaxMembers");
        r1.setValue(3);
        r1.setGroup(g);
        dummyGroupEntities = ImmutableList.of(g);
        dummyGroups = dummyGroupEntities.stream().map(group::new).collect(Collectors.toList());
    }

    @AfterEach
    public void tearDown() {
        reset(GROUP_DAO);
    }

    @Test
    public void testHandleGroupsGet() {
        when(GROUP_DAO.findAll()).thenReturn(dummyGroupEntities);
        final WebTarget tgt = RESOURCES.target("/groups");
        final List<group> resp = tgt.request().get(new GenericType<List<group>>() {});
        verify(GROUP_DAO).findAll();
        assertThat(resp).isEqualTo(dummyGroups);
    }
}
