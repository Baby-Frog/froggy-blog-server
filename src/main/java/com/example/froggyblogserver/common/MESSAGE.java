package com.example.froggyblogserver.common;

public class MESSAGE {
    public static class VALIDATE{
        public static final String INPUT_INVALID = "Input invalid!!!";
        public static final String IP_EMPTY = "IP is not null";
        public static final String IP_INVALID = "IP invalid";
        public static final String USERNAME_PASSWORD_INVALID = "Username or password invalid!!!";
        public static final String USERNAME_ALREADY_EXIST = "Username already exist!!!";
    }

    public static class RESPONSE{
        public static final String REGISTER_SUCCESS = "Register success!!!";
        public static final String REGISTER_FAIL = "Register failed, please try again in minutes!!!";
    }
    public static class TOKEN{
        public static final String TOKEN_INVALID = "Token invalid";
    }
}
