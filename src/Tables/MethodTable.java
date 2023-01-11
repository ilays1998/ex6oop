package Tables;

import java.util.ArrayList;
import java.util.HashMap;

public class MethodTable {
    HashMap<String, Boolean> methods;
    HashMap<String, ArrayList<String>> variablesType;

    public MethodTable() {
        this.methods = new HashMap<>();
        this.variablesType = new HashMap<>();
    }

    public void addMethodDec(String name) {
        methods.put(name, true);
    }

    public void addMethodCall(String name) {
        if (methods.containsKey(name) && methods.get(name))
            return;
        methods.put(name, false);
    }
    
    public boolean checkTable() {
        return !(methods.containsValue(false));
    }
}
