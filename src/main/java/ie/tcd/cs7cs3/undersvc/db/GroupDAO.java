package ie.tcd.cs7cs3.undersvc.db;

import ie.tcd.cs7cs3.undersvc.api.group;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

public class GroupDAO extends AbstractDAO<group> {
    /**
     * Creates a new DAO with a given session provider.
     *
     * @param sessionFactory a session provider
     */
    public GroupDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }
}
