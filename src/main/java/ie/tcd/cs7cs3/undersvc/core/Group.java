package ie.tcd.cs7cs3.undersvc.core;

import com.google.common.collect.ImmutableList;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.MultiPoint;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="groups")
@NamedQueries(
        {
                @NamedQuery(
                    name="ie.tcd.cs7cs3.undersvc.Group.findAll",
                    query="SELECT g FROM Group g"
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

    public Group() {
        this.id = 0L;
        this.state = "FORMING";
        this.departureTimestamp = 0;
        this.creationTimestamp = System.currentTimeMillis();
        this.points = new MultiPoint(null, new GeometryFactory());
        this.groupMembers = new ArrayList<>();
        this.groupRestrictions = new ArrayList<>();
    }

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
}
