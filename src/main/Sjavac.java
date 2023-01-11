package main;

import java.io.*;

public class Sjavac {
    private static String line;
    public static void main(String[] args) {
        try (FileReader fileReader = new FileReader(args[0]);
             BufferedReader bufferedReader = new BufferedReader(fileReader)){
            while((line = bufferedReader.readLine()) != null) {

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
