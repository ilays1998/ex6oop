package main;

import java.util.ArrayList;


/// the class of the scope that is contain the variables in the scope
public class Scope {

    Scope prevScope = null;
    public ArrayList<VariableLine> variables;
    public Scope() {this.variables = new ArrayList<>();}

    public void setPrevScope(Scope prevScope) {
        this.prevScope = prevScope;
    }

    /// this function check if this variable is exist in this scope or up to this scope
    public boolean exists(String name) {
        Scope curScope = this;
        while (curScope != null) {
            for (int i = 0; i < curScope.variables.size(); i++) {
                if (curScope.variables.get(i).name.equals(name)) {
                    return true;
                }
            }
            curScope = curScope.prevScope;
        }
        return false;
    }


    /// this function check if this variable is exist in this scope
    public boolean contain(String name) {
        for (VariableLine variable : variables) {
            if (variable.name.equals(name)) {
                return true;
            }
        }
        return false;
    }


    /// this function check if this variable is exist in this scope or up to this scope and not final
    public boolean existsNotFinal(String name) {
        Scope curScope = this;
        while (curScope != null) {
            for (int i = 0; i < curScope.variables.size(); i++) {
                if (curScope.variables.get(i).name.equals(name) && !curScope.variables.get(i).isFinal) {
                    return true;
                }
            }
            curScope = curScope.prevScope;
        }
        return false;
    }


    /// find the variable if exist in this scope and up then return it object
    public VariableLine findVar(String name) {
        Scope curScope = this;
        while (curScope != null) {
            for (int i = 0; i < curScope.variables.size(); i++) {
                if (curScope.variables.get(i).name.equals(name)) {
                    return curScope.variables.get(i);
                }
            }
            curScope = curScope.prevScope;
        }
        return null;
    }


    /// this function add variable in this scope
    public void addNew(String type, String name, String value, boolean isFinal) {
        VariableLine variable = new VariableLine(type, name, value, isFinal);
        variables.add(variable);
    }
}