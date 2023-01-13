package main;

import java.util.regex.Pattern;

public class CheckRow {

    public static void checkSemicolon(String line) throws RowException {
        if (!Pattern.matches("\\s*;\\s*$", line))
            throw new RowException();
    }

    public static void checkBracketOpening(String line) throws RowException {
        if (!Pattern.matches("\\s*\\{\\s*", line))
            throw new RowException();
    }

    public static void checkBracketClosing(String line) throws RowException {
        if (!Pattern.matches("\\s*}\\s*", line))
            throw new RowException();
    }

    public static void main(String[] args) {
        try {
            CheckRow.checkBracketClosing("  { ");
        } catch (RowException e) {
            e.printStackTrace();
        }

    }

}




