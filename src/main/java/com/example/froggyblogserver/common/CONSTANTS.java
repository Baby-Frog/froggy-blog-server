package com.example.froggyblogserver.common;

public class CONSTANTS {

    public static class BOOLEAN {
        public final static boolean TRUE = true;
        public final static boolean FALSE = false;
    }
    public static class POST_STATUS{
        public final static String BANNED = "BANNED";
        public final static String ABORT = "ABORT";
        public final static String PUBLISHED = "PUBLISHED";
        public final static String PENDING = "PENDING";
    }

    public static class PROPERTIES{
        public final static String EMAIL = "email";
        public final static String PASSWORD = "password";
        public final static String TOKEN = "token";
        public final static String CAPTCHA = "captcha";
    }

    public static class ROLE{
        public static final String USER = "USER";
        public static final String PUBLISHER = "PUBLISHER";
        public static final String ADMINISTRATOR = "ADMINISTRATOR";
    }
    public  static class PROVIDER{
        public static final String FACEBOOK = "FACEBOOK";
        public static final String GOOGLE = "GOOGLE";
        public static final String SYSTEM = "SYSTEM";
    }

    public static class SORT{
        public static final String ASC = "ASC";
        public static final String DESC = "DESC";
    }
}
