package main;

///this class for save variable line
public class VariableLine {
    public String name;
    public String type;
    public String value;
    public boolean isFinal;
    VariableLine(String type, String name, String value, boolean isFinal) {
        this.isFinal = isFinal;
        this.name = name;
        this.type = type;
        this.value = value;
    }
}
