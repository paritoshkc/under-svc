package ie.tcd.cs7cs3.undersvc.core;

import javax.persistence.*;
import java.util.HashMap;

/**
 * GroupRestriction is an entity class for representing how a group restriction is stored in a backing database.
 */
@Entity
@Table(name="group_restrictions")
@NamedQueries(
        {
                @NamedQuery(
                        name="ie.tcd.cs7cs3.undersvc.GroupRestrictions.findAll",
                        query="SELECT g FROM GroupMember g"
                ),
                @NamedQuery(
                        name = "ie.tcd.cs7cs3.undersvc.GroupRestriction.deleteGroupRestrictionsById",
                        query = "DELETE FROM GroupRestriction g WHERE g.id =:id "
                )
        }
)
public class GroupRestriction{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    @Column(name = "type")
    private String type;

    @Column(name = "value")
    private int value;

    public GroupRestriction() {};

    public GroupRestriction(
            final long id,
            final Group group,
            final String type,
            final int value
    ) {
        this.id = id;
        this.group = group;
        this.type = type;
        this.value = value;
    }

    public Group getGroup() {
        return group;
    }

    public String getType() {
        return type;
    }

    public int getValue() {
        return value;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
