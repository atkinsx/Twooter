//import twooter.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class TwooterGUI
{
    private GridLayout grid = new GridLayout();
    private int xSize = 8;
    private int ySize = 1;
    private JButton send;
    private JButton submit;
    private JFrame window;
    private JPanel panel;
    private JTextField messageBox;
    private JTextField usernameBox;

    public TwooterGUI(Twooter twooter)
    {
        window = new JFrame("Twooter");
        panel = new JPanel();
        grid = new GridLayout(xSize, ySize);
        messageBox = new JTextField();
        usernameBox = new JTextField();
        send = new JButton("Send");
        submit = new JButton("Submit");

        send.addActionListener(twooter);
        submit.addActionListener(twooter);

        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setupLoginWindow();
    }

    public JButton getSend()
    {
        return send;
    }

    public JButton getSubmit()
    {
        return submit;
    }

    public String getInputMessage()
    {
        return messageBox.getText();
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
        panel.add(messageBox);
        panel.add(usernameBox);
        panel.add(send);
        panel.add(submit);
        window.setSize(800,800);
        window.setVisible(true);
    }
}