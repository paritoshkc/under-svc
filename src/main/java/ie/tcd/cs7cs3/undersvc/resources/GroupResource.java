package ie.tcd.cs7cs3.undersvc.resources;


import ie.tcd.cs7cs3.undersvc.db.GroupDAO;

import javax.ws.rs.*;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.awt.*;

@Path("/groups/{groupID}")
@Produces(MediaType.APPLICATION_JSON)
public class GroupResource
{
    private GroupDAO groupDAO;

    /**
     * addGroupMember adds members to existing Groups
     */
    @POST
    public GroupsResource handleAddGroupMember() {

    }
}
