package ie.tcd.cs7cs3.undersvc.api;

import java.util.List;
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

    public group(String groupState, List<point> points, List<UUID> memberUUIDs, long depTime, List<restriction> restrictions) {
        this.groupState = groupState;
        this.points = points;
        this.memberUUIDs = memberUUIDs;
        this.depTime = depTime;
        this.restrictions = restrictions;
    }

    public String getGroupState() {
        return groupState;
    }

    public List<point> getPoints() {
        return points;
    }

    public List<UUID> getMemberUUIDs() {
        return memberUUIDs;
    }

    public long getDepTime() {
        return depTime;
    }

    public List<restriction> getRestrictions() {
        return restrictions;
    }
}


