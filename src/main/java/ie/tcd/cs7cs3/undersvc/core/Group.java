package ie.tcd.cs7cs3.undersvc.core;

import org.locationtech.jts.geom.MultiPoint;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name="groups")
@NamedQueries(
        {
                @NamedQuery(
                    name="ie.tcd.cs7cs3.undersvc.Group.findAll",
                    query="SELECT 1 FROM groups;" // TODO
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

    @Column(name = "points")
    private MultiPoint points;

    @OneToMany(mappedBy = "group_members")
    private List<GroupMember> groupMembers;

    @OneToMany(mappedBy = "group_restrictions")
    private List<GroupRestriction> groupRestrictions;
}
