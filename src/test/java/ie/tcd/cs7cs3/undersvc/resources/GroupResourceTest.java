package ie.tcd.cs7cs3.undersvc.resources;

import com.google.common.collect.ImmutableList;
import ie.tcd.cs7cs3.undersvc.api.GroupResponse;
import ie.tcd.cs7cs3.undersvc.core.Group;
import ie.tcd.cs7cs3.undersvc.core.GroupMember;
import ie.tcd.cs7cs3.undersvc.core.GroupRestriction;
import ie.tcd.cs7cs3.undersvc.db.GroupDAO;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import io.dropwizard.testing.junit5.ResourceExtension;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.io.ParseException;
import org.mockito.ArgumentCaptor;

import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static ie.tcd.cs7cs3.undersvc.utils.GeometryUtils.WKT2MultiPoint;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(DropwizardExtensionsSupport.class)
class GroupResourceTest {
    private static final GroupDAO GROUP_DAO = mock(GroupDAO.class);
    private static final ResourceExtension RESOURCES = ResourceExtension.builder()
            .addResource(new GroupResource(GROUP_DAO))
            .build();

    private ArgumentCaptor<Group> groupArgumentCaptor = ArgumentCaptor.forClass(Group.class);
    private static Group dummyGroupEntity;
    private static GroupResponse dummyGroup;
//    private long id;
    private List<String> memberUUIDs;
    private HashMap<String, Integer> restrictions;

    @BeforeEach
    void setUp() throws ParseException
    {
        final MultiPoint p = WKT2MultiPoint("MULTIPOINT((37.516455 126.721757))");
        dummyGroupEntity = new Group();
        final GroupRestriction r1 = new GroupRestriction();
        final GroupMember m1 = new GroupMember();
        dummyGroupEntity.setId(3);
        dummyGroupEntity.setCreationTimestamp(0L);
        dummyGroupEntity.setDepartureTimestamp(1000L);
        dummyGroupEntity.setState("FORMING");
        dummyGroupEntity.setPoints(p);
        dummyGroupEntity.addGroupMember(m1);
        dummyGroupEntity.addGroupRestriction(r1);
        m1.setGroup(dummyGroupEntity);
        r1.setType("MaxMembers");
        r1.setValue(3);
        r1.setGroup(dummyGroupEntity);
        m1.setUuid(UUID.fromString("deadbeef-dead-beef-dead-deadbeefdead"));
        this.restrictions = new HashMap<String, Integer>();
        for (final GroupRestriction gr : dummyGroupEntity.getGroupRestrictions()) {
            restrictions.put(gr.getType(), gr.getValue());
        }
        memberUUIDs = ImmutableList.of(m1.getUuid().toString());
        dummyGroup = new GroupResponse(dummyGroupEntity.getId(), dummyGroupEntity.getState(), dummyGroupEntity.getPoints().toText(), this.memberUUIDs, dummyGroupEntity.getCreationTimestamp(), dummyGroupEntity.getDepartureTimestamp(),
                this.restrictions);
    }

    @AfterEach
    void tearDown() {
        reset(GROUP_DAO);
    }

    @Test
    void handleGroupGetById() {
//        this.id = id;
        long id = dummyGroupEntity.getId();
        when(GROUP_DAO.findById(dummyGroupEntity.getId())).thenReturn(java.util.Optional.of(dummyGroupEntity));
        final WebTarget tgt = RESOURCES.target("/groups/3");
//        System.out.println(tgt);
        final GroupResponse resp;
        resp = tgt.request().get(new GenericType<GroupResponse>(){});
//        verify(GROUP_DAO).findById(dummyGroupEntity.getId());
        verify(GROUP_DAO).findById(dummyGroupEntity.getId());
        assertThat(resp).isEqualTo(dummyGroup);
    }

    @Test
    void handleGroupDeleteByID()
    {

    }
}