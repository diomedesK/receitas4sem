package com.receitas.app.utils;

public class MyLogger {
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_BOLD = "\u001B[1m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_WHITE = "\u001B[37m";
    private static final String ANSI_GREEN = "\u001B[32m";

    public static void error(String message) {
        System.out.println(ANSI_BOLD + ANSI_RED + "Error: " + message + ANSI_RESET);
    }

    public static void warning(String message) {
        System.out.println(ANSI_BOLD + ANSI_YELLOW + "Warning: " + message + ANSI_RESET);
    }

    public static void info(String message) {
        System.out.println(ANSI_BOLD + ANSI_WHITE + message + ANSI_RESET);
    }

    public static void success(String message) {
        System.out.println(ANSI_BOLD + ANSI_GREEN + message + ANSI_RESET);
    }

}

