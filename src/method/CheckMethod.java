package method;

import main.CheckRow;
import main.CheckVriable;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckMethod {
    public static String definition;
    public static String name;
    public static final String CHECK_METHOD_NAME = "^([a-zA-Z]\\w*)\\s*";
    public static String end;
    public static boolean lastReturn;
    public static boolean methodBody;
    public static boolean endMethod;


    public static void checkMethod() throws MethodException {
        //checkMethodDec() && checkMethodBody();
        throw new MethodException();
    }

    public static void checkMethodBody(String line) throws MethodException {
//        try {
//            if (CheckVriable.check(line))
//                return true;
//
//            return true;
//        } catch (ValidityError validityError) {
//            validityError.printStackTrace();
//            throw new MethodException();
//        }
    }

    public static void checkVoid() throws MethodException {
        Pattern pattern = Pattern.compile("^void\\s+");
        Matcher m = pattern.matcher(definition.trim());
        if (!m.find())
            throw new MethodException();
        definition = definition.substring(m.end());
    }

    public static void checkMethodName() throws MethodException {
        Pattern pattern = Pattern.compile(CHECK_METHOD_NAME);
        definition = definition.trim();
        int index = definition.indexOf('(');
        if (index == -1)
            throw new MethodException("METHOD ILLEGAL");
        name = definition.substring(0, index);
        Matcher m = pattern.matcher(name);
        if (!m.find())
            throw new MethodException("NAME METHOD ILLEGAL");
        definition = definition.substring(m.end());
    }


    public static void checkMethodDec(String line) throws MethodException{
        definition = line;
        if (methodBody)
            throw new MethodException("METHOD CANNOT BE DECLARE IN METHOD BODY");

        try {
            checkVoid();
            checkMethodName();
            checkParameterList();
            checkParameterListDec();
            CheckRow.checkBracketOpening(end);
        } catch (Exception e) {
            e.printStackTrace();
            throw new MethodException("DECLARATION METHOD ILLEGAL:" + line);
        }
    }

    //append this to scope
    private static void checkParameterListDec() throws MethodException {
        String[] parameters = definition.split(",");
        ArrayList<String> types = new ArrayList<>();
        Matcher m;
        for (String par: parameters) {
            m = Pattern.compile(CheckVriable.FINAL_VARDIK_LINE +"|"
                    + CheckVriable.TYPE + "\\s" + CheckVriable.VARDICNAME + "|\\s*")
                    .matcher(par.trim());
            if (!m.matches())
                throw new MethodException("PARAMETER LIST ILLEGAL");
            types.add(m.group(1));
        }
        //System.out.println(types);
        MethodTable.addMethodDec(name, types);
    }

    public static void checkParameterList() throws MethodException {
        Pattern pattern = Pattern.compile("^\\(.*\\)");
        Matcher m = pattern.matcher(definition.trim());
        if (!m.find())
            throw new MethodException();
        end = definition.substring(m.end());
        definition = definition.substring(m.start() + 1, m.end() - 1);
    }

    //append HashMap
    public static void checkMethodCall(String line) throws MethodException {
        definition = line.trim();
        try {
            checkMethodName();
            checkParameterList();
            checkParameterListCall();
            CheckRow.checkSemicolon(end);
        } catch (Exception e) {
            e.printStackTrace();
            throw new MethodException("CALL METHOD ILLEGAL:" + line);
        }

    }

    private static void checkParameterListCall() throws MethodException {
        String[] parameters = definition.split(",");
        ArrayList<String> types = new ArrayList<>();
        for (String par: parameters) {
            if (!Pattern.matches(CheckVriable.INT + "|" +
                    CheckVriable.BOOLEAN + "|" + CheckVriable.STRING + "|" +
                    CheckVriable.DOBULE + "|" +CheckVriable.CHAR + "|\\s*", par.trim()))
                throw new MethodException("PARAMETER LIST ILLEGAL");
            types.add(par.trim());
        }
        //System.out.println(types);
        MethodTable.addMethodCall(name, types);
    }


    public static void main(String[] args) {
        String name = " void name (int x, boolean bb) {";
        String call = "call(5, 6);";
        CheckMethod.definition = name;
        /*System.out.println(CheckMethod.checkVoid());
        System.out.println(CheckMethod.definition);
        System.out.println(CheckMethod.checkParameterList());
        System.out.println(CheckMethod.definition);*/
        try {
            checkMethodDec(name);
            checkMethodCall(call);
        } catch (MethodException e) {
            e.printStackTrace();
            System.out.println();
        }


    }

}
