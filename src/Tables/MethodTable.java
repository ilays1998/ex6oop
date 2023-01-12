package Tables;

import java.util.ArrayList;
import java.util.HashMap;

public class MethodTable {
    HashMap<String, Boolean> methods;
    HashMap<String, ArrayList<String>> variablesType;
    HashMap<String, ArrayList<ArrayList<String>>> variablesList;
    
    public MethodTable() {
        this.methods = new HashMap<>();
        this.variablesType = new HashMap<>();
        this.variablesList = new HashMap<>();
    }

    public void addMethodDec(String name, ArrayList<String> types) {
        methods.put(name, true);
        variablesType.put(name, types);
    }

    public ArrayList<String> getTypes(String name) {
        return variablesType.get(name);
    }

    public void addMethodCall(String name, ArrayList<String> variables) {
        if (!variablesList.containsKey(name))
            variablesList.put(name, new ArrayList<>());
        variablesList.get(name).add(variables);
        if (methods.containsKey(name) && methods.get(name))
            return;
        methods.put(name, false);
    }
    
    public boolean checkTable() {
        if  (methods.containsValue(false)) 
            return false;
        for (String name: methods.keySet()) {
            if (!checkTypeEqualsVariables(name))
                return false;
        }
        return true;
    }

    //check VarVSTYPE is method need to be in Variables that compare type (boolean,
    //char, int) to value (true, c, 5)
    private boolean checkTypeEqualsVariables(String name) {
        ArrayList<String> types = getTypes(name);
        for (ArrayList<String> list: variablesList.get(name)) {
            if (list.size() != types.size())
                return false;
            for (String elem : list) {
                if (!checkVarVsType())
                    return false;
            }
        }
        return true;
    }
}
