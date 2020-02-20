package ie.tcd.cs7cs3.undersvc.core;

import javax.persistence.*;
import java.util.UUID;

/**
 * GroupMember is an entity class representing a member of a group, as stored in a backing database.
 */
@Entity
@Table(name = "group_members")
@NamedQueries(
        value = {
                @NamedQuery(
                        name = "ie.tcd.cs7cs3.undersvc.GroupMember.findAll",
                        query = "SELECT g FROM GroupMember g"
                ),
                @NamedQuery(
                        name = "ie.tcd.cs7cs3.undersvc.GroupMember.findMembersByGroupId",
                        query = "SELECT g.uuid FROM GroupMember g WHERE g.group.id =:groupId"
                ),
                @NamedQuery(
                        name = "ie.tcd.cs7cs3.undersvc.GroupMember.deleteGroupMembersById",
                        query = "DELETE FROM GroupMember g WHERE g.uuid =:uuid AND g.group.id =:groupId"
                )
        }
)
public class GroupMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    @Column(name = "uuid")
    private UUID uuid;

    public GroupMember() {}

    public GroupMember(
            final long id,
            final Group group,
            final UUID uuid
    ) {
        this.id = id;
        this.group = group;
        this.uuid = uuid;
    }

    public Group getGroup() {
        return group;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }
}
