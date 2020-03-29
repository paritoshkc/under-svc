package ie.tcd.cs7cs3.undersvc.resources;

import ie.tcd.cs7cs3.undersvc.core.GroupMember;
import ie.tcd.cs7cs3.undersvc.db.GroupMemberDAO;
import io.dropwizard.hibernate.UnitOfWork;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.UUID;

/**
 * {@link UserRatingResource} is a resource for GET and POST to `/ratings/{memberId}`.
 */

@Path("/ratings/{memberId}")
@Produces(MediaType.APPLICATION_JSON)

public class UserRatingResource {
    private static final Logger LOG = LoggerFactory.getLogger(UserRatingResource.class);
    private GroupMemberDAO groupMemberDAO;

    public UserRatingResource(GroupMemberDAO groupMemberDAO) {
    }


    @GET
    @UnitOfWork
    public double handleUserRatingGetByMemberId(@PathParam("memberId")UUID uuid) {
        double ratings = this.groupMemberDAO.findRatingById(uuid);
        return ratings;
    }

    @POST
    @UnitOfWork
    public String handleUserRatingUpdateByMemberId(@PathParam("memberId") UUID uuid,
                                                 @QueryParam("rating") double rating){

        double newRating = this.groupMemberDAO.updateRatingsById(uuid,rating);
        return "User rating now updated to " + Double.toString(newRating);
    }
}
