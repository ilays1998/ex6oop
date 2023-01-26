package method;

import main.CheckVriable;
import main.ValidityError;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class MethodTable {
    private static HashMap<String, Boolean> methods = new HashMap<>();
    private static HashMap<String, ArrayList<String>> variablesType = new HashMap<>();
    private static HashMap<String, ArrayList<ArrayList<String>>> variablesList = new HashMap<>();

    public static void addMethodDec(String name, ArrayList<String> types) throws MethodTableCheckException {
        name = name.trim();
        if (methods.containsKey(name) && methods.get(name)) {
            throw new MethodTableCheckException("OVERLOADING METHOD");
        }
        else {
            methods.put(name, true);
            variablesType.put(name, types);
        }
    }

    public static ArrayList<String> getTypes(String name) {
        return variablesType.get(name.trim());
    }

    public static void addMethodCall(String name, ArrayList<String> variables) {
        name = name.trim();
        if (!variablesList.containsKey(name))
            variablesList.put(name, new ArrayList<>());
        variablesList.get(name).add(variables);
        if (methods.containsKey(name) && methods.get(name))
            return;
        methods.put(name, false);
    }
    
    public static void checkTable() throws MethodTableCheckException {
        if (methods.containsValue(false))
            throw new MethodTableCheckException("USE IN METHOD WITHOUT DECLARATION");
        for (String name: methods.keySet()) {
            if (variablesType.get(name).isEmpty()) {
                if (!variablesList.containsKey(name))
                    continue;
                for (ArrayList<String> a:variablesList.get(name))
                    for (String str : a)
                        if (!str.trim().equals(""))
                            throw new MethodTableCheckException("METHOD WITHOUT PARAMETERS BUT CALL WITH: " + name);
                continue;
            }
            if (variablesList.containsKey(name) && !variablesList.get(name).isEmpty())
                checkTypeEqualsVariables(name);
        }
    }

    //check VarVSTYPE is method need to be in Variables that compare type (boolean,
    //char, int) to value (true, c, 5)
    private static void checkTypeEqualsVariables(String name) throws MethodTableCheckException {
        ArrayList<String> types = getTypes(name);
        for (ArrayList<String> list: variablesList.get(name)) {
            if (list.size() != types.size())
                throw new MethodTableCheckException("ILLEGAL SIZE OF PARAMETERS IN METHOD: " + name);
            for (int i = 0; i < types.size(); i++) {
                try {
                    checkVarVsType(types.get(i), list.get(i));
                } catch (MethodTableCheckException methodTableCheckExeption) {
                    throw new MethodTableCheckException("PARAMETERS TYPE NOT FIT IN METHOD: " + name);
                }

            }
        }
    }

    private static void checkVarVsType(String type, String value) throws MethodTableCheckException {
//        if (type.equals("double"))
//        {
//            try {
//                CheckVriable.validTypeValue(type, value);
//            } catch (ValidityError validityError) {
//                checkVarVsType("int", value);
//            }
//        }
//        else if (type.equals("boolean")) {
//            try {
//                CheckVriable.validTypeValue(type, value);
//            } catch (ValidityError validityError) {
//                checkVarVsType("double", value);
//            }
//        }
//        else {
        try {
            CheckVriable.validTypeValue(type, value);
        } catch (ValidityError validityError) {
            throw new MethodTableCheckException();
        }
    }
}
