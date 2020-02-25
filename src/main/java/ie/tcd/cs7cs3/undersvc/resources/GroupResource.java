package ie.tcd.cs7cs3.undersvc.resources;

import ie.tcd.cs7cs3.undersvc.api.GroupResponse;
import ie.tcd.cs7cs3.undersvc.core.Group;
import ie.tcd.cs7cs3.undersvc.core.GroupMember;
import ie.tcd.cs7cs3.undersvc.core.GroupRestriction;
import ie.tcd.cs7cs3.undersvc.db.GroupDAO;
import io.dropwizard.hibernate.UnitOfWork;
import org.locationtech.jts.geom.MultiPoint;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.*;

/**
 * {@link GroupsResource} is a resource for handling GET, POST and PUT requests to `/groups/{groupId}`.
 */
@Path("/groups/{groupID}")
@Produces(MediaType.APPLICATION_JSON)
public class GroupResource
{
    private final GroupDAO groupDAO;
    private GroupMember member;
    long id;
    private GroupResponse groupResponse;
    String groupState; long depTime; String points; List<String> uuids; Map<String, Integer> restriction;

    public GroupResource(GroupDAO groupDAO) {
        this.groupDAO = groupDAO;
    }

    @GET
    @UnitOfWork
    public GroupResponse handleGroupGetById(@PathParam("groupID") long id)
    {
        final Optional<Group> maybeGroup = groupDAO.findById(id);
        if (!maybeGroup.isPresent()) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return new GroupResponse(maybeGroup.get());
    }

    @POST
    @UnitOfWork
//    public void handleGroupUpdateById(@PathParam("groupID") long groupId, String groupState,
//                                      long depTime, MultiPoint points, List<UUID> uuids, List<GroupRestriction> restriction)
    public GroupResponse handleGroupUpdateById(@PathParam("groupID") long groupId, @Valid GroupResponse groupResponse)
//    public void handleGroupUpdateById(@Valid GroupResponse groupResponse)
    {
        id = groupId;
//        this.groupResponse = groupResponse;
//        this.groupState = groupState;
//        this.depTime = depTime;
//        this.points = points;
//        this.uuids = uuids;
//        this.restriction = restriction;
        Optional<Group> maybeGroup = groupDAO.findById(groupId);
        Group group = maybeGroup.get();
//        groupResponse = new GroupResponse(group);
//        id = Objects.requireNonNull(groupResponse).getGroupId();
        groupState = groupResponse.getGroupState();

        depTime = groupResponse.getDepTime();
        points = groupResponse.getPoints();
        uuids = groupResponse.getMemberUUIDs();
        restriction = groupResponse.getRestrictions();

        System.out.println("id: " + id );
        System.out.println("group state: " + groupState);
        System.out.println("dep Time: " + depTime);

        for(int i = 0; i<uuids.size(); i++)
        {
            System.out.println("UUid "+i+" : " + uuids.get(i));
        }


        this.groupDAO.deleteMemberByUuid(groupId, uuids);
//        Optional<Group> maybeGroup = this.groupDAO.findById(groupId);
//        Group group = maybeGroup.get();
//        if (!this.groupDAO.isMembersSame(groupId, uuids))
//        {
//            List<UUID> list = groupDAO.getAdditionalMembersInGroup(groupId, uuids);
//
//            Iterator iterator = list.iterator();
//            while(iterator.hasNext())
//            {
//                UUID temp = (UUID) iterator.next();
//                member = new GroupMember();
//                member.setUuid(temp);
//                member.setGroup(group);
//
//                group.addGroupMember(member);
//            }
//        }
//        group.setState(groupState);
//        group.setDepartureTimestamp(depTime);
//        group.setPoints(points);
//        group.setGroupRestrictions(restriction);
        return new GroupResponse(group);
    }

    @DELETE
    @UnitOfWork
    public void handleGroupDeleteByID(@PathParam("groupID") long id)
    {
        this.groupDAO.delete(id);
    }
}
