package ca.cmpt276.iteration1.model;

/*
    Child class:
    -   Stores name of child to be added to a children manager in main menu.
 */

public class Child {
    private String name;
    private String bitmap;
    public Child(String name) {
        this.name = name;
    }

    public Child(String name, String bitmap) {
        this.name = name;
        this.bitmap = bitmap;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setBitmap(String bitmap) {
        this.bitmap = bitmap;
    }

    public  String getBitmap() {
        return bitmap;
    }
}
