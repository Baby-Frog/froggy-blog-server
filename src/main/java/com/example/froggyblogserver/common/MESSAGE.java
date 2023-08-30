package com.example.froggyblogserver.common;

public class MESSAGE {
    public static class VALIDATE{
        public static final String USER_NOT_PERMISSION = "user are not role permission!!!";
        public static final String INPUT_INVALID = "Invalid";
        public static final String EMAIL_INVALID = "Your e-mail address is invalid, please try another one";
        public static final String EMAIL_PASSWORD_INVALID = "Your e-mail address or password is invalid, please try again";
        public static final String EMAIL_ALREADY_EXIST = "E-mail address already exists, please try another one";
        public static final String VERIFY_EXPIRES = "Verify code has been expired";
        public static final String PASSWORD_INCORRECT = "Password and confirm password doesn't match";

    }

    public static class RESPONSE{
        public static final String REGISTER_SUCCESS = "Your account has been registered, please login to proceed.";
        public static final String REGISTER_FAIL = "Oops! Something happened, please try again later";
        public static final String FAIL = "Oops! Something happened, please try again later";
    }
    public static class TOKEN{
        public static final String TOKEN_INVALID = "Token invalid";
    }
}
