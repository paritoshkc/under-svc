package ie.tcd.cs7cs3.undersvc.core;

import javax.persistence.*;

@Entity
@Table(name="group_restrictions")
public class GroupRestriction {
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
