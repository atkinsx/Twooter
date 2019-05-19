import twooter.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;

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
        //https://www.homeandlearn.co.uk/java/java_and_databases.html
        String username;
        gui.setCurrentUsername();
        username = gui.getCurrentUsername();
        //System.out.println(username);
        String token = code.createUser(client, username);

        //if username does not exist

        try
        {
            code.writeTokenToFile(username, token);
        }
        catch (IOException event)
        {
            System.out.println("Error: " + event.getMessage());
        }

        //if username does exist

        try
        {
            System.out.println(code.readTokenFromFile(username));
            //also renew token
        }
        catch (IOException event)
        {
            System.out.println("Error: " + event.getMessage());
        }
    }
    
}