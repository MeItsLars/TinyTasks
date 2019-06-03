package ru.group12.tinytasks.util.database.objects.enums;

public enum Category {

    BRICKLAYING,
    CARPENT,
    CLEANING,
    COOKING,
    GARDEN_WORK("Garden work"),
    GROCERIES_SHOPPING("Groceries Shopping"),
    PAINT,
    PLUMB,
    REPAIR,
    TUTORING,
    VACUUMING,
    WASTE_DISPOAL("Waste Disposal"),
    OTHER("Other");

    private String name;

    Category(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
