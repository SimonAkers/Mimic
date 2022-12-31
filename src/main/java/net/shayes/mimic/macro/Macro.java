package net.shayes.mimic.macro;

public abstract class Macro {
    String name;

    public Macro() {

    }

    public Macro(String name) {
        this.name = name;
    }

    public abstract void execute();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
