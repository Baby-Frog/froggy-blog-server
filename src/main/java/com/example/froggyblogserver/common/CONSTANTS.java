package com.example.froggyblogserver.common;

public class CONSTANTS {

    public static class IS_DELETE{
        public final static byte TRUE = 1;
        public final static byte FALSE = 0;
    }
    public static class POST_STATUS{
        public final static String BANNED = "BANNED";
        public final static String ABORT = "ABORT";
        public final static String PUBLISHED = "PUBLISHED";
    }

    public static class PROPERTIES{
        public final static String EMAIL = "email";
        public final static String PASSWORD = "password";
        public final static String TOKEN = "token";
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
