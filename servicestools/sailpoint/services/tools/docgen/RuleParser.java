package sailpoint.services.tools.docgen;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by adam.creaney on 3/30/16.
 */
public class RuleParser {
    public List<HashMap<String, List<String>>> getMethods(String rule){

        List<HashMap<String, List<String>>> methods = new ArrayList<HashMap<String, List<String>>>();

        int pos = 0;
        char lf = '\n';
        char cr = '\r';
        char space = ' ';

        int len = rule.length();
        String currentLine = "";

        StringBuilder sbComments;
        while(pos < len){
            boolean b = false;
            char posChar = rule.charAt(pos);
            if(posChar == cr){
                //increment pos before reading its value
                posChar = rule.charAt(++pos);
            }
            if(posChar == lf){
                pos++;
                while(pos < len){
                    if(posChar != space){
                        break;
                    }
                    posChar = rule.charAt(++pos);
                }
            }
            //If we are here we should be on the first non blank character for a new line
            //see if the next two chars are /*
            sbComments = new StringBuilder();
            Pattern p = Pattern.compile("^\\s*/\\*");
            Matcher m = p.matcher(currentLine);
            b = m.matches();
            if(b){
                while(pos < len){
                    sbComments.append(posChar);
                    if(safeSubStringMatch(rule, pos, 2, "*/")){
                        posChar = rule.charAt(++pos);
                        sbComments.append(posChar);
                        posChar = rule.charAt(++pos);
                        sbComments.append(posChar);
                        posChar = rule.charAt(++pos);
                        if(posChar == cr){
                            posChar = rule.charAt(++pos);
                        }
                        if(posChar == lf){
                            posChar = rule.charAt(++pos);
                        }
                        //Don't deal with more than 5 blank lines after the javadoc block
                        for(int i = 0; i < 5; i++){
                            currentLine = safeReadLine(rule, pos);
                            if(currentLine.trim().length() == 0){
                                //this function steps back one char just in case it is
                                //already at the start of a new line
                                //we don't want to add this to the comments block
                                pos = consumeUntilNewLine(rule, pos + 1);
                            }
                            else{
                                break;
                            }
                        }
                        break;
                    }
                    pos++;
                    posChar = rule.charAt(pos);
                }
                currentLine = safeReadLine(rule, pos);
            }
            //if we are here we may have comments
            //we are now looking for a method
            while(pos < len){
                pos = consumeUntilNewLine(rule, pos);
             
                boolean isMethod = isMethodsRegex(safeSubstringRange(rule, pos, rule.length()));
                if(isMethod){
                    pos = consumeWhiteSpace(rule, pos);
                    posChar = rule.charAt(pos);
                    int endPos = consumeNonWhiteSpaceWithExcludes(rule, pos);
                    String access = safeSubstringRange(rule, pos, endPos);
                    pos = endPos;

                    pos = consumeWhiteSpace(rule, pos);
                    posChar = rule.charAt(pos);
                    endPos = consumeNonWhiteSpaceWithExcludes(rule, pos);
                    String returns = safeSubstringRange(rule, pos, endPos);
                    pos = endPos;

                    pos = consumeWhiteSpace(rule, pos);
                    posChar = rule.charAt(pos);
                    endPos = consumeNonWhiteSpaceWithExcludes(rule, pos);
                    String methodName = safeSubstringRange(rule, pos, endPos);
                    pos = endPos;

                    //special case where there is no access or return (methodName results in blank from above logic)
                    if(access.trim().length() > 0 && returns.trim().length() > 0 && methodName.trim().length() == 0){
                        methodName = returns;
                        returns = access;
                        //if the previous code sets the return as public or private thats a problem
                        if(returns.equalsIgnoreCase("public") | returns.equalsIgnoreCase("private")){
                            returns = "void";
                        }
                    }

                    pos = consumeUntilFound(rule, pos, '(');
                    pos++;
                    endPos = consumeUntilFound(rule, pos, ')');
                    String methodParams = safeSubstringRange(rule, pos, endPos);
                    pos = endPos;
                    String codeBlock = getCodeBlock(rule, pos);
                    HashMap<String, List<String>> method = new HashMap<String, List<String>>();
                    List<String> lines = null;
                    if(access != returns){
                        lines = new ArrayList<String>();
                        lines.add(access);
                        method.put("access", lines);
                    }

                    lines = new ArrayList<String>();
                    lines.add(returns);
                    method.put("returns", lines);

                    lines = new ArrayList<String>();
                    lines.add(methodName);
                    method.put("name", lines);

                    lines = new ArrayList<String>();
                    if(methodParams.trim().length() > 0){
                        String[] temp = methodParams.split(",");
                        List<String> methodParamsArray = Arrays.asList(temp);
                        lines.addAll(methodParamsArray);
                    }

                    method.put("params", lines);

                    lines = new ArrayList<String>();
                    lines.add(sbComments.toString());
                    method.put("comments", lines);

                    lines = new ArrayList<String>();
                    lines.add(codeBlock);
                    method.put("code", lines);

                    methods.add(method);
                    break;

                }
                else{
                    pos++;
                    pos = consumeUntilNewLine(rule, pos);
                    pos--;
                    break;
                }
            }
            pos++;
            currentLine = safeReadLine(rule, pos);
        }
        return methods;
    }

    private String getCodeBlock(String rule, int startPos) {
        StringBuilder sb = new StringBuilder();
        int pos = consumeUntilFound(rule, startPos, '{');
        char c = rule.charAt(pos);
        sb.append(c);
        c = rule.charAt(++pos);
        int depth = 1;
        boolean quoteBlock = false;

        while (depth > 0 && pos < rule.length()){
            sb.append(c);
            if(c == '"'){
                quoteBlock = !quoteBlock;
            }
            else if(c == '\\' && quoteBlock){
                c = rule.charAt(++pos);
                sb.append(c);
            }
            else if(c == '{'){
                depth++;
            }
            else if(c == '}'){
                depth--;
            }
            else if(c == '/'){
                char nextChar = rule.charAt(pos + 1);
                if(nextChar == '/'){
                    int end = consumeUntilNewLine(rule, pos + 1);
                    String line = safeSubstringRange(rule, pos, end);
                    sb.append(line);
                    pos = end - 1;
                }
                else if(nextChar == '*'){
                    char lastChar = c;
                    while(pos < rule.length()){
                        sb.append(c);
                        lastChar = c;
                        c = rule.charAt(++pos);
                        if(c == '/' && lastChar == '*'){
                            sb.append(c);
                            break;
                        }
                    }
                }
                else{
                    sb.append("Error parsing");
                    break;
                }
            }
            c = rule.charAt(++pos);
        }
        return sb.toString();
    }

    private int consumeNonWhiteSpaceWithExcludes(String s, int pos) {
        while(pos < s.length()){
            char posChar = s.charAt(pos);
            if(posChar == ' ' || posChar == '('){
                break;
            }
            pos++;
        }
        return pos;
    }

    private int consumeWhiteSpace(String s, int pos) {
        while(pos < s.length()){
            char posChar = s.charAt(pos);
            if(posChar != ' ' && posChar != '\n' && posChar !='\r'){
                break;
            }
            pos++;
        }
        return pos;
    }

    private int consumeUntilFound(String s, int pos, char c){
        while(pos < s.length()){
            char posChar = s.charAt(pos);
            if(posChar == c){
                break;
            }
            pos++;
        }
        return pos;
    }

    private int consumeNonWhiteSpace(String s, int pos){
        while(pos < s.length()){
            char posChar = s.charAt(pos);
            if(posChar == ' ' || posChar == '('){
                break;
            }
            pos++;
        }
        return pos;
    }

    private String safeSubstringRange(String sourceString, int start, int endExclusive) {
        StringBuilder sb = new StringBuilder();
        int pos = start;
        while(pos < sourceString.length() && pos < endExclusive){
            sb.append(sourceString.charAt(pos));
            pos++;
        }
        return sb.toString();
    }

    private int consumeUntilNewLine(String s, int pos) {
        if(pos > 0){
            pos -= 1;
        }
        while(pos < s.length()){
            char posChar = s.charAt(pos);
            if(posChar == '\n'){
                pos++;
                break;
            }
            pos++;
        }
        return pos;
    }

    private String safeReadLine(String sourceString, int start) {
        StringBuilder sb = new StringBuilder();
        int pos = start;
        while(pos < sourceString.length()){
            char c = sourceString.charAt(pos);
            if(c == '\r'){
                c = sourceString.charAt(++pos);
            }
            if(c == '\n'){
                break;
            }
            sb.append(c);
            pos++;
        }
        return sb.toString();
    }

    private boolean safeSubStringMatch(String sourceString, int start, int len, String matchString) {
        boolean ret = false;
        String sRet = "";

        if(start + len <= sourceString.length()){
            //sRet = sourceString.substring(start, len);
            sRet = sourceString.substring(start, start + len);
        }
        if(sRet.equalsIgnoreCase(matchString)){
            ret = true;
        }
        return ret;
    }

    private boolean isMethodsRegex(String rule){
        boolean ret = false;
        Pattern p = Pattern.compile("^\\s*(public|private)?\\s*(\\w+)\\s+(\\w+)\\(([^)]*)\\)([^{^;]*)\\s*(\\{)");
        Matcher m = p.matcher(rule);
        if(m.find()){
            ret = true;
        }
        return ret;
    }
}
