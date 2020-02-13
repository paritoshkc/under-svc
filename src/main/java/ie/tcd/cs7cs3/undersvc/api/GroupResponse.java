package ie.tcd.cs7cs3.undersvc.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import ie.tcd.cs7cs3.undersvc.core.Group;
import ie.tcd.cs7cs3.undersvc.core.GroupRestriction;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * group is a class to represent a group in JSON.
 */

public class GroupResponse {
    public static final String Forming = "Forming";
    public static final String Moving = "Moving";
    public static final String Finished = "Finished";
    private long groupId;
    private String groupState;
    private String points;
    private List<String> memberUUIDs;
    private long createTime;
    private long depTime;
    private Map<String, Integer> restrictions;

    public GroupResponse() {
        // jackson
    }

    public GroupResponse(long groupID, String groupState, String points, List<String> memberUUIDs, long createTime, long depTime, HashMap<String, Integer> restrictions) {
        this.groupId = groupID;
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
    public GroupResponse(@Nonnull final Group groupEntity)
    {
        this.groupId=groupEntity.getId();
        this.groupState = groupEntity.getState();
        this.points = groupEntity.getPoints().toText();
        this.memberUUIDs = groupEntity.getGroupMembers().stream().map(gm -> gm.getUuid().toString()).collect(Collectors.toList());
        this.depTime = groupEntity.getDepartureTimestamp();
        this.createTime = groupEntity.getCreationTimestamp();
        this.restrictions = new HashMap<>();
        for (final GroupRestriction gr : groupEntity.getGroupRestrictions()) {
            restrictions.put(gr.getType(), gr.getValue());
        }
    }

    public long getGroupId() {
        return groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
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
    public List<String> getMemberUUIDs() {
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
    public Map<String, Integer> getRestrictions() {
        return restrictions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GroupResponse group = (GroupResponse) o;
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


