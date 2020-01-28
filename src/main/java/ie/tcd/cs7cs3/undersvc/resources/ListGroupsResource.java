package ie.tcd.cs7cs3.undersvc.resources;

import com.google.common.collect.ImmutableList;
import ie.tcd.cs7cs3.undersvc.api.group;
import ie.tcd.cs7cs3.undersvc.api.point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.UUID;

@Path("/groups")
@Produces(MediaType.APPLICATION_JSON)
public class ListGroupsResource {
    private static final Logger LOG = LoggerFactory.getLogger(ListGroupsResource.class);
    private static final List<group> dummyGroups = ImmutableList.of(
            new group(
                    group.Forming,
                    ImmutableList.of(new point(1.0, 2.0)),
                    ImmutableList.of(UUID.fromString("783575bf-5629-44af-9a5e-c22174d722e3")),
                    1234567,
                    ImmutableList.of()
            )
    );

    public ListGroupsResource() {}

    @GET
    public List<group> handleGroupsGet() {
        LOG.info("found {} groups", dummyGroups.size());
        return dummyGroups;
    }
}
