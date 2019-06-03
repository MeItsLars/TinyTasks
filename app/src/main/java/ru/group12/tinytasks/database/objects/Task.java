package ru.group12.tinytasks.database.objects;

import java.io.Serializable;

public class Task implements Serializable {

    private String createdUserName;
    private User createdUser;
    private String category;
    private String cityLocation;
    private String street;
    private String priceRange;
    private String taskName;
    private String description;
    // private array[] images;

    public Task(String createdUserName, User createdUser, String category, String taskName, String description, String priceRange, String cityLocation, String street
    ) {
        this.createdUser = createdUser;
        this.createdUserName = createdUserName;
        this.category = category;
        this.taskName = taskName;
        this.description = description;
        this.priceRange = priceRange;
        setLocation(createdUser, cityLocation, street);
    }

    public void setLocation (User createdUser, String cityLocation, String street) {
        if (street.equals("Empty")) {
            // this.street = createdUser.getStreet;
        }
        else
            this.street = street;
        if (cityLocation.equals("Empty")) {
            // this.street = createdUser.getStreet;
        }
        else
            this.cityLocation = cityLocation;
    }

    public String getCreatedUserName() {
        return createdUserName;
    }

    public User getCreatedUser() {
        return createdUser;
    }

    public String getTaskName() {
        return taskName;
    }

    public String getDescription() {
        return description;
    }

    public String getStreet() {
        return street;
    }

    public String getCategory (){
        return category;
    }

    public String getPriceRange() {
        return priceRange;
    }

    public String getCityLocation() {
        return cityLocation;
    }

    /* public void addImage () {
        Adds one image
    }

    public array[] getImages () {
        Gets all images
    }
     */
}
