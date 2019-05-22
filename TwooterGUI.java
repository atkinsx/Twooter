import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
/**
 * This class is used to create a GUI so the user can interact with
 * the program effectively. The class is made up of many Swing elements
 * and functions that edit, show and hide Swing objects
 */
public class TwooterGUI
{
    private static final int MAX_MESSAGES = 30;
    private static final int MAX_FOLLOWING = 30;

    private Font inputFont = new Font("Arial", Font.PLAIN, 40);
    private Font buttonFont = new Font("Arial", Font.PLAIN, 24);
    private Font messageFont = new Font("Arial", Font.PLAIN, 18);

    private BoxLayout layoutMP;
    private BoxLayout layoutIP;
    private BoxLayout layoutPP;

    private JButton home;
    private JButton send;
    private JButton submit;
    private JButton search;
    private JButton follow;

    private JFrame window;

    private JPanel loginPanel;
    private JPanel usersPanel;
    private JPanel messagesPanel;
    private JPanel inputPanel;
    private JPanel postPanel;

    private JScrollPane messageList;
    private JScrollPane userList;
    private JSplitPane main;

    private JTextArea[] messages;
    private JTextField messageBox;
    private JTextField usernameBox;

    private JButton[] users;

    /**
     * Constructor for the TwooterGUI method.
     * 
     * The constructor focuses on initialising the GUI with all of the
     * UI elements before loading. It then adds them all to the window
     * to reduce slowdown during runtime.
     * 
     * @param twooter passes in the code layer to add
     * action listeners.
     */
    public TwooterGUI(Twooter twooter)
    {
        window = new JFrame("Twooter");
        loginPanel = new JPanel();
        usersPanel = new JPanel();
        messagesPanel = new JPanel();
        inputPanel = new JPanel();
        postPanel = new JPanel();

        userList = new JScrollPane(usersPanel);
        messageList = new JScrollPane(postPanel);
        main = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, userList, messageList); //splits the screen in half; a list of messages and a list of users

        layoutMP = new BoxLayout(messagesPanel, BoxLayout.Y_AXIS);
        layoutIP = new BoxLayout(inputPanel, BoxLayout.Y_AXIS);
        layoutPP = new BoxLayout(postPanel, BoxLayout.Y_AXIS);

        GridLayout templayoutUP = new GridLayout(50, 1);

        usersPanel.setLayout(templayoutUP);
        messagesPanel.setLayout(layoutMP);
        inputPanel.setLayout(layoutIP);
        postPanel.setLayout(layoutPP);

        messageBox = new JTextField();
        usernameBox = new JTextField();

        home = new JButton("Home");
        search = new JButton("Search");
        send = new JButton("Send");
        submit = new JButton("Submit");
        follow = new JButton("Follow");

        submit.setFont(buttonFont);

        postPanel.add(messageBox);
        messageBox.setPreferredSize(new Dimension(700, 250));
        messageBox.setFont(inputFont);
        postPanel.add(send);
        postPanel.add(search);
        postPanel.add(follow);
        postPanel.add(inputPanel);
        postPanel.add(messagesPanel);
        usersPanel.add(home);

        messages = new JTextArea[MAX_MESSAGES];
        users = new JButton[MAX_MESSAGES + MAX_FOLLOWING];

        send.addActionListener(twooter);
        search.addActionListener(twooter);
        submit.addActionListener(twooter);
        home.addActionListener(twooter);
        follow.addActionListener(twooter);

        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setupLoginWindow();

        for (int i = 0; i < MAX_MESSAGES; i++)
        {
            messages[i] = new JTextArea();
            users[i] = new JButton("empty");
            users[i].addActionListener(twooter);
            users[i + MAX_FOLLOWING] = new JButton("empty");
            users[i + MAX_FOLLOWING].addActionListener(twooter);
        }
    }

    /**
     * Accessor method for the JButton send
     * @return the send button
     */
    public JButton getSend()
    {
        return send;
    }

    /**
     * Accessor method for the JButton search
     * @return the search button
     */
    public JButton getSearch()
    {
        return search;
    }

    /**
     * Accessor method for the JButton submit
     * @return the submit button
     */
    public JButton getSubmit()
    {
        return submit;
    }

    /**
     * Accessor method for the JButton home
     * @return the home button
     */
    public JButton getHome()
    {
        return home;
    }

    /**
     * Accessor method for the JButton follow
     * @return the follow button
     */
    public JButton getFollow()
    {
        return follow;
    }

    /**
     * Gets the message typed by the user to post
     * to the feed
     * @return the output of the text box for sending
     */
    public String getInputMessage()
    {
        String output = messageBox.getText();
        messageBox.setText(null);
        return output;
    }

    /**
     * Gets the username entered in the login menu
     * @return the output of the username textbox
     */
    public String getInputUsername()
    {
        return usernameBox.getText();
    }

    /**
     * Changes the value of the textboxes; usually
     * set to blank so the user can type a new message
     * @param input the new value of the textbox (usually blank)
     */
    public void setInputUsername(String input)
    {
        usernameBox.setText(input);
    }

    /**
     * Sets up the initial login window, adding many of the features to the window
     */
    public void setupLoginWindow()
    {
        JLabel title = new JLabel("Twooter");
        JLabel subtitle = new JLabel("Not-so professional social media for SCC110");
        Font titleFont = new Font("Arial", Font.PLAIN, 150);
        Font subtitleFont = new Font("Arial", Font.PLAIN, 40);

        title.setFont(titleFont);
        subtitle.setFont(subtitleFont);

        window.add(loginPanel);
        loginPanel.setLayout(new GridLayout(6, 1));
        loginPanel.add(title);
        loginPanel.add(subtitle);
        loginPanel.add(usernameBox);
        loginPanel.add(submit);
        usernameBox.setFont(inputFont);
        window.setSize(1000,1000);
        window.setVisible(true);
    }

    /**
     * Sets up the messaging window
     */
    public void setupMainWindow()
    {
        window.add(main);
        messageList.getVerticalScrollBar().setUnitIncrement(20);
        main.setResizeWeight(0.1);
        loginPanel.setVisible(false);
    }

    /**
     * Prints the messages from the server to the screen
     * @param i the index of the message in the array
     * @param message the message to post
     * @param username the user who sent the message
     * @param id the ID of the message
     */
    public void outputMessageStream(int i, String message, String username, String id)
    {
        if (i == 0)
        {
            messagesPanel.setVisible(false);
        }

        if (message.length() > 500)
        {
            message = "Normally I don't condone censorship, but that wall of text was probably an image, and I'm not competent enough to know how to show you images. But if it makes you feel any better, it was probably very pretty or funny or cool or something.";
        }

        String newText = "@" + username + ": " + message + "\n\n" + id;
        messages[i].setText(newText);
        messages[i].setLineWrap(true);
        messages[i].setBorder(BorderFactory.createLineBorder(Color.BLACK));
        messages[i].setFont(messageFont);
        messages[i].setVisible(true);
        messagesPanel.add(messages[i]);

        if (i == MAX_MESSAGES - 1)
        {
            messagesPanel.setVisible(true);
        }
    }

    /**
     * Checks if the latest message is a new message to prevent unneeded updates
     * @param inputMessage the new message to check
     * @param inputUsername the user who sent the message 
     * @param inputID the ID of the message
     * @return whether the message has already been sent
     */
    public boolean isNewMessage(String inputMessage, String inputUsername, String inputID)
    {
        String latestMessage = "@" + inputUsername + ": " + inputMessage + "\n\n" + inputID;
        String message = "Normally I don't condone censorship, but that wall of text was probably an image, and I'm not competent enough to know how to show you images. But if it makes you feel any better, it was probably very pretty or funny or cool or something.";
        if (latestMessage.equals(messages[0].getText()) || latestMessage.equals(message))
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    /**
     * Accessor for the messagesPanel
     * @return the messagePanel
     */
    public JPanel getMessagesPanel()
    {
        return messagesPanel;
    }

    /**
     * Creates a dialog box for warnings and messages
     * @param error the details of the error
     * @param title a summary of the error
     */
    public void alert(String error, String title)
    {
        JOptionPane.showMessageDialog(null, error, title, JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Lists all of the recently active users in the sidebar
     * @param index location of the user in the array
     * @param username the username of the user
     * @param currentUser the user who is using the app
     */
    public void listUsers(int index, String username, String currentUser)
    {
        if (index == 0)
        {
            usersPanel.setVisible(false);
        }

        int i = 0;
        boolean isValid = true;

        while (!users[i].getText().equals("empty") && isValid)
        {
            if(username.equals(users[i].getText()) || i >= MAX_MESSAGES + MAX_FOLLOWING)
            {
                isValid = false;
            }
            else
            {
                i++;
            }
        }

        if (users[i].getText().equals("empty") && isValid)
        {
            users[i].setText(username);
            users[i].setVisible(true);
        }

        if (users[index].getText().equals("empty"))
        {
            users[index].setVisible(false);
        }

        if (currentUser.equals(users[i].getText()))
        {
            users[i].setBackground(Color.ORANGE); //uniquely identifies the user
        }
        else
        {
            users[i].setBackground(Color.WHITE);
        }

        usersPanel.add(users[index]);

        if (index == MAX_MESSAGES - 1)
        {
            usersPanel.setVisible(true);
        }
    }

    /**
     * Allows the user to filter through messages for specific users and queries
     */
    public void filterMessages()
    {
        int count = 0;

        for (int i = 0; i < MAX_MESSAGES; i++)
        {
            messages[i].setVisible(true);

            if (!messages[i].getText().contains(messageBox.getText()))
            {
                messages[i].setVisible(false);
                count++;
            }
        }

        messageBox.setText(null); //empties the textbox for next input

        if (count == MAX_MESSAGES)
        {
            alert("No results for that query", "No results");

            for (int i = 0; i < MAX_MESSAGES; i++)
            {
                messages[i].setVisible(true);
            }
        }
    }

    /**
     * Returns the user at the given index
     * @param index the index of the user in the array
     * @return a specific user
     */
    public JButton getUser(int index)
    {
        return users[index];
    }

    /**
     * Filters the messages to a specific user's messages
     * @param user the selected user
     */
    public void filterByUser(JButton user)
    {
        int count = 0;

        for (int i = 0; i < MAX_MESSAGES; i++)
        {
            messages[i].setVisible(true);

            if (!messages[i].getText().contains(user.getText()) || !(messages[i].getText().indexOf(user.getText()) == 1))
            {
                messages[i].setVisible(false);
                count++;
            }
        }

        if (count == MAX_MESSAGES)
        {
            alert("This user hasn't posted anything recently.", "No results");

            user.setBackground(Color.WHITE);

            for (int i = 0; i < MAX_MESSAGES; i++)
            {
                messages[i].setVisible(true);
            }
        }
    }

    /**
     * Returns the username of the clicked button
     * @return the user's username
     */
    public String getSelectedUsername()
    {
        for (int i = 0; i < MAX_MESSAGES + MAX_FOLLOWING; i++)
        {
            if (users[i].getBackground() == Color.CYAN)
            {
                return users[i].getText();
            }
        }

        return null;
    }

    /**
     * Outputs the users that the user is following
     * to the UI
     * @param input the output of the "following.txt"
     * text file
     */
    public void outputFollowing(String input)
    {
        String saveString;
        int startPointer;
        int endPointer;

        while (!input.equals(""))
        {
            startPointer = input.indexOf('&');
            endPointer = input.indexOf('/');
            saveString = input.substring(startPointer + 1, endPointer);
            listUsers(1, saveString, "N/A");
            input = input.substring(endPointer + 1);
        }
    }
}