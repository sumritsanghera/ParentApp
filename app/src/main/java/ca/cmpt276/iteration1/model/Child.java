package ca.cmpt276.iteration1.model;

/*
    Child class:
    -   Stores name of child to be added to a children manager in main menu.
 */

public class Child {
    private String name;
    public Child(String name) {
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
