package method;

import main.CheckVriable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WhileIfBlock {
    public static int depth;
    public static final String CONDITION = CheckVriable.BOOLEAN;
    public static final String BLOCK_DEC = "(^if|^while)\\s*\\(" +
        CONDITION + "\\)\\s*\\{\\s*";
    public static final String BLOCK_DEC_OR = "(^if|^while)\\s*\\(" +
            "(" + CONDITION + "\\|\\|)+" + CONDITION + "\\)\\s*\\{\\s*";
    public static final String BLOCK_DEC_AND = "(^if|^while)\\s*\\(" +
            "(" + CONDITION + "&&)+" + CONDITION + "\\)\\s*\\{\\s*";
    public static final String EMPTY = "(^if|^while)\\s*\\(" + "([^)]*)" + "\\)\\s*\\{\\s*";

    public static void checkBlockDec(String line) throws Exception {
        Pattern pattern = Pattern.compile(BLOCK_DEC + '|' + BLOCK_DEC_OR +
                '|' + BLOCK_DEC_AND);
        if (!pattern.matcher(line.trim()).matches())
        {
            pattern = Pattern.compile(EMPTY);
            Matcher m = pattern.matcher(line);
            if (m.matches()) {
                if (Pattern.matches("\\s*", m.group(2)))
                    throw new WhileIfException("EMPTY CONDITION");
                String[] parameters = m.group(2).split(",");
                for (String par: parameters) {
                    if (!CheckVriable.scopes.get(CheckVriable.scopes.size() - 1).exists(par.trim()) &&
                        !Pattern.matches(CheckVriable.BOOLEAN, par.trim()))
                        throw new MethodException("PARAMETER LIST ILLEGAL");
                }
                //System.out.println(m.group(2));
            }
            else
                throw new Exception();
        }
        else
            throw new Exception();


    }

    /*public static void main(String[] args) {
        String whi = "if(x) {";
        try {
            checkBlockDec(whi);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/
}
