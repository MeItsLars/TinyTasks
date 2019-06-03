package ru.group12.tinytasks.util.database.objects;

public class User {

    private String uid;
    private String email;
    private String name;
    private String surname;
    private String phoneNumber;
    private String birthdate;
    private String gender;

    public User(String uid, String email, String name, String surname, String phoneNumber, String birthdate, String gender) {
        this.name = name;
        this.surname = surname;
        this.phoneNumber = phoneNumber;
        this.birthdate = birthdate;
        this.gender = gender;
    }

    public String getUid() {
        return uid;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getFullName() {
        return name.concat(" ").concat(surname);
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
