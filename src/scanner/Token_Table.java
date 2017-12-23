package scanner;

public class Token_Table {     //符号表内容
    public Token[] TokenTab =
            {
                    new Token(Token_Type.CONST_ID, "PI", 3.1415926),
                    new Token(Token_Type.CONST_ID, "E", 2.71828),
                    new Token(Token_Type.T, "T", 0.0),
                    new Token(Token_Type.FUNC, "SIN", 0.0),
                    new Token(Token_Type.FUNC, "COS", 0.0),
                    new Token(Token_Type.FUNC, "TAN", 0.0),
                    new Token(Token_Type.FUNC, "LN", 0.0),
                    new Token(Token_Type.FUNC, "EXP", 0.0),
                    new Token(Token_Type.FUNC, "SQRT", 0.0),
                    new Token(Token_Type.ORIGIN, "ORIGIN", 0.0),
                    new Token(Token_Type.SCALE, "SCALE", 0.0),
                    new Token(Token_Type.ROT, "ROT", 0.0),
                    new Token(Token_Type.IS, "IS", 0.0),
                    new Token(Token_Type.FOR, "FOR", 0.0),
                    new Token(Token_Type.FROM, "FROM", 0.0),
                    new Token(Token_Type.TO, "TO", 0.0),
                    new Token(Token_Type.STEP, "STEP", 0.0),
                    new Token(Token_Type.DRAW, "DRAW", 0.0),
            };

    public Token getToken(int i) { return TokenTab[i];}

}
