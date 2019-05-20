import twooter.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;

public class Twooter implements ActionListener
{
    private TwooterClient client;
    private TwooterGUI gui;
    private String username;
    private String token;
    private String txtFile;


    public Twooter()
    {
        client = new TwooterClient();
        gui = new TwooterGUI(this);
        boolean isLoggedIn = checkForFile();

        if (isLoggedIn)
        {
            username = readFromFile("username");
            token = readFromFile("token");
            gui.setInputUsername(username);
        }
    }

    public void actionPerformed(ActionEvent e)
    {
        username = gui.getInputUsername();
        token = getToken();

        if (token != null)
        {
            createNewFile("username", username);
            createNewFile("token", token);
        }
        else
        {
            try
            {
                username = readFromFile("username");
                token = readFromFile("token");
                client.refreshName(username, token);
                System.out.println(username);
                System.out.println(token);
            }
            catch (IOException event)
            {
                System.out.println("Some Kinda Error: " + event.getMessage());
            }
        }
    }

    public boolean checkForFile()
    {
        if (readFromFile("username") == null || readFromFile("token") == null)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    public String readFromFile(String type)
    {
        try
        {
            txtFile = type + ".txt";
            FileReader fileReader = new FileReader(txtFile);
            BufferedReader textReader = new BufferedReader(fileReader);
            String output = textReader.readLine();

            textReader.close();

            return output;
        }
        catch (IOException event)
        {
            System.out.println("Check Error: " + event.getMessage());
            return null;
        }
    }

    public void createNewFile(String type, String input)
    {
        try
        {
            txtFile = type + ".txt";
        
            FileWriter newFile = new FileWriter(txtFile, false);
            BufferedWriter printFile = new BufferedWriter(newFile);
            printFile.write(input);

            printFile.close();
        }
        catch (IOException event)
        {
            System.out.println("Creation Error: " + event.getMessage());
        }
    }

    public String getToken()
    {
        try
        {
            token = client.registerName(username);
            
            if (token != null)
            //if (!client.isActiveName(username) && username.length() > 4 && username.length() < 33)
            {
                System.out.println("Token: " + token);
                System.out.println("Username: " + username);
            }
            else
            {
                System.out.println("The username is already taken. Confirming this user is you...");
            }

            return token;
        }
        catch(Exception e)
        {
            System.out.println(e);
            return null;
        }
    }

    // public String readFromFile(String type) throws IOException
    // {
    //     txtFile = type + ".txt";
    //     FileReader fileReader = new FileReader(txtFile);
    //     BufferedReader textReader = new BufferedReader(fileReader);
    //     String output = textReader.readLine();

    //     textReader.close();
    //     return output;
    // }

    // public void writeToFile(String type, String input) throws IOException
    // {
        
    // }

    public void postMessage()
    {
        //client.postMessage(token, name, "Please don't read this... :(");
    }

    public void readMessages()
    {
        try
        {
            Message[] test = client.getMessages();

            for (int i = 0; i < test.length; i++)
                System.out.println(test[i].message);
        }
        catch (IOException event)
        {
            System.out.println("Read Error: " + event.getMessage());
        }
    }

    public void close()
    {
        client.disableLiveFeed();
    }
    
}