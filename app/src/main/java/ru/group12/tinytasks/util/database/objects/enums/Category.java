package ru.group12.tinytasks.util.database.objects.enums;

public enum Category {

    HOUSE_WORK("House work"),
    GARDEN_WORK("Garden work"),
    OTHER("Other");

    private String name;

    Category(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
