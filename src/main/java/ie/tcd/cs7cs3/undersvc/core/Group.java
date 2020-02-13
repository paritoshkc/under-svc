package ie.tcd.cs7cs3.undersvc.core;

import ie.tcd.cs7cs3.undersvc.api.GroupResponse;
import ie.tcd.cs7cs3.undersvc.utils.GeometryUtils;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.io.ParseException;

import javax.persistence.*;
import java.util.*;

/**
 * Group is an entity class that represents how a group is stored in a relational database.
 * This is intentionally distinct from the {@link GroupResponse} class which exists only to be
 * serialized as JSON.
 */
@Entity
@Table(name="groups")
@NamedQueries(
        {
                @NamedQuery(
                    name="ie.tcd.cs7cs3.undersvc.Group.findAll",
                    query="SELECT g FROM Group g"
                ),
                @NamedQuery(
                        name = "ie.tcd.cs7cs3.undersvc.Group.deleteGroupById",
                        query = "DELETE FROM Group g WHERE g.id =:id "
                )
        }
)
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "state")
    private String state;

    @Column(name = "departure_timestamp")
    private long departureTimestamp;

    @Column(name = "creation_timestamp")
    private long creationTimestamp;

    @Column(name = "points")
    private MultiPoint points;

    @OneToMany(mappedBy = "group")
    private List<GroupMember> groupMembers;

    @OneToMany(mappedBy = "group")
    private List<GroupRestriction> groupRestrictions;

    /**
     * When creating a new Group, it's easiest to use this constructor and call the setter values first.
     */
    public Group() {
        this.id = 0L;
        this.state = "FORMING";
        this.departureTimestamp = 0;
        this.creationTimestamp = System.currentTimeMillis();
        this.points = new MultiPoint(null, new GeometryFactory());
        this.groupMembers = new ArrayList<>();
        this.groupRestrictions = new ArrayList<>();
    }

    /**
     * This constructor is mainly for Hibernate to be able to set fields properly. Don't feel like you need to use it.
     * @param id
     * @param state
     * @param creationTimestamp
     * @param departureTimestamp
     * @param points
     * @param groupMembers
     * @param groupRestrictions
     */
    public Group(
            final long id,
            final String state,
            final long creationTimestamp,
            final long departureTimestamp,
            final MultiPoint points,
            final List<GroupMember> groupMembers,
            final List<GroupRestriction> groupRestrictions
    ) {
        this.id = id;
        this.state = state;
        this.creationTimestamp = creationTimestamp;
        this.departureTimestamp = departureTimestamp;
        this.points = points;
        this.groupMembers = new ArrayList<>(groupMembers);
        this.groupRestrictions = new ArrayList<>(groupRestrictions);
    }

    /**
     * This constructor is a convenience method for transforming a {@link GroupResponse} to something
     * that can be stored in a relational database.
     * @param g the group object you want to transform
     * @throws ParseException if the group's points is not a valid Well-Known-Text representation of Geometry.
     */
    public Group(final GroupResponse g) throws ParseException {
        this.state = g.getGroupState();
        this.creationTimestamp = g.getCreateTime();
        this.departureTimestamp = g.getDepTime();
        this.points = GeometryUtils.WKT2MultiPoint(g.getPoints());
        this.groupMembers = new ArrayList<>();
        for (final String gmid : g.getMemberUUIDs()) {
            final GroupMember gm = new GroupMember();
            gm.setUuid(UUID.fromString(gmid));
            gm.setGroup(this);
            this.addGroupMember(gm);
        }
        this.groupRestrictions = new ArrayList<>();
        for (final HashMap.Entry<String, Integer> r : g.getRestrictions().entrySet()) {
            final GroupRestriction gr = new GroupRestriction();
            gr.setType(r.getKey());
            gr.setValue(r.getValue());
            gr.setGroup(this);
            this.addGroupRestriction(gr);
        }
    }

    public long getId() {
        return id;
    }

    public String getState() {
        return state;
    }

    public long getCreationTimestamp() {
        return creationTimestamp;
    }

    public long getDepartureTimestamp() {
        return departureTimestamp;
    }

    public MultiPoint getPoints() {
        return points;
    }

    public List<GroupMember> getGroupMembers() {
        return groupMembers;
    }

    public List<GroupRestriction> getGroupRestrictions() {
        return groupRestrictions;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setDepartureTimestamp(long departureTimestamp) {
        this.departureTimestamp = departureTimestamp;
    }

    public void setCreationTimestamp(long creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
    }

    public void setPoints(MultiPoint points) {
        this.points = points;
    }

    public void setGroupMembers(final List<GroupMember> groupMembers) {
        this.groupMembers = groupMembers;
    }

    public void setGroupRestrictions(final List<GroupRestriction> groupRestrictions) {
        this.groupRestrictions = groupRestrictions;
    }

    public void addGroupMember(final GroupMember m) {
        this.groupMembers.add(m);
    }

    public void addGroupRestriction(final GroupRestriction r) {
        this.groupRestrictions.add(r);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Group group = (Group) o;
        return id == group.id &&
                departureTimestamp == group.departureTimestamp &&
                creationTimestamp == group.creationTimestamp &&
                Objects.equals(state, group.state) &&
                Objects.equals(points, group.points) &&
                Objects.equals(groupMembers, group.groupMembers) &&
                Objects.equals(groupRestrictions, group.groupRestrictions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, state, departureTimestamp, creationTimestamp, points, groupMembers, groupRestrictions);
    }
}
