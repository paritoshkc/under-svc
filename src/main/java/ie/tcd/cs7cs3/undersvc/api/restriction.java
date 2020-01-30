package ie.tcd.cs7cs3.undersvc.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

/**
 * restriction represents a group restriction.
 */
public class restriction {
    public static String MaxPeople = "MaxNumberOfPeople";

    private String type;
    private int value;

    public restriction() {
        // jackson
    }

    public restriction(String type, int value) {
        this.type = type;
        this.value = value;
    }

    @JsonProperty
    public String getType() {
        return type;
    }

    @JsonProperty
    public int getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        restriction that = (restriction) o;
        return value == that.value &&
                type.equals(that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, value);
    }
}
