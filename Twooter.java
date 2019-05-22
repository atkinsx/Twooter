import twooter.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;

/**
 * This class represents the code that runs behind the front-end.
 * 
 * It also acts as a client to the Twooter API, bridging between the
 * information on the server side and the user.
 * 
 * @see TwooterGUI
 * 
 * @author Xaq Atkins
 */
public class Twooter implements ActionListener, UpdateListener
{
    private static final int MAX_MESSAGES = 30;

    private TwooterClient client;
    private TwooterGUI gui;
    private Message[] listOfMessages;
    private String username;
    private String token;
    private String txtFile;

    /**
     * Constructor method. Immediately instantiates the client side
     * code and the GUI, before proceeding to allow the user to log in.
     */
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

    /**
     * The method that runs whenever a button is clicked.
     * 
     * It selects between the potential buttons by comparing
     * the source of the event with the accessor methods of the unique
     * buttons. It then runs functions that correspond to that button.
     * 
     * For instance, hitting the "Submit" button sends the user's login
     * info to the server and begins setting up the GUI for the main page
     * of the app.
     * 
     * @param e The event caused by the button clicked
     */
    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource() == gui.getSubmit())
        {
            if (checkIfIsUp()) //Checks if the server is up and attempts to connect if so
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
        else if (e.getSource() == gui.getSend()) //Sends a message on button click
        {
            sendMessage();
        }
        else if (e.getSource() == gui.getSearch()) //Searches for a message, user or #tag and returns the result of the query
        {
            gui.filterMessages();
        }
        else if (e.getSource() == gui.getHome()) //returns the user to the main 
        {
            updateMessagesList();
            retrieveMessages();
            getUsers();
        }
        else if (e.getSource() == gui.getFollow()) //finds the followed users
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
        else //attempts to decide which button was pressed
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

    /**
     * Method that sends off the username and token to the server.
     * 
     * It also reads the previous login details from a text file
     * stored with the .jar files.
     */
    public void submitLogin()
    {
        username = gui.getInputUsername();
        token = getToken();

        if (token != null) //if the token is null, then the user already exists
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
                client.refreshName(username, token); //prevents the account from expiring
            }
            catch (IOException e)
            {
                System.out.println("Server Error: " + e.getMessage());
            }
        }
    }

    /**
     * Method that checks if a file exists.
     * @param filename The name of the file the code is looking for.
     * @return true or false depending on whether the file exists or not.
     */
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

    /**
     * A method that reads from a text file, used to obtain
     * login information and 
     * @param name the name of the file once it is saved (i.e. name.txt)
     * @return result, the entire string in the text file
     */
    public String readFromFile(String name)
    {
        try
        {
            txtFile = name + ".txt";
            FileReader fileReader = new FileReader(txtFile);
            BufferedReader textReader = new BufferedReader(fileReader);
            String output = "";
            String result = "";

            do //keeps reading lines until it reaches an empty line and stores them in result
            {
                result += output;
                output = textReader.readLine();
            } while(output != null);

            textReader.close();

            return result;
        }
        catch (IOException e)
        {
            System.out.println("IO Error: " + e.getMessage());
            return null;
        }
    }

    /**
     * Method that creates a .txt file if none exists already
     * and prints some text to it
     * @param name name of the file to be created
     * @param input text to be stored in the file
     */
    public void createNewFile(String name, String input)
    {
        try
        {
            txtFile = name + ".txt";
        
            FileWriter newFile = new FileWriter(txtFile, false);
            BufferedWriter printFile = new BufferedWriter(newFile);
            printFile.write(input);

            printFile.close();
        }
        catch (IOException e)
        {
            System.out.println("IO Error: " + e.getMessage());
        }
    }

    /**
     * A procedure that writes a string to a pre-exsting file.
     * 
     * @param name the name of the file to be edited
     * @param input the string to be entered into the file
     */
    public void writeToFile(String name, String input)
    {
        try
        {
            txtFile = name + ".txt";
        
            FileWriter newFile = new FileWriter(txtFile, true);
            BufferedWriter printFile = new BufferedWriter(newFile);

            printFile.write(input);
            printFile.newLine(); //creates a line break in the file


            printFile.close();
        }
        catch (IOException e)
        {
            System.out.println("IO Error: " + e.getMessage());
        }
    }

    /**
     * A function that retrieves the token from the server
     * based on the input username; if it is a valid, available
     * username, it will return a token, otherwise it will
     * return null
     * @return token that corresponds to the new username, or null
     * if username is invalid
     */
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

    /**
     * Procedure that sends the last 30 messages from the server
     * to the GUI to be output
     */
    public void retrieveMessages()
    {
        for (int i = 0; i < MAX_MESSAGES; i++)
        {
            gui.outputMessageStream(i, listOfMessages[i].message, listOfMessages[i].name, listOfMessages[i].id);
        }
    }

    /**
     * Closes the live feed service
     */
    public void close()
    {
        client.disableLiveFeed();
    }

    /**
     * Updates the list of messages from the live feed
     */
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

    /**
     * This runs on prompt from the server.
     * Every time something changes server side,
     * it sends an even to this handler.
     * @param e The event flagged by the action
     */
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
    
    /**
     * Checks if the server is up
     * @return true if it is, false if not
     */
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

    /**
     * Procedure that updates the list of users displayed on
     * the UI
     */
    public void getUsers()
    {
        for (int i = 0; i < MAX_MESSAGES; i++)
        {
            gui.listUsers(i, listOfMessages[i].name, username);
        }
    }

    /**
     * Procedure that allows the user to follow other
     * users. It writes the followed users into a text
     * file so the follows aren't lost when the program
     * is closed.
     * 
     * @param user the user to be followed
     */
    public void followUser(String user)
    {
        String check;
        boolean doesFileExist;

        if (!user.equals(username)) //Doesn't allow user to follow themselves
        {
            doesFileExist = checkForFile("following"); //checks for the file that is being written to

            if (!doesFileExist) //creates a new file if the required one doesn't exist
            {
                createNewFile("following", "");
            }
            
            check = readFromFile("following");

            if (!check.contains(user)) //checks to see if the named user is already being followed
            {
                writeToFile("following", "&" + user + "/"); //adds special characters to make the start/end of each name easy to find
                gui.alert("You are now following " + user, "Follow Confirmation");
            }
            else
            {
                gui.alert("You are already following " + user, "Follow Denied");
            }
        }
        else
        {
            gui.alert("You can't follow yourself!", "Error");
        }
    }

    /**
     * Adds the followed users to the GUI for the user's benefit
     * It then refreshes the GUI and displays the info
     */
    public void getFollowing()
    {
        String userFollows = readFromFile("following");
        gui.outputFollowing(userFollows);
        getUsers();
    }
}