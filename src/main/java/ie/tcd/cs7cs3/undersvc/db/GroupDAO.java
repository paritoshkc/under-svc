package ie.tcd.cs7cs3.undersvc.db;

import ie.tcd.cs7cs3.undersvc.core.Group;
import ie.tcd.cs7cs3.undersvc.core.GroupMember;
import ie.tcd.cs7cs3.undersvc.core.GroupRestriction;
import io.dropwizard.hibernate.AbstractDAO;
import org.geolatte.geom.MultiPoint;
import org.hibernate.Session;
import org.hibernate.SessionBuilder;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.persistence.Parameter;
import javax.transaction.Transactional;
import java.sql.SQLException;
import java.util.*;

/**
 * GroupDAO is the data access layer for Groups. If you want to perform CRUD operations on groups, this is who you want
 * to talk to.
 */

public class GroupDAO extends AbstractDAO<Group> {
    private long groupId;
    private List<String> memberIds;

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

    /**
     *
     * @param g
     * @return
     */
    public Group create(final Group g) {
        final Session session = currentSession();
        final Group created = persist(g);
        for (final GroupMember gm : g.getGroupMembers()) {
            session.persist(gm);
        }
        for (final GroupRestriction r : g.getGroupRestrictions()) {
            session.persist(r);
        }
        return created;
    }

    public void deleteMemberByUuid(long groupId, List<String> memberIds)
    {
        this.groupId = groupId;
        System.out.println("deleteMemberByUuid: groupId: " + groupId);
        this.memberIds = memberIds;

        List<?> uuid;
        Query<?> q = this.namedQuery("ie.tcd.cs7cs3.undersvc.GroupMember.findMembersByGroupId");

        q.setParameter("groupId", groupId );

//        q.executeUpdate();
        uuid = q.list();
        for(int i =0; i<uuid.size(); i++)
            System.out.println(i + " : Existing List of members : deleteMemberByUuid: memberId: "+ uuid.get(i));
        for(int i =0; i<memberIds.size(); i++)
            System.out.println(i + "Input list of members: deleteMemberByUuid: memberId: "+ memberIds.get(i));

//        uuid = q.getResultList();
        Iterator iterator = uuid.iterator();
        Transaction tx = null;
        int ctr = 0;
        while(iterator.hasNext())
        {
//            UUID temp = (UUID) iterator.next();
            UUID temp = (UUID) iterator.next();
            System.out.println(ctr + " : temp (member in existing list)" + temp);
            if(memberIds.contains(temp.toString()))
            {
                System.out.println("members(input list) contains temp (in existing list)");
//                continue;
            }
            else
            {
                System.out.println("members(input list) DOES NOT contain temp");
                Query<?> q1 = this.namedQuery("ie.tcd.cs7cs3.undersvc.GroupMember.deleteGroupMembersById");
                System.out.println("Query : " + q1.getQueryString());
                System.out.println("inside ELSE: temp: " + temp);
//                System.out.println("inside ELSE: groupId: " + groupId);
                q1.setParameter("id", (UUID)temp);
//                q1.setParameter("groupId", groupId);
                Parameter<?> u = q1.getParameter("id");
//                Parameter<?> g = q1.getParameter("groupId");
                System.out.println("parameter uuid: " + u + " // " + u.toString());
//                System.out.println("parameter groupID: " + g + " // " + g.toString());
                q1.executeUpdate();
            }
            System.out.println(ctr + " : END WHILE");
            ctr++;
        }
        Query<?> q2 = this.namedQuery("ie.tcd.cs7cs3.undersvc.GroupMember.findMembersByGroupId");
        q2.setParameter("groupId", groupId );
//        q.executeUpdate();
        uuid = q2.list();
        System.out.println("After deleting: ");
        for(int i =0; i<uuid.size(); i++)
            System.out.println(i + " : Existing List of members : deleteMemberByUuid: memberId: "+ uuid.get(i));
    }
    public boolean isMembersSame(long groupId, List<UUID> memberIds)
    {
        List<?> uuid;
        Query<?> q = this.namedQuery("ie.tcd.cs7cs3.undersvc.GroupMember.findMembersByGroupId");
        q.setParameter("groupId", groupId );
        q.executeUpdate();
        uuid = q.getResultList();
        return memberIds.equals(uuid);
    }

    public List<UUID> getAdditionalMembersInGroup(long groupId, List<UUID> memberIds)
    {
        List<?> uuid;
        List<UUID> output = new ArrayList<UUID>();
        Query<?> q = this.namedQuery("ie.tcd.cs7cs3.undersvc.GroupMember.findMembersByGroupId");
        q.setParameter("groupId", groupId );
        q.executeUpdate();
        uuid = q.getResultList();
        Iterator iterator = memberIds.iterator();
        while (iterator.hasNext())
        {
            UUID temp = (UUID) iterator.next();
            if (!uuid.contains(temp))
                output.add(temp);
        }
        return output;
    }
    public void deleteGroupById(long id)
    {
        Query<?> q = this.namedQuery("ie.tcd.cs7cs3.undersvc.GroupRestriction.deleteGroupRestrictionsById");
        q.setParameter("id", id);
        q.executeUpdate();

        q = this.namedQuery("ie.tcd.cs7cs3.undersvc.GroupMember.deleteGroupMembersByGroupId");
        q.setParameter("groupId", id);
        q.executeUpdate();

        q = this.namedQuery("ie.tcd.cs7cs3.undersvc.Group.deleteGroupById");
        q.setParameter("id",id);
        q.executeUpdate();
    }

    public void deleteMemberById(long groupId, long memberId)
    {
        Query<?> q = this.namedQuery("ie.tcd.cs7cs3.undersvc.GroupMember.deleteGroupMembersById");
        System.out.println("Query : " + q.getQueryString());
//                System.out.println("inside ELSE: groupId: " + groupId);
        q.setParameter("id", memberId);
        q.setParameter("gid", groupId);
        q.executeUpdate();
    }
//    public void deleteMemberById(long memberId)
//    {
//        Query<?> q = this.namedQuery("ie.tcd.cs7cs3.undersvc.GroupMember.deleteGroupMembersById");
//        System.out.println("Query : " + q.getQueryString());
////                System.out.println("inside ELSE: groupId: " + groupId);
////        UUID uuid = UUID.fromString(memberId);
//
////        q.setParameter("id", uuid);
//        q.setParameter("id", memberId);
////        q.setParameter("groupId", groupId);
//        q.executeUpdate();
//    }


}
