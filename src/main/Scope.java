package main;

import java.util.ArrayList;


/// the class of the scope that is contain the variables in the scope
public class Scope {

    Scope previousScope = null;
    public ArrayList<VariableLine> variables;
    public Scope() {this.variables = new ArrayList<>();}

    public void setPrevScope(Scope prevScope) {
        this.previousScope = prevScope;
    }

    public boolean found(String name) {
        Scope curScope = this;
        while (curScope != null) {
            for (int i = 0; i < curScope.variables.size(); i++) {
                if (curScope.variables.get(i).name.equals(name)) {
                    return true;
                }
            }
            curScope = curScope.previousScope;
        }
        return false;
    }


    public boolean inThisScope(String name) {
        for (VariableLine variable : variables) {
            if (variable.name.equals(name)) {
                return true;
            }
        }
        return false;
    }


    public VariableLine getVariable(String name) {
        Scope curScope = this;
        while (curScope != null) {
            for (int i = 0; i < curScope.variables.size(); i++) {
                if (curScope.variables.get(i).name.equals(name)) {
                    return curScope.variables.get(i);
                }
            }
            curScope = curScope.previousScope;
        }
        return null;
    }


    public void addVrivalbe(String type, String name, String value, boolean isFinal) {
        variables.add(new VariableLine(type, name, value, isFinal));
    }
}