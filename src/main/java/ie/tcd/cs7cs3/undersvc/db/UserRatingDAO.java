package ie.tcd.cs7cs3.undersvc.db;

import ie.tcd.cs7cs3.undersvc.core.GroupMember;
import ie.tcd.cs7cs3.undersvc.core.UserRating;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import javax.validation.constraints.Null;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

/**
 * GroupMemberDAO is the data access layer for GroupMembers.
 * If you want to perform Get and Update operations on Group Member ratings,
 * this is who you to talk to.
 */
public class UserRatingDAO extends AbstractDAO<UserRating> {
    /**
     * Creates a new DAO with a given session provider.
     * @param sessionFactory a session provider
     */
    public UserRatingDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    /**
     * gets all the columns (user ratings) for the corresponding user uuid
     * @param uuid user id for fetching ratings
     * @return averaged rating of that user
     */
    public double getRatingById(final UUID uuid) {
        Query<?> query = this.namedQuery(
                "ie.tcd.cs7cs3.undersvc.UserRating.getRatingsByMemberId");
        query.setParameter("memberID", uuid);
        List<Double> ratingResult = (List<Double>) query.getResultList();
        Iterator iterator = ratingResult.iterator();
        int count = ratingResult.size();
        double total = 0.0;

        // What if no user-rating exists for that user
        if (count == 0) {
            System.out.println("No Ratings exist for this user");
            return 0; }

        // Averaging
        while (iterator.hasNext()) total += (double) iterator.next();
        total = total/count;

        //Prints all answers
        System.out.println("Ratings Results are:");
        System.out.println(total);
        return total;
    }

    /**
     * simply inserts another entry into the user_ratings table
     * @param uuid member uuid
     * @param rating rating you want to give
     * @return (should be optional) return a successful message
     */
    public void insertRatingsById(final UUID uuid, final double rating) {
        final Session session = currentSession();

        final UserRating inserted = new UserRating();
        inserted.setRating(rating);
        inserted.setUuid(uuid);
        session.persist(inserted);
        session.getTransaction().commit();

    }

}
