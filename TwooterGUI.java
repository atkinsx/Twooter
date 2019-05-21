//import twooter.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class TwooterGUI
{
    private static final int MAX_MESSAGES = 30;

    private Font inputFont = new Font("Arial", Font.PLAIN, 40);
    private Font messageFont = new Font("Arial", Font.PLAIN, 18);

    private BoxLayout layoutUP;
    private BoxLayout layoutMP;
    private BoxLayout layoutIP;
    private BoxLayout layoutPP;

    private GridLayout grid;
    private int xSize = 8;
    private int ySize = 1;

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

    private JTextArea[] messages = new JTextArea[MAX_MESSAGES];
    private JTextField messageBox;
    private JTextField usernameBox;

    private JButton buttons[] = new JButton[50];

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

        grid = new GridLayout(xSize, ySize);
        messageBox = new JTextField();
        usernameBox = new JTextField();
        send = new JButton("Send");
        submit = new JButton("Submit");

        layoutUP = new BoxLayout(usersPanel, BoxLayout.Y_AXIS);
        layoutMP = new BoxLayout(messagesPanel, BoxLayout.Y_AXIS);
        layoutIP = new BoxLayout(inputPanel, BoxLayout.Y_AXIS);
        layoutPP = new BoxLayout(postPanel, BoxLayout.Y_AXIS);

        usersPanel.setLayout(layoutUP);
        messagesPanel.setLayout(layoutMP);
        inputPanel.setLayout(layoutIP);
        postPanel.setLayout(layoutPP);

        postPanel.add(messageBox);
        messageBox.setPreferredSize(new Dimension(500, 250));
        messageBox.setFont(inputFont);
        postPanel.add(send);
        postPanel.add(inputPanel);
        postPanel.add(messagesPanel);

        send.addActionListener(twooter);
        submit.addActionListener(twooter);

        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setupLoginWindow();

        for (int i = 0; i < MAX_MESSAGES; i++)
        {
            messages[i] = new JTextArea();
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
        window.add(loginPanel);
        loginPanel.setLayout(grid);
        loginPanel.add(usernameBox);
        loginPanel.add(submit);
        usernameBox.setFont(inputFont);
        window.setSize(800,800);
        window.setVisible(true);
    }

    public void setupMainWindow()
    {
        window.add(main);
        main.setResizeWeight(0.1);
        loginPanel.setVisible(false);
        madness();
    }

    public void outputMessageStream(int i, String message, String username)
    {
        String newText = username + ": " + message;
        //String newText = i + ") " + username + ": " + message;
        //if (!newText.equals(messages[i].getText()))
        //{
            // if (i == 27)
            // {
            //     System.out.println("NEW MESSAGES: " + newText + " != " + messages[i].getText());
            // }
            //messagesPanel.remove(messages[i]);
            messages[i].setText(newText);
            messages[i].setLineWrap(true);
            messages[i].setBorder(BorderFactory.createLineBorder(Color.BLACK));
            messages[i].setFont(messageFont);
            messagesPanel.add(messages[i]);
        //}
        // else
        // {
        //     System.out.println("OLD MESSAGES: " + newText + " == " + messages[i].getText());
        // }
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
            System.out.println(latestMessage + " != " + messages[0].getText());
            return true;
        }
    }

    public JPanel getMessagesPanel()
    {
        return messagesPanel;
    }

    public JButton getButton(int index)
    {
        return buttons[index];
    }

    public void alert(String error, String title)
    {
        JOptionPane.showMessageDialog(null, error, title, JOptionPane.INFORMATION_MESSAGE);
    }

    public void madness()
    {
        for (int i = 0; i < 50; i++)
        {
            buttons[i] = new JButton("text" + i);
            usersPanel.add(buttons[i]);
        }
    }
}