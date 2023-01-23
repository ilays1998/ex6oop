package method;

import main.CheckRow;
import main.CheckVriable;
import main.RowException;
import main.ValidityError;
import main.CheckVriable;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckMethod {
    private static final String RETURN_REG = "\\s*return\\s*;\\s*";
    public static String definition;
    public static String name;
    public static final String CHECK_METHOD_NAME = "^([a-zA-Z]\\w*)\\s*";
    public static String end;
    public static boolean lastReturn;
    public static boolean methodBody;
    public static boolean endMethod;
    public static final String PREFIX_WHILE = "\\s*while\\s*\\(.*" ;
    public static final String PREFIX_IF = "\\s*if\\s*\\(.*" ;



    public static void checkReturn(String line) throws MethodException {
        if (!Pattern.matches(RETURN_REG, line))
            throw new MethodException("RETURN ILLEGAL");
        lastReturn = true;
    }

    public static void checkEndMethod(String line) throws RowException {
        CheckRow.checkBracketClosing(line);
        endMethod = true;
    }

    public static void checkMethodBody(String line) throws MethodException, RowException, ValidityError {
        if (line.trim().length() < 2) {
            checkEndMethod(line);
        }
        else if (Pattern.matches(RETURN_REG, line)) {
            endMethod = false;
            checkReturn(line);
        }
        else {
            endMethod = false;
            lastReturn = false;
            if (Pattern.matches(PREFIX_WHILE, line) || Pattern.matches(PREFIX_IF, line)) {
                //need to add the new scope
                CheckVriable.addNewScope();
                WhileIfBlock.depth += 1;
                CheckMethod.endMethod = false;
            }
            else{
                try {
                    CheckVriable.check(line);
                } catch (Exception e) {
                    checkMethodCall(line);
                }
            }
        }
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
            m = Pattern.compile(CheckVriable.TYPE  + "\\s" + CheckVriable.VARDICNAME  +"|"
                     +CheckVriable.FINAL_VARDIK_LINE + "|\\s*")
                    .matcher(par.trim());
            //8888888888888888888888888
            //problem in CheckVriable.FINAL_VARDIK_LINE

            if (!m.matches())
                throw new MethodException("PARAMETER LIST ILLEGAL");
            if (m.group(1) == null)
                return;
            if (m.group(1).equals("final")) {
                types.add(m.group(2));
                CheckVriable.scopes.get(CheckVriable.scopes.size() - 1).addNew(m.group(2),
                        m.group(3), null, true);
            }
            else {
                types.add(m.group(1));
                CheckVriable.scopes.get(CheckVriable.scopes.size() - 1).addNew(m.group(1),
                        m.group(2), null, false);
            }

            //888888888888888888888888888888
            //need to modify this to without value

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


    //need to add parameters in scope
    private static void checkParameterListCall() throws MethodException {
        String[] parameters = definition.split(",");
        ArrayList<String> types = new ArrayList<>();
        for (String par: parameters) {
            if (!Pattern.matches(CheckVriable.INT + "|" +
                    CheckVriable.BOOLEAN + "|" + CheckVriable.STRING + "|" +
                    CheckVriable.DOBULE + "|" +CheckVriable.CHAR + "|\\s*", par.trim()))
                if (!CheckVriable.scopes.get(CheckVriable.scopes.size() - 1).exists(par.trim()))
                    throw new MethodException("PARAMETER LIST ILLEGAL");
                else {
                    types.add(CheckVriable.scopes.get(CheckVriable.scopes.size() - 1).
                            findVar(par.trim()).value);
                    continue;
                }
            types.add(par.trim());
        }
        //System.out.println(types);
        MethodTable.addMethodCall(name, types);
    }


    /*public static void main(String[] args) {
        String name = " void name (int x, boolean bb) {";
        String call = "call(5, 6);";
        String ret = "return ;";
        String close = "}";
        CheckMethod.definition = name;
        *//*System.out.println(CheckMethod.checkVoid());
        System.out.println(CheckMethod.definition);
        System.out.println(CheckMethod.checkParameterList());
        System.out.println(CheckMethod.definition);*//*
        try {
            checkMethodDec(name);
            checkMethodCall(call);
            checkReturn(ret);
            checkEndMethod(close);
        } catch (MethodException | RowException e) {
            e.printStackTrace();
            System.out.println();
        }


    }*/

}
