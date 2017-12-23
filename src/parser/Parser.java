package parser;

import scanner.Token;
import scanner.scan;
import scanner.Token_Type;

public class Parser {
    public static Token token;
    public static double Parameter = 0,
            Origin_x = 0, Origin_y = 0,
            Scale_x = 1, Scale_y = 1,
            Rot_angle = 0;
    public static final int Dots = 4096;
    public static double[][] point = new double[Dots][2];
    public static int Line = 0, Row = 0;

    public static void FetchToken() {
        token = scan.GetToken();
        if (token.getType() == Token_Type.ERRTOKEN) SyntaxError(1);
    }

    public static void MatchToken(int The_Token) {
        if (token.getType() != The_Token) {
            System.out.println("MatchToken\n");
            SyntaxError(2);
        }
        FetchToken();
    }

    public static void SyntaxError(int case_of) {
        switch (case_of) {
            case 1:
                ErrMsg(scan.LineNo, " Error Token ", token.getLexeme());
                break;
            case 2:
                ErrMsg(scan.LineNo, "Unexpected Token ", token.getLexeme());
                break;
        }
    }

    public static void ErrMsg(int LineNo, String descrip, String string) {
        System.out.println("LineNO: " + LineNo + "   " + descrip + "   " + string + '\n');
        scan.CloseScanner();
        System.exit(1);
    }

    public static void ParserInit(String FileName) {    //main interface
        if (!scan.InitScanner(FileName)) {
            System.out.println("Open Source File Failed!\n");
            return;
        }
        FetchToken();
        Program();
        scan.CloseScanner();
    }

    public static void Program() {
        while (token.getType() != Token_Type.NONTOKEN) {
            Statement();
            MatchToken(Token_Type.SEMICO);
        }
    }

    public static void Statement() {
        switch (token.getType()) {
            case Token_Type.ORIGIN:
                OriginStatement();
                break;
            case Token_Type.SCALE:
                ScaleStatement();
                break;
            case Token_Type.ROT:
                RotStatement();
                break;
            case Token_Type.FOR:
                ForStatement();
                break;
            default:
                System.out.println("Statement\n");
                SyntaxError(2);
        }
    }

    public static void OriginStatement() {
        MatchToken(Token_Type.ORIGIN);
        MatchToken(Token_Type.IS);
        MatchToken(Token_Type.L_BRACKET);
        Origin_x = Expression();

        MatchToken(Token_Type.COMMA);
        Origin_y = Expression();

        MatchToken(Token_Type.R_BRACKET);
    }

    public static void ScaleStatement() {
        MatchToken(Token_Type.SCALE);
        MatchToken(Token_Type.IS);
        MatchToken(Token_Type.L_BRACKET);
        Scale_x = Expression();

        MatchToken(Token_Type.COMMA);
        Scale_y = Expression();

        MatchToken(Token_Type.R_BRACKET);
    }

    public static void RotStatement() {
        MatchToken(Token_Type.ROT);
        MatchToken(Token_Type.IS);
        Rot_angle = Expression();

    }

    public static void ForStatement() {
        double start, end, step, x_p, y_p;
        MatchToken(Token_Type.FOR);
        MatchToken(Token_Type.T);
        MatchToken(Token_Type.FROM);
        start = Expression();

        MatchToken(Token_Type.TO);
        end = Expression();

        MatchToken(Token_Type.STEP);
        step = Expression();

        scan.flag = true;
        MatchToken(Token_Type.DRAW);

        for (Parameter = start; Parameter <= end; Parameter += step) {
            MatchToken(Token_Type.L_BRACKET);
            x_p = Expression();
            MatchToken(Token_Type.COMMA);
            y_p = Expression();
            MatchToken(Token_Type.R_BRACKET);

            point[Line][Row] = Coor_x(x_p, y_p);    //store horizontal coordinate
            point[Line][1 - Row] = Coor_y(x_p, y_p);    //vertical coordinate
            Line++;                                 //counter for numbers of coordinates

            if (Parameter + step <= end) {
                scan.BackToken();
                token = scan.GetToken();
            }
//            System.out.println("Start Parameter Step End"+'\n'+start+"  "+Parameter+"  "+step+"  "+end+'\n');
        }
        scan.BackStr = "";
        scan.flag = false;
        Parameter = 0;
    }

    public static double Expression() {
        double left = Term();

        while (true) {
            switch (token.getType()) {
                case (Token_Type.PLUS):
                    MatchToken(token.getType());
                    left += Term();
                    break;
                case (Token_Type.MINUS):
                    MatchToken(token.getType());
                    left -= Term();
                    break;
                default:
                    return left;
            }
        }
    }

    public static double Term() {
        double left = Factor();
        while (true) {
            switch (token.getType()) {
                case (Token_Type.MUL):
                    MatchToken(token.getType());
                    left *= Factor();
                    break;
                case (Token_Type.DIV):
                    MatchToken(token.getType());
                    left /= Factor();
                    break;
                default:
//                    System.out.println("Term:left "+left+'\n');
                    return left;
            }
        }
    }

    public static double Factor() {
        double right;
        if (token.getType() == Token_Type.PLUS) {
            MatchToken(Token_Type.PLUS);
            right = Factor();
        } else if (token.getType() == Token_Type.MINUS) {
            MatchToken(Token_Type.MINUS);
            right = Factor();
            right = 0 - right;  //reverse
        } else right = Component();
        return right;
    }

    public static double Component() {
        double left, right;
        left = Atom();
        if (token.getType() == Token_Type.POWER) {
            MatchToken(Token_Type.POWER);
            right = Component();
            left = Math.pow(left, right);
        }
        return left;
    }

    public static double Atom() {
        double address = 0, tmp;
        switch (token.getType()) {
            case Token_Type.CONST_ID:
                address = token.getValue();
                MatchToken(Token_Type.CONST_ID);
                break;
            case Token_Type.T:
                address = Parameter;
                MatchToken(Token_Type.T);
                break;
            case Token_Type.FUNC:
                switch (token.getLexeme()) {
                    case "SIN":
                        MatchToken(Token_Type.FUNC);
                        MatchToken(Token_Type.L_BRACKET);
                        tmp = Expression();
                        address = Math.sin(tmp);
                        MatchToken(Token_Type.R_BRACKET);
                        break;
                    case "COS":
                        MatchToken(Token_Type.FUNC);
                        MatchToken(Token_Type.L_BRACKET);
                        tmp = Expression();
                        address = Math.cos(tmp);
                        MatchToken(Token_Type.R_BRACKET);
                        break;
                    case "TAN":
                        MatchToken(Token_Type.FUNC);
                        MatchToken(Token_Type.L_BRACKET);
                        tmp = Expression();
                        address = Math.tan(tmp);
                        MatchToken(Token_Type.R_BRACKET);
                        break;
                    case "LN":
                        MatchToken(Token_Type.FUNC);
                        MatchToken(Token_Type.L_BRACKET);
                        tmp = Expression();
                        address = Math.log(tmp);
                        MatchToken(Token_Type.R_BRACKET);
                        break;
                    case "EXP":
                        MatchToken(Token_Type.FUNC);
                        MatchToken(Token_Type.L_BRACKET);
                        tmp = Expression();
                        address = Math.exp(tmp);
                        MatchToken(Token_Type.R_BRACKET);
                        break;
                    case "SQRT":
                        MatchToken(Token_Type.FUNC);
                        MatchToken(Token_Type.L_BRACKET);
                        tmp = Expression();
                        address = Math.sqrt(tmp);
                        MatchToken(Token_Type.R_BRACKET);
                        break;
                    default:
                        address = 0;
                }
                break;
            case Token_Type.L_BRACKET:
                MatchToken(Token_Type.L_BRACKET);
                address = Expression();
                MatchToken(Token_Type.R_BRACKET);
                break;
            default:
                SyntaxError(2);
        }
//        System.out.println("Address:"+address+'\n');
        return address;
    }

    public static double Coor_x(double x, double y) {   //calculate horizontal coordinate
        double Horizon;
        Horizon = x * Scale_x;
        y = y * Scale_y;
        Horizon = Horizon * Math.cos(Rot_angle) + y * Math.sin(Rot_angle) + Origin_x;
        return Horizon;
    }

    public static double Coor_y(double x, double y) {   //calculate vertical coordinate
        double Vertical;
        x = x * Scale_x;
        Vertical = y * Scale_y;
        Vertical = Vertical * Math.cos(Rot_angle) - x * Math.sin(Rot_angle) + Origin_y;
        return Vertical;
    }

}
