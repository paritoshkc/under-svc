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
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * {@link GroupsResource} is a resource for handling GET, POST and PUT requests to `/groups/{groupId}`.
 */
@Path("/groups/{groupID}")
@Produces(MediaType.APPLICATION_JSON)
public class GroupResource
{
    private final GroupDAO groupDAO;
    private GroupMember member;

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
    public GroupResponse handleGroupUpdateById(@PathParam("groupID") long groupId, String groupState,
                                      long depTime, MultiPoint points, List<UUID> uuids, List<GroupRestriction> restriction)
    {
        this.groupDAO.deleteMemberByUuid(groupId, uuids);
        Optional<Group> maybeGroup = groupDAO.findById(groupId);
        Group group = maybeGroup.get();
        if (!groupDAO.isMembersSame(groupId, uuids))
        {
            List<UUID> list = groupDAO.getAdditionalMembersInGroup(groupId, uuids);

            Iterator iterator = list.iterator();
            while(iterator.hasNext())
            {
                UUID temp = (UUID) iterator.next();
                member = new GroupMember();
                member.setGroup(group);
                member.setUuid(temp);
                group.addGroupMember(member);
            }
        }
        group.setState(groupState);
        group.setDepartureTimestamp(depTime);
        group.setPoints(points);
        group.setGroupRestrictions(restriction);
        return new GroupResponse(group);
    }

    @DELETE
    @UnitOfWork
    public void handleGroupDeleteByID(@PathParam("groupID") long id)
    {
        this.groupDAO.delete(id);
    }
}
