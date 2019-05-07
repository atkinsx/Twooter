//import twooter.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class TwooterGUI implements ActionListener
{
    private GridLayout grid = new GridLayout();
    private int xSize = 8;
    private int ySize = 1;
    private JButton submit;
    private JFrame window;
    private JPanel panel;
    private JTextField usernameBox;
    private String windowName = "Twooter";

    public TwooterGUI()
    {
        window = new JFrame();
        panel = new JPanel();
        grid = new GridLayout(xSize, ySize);
        usernameBox = new JTextField();
        submit = new JButton("Submit");
        submit.addActionListener(this);

        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setupLoginWindow();
    }

    public void setupLoginWindow()
    {
        window.setContentPane(panel);
        panel.setLayout(grid);
        panel.add(usernameBox);
        panel.add(submit);
        window.setTitle(windowName);
        window.setSize(800,800);
        window.setVisible(true);
    }

    
    public void actionPerformed(ActionEvent e)
    {
        System.out.println(usernameBox.getText());
    }
    
}