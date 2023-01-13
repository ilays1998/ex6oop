package main;


import method.CheckMethod;
import method.MethodTable;
import method.MethodTableCheckException;
import method.WhileIfBlock;

import java.io.*;
import java.util.regex.Pattern;

public class Sjavac {
    private static String line;
    public static void main(String[] args) {
        ///this for the '{' in the program
        CheckVriable.scopes.add(new Scope());
        CheckMethod.methodBody = false;
        CheckMethod.lastReturn = false;
        CheckMethod.endMethod = false;
        WhileIfBlock.depth = 0;

        try (FileReader fileReader = new FileReader(args[0]);
             BufferedReader bufferedReader = new BufferedReader(fileReader)){
            while((line = bufferedReader.readLine()) != null) {
                if (Pattern.matches("^void .*", line.trim())) {
                    CheckMethod.checkMethodDec(line);
                    CheckMethod.methodBody = true;
                    CheckMethod.endMethod = false;
                    CheckMethod.lastReturn = false;
                    //need to add new scope
                }
                else if (WhileIfBlock.depth > 0) {
                    CheckMethod.checkMethodBody(line);
                    if (CheckMethod.endMethod) {
                        //need to erase the last scope
                        WhileIfBlock.depth -= 1;
                    }
                }
                else if (CheckMethod.methodBody) {
                    CheckMethod.checkMethodBody(line);
                    if (CheckMethod.endMethod){
                        if (!CheckMethod.lastReturn)
                            throw new Exception("METHOD MUST END WITH RETURN");
                        CheckMethod.methodBody = false;
                        //need to erase the last scope
                    }
                }
                else {
                    CheckVriable.check(line);
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(1);
        }
        try {MethodTable.checkTable();} catch (MethodTableCheckException e) {
            e.printStackTrace();
            System.out.println(1);
        }
    }
    //need to check MethodTable in the end
}
