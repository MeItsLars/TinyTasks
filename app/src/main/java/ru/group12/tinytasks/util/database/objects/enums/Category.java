package ru.group12.tinytasks.util.database.objects.enums;

public enum Category {

    BRICKLAYING("Brick laying"),
    CARPENTING("Carpenting"),
    CLEANING("Cleaning"),
    COOKING("Cooking"),
    GARDEN_WORK("Garden work"),
    GROCERIES_SHOPPING("Groceries Shopping"),
    HOUSHOLDING("Householding"),
    PAINTING("Painting"),
    PLUMBING("Plumbing"),
    REPAIRING("Repairing"),
    TUTORING("Tutoring"),
    OTHER("Other");

    private String name;

    Category(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
