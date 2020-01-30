package ie.tcd.cs7cs3.undersvc.resources;

import com.google.common.collect.ImmutableList;
import ie.tcd.cs7cs3.undersvc.api.group;
import ie.tcd.cs7cs3.undersvc.api.point;
import ie.tcd.cs7cs3.undersvc.db.GroupDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.UUID;

/**
 * {@link GroupsResource} is a resource for handling GET, POST and PUT requests to `/groups`.
 */
@Path("/groups")
@Produces(MediaType.APPLICATION_JSON)
public class GroupsResource {
    private final GroupDAO groupDAO;
    private static final Logger LOG = LoggerFactory.getLogger(GroupsResource.class);
    private static final List<group> dummyGroups = ImmutableList.of(
            new group(
                    group.Forming,
                    ImmutableList.of(new point(1.0, 2.0)),
                    ImmutableList.of(UUID.fromString("783575bf-5629-44af-9a5e-c22174d722e3")),
                    1234567,
                    ImmutableList.of()
            )
    );

    /**
     * GroupsResource creates a new instance of a GroupsResource.
     *
     * @param groupDAO a DAO for fetching groups from a datasource.
     */
    public GroupsResource(final GroupDAO groupDAO) {
        this.groupDAO = groupDAO;
    }

    @GET
    public List<group> handleGroupsGet() {
        // TODO: fetch from DB
        LOG.info("found {} groups", dummyGroups.size());
        return dummyGroups;
    }
}
