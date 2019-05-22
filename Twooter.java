import twooter.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;

public class Twooter implements ActionListener, UpdateListener
{
    private static final int MAX_MESSAGES = 30;

    private TwooterClient client;
    private TwooterGUI gui;
    private Message[] listOfMessages;
    private String username;
    private String token;
    private String txtFile;

    public Twooter()
    {
        client = new TwooterClient();
        gui = new TwooterGUI(this);
        client.addUpdateListener(this);
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
        System.out.println(e.getSource());
        if (e.getSource() == gui.getSubmit())
        {
            if (checkIfIsUp())
            {
                submitLogin();
                gui.setupMainWindow();
                updateMessagesList();
                retrieveMessages();
                getUsers();
                client.enableLiveFeed();
            }
            else
            {
                gui.alert("Error: Server appears to be down, try again later.", "ERROR");
            }
        }
        else if (e.getSource() == gui.getSend())
        {
            sendMessage();
        }
        else
        {
            System.out.println("NO");
        }
    }

    public void submitLogin()
    {
        username = gui.getInputUsername();
        token = getToken();

        if (token != null)
        {
            gui.alert("Username ''" + username + "' doesn't exist. Creating new user...", "NO USER FOUND");

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
            catch (IOException e)
            {
                System.out.println("Some Kinda Error: " + e.getMessage());
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
        catch (IOException e)
        {
            System.out.println("Check Error: " + e.getMessage());
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
        catch (IOException e)
        {
            System.out.println("Creation Error: " + e.getMessage());
        }
    }

    public String getToken()
    {
        try
        {
            token = client.registerName(username);
            
            if (token != null)
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

    public void sendMessage()
    {
        String newMessage = gui.getInputMessage();

        try
        {
            client.postMessage(token, username, newMessage);
            System.out.println(token + " / " + username + ": " + newMessage);
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }

    public void retrieveMessages()
    {
        for (int i = 0; i < MAX_MESSAGES; i++)
        {
            gui.outputMessageStream(i, listOfMessages[i].message, listOfMessages[i].name);
        }
    }

    public void close()
    {
        client.disableLiveFeed();
    }

    public void updateMessagesList()
    {
        try
        {
            listOfMessages = client.getMessages();
        }
        catch (IOException e)
        {
            System.out.println("Read Error: " + e.getMessage());
        }
    }

    public void handleUpdate(TwooterEvent e)
    {
        updateMessagesList();

        System.out.println("Getting messages...");
        if (gui.isNewMessage(listOfMessages[0].message, listOfMessages[0].name))
        {
            retrieveMessages();
            getUsers();
        }
        else
        {
            System.out.println("No new messages.");
        }
    }
    
    public boolean checkIfIsUp()
    {
        boolean result = false;

        try
        {
            result = client.isUp();
        }
        catch(IOException e)
        {
            System.out.println("Read Error: " + e.getMessage());
        }

        return result;
    }

    public void getUsers()
    {
        for (int i = 0; i < MAX_MESSAGES; i++)
        {
            //System.out.println("int i: " + i);
            gui.listUsers(i, listOfMessages[i].name);
        }
    }
}