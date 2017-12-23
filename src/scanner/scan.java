package scanner;

import java.io.*;
import java.lang.String;

import static scanner.Token_Type.*;

public class scan {
    public static int LineNo;
    public static String TokenBuffer;   //token char buffer

    public static boolean flag = false;
    public static String BackStr = "";

    private static Token_Table token_table = new Token_Table();
    public static PushbackInputStream reader = null;

    public static boolean InitScanner(String fileName) {
        File srcFile = new File(fileName);

        try {
            reader = new PushbackInputStream(new FileInputStream(srcFile), 1024);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (reader == null) return false;
        else {
            LineNo = 1;
            return true;
        }
    }

    public static void CloseScanner() {
        if (reader != null) {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static char GetChar() {
        int _Char = 0;
        try {
            if ((_Char = reader.read()) == -1) return 0;  //end of file

        } catch (IOException e) {
            e.printStackTrace();
        }
        char ch = (char) _Char;
        if (flag) BackStr += ch;
        return Character.toUpperCase(ch);
    }

    public static void BackChar(char Char) {
        int index;
        if (Char != 0) {
            try {
                reader.unread(Char);
                if (flag) {
                    index = BackStr.length();
                    BackStr = BackStr.substring(0, index - 1);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void AddCharTokenString(char Char) {
        int TokenLength = TokenBuffer.length();
        int TOKEN_LEN = 100;
        if (TokenLength + 1 >= TOKEN_LEN) return;
        TokenBuffer += Char;
    }

    static void EmptyTokenString() {
        TokenBuffer = new String();
    }

    static Token JudgeKeyToken(String IDString) {
        for (int loop = 0; loop < token_table.TokenTab.length; loop++) {
            if (token_table.getToken(loop).getLexeme().equals(IDString))
                return token_table.getToken(loop);
        }
        return new Token(ERRTOKEN, "ERR", 0);
    }

    public static Token GetToken() {
        Token token = new Token(ERRTOKEN, "ERR", 0);
        char Char;
        EmptyTokenString();

        for (; ; ) {
            Char = GetChar();
            if (Char == 0) {
                token.setType(Token_Type.NONTOKEN);
                return token;
            }
            if (Char == '\n') LineNo++;
            if (!Character.isWhitespace(Char)) break;
        }
        AddCharTokenString(Char);

        if (Character.isAlphabetic(Char)) {
            for (; ; ) {
                Char = GetChar();
                if (Character.isAlphabetic(Char) || Character.isDigit(Char))
                    AddCharTokenString(Char);
                else break;
            }
            BackChar(Char);
            token = JudgeKeyToken(TokenBuffer); //FUNC too
            token.setLexeme(TokenBuffer);
            return token;
        } else if (Character.isDigit(Char)) {
            for (; ; ) {
                Char = GetChar();
                if (Character.isDigit(Char)) AddCharTokenString(Char);
                else break;
            }
            if (Char == '.') {
                AddCharTokenString(Char);
                for (; ; ) {
                    Char = GetChar();
                    if (Character.isDigit(Char)) AddCharTokenString(Char);
                    else break;
                }
            }
            BackChar(Char);
            token.setType(Token_Type.CONST_ID);
            token.setValue(Double.valueOf(TokenBuffer));
            token.setLexeme(TokenBuffer);
            return token;
        } else {
            switch (Char) {
                case ';':
                    token.setType(Token_Type.SEMICO);
                    token.setLexeme(";");
                    break;
                case '(':
                    token.setType(Token_Type.L_BRACKET);
                    token.setLexeme("(");
                    break;
                case ')':
                    token.setType(Token_Type.R_BRACKET);
                    token.setLexeme(")");
                    break;
                case ',':
                    token.setType(Token_Type.COMMA);
                    token.setLexeme(",");
                    break;
                case '+':
                    token.setType(Token_Type.PLUS);
                    token.setLexeme("+");
                    break;

                case '-':
                    Char = GetChar();
                    if (Char == '-') {
                        while (Char != '\n' && Char != 0)
                            Char = GetChar();
                        BackChar(Char);
                        return GetToken();
                    } else {
                        BackChar(Char);
                        token.setType(Token_Type.MINUS);
                        token.setLexeme("-");
                        break;
                    }
                case '/':
                    Char = GetChar();
                    if (Char == '/') {
                        while (Char != '\n' && Char != 0)
                            Char = GetChar();
                        BackChar(Char);
                        return GetToken();
                    } else {
                        BackChar(Char);
                        token.setType(Token_Type.DIV);
                        token.setLexeme("/");
                        break;
                    }
                case '*':
                    Char = GetChar();
                    if (Char == '*') {
                        token.setType(Token_Type.POWER);
                        token.setLexeme("**");
                        break;
                    } else {
                        BackChar(Char);
                        token.setType(Token_Type.MUL);
                        token.setLexeme("*");
                        break;
                    }

                default:
                    token.setType(Token_Type.ERRTOKEN);
                    token.setLexeme("ERR");
                    break;
            }
        }
//        System.out.println(TokenBuffer);
        token.setLexeme(TokenBuffer);
        return token;
    }

    public static void BackToken() {    //ForStatement
//        System.out.println(BackStr);
        try {
            reader.unread(BackStr.getBytes());
            BackStr = "";
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}



