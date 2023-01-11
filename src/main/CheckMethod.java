package main;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckMethod {
    public static String definition;
    public static String end;


    public static boolean checkMethod() {
        //checkMethodDec() && checkMethodBody();
        return true;
    }

    private static boolean checkMethodBody() {
        return true;
    }

    public static boolean checkVoid() {
        Pattern pattern = Pattern.compile("^void\\s+");
        Matcher m = pattern.matcher(definition.trim());
        if (!m.find())
            return false;
        definition = definition.substring(m.end());
        return true;
    }

    public static boolean checkMethodName() {
        return true;
    }

    //append HashMap
    public static boolean checkMethodDec(String line) {
        definition = line;

        return checkVoid() && checkMethodName() && checkParameterList() &&
            checkParameterListDec() && CheckRow.checkBracketOpening(end);
    }

    private static boolean checkParameterListDec() {
        return true;
    }

    public static boolean checkParameterList() {
        Pattern pattern = Pattern.compile("\\(.*\\)");
        Matcher m = pattern.matcher(definition);
        if (!m.find())
            return false;
        end = definition.substring(m.end() + 1);
        definition = definition.substring(m.start() + 1, m.end() - 1);
        return true;
    }

    //append HashMap
    public static boolean checkMethodCall(String line) {
        definition = line.trim();
        return checkMethodName() && checkParameterList() && checkParameterListCall();
    }

    private static boolean checkParameterListCall() {
        return true;
    }


    public static void main(String[] args) {
        String name = " void (fd) {";
        CheckMethod.definition = name;
        /*System.out.println(CheckMethod.checkVoid());
        System.out.println(CheckMethod.definition);
        System.out.println(CheckMethod.checkParameterList());
        System.out.println(CheckMethod.definition);*/

        System.out.println(checkMethodDec(name));
        System.out.println(end);


    }

}
