package ie.tcd.cs7cs3.undersvc.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import ie.tcd.cs7cs3.undersvc.core.Group;
import ie.tcd.cs7cs3.undersvc.core.GroupMember;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * group is a class to represent a group in JSON.
 */

public class group {
    public static final String Forming = "Forming";
    public static final String Moving = "Moving";
    public static final String Finished = "Finished";
    private String groupState;
    private String points;
    private List<UUID> memberUUIDs;
    private long createTime;
    private long depTime;
    private List<restriction> restrictions;

    public group() {
        // jackson
    }

    public group(String groupState, String points, List<UUID> memberUUIDs, long createTime, long depTime, List<restriction> restrictions) {
        this.groupState = groupState;
        this.points = points;
        this.memberUUIDs = memberUUIDs;
        this.depTime = depTime;
        this.createTime = createTime;
        this.restrictions = restrictions;
    }

    /**
     * This is a convenience constructor for transforming an api.group to a core.Group.
     * @param groupEntity the core Group entity
     */
    public group(@Nonnull final Group groupEntity) {
        this.groupState = groupEntity.getState();
        this.points = groupEntity.getPoints().toText();
        this.memberUUIDs = groupEntity.getGroupMembers().stream().map(GroupMember::getUuid).collect(Collectors.toList());
        this.depTime = groupEntity.getDepartureTimestamp();
        this.createTime = groupEntity.getCreationTimestamp();
        this.restrictions = groupEntity.getGroupRestrictions().stream().map(r -> new restriction(r.getType(), r.getValue())).collect(Collectors.toList());
    }

    @JsonProperty
    public String getGroupState() {
        return groupState;
    }

    @JsonProperty
    public String getPoints() {
        return points;
    }

    @JsonProperty
    public List<UUID> getMemberUUIDs() {
        return memberUUIDs;
    }

    @JsonProperty
    public long getCreateTime() {
        return createTime;
    }

    @JsonProperty
    public long getDepTime() {
        return depTime;
    }

    @JsonProperty
    public List<restriction> getRestrictions() {
        return restrictions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        group group = (group) o;
        return depTime == group.depTime &&
                createTime == group.createTime &&
                groupState.equals(group.groupState) &&
                points.equals(group.points) &&
                memberUUIDs.equals(group.memberUUIDs) &&
                restrictions.equals(group.restrictions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(groupState, points, memberUUIDs, depTime, restrictions);
    }
}


