package ru.group12.tinytasks.database.objects;

public class User {

    private String name;
    private String surname;
    private String phoneNumber;
    private String birthdate;
    private String gender;

    public User(String name, String surname, String phoneNumber, String birthdate, String gender) {
        this.name = name;
        this.surname = surname;
        this.phoneNumber = phoneNumber;
        this.birthdate = birthdate;
        this.gender = gender;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public String getGender() {
        return gender;
    }
}
