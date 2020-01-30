package ie.tcd.cs7cs3.undersvc.core;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "group_members")
public class GroupMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    @Column(name = "uuid")
    private UUID uuid;
}
