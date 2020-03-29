package ie.tcd.cs7cs3.undersvc.db;

import ie.tcd.cs7cs3.undersvc.core.GroupMember;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;

/**
 * GroupMemberDAO is the data access layer for GroupMembers.
 * If you want to perform Get and Update operations on Group Member ratings,
 * this is who you to talk to.
 */
public class GroupMemberDAO extends AbstractDAO<GroupMember> {
    /**
     * Creates a new DAO with a given session provider.
     *
     * @param sessionFactory a session provider
     */
    public GroupMemberDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public double findRatingById(final UUID uuid) {
        Query<?> query = this.namedQuery("ie.tcd.cs7cs3.undersvc.GroupMember.getRatingsByMemberId");
        query.setParameter("memberID", uuid);
//        query.executeUpdate();
        List<Double> ratingResult = (List<Double>) query.getResultList();
        //Prints all answers
        System.out.println("Ratings Results are:");
        System.out.println(ratingResult);
        return ratingResult.get(0);
    }

    public double updateRatingsById(final UUID uuid,final double rating) {
        // Getting previous rating of the user
        Query<?> query = this.namedQuery("ie.tcd.cs7cs3.undersvc.GroupMember.getRatingsByMemberId");
        query.setParameter("memberID", uuid);
//        query.execeuteQuery*(;
        List<Double> ratingResult = (List<Double>) query.getResultList();

        // Calculating new rating for user
        Double prevRating = ratingResult.get(0);
        Double newRating = (prevRating+rating) /2;

        // Updating new rating for the user
        query = this.namedQuery("ie.tcd.cs7cs3.undersvc.GroupMember.updateRatingsByMemberId");
        query.setParameter("memberID", uuid);
        query.setParameter("newRating", newRating);
        query.executeUpdate();

        return newRating;
    }

}
