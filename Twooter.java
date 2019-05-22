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
        boolean isLoggedIn = checkForFile("username") && checkForFile("token");

        if (isLoggedIn)
        {
            username = readFromFile("username");
            token = readFromFile("token");
            gui.setInputUsername(username);
        }
    }

    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource() == gui.getSubmit())
        {
            if (checkIfIsUp())
            {
                submitLogin();
                gui.setupMainWindow();
                updateMessagesList();
                retrieveMessages();
                getUsers();
                getFollowing();
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
        else if (e.getSource() == gui.getSearch())
        {
            gui.filterMessages();
        }
        else if (e.getSource() == gui.getHome())
        {
            updateMessagesList();
            retrieveMessages();
            getUsers();
        }
        else if (e.getSource() == gui.getFollow())
        {
            String userToFollow = gui.getSelectedUsername();

            if (userToFollow != null)
            {
                followUser(userToFollow);
            }
            else
            {
                gui.alert("No user selected! Choose one from the side bar.", "ERROR");
            }
        }
        else
        {
            for (int i = 0; i < MAX_MESSAGES; i++)
            {
                if (username.equals(gui.getUser(i).getText()))
                {
                    gui.getUser(i).setBackground(Color.ORANGE);
                }
                else
                {
                    gui.getUser(i).setBackground(Color.WHITE);
                }
                
                if (gui.getUser(i) == e.getSource())
                {
                    gui.getUser(i).setBackground(Color.CYAN);
                    gui.filterByUser(gui.getUser(i));
                }
            }
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

    public boolean checkForFile(String filename)
    {
        if (readFromFile(filename) == null)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    public String readFromFile(String name)
    {
        try
        {
            txtFile = name + ".txt";
            FileReader fileReader = new FileReader(txtFile);
            BufferedReader textReader = new BufferedReader(fileReader);
            String output = "";
            String result = "";

            do
            {
                result += output;
                output = textReader.readLine();
            } while(output != null);

            textReader.close();

            return result;
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

    public void writeToFile(String name, String input)
    {
        try
        {
            txtFile = name + ".txt";
        
            FileWriter newFile = new FileWriter(txtFile, true);
            BufferedWriter printFile = new BufferedWriter(newFile);

            printFile.write(input);
            printFile.newLine();


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
            gui.outputMessageStream(i, listOfMessages[i].message, listOfMessages[i].name, listOfMessages[i].id);
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

        if (gui.getSelectedUsername() == null)
        {
            System.out.println("Getting messages...");
            if (gui.isNewMessage(listOfMessages[0].message, listOfMessages[0].name, listOfMessages[0].id))
            {
                retrieveMessages();
                getUsers();
            }
            else
            {
                System.out.println("No new messages.");
            }
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
            gui.listUsers(i, listOfMessages[i].name, username);
        }
    }

    public void followUser(String user)
    {
        String check;
        boolean doesFileExist;

        if (!user.equals(username))
        {
            doesFileExist = checkForFile("following");

            if (!doesFileExist)
            {
                createNewFile("following", "");
            }
            
            check = readFromFile("following");

            if (!check.contains(user))
            {
                writeToFile("following", "&" + user + "/");
                gui.alert("You are now following " + user, "Follow Confirmation");
            }
            else
            {
                gui.alert("You are already following " + user, "Follow Denied");
            }
        }
    }

    public void getFollowing()
    {
        String userFollows = readFromFile("following");
        gui.outputFollowing(userFollows);
        getUsers();
    }
}