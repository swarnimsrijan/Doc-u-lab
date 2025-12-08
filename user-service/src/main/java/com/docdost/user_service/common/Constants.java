package com.docdost.user_service.common;

public final class Constants {

    private Constants() {}  // prevent instantiation

    // ============================================================
    // JWT CONSTANTS
    // ============================================================
    public static final String JWT_SECRET = "your-very-secure-secret-key";
    public static final long JWT_EXPIRATION_MS = 86400000; // 24 hours
    public static final String JWT_HEADER = "Authorization";
    public static final String JWT_TOKEN_PREFIX = "Bearer ";
    public static final String JWT_CLAIM_ROLES = "roles";
    public static final String JWT_CLAIM_USER_ID = "userId";


    // ============================================================
    // USER VALIDATION CONSTANTS
    // ============================================================
    public static final int USERNAME_MIN_LENGTH = 3;
    public static final int USERNAME_MAX_LENGTH = 25;
    public static final int PASSWORD_MIN_LENGTH = 8;

    public static final String EMAIL_REGEX =
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

    public static final String USERNAME_REGEX =
            "^[A-Za-z0-9_]{3,25}$";  // alphanumeric + underscore


    // ============================================================
    // ROLE NAMES
    // ============================================================
    public static final String ROLE_USER = "USER";
    public static final String ROLE_ADMIN = "ADMIN";
    public static final String ROLE_OWNER = "OWNER";
    public static final String ROLE_EDITOR = "EDITOR";
    public static final String ROLE_READER = "READER";


    // ============================================================
    // API PATHS
    // ============================================================
    public static final String API_BASE = "/api/v1";
    public static final String AUTH_BASE = API_BASE + "/auth";
    public static final String USER_BASE = API_BASE + "/users";


    // ============================================================
    // LOGGING MESSAGES
    // ============================================================
    public static final String LOG_USER_REGISTERED = "User successfully registered: {}";
    public static final String LOG_USER_LOGIN = "User login attempt: {}";
    public static final String LOG_USER_LOGGED_IN = "User logged in successfully: {}";
    public static final String LOG_USER_UPDATED = "User profile updated for userId: {}";
    public static final String LOG_USER_NOT_FOUND = "User not found with identifier: {}";
    public static final String LOG_JWT_GENERATED = "JWT token generated for userId: {}";
    public static final String LOG_JWT_VALIDATION_FAILED = "JWT validation failed: {}";
    public static final String LOG_UNAUTHORIZED_ACCESS =
            "Unauthorized access attempt detected from IP: {}";
    public static final String LOG_PASSWORD_UPDATED = "Password updated for userId: {}";


    // ============================================================
    // SUCCESS MESSAGES
    // ============================================================
    public static final String SUCCESS_USER_REGISTER = "User registered successfully.";
    public static final String SUCCESS_USER_LOGIN = "User login successful.";
    public static final String SUCCESS_USER_UPDATE = "User details updated successfully.";
    public static final String SUCCESS_PASSWORD_UPDATE = "Password updated successfully.";
    public static final String SUCCESS_TOKEN_REFRESH = "Token refreshed successfully.";


    // ============================================================
    // ERROR MESSAGES
    // ============================================================
    public static final String ERROR_USER_ALREADY_EXISTS =
            "A user with this email already exists.";
    public static final String ERROR_USERNAME_TAKEN =
            "This username is already taken.";
    public static final String ERROR_INVALID_CREDENTIALS =
            "Invalid email/username or password.";
    public static final String ERROR_INVALID_EMAIL_FORMAT =
            "Email format is invalid.";
    public static final String ERROR_INVALID_USERNAME_FORMAT =
            "Username must be alphanumeric and between 3–25 characters.";
    public static final String ERROR_USER_NOT_FOUND = "User not found.";
    public static final String ERROR_UNAUTHORIZED = "Unauthorized request.";
    public static final String ERROR_FORBIDDEN = "You do not have access.";
    public static final String ERROR_TOKEN_EXPIRED = "Token has expired.";
    public static final String ERROR_INVALID_TOKEN = "Invalid JWT token.";
    public static final String ERROR_PASSWORD_TOO_WEAK =
            "Password is too weak. Minimum 8 characters required.";
    public static final String ERROR_SOMETHING_WENT_WRONG =
            "Something went wrong. Please try again later.";


    // ============================================================
    // SYSTEM CONSTANTS
    // ============================================================
    public static final String SYSTEM_USER_ID = "SYSTEM";
    public static final String TIMEZONE_UTC = "UTC";
    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";


    // ============================================================
    // EXCEPTION TYPES / KEYS
    // ============================================================
    public static final String EX_USER_NOT_FOUND = "USER_NOT_FOUND";
    public static final String EX_INVALID_TOKEN = "INVALID_TOKEN";
    public static final String EX_BAD_REQUEST = "BAD_REQUEST";
    public static final String EX_INTERNAL_ERROR = "INTERNAL_SERVER_ERROR";
}
