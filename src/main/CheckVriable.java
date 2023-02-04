package main;

import main.Scope;
import main.ValidityError;
import main.VariableLine;
import method.MethodTable;

import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckVriable {

    
    /// we can move (scopes and scopeDepth ) to Sjavac class
    public static ArrayList<Scope> scopes = new ArrayList<>();
    private static int scopeDepth = 0;
    public static final String INT = "((\\s*)(-{0,1}\\d+)\\s*)";
    public static final String DOBULE = "((\\s*)(-{0,1}\\d+\\.{0,1}\\d+|" + INT + ")\\s*)";
    public static final String STRING = "\\s*\\\"\\s*[\\w]+\\s*\\\"\\s*|\\\"\\\"";
    public static final String CHAR = "\\s*\\'.{1}\\'\\s*";
    public static final String BOOLEAN = "((\\s*)(\\s*true\\s*|\\s*false\\s*|(\\s*)(-{0,1}\\d+(\\.\\d+){0,1})\\s*))";
    public static final String TYPE = "(String|double|int|char|boolean)";
    public static final String VARDICNAME = "(\\s*([a-zA-Z]\\w*)\\s*|\\s*(_\\w+)\\s*)\\s*";
    public static final String FINAL_VARDIK_LINE = "\\s*(final)\\s+" + TYPE + "\\s" + VARDICNAME
            + "\\s*=\\s*(" + DOBULE + "|" + INT + "|" + BOOLEAN + "|" + VARDICNAME + "|" + STRING + "|"
            + CHAR + ")+\\s*;\\s*";
    public static final String WITH_OUT_TYPE = "\\s*" + VARDICNAME + "\\s=\\s*(" + DOBULE +
            "|" + INT + "|" + BOOLEAN + "|" + VARDICNAME + "|" + STRING + "|" + CHAR +  ")+\\s*;\\s*";
    public static final String WITHOUT_EQUAL_MODIFER = "\\s*" + TYPE + "\\s(\\s*" + VARDICNAME +
            "\\s*)\\s*;\\s*";
    public static final String VARIDK_LINE = "\\s*" + TYPE + "\\s*" + VARDICNAME + "\\s*=\\s*(" +
            DOBULE + "|\\\"\\\"|" + INT + "|" + BOOLEAN + "|" + VARDICNAME + "|" + STRING + "|"
            + CHAR + ")\\s*;\\s*";

    public static final String VARIABLE_VALUE = DOBULE + "|" + INT + "|" + STRING + "|" + BOOLEAN + "|" +
            CHAR;
    public static final String ASSIGNMENT = "\\s*=\\s*(" + VARIABLE_VALUE + "|" + VARDICNAME + ")";
    public static final String DECLARATION_EXPRESSION = "(" + VARDICNAME + ")\\s*(" + ASSIGNMENT + ")?";
    public static final String VARIABLE_DECLARATION_LINE = "\\s*" + "(" + "final" + "\\s+)?(" + TYPE + ")\\s+("
            + "(" + DECLARATION_EXPRESSION + "\\s*,\\s*)*" + DECLARATION_EXPRESSION + ")\\s*;\\s*";





    ///this main function that check the row
    public static void check(String line) throws ValidityError {
        if (Pattern.compile(FINAL_VARDIK_LINE).matcher(line).matches()){
            withFinal(Pattern.compile(FINAL_VARDIK_LINE).matcher(line),
                            scopeDepth);

        } else if (Pattern.compile(WITH_OUT_TYPE).matcher(line).matches()) {
            assignValue(Pattern.compile(WITH_OUT_TYPE).matcher(line),
                      scopeDepth);

        } else if (Pattern.compile(WITHOUT_EQUAL_MODIFER).matcher(line).matches()) {
            initializion(Pattern.compile(WITHOUT_EQUAL_MODIFER).matcher(line),
                          scopeDepth);

        } else if (Pattern.compile(VARIDK_LINE).matcher(line).matches()) {
            deceleration(Pattern.compile(VARIDK_LINE).matcher(line),
                          scopeDepth);

        }else if (Pattern.compile(VARIABLE_DECLARATION_LINE).matcher(line).matches()){
            initializaManyVariable(Pattern.compile(VARIABLE_DECLARATION_LINE).matcher(line),
                          scopeDepth);
        }
        else{
            throw new ValidityError("VARIABLE ILLEGAL: " + line);
        }
    }

    public static void initializaManyVariable(Matcher matcher,int scopeDepth) throws ValidityError{
        if (!matcher.matches()) {
            throw new ValidityError();
        }
        boolean isFinal = matcher.group(1) != null;
        String variableType = matcher.group(2);
        String declares = matcher.group(4);


        matcher = Pattern.compile(DECLARATION_EXPRESSION).matcher(declares);
        while (matcher.find()) {
            if (matcher.group(0).contains("=")){
                String variableName = matcher.group(1);
                String value=matcher.group(6);
                scopes.get(scopeDepth).addVrivalbe(variableType, variableName, value, isFinal);
            }
            else {
                String variableName = matcher.group(1);
                scopes.get(scopeDepth).addVrivalbe(variableType, variableName,  null, isFinal);
            }
        }
    }

    public static void withFinal(Matcher matcher, int scopeDepth) throws ValidityError {
        if (!matcher.matches()) {
            throw new ValidityError();
        }
        String all= matcher.group();
        String type = matcher.group(2).trim();
        String name = matcher.group(3).trim();
        String value = matcher.group(6).trim();
        newVariable(type, name, value, scopeDepth, true);
    }

    public static void assignValue(Matcher matcher, int scopeDepth) throws ValidityError {
        if (!matcher.matches()) {
            throw new ValidityError();
        }
        String name = matcher.group(1).trim();
        String value = matcher.group(4).trim();
        if (scopes.get(scopeDepth).found(name) && !scopes.get(scopeDepth).getVariable(name).isFinal) {
            newVariable(null, name, value, scopeDepth, false);
            return;
        }
        throw new ValidityError("YOU TRY TO ASSIGN VALUE TO AN DEFINE VARIABLE");
    }


    public static void initializion(Matcher matcher, int scopeDepth) throws ValidityError {
        if (!matcher.matches()) {
            throw new ValidityError();
        }
        String type = matcher.group(1).trim();
        String name = matcher.group(2).trim();
        if (scopes.get(scopeDepth).inThisScope(name))
        {
            throw new ValidityError("THERE IS DUPLICATION IN DEFINING THE SAME VARIABLE");
        }
        scopes.get(scopeDepth).addVrivalbe(type, name, null, false);
    }


    public static void deceleration(Matcher matcher, int scopeDepth) throws ValidityError {
        if (!matcher.matches()) {
            throw new ValidityError();
        }
        String type = matcher.group(1).trim();
        String name = matcher.group(2).trim();
        String value = matcher.group(5).trim();
        if (scopes.get(scopes.size() - 1).inThisScope(name)) {

            throw new ValidityError("THERE IS DUPLICATION IN DEFINING THE SAME VARIABLE");
        }
        if (scopes.get(scopeDepth).found(value) && scopes.get(scopeDepth).getVariable(value).value != null) {
            if (scopes.get(scopeDepth).getVariable(value).type.equals(type)) {
                return;
            }
            if (type.equals("boolean") && (scopes.get(scopeDepth).getVariable(value).type.equals("int") ||
                    scopes.get(scopeDepth).getVariable(value).type.equals("double"))) {
                return;
            }
            if (type.equals("double") && scopes.get(scopeDepth).getVariable(value).type.equals("int")) {
                return;
            }
        }
        validTypeValue(type, value);
        scopes.get(scopeDepth).addVrivalbe(type.trim(), name.trim(), value.trim(), false);
    }

    /// this function responsible for adding new variable to the scope in case of defining new variable or assign value to exist variable
    private static void newVariable(String type, String name, String value, int scopeDepth, boolean isFinal) throws ValidityError {
        if (isFinal) {
            if (scopes.get(scopeDepth).inThisScope(name) ) {
                throw new ValidityError("THERE IS DUPLICATION IN DEFINING THE SAME VARIABLE");
            }
            try {
                validTypeValue(type, value);

            } catch (ValidityError e) {
                if (!scopes.get(scopeDepth).found(value)) {
                    throw new ValidityError("THERE IS DUPLICATION IN DEFINING THE SAME VARIABLE");
                }
                if (!scopes.get(scopeDepth).getVariable(value).type.equals(type)) {
                    throw new ValidityError("THRE IS NOT MATCHING BETWEEN TYPES");
                }
            }
            scopes.get(scopeDepth).addVrivalbe(type, name, value, true);
            return;
        }
        VariableLine variable = scopes.get(scopeDepth).getVariable(name);
        validTypeValue(variable.type, value);
        variable.value = value;
    }

    ///this function for check valid value according to type
    public static void validTypeValue(String type, String value) throws ValidityError{
        boolean match = false;
        switch (type) {
            case "int" -> match = Pattern.compile(INT).matcher(value).matches();
            case "double" ->
                    match = Pattern.compile(DOBULE).matcher(value).matches();
            case "char" -> match = Pattern.compile(CHAR).matcher(value).matches();
            case "String" ->
                    match = Pattern.compile(STRING).matcher(value).matches();
            case "boolean" ->match = Pattern.compile(BOOLEAN).matcher(value).matches();
            default -> throw new ValidityError("THERE IS NO MATCH BETWEEN TYPE AND VALUE");
        }
        if (!match) {
            throw new ValidityError("THERE IS NO MATCH BETWEEN TYPE AND VALUE");
        }
    }

    public static void addNewScope() {
        Scope newScope = new Scope();
        newScope.setPrevScope(CheckVriable.scopes.get(CheckVriable.scopes.size() - 1));
        CheckVriable.scopes.add(newScope);
        scopeDepth++;
    }
}