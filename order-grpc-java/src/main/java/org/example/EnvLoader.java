package org.example;

import io.github.cdimascio.dotenv.Dotenv;

public class EnvLoader {
    private static final Dotenv dotenv = Dotenv.load();

    public static String getDatabaseUrl() {
        return dotenv.get("DATABASE_URL");
    }
    public static String getDatabaseUser() {
        return dotenv.get("DATABASE_USER");
    }
    public static String getDatabasePassword() {
        return dotenv.get("DATABASE_PASSWORD");
    }
}