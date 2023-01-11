package main;

import java.util.regex.Pattern;

public class CheckRow {
    public static boolean checkEmptyLine(String line) {
        return Pattern.matches("^\\s*$", line);
    }

    public static boolean checkCommand(String line) {
        return Pattern.matches("^//.*", line);
    }

    public static boolean checkSemicolon(String line) {
        return Pattern.matches("\\s*;\\s*$", line);
    }

    public static boolean checkBracketOpening(String line) {
        return Pattern.matches("\\s*\\{\\s*", line);
    }

    public static boolean checkBracketClosing(String line) {
        return Pattern.matches("\\s*}\\s*", line);
    }

    public static void main(String[] args) {
        System.out.println(CheckRow.checkBracketClosing("  { "));

    }

}




