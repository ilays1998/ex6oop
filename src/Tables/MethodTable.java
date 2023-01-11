package Tables;

import java.util.HashMap;

public class MethodTable {
    HashMap<String, Boolean> methods;

    public MethodTable() {
        this.methods = new HashMap<>();
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
