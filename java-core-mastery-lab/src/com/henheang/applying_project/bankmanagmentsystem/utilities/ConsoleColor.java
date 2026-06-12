package com.henheang.applying_project.bankmanagmentsystem.utilities;

public class ConsoleColor {

    // ANSI escape code format: \u001B[<code's
    // \u001B is the ESC character, [0m resets all formatting

    public static final String RESET  = "\u001B[0m";  // back to normal
    public static final String RED    = "\u001B[31m"; // errors
    public static final String GREEN  = "\u001B[32m"; // success
    public static final String YELLOW = "\u001B[33m"; // warnings / prompts
    public static final String CYAN   = "\u001B[36m"; // titles / headers
    public static final String BOLD   = "\u001B[1m";  // bold text

    // Helper methods so you don't have to write RESET every time
    public static String red(String text)    { return RED    + text + RESET; }
    public static String green(String text)  { return GREEN  + text + RESET; }
    public static String yellow(String text) { return YELLOW + text + RESET; }
    public static String cyan(String text)   { return CYAN   + text + RESET; }
    public static String bold(String text)   { return BOLD   + text + RESET; }
}