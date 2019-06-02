package ru.group12.tinytasks.database.enums;

public enum UserCreationResult {

    EMAIL_INVALID_LAYOUT("Invalid email address"),
    EMAIL_ALREADY_EXISTS("There is already an account with this email address"),
    PASSWORD_INVALID_LENGTH("The password should be 8-30 characters."),
    PASSWORD_NO_LOWERCASE("The password should contain at least one lowercase letter."),
    PASSWORD_NO_UPPERCASE("The password should contain at least one uppercase letter."),
    PASSWORD_NO_NUMBER("The password should contain at least one number."),
    PASSWORD_NO_SPECIAL_CHAR("The password should contain at least one special character (~`!@#$%^&*()+=_-{}[]\\|:;”’?/<>,.)."),
    FIRST_NAME_INVALID("Invalid first name."),
    LAST_NAME_INVALID("Invalid last name."),
    PHONE_NUMBER_INVALID("Invalid phone number."),
    BIRTH_DATE_INVALID("Invalid birth date."),
    GENDER_INVALID("Invalid gender."),
    SUCCESS("");

    private String message;

    UserCreationResult(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
