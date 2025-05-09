
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.TextField;
import java.awt.Window;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Executer {
    public static void main(String[] args) {
        String testAffichage2 = "1 |\u001B[47m \u001B[30m♜ \u001B[0m\u001B[0m \u001B[30m♞ \u001B[0m\u001B[47m \u001B[30m♝ \u001B[0m\u001B[0m \u001B[30m♚ \u001B[0m\u001B[47m \u001B[30m♛ \u001B[0m\u001B[0m \u001B[30m♝ \u001B[0m\u001B[47m \u001B[30m♞ \u001B[0m\u001B[0m \u001B[30m♜ \u001B[0m|\n2 |\u001B[0m \u001B[30m♟ \u001B[0m\u001B[47m \u001B[30m♟ \u001B[0m\u001B[0m \u001B[30m♟ \u001B[0m\u001B[47m \u001B[30m♟ \u001B[0m\u001B[0m \u001B[30m♟ \u001B[0m\u001B[47m \u001B[30m♟ \u001B[0m\u001B[0m \u001B[30m♟ \u001B[0m\u001B[47m \u001B[30m♟ \u001B[0m\u001B[0m|\n3 |\u001B[47m   \u001B[0m   \u001B[47m   \u001B[0m   \u001B[47m   \u001B[0m   \u001B[47m   \u001B[0m   |\n4 |\u001B[0m   \u001B[47m   \u001B[0m   \u001B[47m   \u001B[0m   \u001B[47m   \u001B[0m   \u001B[47m   \u001B[0m|\n5 |\u001B[47m   \u001B[0m   \u001B[47m   \u001B[0m   \u001B[47m   \u001B[0m   \u001B[47m   \u001B[0m   |\n6 |\u001B[0m   \u001B[47m   \u001B[0m   \u001B[47m   \u001B[0m   \u001B[47m   \u001B[0m   \u001B[47m   \u001B[0m|\n7 |\u001B[47m \u001B[37m♙ \u001B[0m\u001B[0m \u001B[37m♙ \u001B[0m\u001B[47m ♙ \u001B[0m ♙ \u001B[47m ♙ \u001B[0m ♙ \u001B[47m ♙ \u001B[0m ♙ |\n8 |\u001B[0m ♖ \u001B[47m ♘ \u001B[0m ♗ \u001B[47m ♕ \u001B[0m ♔ \u001B[47m ♗ \u001B[0m ♘ \u001B[47m ♖ \u001B[0m|\n    A  B  C  D  E  F  G  H";


        System.out.println(testAffichage2);
        System.out.println("Queen: \\u265B.\\n");


        JFrame frame = new JFrame("Jeu d'échec");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(true);

        panel.setLayout(new FlowLayout());

        Rectangle rect = new Rectangle();
        rect.setBounds(10, 10, 200, 300);

        JLabel label = new JLabel("ahahaha c'est bob ♜lenon");
        label.setFont(Font.getFont(Font.MONOSPACED));
        

        JPanel panel2 = new JPanel();
        panel2.setBackground(Color.RED);
        panel2.setBounds(10, 10, 200, 300);

        panel2.add(label);

        JButton button = new JButton("Enter");
        button.setBounds(20, 20, 200, 50);
    
        TextField text = new TextField("caca");
        text.setLocation(10, 10);
        panel.setBackground(Color.BLUE);
        panel.add(text);
        panel.add(button);

        frame.add(panel2);

        frame.add(panel);

        frame.setLocationByPlatform(true);
        frame.setVisible(true);
        frame.setResizable(true);

        frame.setBounds(0, 0, 500, 500);
        Window win = new Window(frame);

        Partie.nouvellePartie();

    }
}