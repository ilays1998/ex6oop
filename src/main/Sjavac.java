package main;

import main.CheckVriable;
import main.Scope;
import method.CheckMethod;
import method.MethodTable;
import method.MethodTableCheckException;
import method.WhileIfBlock;

import java.io.*;
import java.util.regex.Pattern;

import static main.CheckVriable.addNewScope;

public class Sjavac {
    private static String line;
    public static void main(String[] args) {
        ///this for the '{' in the program
        CheckVriable.scopes.add(new Scope());
        CheckMethod.methodBody = false;
        CheckMethod.lastReturn = false;
        CheckMethod.endMethod = false;
        WhileIfBlock.depth = 0;
        try {
            if (args.length < 1)
                throw new IOException("NUM OF ARGUMENT ILLEGAL");
            String[] fileSplit = args[0].split("\\.");
            if (fileSplit.length < 2 || !fileSplit[fileSplit.length - 1].equals("sjava"))
                throw new IOException("ILLEGAL FILE FORMAT");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(2);
        }

        try (FileReader fileReader = new FileReader(args[0]);
             BufferedReader bufferedReader = new BufferedReader(fileReader)){
            while((line = bufferedReader.readLine()) != null) {
                if (Pattern.matches("^//.*", line))
                    continue;
                if (Pattern.matches("^\\s*$", line))
                    continue;
                if (Pattern.matches("^void .*", line.trim()) && !CheckMethod.methodBody) {
                    CheckMethod.checkMethodDec(line);
                    CheckMethod.methodBody = true;
                    CheckMethod.endMethod = false;
                    CheckMethod.lastReturn = false;
                    //need to add new scope
                    addNewScope();
                }
                else if (WhileIfBlock.depth > 0) {
                    CheckMethod.checkMethodBody(line);
                    if (CheckMethod.endMethod) {
                        //need to erase the last scope
                        WhileIfBlock.depth -= 1;
                        CheckVriable.scopes.remove(CheckVriable.scopes.size() - 1);
                    }
                }
                else if (CheckMethod.methodBody) {
                    CheckMethod.checkMethodBody(line);
                    if (CheckMethod.endMethod){
                        if (!CheckMethod.lastReturn)
                            throw new Exception("METHOD MUST END WITH RETURN");
                        CheckMethod.methodBody = false;
                        //need to erase the last scope
                        CheckVriable.scopes.remove(CheckVriable.scopes.size() - 1);
                    }
                }
                else {
                    CheckVriable.check(line);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(2);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(1);
            return;
        }
        try {MethodTable.checkTable();} catch (MethodTableCheckException e) {
            e.printStackTrace();
            System.out.println(1);
            return;
        }
        System.out.println(0);
    }


    //need to check MethodTable in the end
}
