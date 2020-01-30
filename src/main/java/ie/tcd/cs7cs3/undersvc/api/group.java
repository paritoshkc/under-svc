package ie.tcd.cs7cs3.undersvc.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * group is a class to represent a group in JSON
 */

public class group {
    public static final String Forming = "Forming";
    public static final String Moving = "Moving";
    public static final String Finished = "Finished";
    private String groupState;
    private List<point> points;
    private List<UUID> memberUUIDs;
    private long depTime;
    private List<restriction> restrictions;

    public group() {
        // jackson
    }

    public group(String groupState, List<point> points, List<UUID> memberUUIDs, long depTime, List<restriction> restrictions) {
        this.groupState = groupState;
        this.points = points;
        this.memberUUIDs = memberUUIDs;
        this.depTime = depTime;
        this.restrictions = restrictions;
    }

    @JsonProperty
    public String getGroupState() {
        return groupState;
    }

    @JsonProperty
    public List<point> getPoints() {
        return points;
    }

    @JsonProperty
    public List<UUID> getMemberUUIDs() {
        return memberUUIDs;
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


