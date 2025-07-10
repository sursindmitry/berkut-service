package com.sursindmitry.berkutservice.constant;

/**
 * Константы для кодов ошибок.
 */
public final class ErrorCode {
    private ErrorCode() {
    }

    /**
     * Коды ошибок для пользователя.
     */
    public static final class User {
        private User() {
        }

        public static final String FIRST_NAME_INVALID = "user.firstName.invalid";
        public static final String LAST_NAME_INVALID = "user.lastName.invalid";
        public static final String EMAIL_NAME_INVALID = "user.email.invalid";
        public static final String PASSWORD_NAME_INVALID = "user.password.invalid";

    }
}
