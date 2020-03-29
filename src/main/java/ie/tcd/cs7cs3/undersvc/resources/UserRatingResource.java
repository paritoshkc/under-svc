package ie.tcd.cs7cs3.undersvc.resources;

import ie.tcd.cs7cs3.undersvc.db.UserRatingDAO;
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
    private UserRatingDAO userRatingDAO;

    public UserRatingResource(UserRatingDAO userRatingDAO) {
        this.userRatingDAO = userRatingDAO;
    }


    @GET
    @UnitOfWork
    public double handleUserRatingGetByMemberId(@PathParam("memberId")UUID uuid) {
        System.out.println("Hitting IT");
        double ratings = this.userRatingDAO.getRatingById(uuid);
        return ratings;
    }

    @POST
    @UnitOfWork
    public String handleUserRatingUpdateByMemberId(@PathParam("memberId") UUID uuid,
                                                 @QueryParam("rating") double rating){
        System.out.println("your entered rating is");
        System.out.println(rating);

        this.userRatingDAO.insertRatingsById(uuid,rating);
        return "successfully inserted " + Double.toString(rating) + "to" + uuid;
    }
}
