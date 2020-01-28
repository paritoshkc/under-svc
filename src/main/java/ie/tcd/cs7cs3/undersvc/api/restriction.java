package ie.tcd.cs7cs3.undersvc.api;

public class restriction {
    public static String MaxPeople = "MaxNumberOfPeople";

    private String type;
    private int value;

    public restriction(String type, int value) {
        this.type = type;
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public int getValue() {
        return value;
    }
}
