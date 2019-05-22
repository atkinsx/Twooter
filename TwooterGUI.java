//import twooter.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class TwooterGUI
{
    private static final int MAX_MESSAGES = 30;

    private Font inputFont = new Font("Arial", Font.PLAIN, 40);
    private Font buttonFont = new Font("Arial", Font.PLAIN, 24);
    private Font messageFont = new Font("Arial", Font.PLAIN, 18);

    private BoxLayout layoutUP;
    private BoxLayout layoutMP;
    private BoxLayout layoutIP;
    private BoxLayout layoutPP;

    private JButton send;
    private JButton submit;

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
        main = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, userList, messageList);

        messageBox = new JTextField();
        usernameBox = new JTextField();
        send = new JButton("Send");
        submit = new JButton("Submit");
        submit.setFont(buttonFont);

        layoutUP = new BoxLayout(usersPanel, BoxLayout.Y_AXIS);
        layoutMP = new BoxLayout(messagesPanel, BoxLayout.Y_AXIS);
        layoutIP = new BoxLayout(inputPanel, BoxLayout.Y_AXIS);
        layoutPP = new BoxLayout(postPanel, BoxLayout.Y_AXIS);

        usersPanel.setLayout(layoutUP);
        messagesPanel.setLayout(layoutMP);
        inputPanel.setLayout(layoutIP);
        postPanel.setLayout(layoutPP);

        postPanel.add(messageBox);
        messageBox.setPreferredSize(new Dimension(700, 250));
        messageBox.setFont(inputFont);
        postPanel.add(send);
        postPanel.add(inputPanel);
        postPanel.add(messagesPanel);

        messages = new JTextArea[MAX_MESSAGES];
        users = new JButton[MAX_MESSAGES];

        send.addActionListener(twooter);
        submit.addActionListener(twooter);

        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setupLoginWindow();

        for (int i = 0; i < MAX_MESSAGES; i++)
        {
            messages[i] = new JTextArea();
            users[i] = new JButton("empty");
            users[i].addActionListener(twooter);
        }
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
        String output = messageBox.getText();
        messageBox.setText(null);
        return output;
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
        JLabel title = new JLabel("twooter");
        JLabel subtitle = new JLabel("profeshanul soshul meedeeuh for scc.110");
        Font titleFont = new Font("Comic Sans", Font.PLAIN, 150);
        Font subtitleFont = new Font("Comic Sans", Font.PLAIN, 50);

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

    public void setupMainWindow()
    {
        window.add(main);
        messageList.getVerticalScrollBar().setUnitIncrement(20);
        main.setResizeWeight(0.1);
        loginPanel.setVisible(false);
        //getUsers();
    }

    public void outputMessageStream(int i, String message, String username)
    {
        if (i == 0)
        {
            messagesPanel.setVisible(false);
        }

        if (message.length() > 500)
        {
            message = "Normally I don't condone censorship, but that wall of text was probably an image, and I'm not competent enough to know how to show you images. But if it makes you feel any better, it was probably very pretty or funny or cool or something.";
        }

        String newText = username + ": " + message;
        messages[i].setText(newText);
        messages[i].setLineWrap(true);
        messages[i].setBorder(BorderFactory.createLineBorder(Color.BLACK));
        messages[i].setFont(messageFont);
        messagesPanel.add(messages[i]);

        if (i == MAX_MESSAGES - 1)
        {
            messagesPanel.setVisible(true);
        }
    }

    public boolean isNewMessage(String inputMessage, String inputUsername)
    {
        String latestMessage = inputUsername + ": " + inputMessage;
        if (latestMessage.equals(messages[0].getText()))
        {
            return false;
        }
        else
        {
            //System.out.println(latestMessage + " != " + messages[0].getText());
            return true;
        }
    }

    public JPanel getMessagesPanel()
    {
        return messagesPanel;
    }

    // public JButton getUser(int index)
    // {
    //     return users[index];
    // }

    public void alert(String error, String title)
    {
        JOptionPane.showMessageDialog(null, error, title, JOptionPane.INFORMATION_MESSAGE);
    }

    public void listUsers(int index, String username)
    {
        if (index == 0)
        {
            usersPanel.setVisible(false);
        }

        usersPanel.remove(users[index]);
        //users[index].setText(username);
        users[index].setBackground(Color.WHITE);
        //users[i].setVisible(false);
        int i = 0;
        boolean isValid = true;

        while (!users[i].getText().equals("empty") && isValid)
        {
            System.out.println(username + ": " + i);
            if(username.equals(users[i].getText()) || i >= MAX_MESSAGES)
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
            System.out.println(username + ": " + i);
            users[i].setText(username);
        }

        usersPanel.add(users[index]);

        if (index == MAX_MESSAGES - 1)
        {
            usersPanel.setVisible(true);
        }
    }
}