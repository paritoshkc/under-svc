package ie.tcd.cs7cs3.undersvc.db;

import ie.tcd.cs7cs3.undersvc.api.group;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

import java.util.List;

public class GroupDAO extends AbstractDAO<group> {
    /**
     * GroupDAO creates a new DAO with a given session provider.
     *
     * @param sessionFactory a session provider
     */
    public GroupDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public List<group> listGroups() {
        return null;
    }
}
