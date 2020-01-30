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
}
