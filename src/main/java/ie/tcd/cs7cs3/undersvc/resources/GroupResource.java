package ie.tcd.cs7cs3.undersvc.resources;

import ie.tcd.cs7cs3.undersvc.api.GroupResponse;
import ie.tcd.cs7cs3.undersvc.core.Group;
import ie.tcd.cs7cs3.undersvc.db.GroupDAO;
import io.dropwizard.hibernate.UnitOfWork;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;

/**
 * {@link GroupsResource} is a resource for handling GET, POST and PUT requests to `/groups/{groupId}`.
 */
@Path("/groups/{groupID}")
@Produces(MediaType.APPLICATION_JSON)
public class GroupResource
{
    private final GroupDAO groupDAO;

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

    @DELETE
    @UnitOfWork
    public void handleGroupDeleteByID(@PathParam("groupID") long id)
    {
        this.groupDAO.delete(id);
    }
}
