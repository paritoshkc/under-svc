package ie.tcd.cs7cs3.undersvc.db;

import ie.tcd.cs7cs3.undersvc.core.Group;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Optional;

/**
 * GroupDAO is the data access layer for Groups. If you want to perform CRUD operations on groups, this is who you want
 * to talk to.
 */
public class GroupDAO extends AbstractDAO<Group> {
    /**
     * GroupDAO creates a new DAO with a given session provider.
     *
     * @param sessionFactory a session provider
     */
    public GroupDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public Optional<Group> findById(final long id) {
        return Optional.ofNullable(this.get(id));
    }

    public List<Group> findAll() {
        return this.list((Query<Group>) this.namedQuery("ie.tcd.cs7cs3.undersvc.Group.findAll"));
    }

    public Group create(final Group g) {
        return persist(g);
    }
}
