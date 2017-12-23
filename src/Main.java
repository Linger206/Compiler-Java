import parser.Parser;

import javax.swing.*;
import java.awt.*;

import static parser.Parser.*;

public class Main extends JFrame {

    public static Graphics graphics;

    public Main() {
        setTitle("Dot paint");
        setSize(1080, 720);
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
    public static void main(String[] args) {
        Main main = new Main();
        ParserInit("C:/Users/Wuming/Desktop/test.txt");
    }

    @Override
    public void paint(Graphics g) {
        graphics = g;
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
            for (int i = 0; i < Parser.Line; i++){
            graphics.drawOval((int)point[i][0], (int)point[i][1], 1, 1);
            System.out.println("(" + point[i][0] + "," + point[1][1] + ")");
        }
        System.out.println("Draw Complete");
    }

}
