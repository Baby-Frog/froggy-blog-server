package com.example.froggyblogserver.common;

public class MESSAGE {
    public static class VALIDATE{
        public static final String USER_NOT_PERMISSION = "user are not role permission!!!";
        public static final String INPUT_INVALID = "Invalid";
        public static final String ID_INVALID = "Your ID invalid";
        public static final String EMAIL_INVALID = "Your e-mail address is invalid, please try another one";
        public static final String EMAIL_PASSWORD_INVALID = "Your e-mail address or password is invalid, please try again";
        public static final String EMAIL_ALREADY_EXIST = "E-mail address already exists, please try another one";
        public static final String VERIFY_EXPIRES = "Verify code has been expired";
        public static final String PASSWORD_INCORRECT = "Password and confirm password doesn't match";
        public static final String OLD_PASSWORD_INCORRECT = "Old password invalid";
        public static final String USER_NOT_EXIST = "User not exist!!!";
        public static final String PAGE_NUMBER_INVALID = "Page number invalid!!!";
        public static final String PAGE_SIZE_INVALID = "Page size invalid!!!";

    }

    public static class RESPONSE{
        public static final String REGISTER_SUCCESS = "Your account has been registered, please login to proceed.";
        public static final String REGISTER_FAIL = "Oops! Something happened, please try again later";
        public static final String FAIL = "Oops! Something happened, please try again later";
        public static final String CHANGE_PASSWORD_SUCCESS = "Change password success!!!";
        public static final String VALIDATE_ERROR = "Validate error!!!";
        public static final String SYSTEM_ERROR = "System error!!!";
        public static final String AUTH_ERROR = "Authentication error!!!";
        public static final String FORBIDDEN = "You don't have access denied!!!";
        public static final String LOG_OUT_SUCCESS = "Logout success!!!";
    }
    public static class TOKEN{
        public static final String TOKEN_INVALID = "Token invalid";
    }
}
