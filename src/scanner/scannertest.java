package scanner;

public class scannertest {
    public static void main(String[] args) {
        Token token;
        if(!scan.InitScanner("C:/Users/Wuming/Desktop/test.txt"))
        {
            System.out.println("Open Source File Error!");
        }
        System.out.println("Token_type    String    Const");
        System.out.println("_______________________________________________");
        while(true)
        {
            token = scan.GetToken();
            if(token.getType() != Token_Type.NONTOKEN)
                System.out.println(token.getType() + "\t" +  token.getLexeme() + "\t" + token.getValue());
            else    break;
        }
        System.out.println("______________________________");
        scan.CloseScanner();

    }

}

