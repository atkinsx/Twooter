import twooter.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Twooter implements ActionListener
{
    private TwooterClient client;
    private TwooterGUI gui;
    private TwooterCode code;
    public Twooter()
    {
        client = new TwooterClient();
        gui = new TwooterGUI(this);
        code = new TwooterCode(client);
    }

    public void actionPerformed(ActionEvent e)
    {
        String username;
        username = gui.getCurrentUsername();
        System.out.println(username);
    }
    
}