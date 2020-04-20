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
import org.mockito.stubbing.OngoingStubbing;

import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.*;
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
    void testHandleGroupGetById() {
//        this.id = id;
        long id = dummyGroupEntity.getId();
        when(GROUP_DAO.findById(dummyGroupEntity.getId())).thenReturn(java.util.Optional.of(dummyGroupEntity));
        final WebTarget tgt = RESOURCES.target("/groups/3");
//        System.out.println(tgt);
        final GroupResponse resp;
        resp = tgt.request().get(new GenericType<GroupResponse>(){});
//        verify(GROUP_DAO).findById(dummyGroupEntity.getId());
//        verify(GROUP_DAO).findById(dummyGroupEntity.getId());
        assertThat(resp).isEqualTo(dummyGroup);
    }

    @Test
    void testHandleGroupUpdateById() throws ParseException {
        GroupMember gm = new GroupMember();
        gm.setUuid(UUID.fromString("cafebabe-dead-beef-babe-cafecafecafe"));
        GroupRestriction gr = new GroupRestriction();
        gr.setValue(2);
        gr.setType("Max Members");
        Group updatedGroupEntity = new Group();
        updatedGroupEntity.setId(3);
        updatedGroupEntity.setCreationTimestamp(0L);
        updatedGroupEntity.setDepartureTimestamp(1000L);
        updatedGroupEntity.setState("FORMING");
        updatedGroupEntity.setPoints(WKT2MultiPoint("MULTIPOINT((17.516455 28.721757))"));
        updatedGroupEntity.addGroupMember(gm);
        updatedGroupEntity.addGroupRestriction(gr);
        gm.setGroup(updatedGroupEntity);
        gr.setGroup(updatedGroupEntity);
        GroupResponse gResp = new GroupResponse(updatedGroupEntity);

//        check if that group exists
        assertThat(updatedGroupEntity.getId()).isEqualTo(dummyGroupEntity.getId());
        doNothing().when(GROUP_DAO).update(updatedGroupEntity);

        final WebTarget tgt = RESOURCES.target("/groups/3");
        final Entity<GroupResponse> e = Entity.entity(gResp, MediaType.APPLICATION_JSON_TYPE);
        Response resp = tgt.request().post(e);
        System.out.println("Response is FINE");

//        will throw error becz group with the same id already exist
        assertThat(resp.getStatusInfo()).isEqualTo(Response.Status.INTERNAL_SERVER_ERROR);
        verify(GROUP_DAO).update(groupArgumentCaptor.capture());
        final Group expected = groupArgumentCaptor.getValue();
        assertThat(updatedGroupEntity.equals(expected));
    }
    @Test
    void testHandleGroupDeleteByID()
    {
        long id = dummyGroupEntity.getId();
        doNothing().when(GROUP_DAO).deleteGroupById(id);

        String URLpath = "/groups/" + id;
        final WebTarget tgt = RESOURCES.target(URLpath);
        final Response resp = tgt.request().delete();
//       ResponseStatus is (No Content) if the action has been performed but
//       the response does not include an entity
        assertThat(resp.getStatusInfo()).isEqualTo(Response.Status.NO_CONTENT);


    }
}