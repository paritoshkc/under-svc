package ie.tcd.cs7cs3.undersvc.resources;

import com.google.common.collect.ImmutableList;
import ie.tcd.cs7cs3.undersvc.api.GroupResponse;
import ie.tcd.cs7cs3.undersvc.core.Group;
import ie.tcd.cs7cs3.undersvc.core.GroupMember;
import ie.tcd.cs7cs3.undersvc.core.GroupRestriction;
import ie.tcd.cs7cs3.undersvc.db.GroupDAO;
import io.dropwizard.testing.junit5.ResourceExtension;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.io.ParseException;
import org.mockito.ArgumentCaptor;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static ie.tcd.cs7cs3.undersvc.utils.GeometryUtils.WKT2MultiPoint;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GroupResourceTest {
    private static final GroupDAO GROUP_DAO = mock(GroupDAO.class);
    private static final ResourceExtension RESOURCES = ResourceExtension.builder()
            .addResource(new GroupsResource(GROUP_DAO))
            .build();

    private ArgumentCaptor<Group> groupArgumentCaptor = ArgumentCaptor.forClass(Group.class);
    private static Group dummyGroupEntity;
    private static GroupResponse dummyGroup;
    private long id;
    private List<String> memberUUIDs;
    private HashMap<String, Integer> restrictions;

    @BeforeEach
    void setUp() throws ParseException
    {
        final MultiPoint p = WKT2MultiPoint("MULTIPOINT((37.516455 126.721757))");
        final Group g = new Group();
        final GroupRestriction r1 = new GroupRestriction();
        final GroupMember m1 = new GroupMember();
        g.setId(2);
        g.setCreationTimestamp(0L);
        g.setDepartureTimestamp(1000L);
        g.setState("FORMING");
        g.setPoints(p);
        g.addGroupMember(m1);
        g.addGroupRestriction(r1);
        m1.setGroup(g);
        r1.setType("MaxMembers");
        r1.setValue(3);
        r1.setGroup(g);
        m1.setUuid(UUID.fromString("deadbeef-dead-beef-dead-deadbeefdead"));
        this.restrictions = new HashMap<String, Integer>();
        for (final GroupRestriction gr : g.getGroupRestrictions()) {
            restrictions.put(gr.getType(), gr.getValue());
        }
        this.memberUUIDs.add(m1.getUuid().toString());
//        dummyGroupEntities = ImmutableList.of(g);
//        dummyGroup = dummyGroupEntity.stream().map(GroupResponse::new).collect(Collectors.toList());
        dummyGroup = new GroupResponse(g.getId(), g.getState(), g.getPoints().toText(), this.memberUUIDs, g.getCreationTimestamp(), g.getDepartureTimestamp(),
                this.restrictions);
    }

    @AfterEach
    void tearDown() {
        reset(GROUP_DAO);
    }

    @Test
    void handleGroupGetById(long id) {
        when(GROUP_DAO.findById(dummyGroupEntity.getId())).thenReturn(java.util.Optional.ofNullable(dummyGroupEntity));
        final WebTarget tgt = RESOURCES.target("/groups/2");
        final GroupResponse rsp = tgt.request().get(new GenericType<GroupResponse>(){});
        verify(GROUP_DAO).findById(dummyGroupEntity.getId());
    }

    @Test
    void handleGroupDeleteByID() {
    }
}