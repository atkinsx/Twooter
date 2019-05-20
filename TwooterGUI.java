//import twooter.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class TwooterGUI
{
    private GridLayout grid = new GridLayout();
    private int xSize = 8;
    private int ySize = 1;
    private JButton submit;
    private JFrame window;
    private JPanel panel;
    private JTextField usernameBox;

    public TwooterGUI(Twooter twooter)
    {
        window = new JFrame("Twooter");
        panel = new JPanel();
        grid = new GridLayout(xSize, ySize);
        usernameBox = new JTextField();
        submit = new JButton("Submit");
        submit.addActionListener(twooter);

        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setupLoginWindow();
    }

    public String getInputUsername()
    {
        return usernameBox.getText();
    }

    public void setInputUsername(String input)
    {
        usernameBox.setText(input);
    }

    public void setupLoginWindow()
    {
        window.setContentPane(panel);
        panel.setLayout(grid);
        panel.add(usernameBox);
        panel.add(submit);
        //window.setTitle(windowName);
        window.setSize(800,800);
        window.setVisible(true);
    }
}