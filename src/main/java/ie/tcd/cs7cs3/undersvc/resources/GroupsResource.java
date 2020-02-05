package ie.tcd.cs7cs3.undersvc.resources;

import ie.tcd.cs7cs3.undersvc.api.group;
import ie.tcd.cs7cs3.undersvc.core.Group;
import ie.tcd.cs7cs3.undersvc.db.GroupDAO;
import io.dropwizard.hibernate.UnitOfWork;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.stream.Collectors;

/**
 * {@link GroupsResource} is a resource for handling GET, POST and PUT requests to `/groups`.
 */
@Path("/groups")
@Produces(MediaType.APPLICATION_JSON)
public class GroupsResource {
    private final GroupDAO groupDAO;
    private static final Logger LOG = LoggerFactory.getLogger(GroupsResource.class);

    /**
     * GroupsResource creates a new instance of a GroupsResource.
     *
     * @param groupDAO a DAO for fetching groups from a datasource.
     */
    public GroupsResource(final GroupDAO groupDAO) {
        this.groupDAO = groupDAO;
    }

    @GET
    @UnitOfWork
    public List<group> handleGroupsGet() {
        return groupDAO.findAll().stream().map(group::new).collect(Collectors.toList());
    }

    @POST
    @UnitOfWork
    public group handleGroupsPost(@Valid group g) throws Exception {
        if (null == g) {
            throw new WebApplicationException();
        }
        final Group created = groupDAO.create(new Group(g));
        return new group(created);
    }
}
