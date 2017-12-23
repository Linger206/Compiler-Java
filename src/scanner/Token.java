package scanner;

public class Token {
    private int type;				// 记号类别
    private String lexeme;			// 构成记号的字符串
    private double value;				// 记号为常数时，常数的值
    //private String Func;		// 记号为函数时, 函数指针

    public Token(int t, String l, double v){
        this.type = t;
        this.lexeme = l;
        this.value = v;
        //Func = F;
    }

    public void setType(int t) { this.type = t; }

    public void setLexeme(String l) { this.lexeme = l;}

    public void setValue(double v) { this.value = v; }

    public int getType() { return type; }

    public double getValue() { return value; }

    public String getLexeme() {
        return lexeme;
    }
}
