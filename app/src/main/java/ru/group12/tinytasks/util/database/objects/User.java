package ru.group12.tinytasks.util.database.objects;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {

    private String uid;
    private String email;
    private String name;
    private String surname;
    private String phoneNumber;
    private String birthdate;
    private String gender;

    public User(String uid, String email, String name, String surname, String phoneNumber, String birthdate, String gender) {
        this.uid =  uid;
        this.email = email;
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

    protected User(Parcel in) {
        uid = in.readString();
        email = in.readString();
        name = in.readString();
        surname = in.readString();
        phoneNumber = in.readString();
        birthdate = in.readString();
        gender = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uid);
        dest.writeString(email);
        dest.writeString(name);
        dest.writeString(surname);
        dest.writeString(phoneNumber);
        dest.writeString(birthdate);
        dest.writeString(gender);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}