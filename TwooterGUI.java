//import twooter.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class TwooterGUI
{
    private static final int MAX_MESSAGES = 30;

    private BoxLayout layout;
    private BoxLayout layout2;

    private GridLayout grid;
    private int xSize = 8;
    private int ySize = 1;

    private JButton send;
    private JButton submit;

    private JFrame window;

    private JPanel loginPanel;
    private JPanel usersPanel;
    private JPanel messagesPanel;

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

        userList = new JScrollPane(usersPanel);
        messageList = new JScrollPane(messagesPanel);
        main = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, userList, messageList);

        grid = new GridLayout(xSize, ySize);
        messageBox = new JTextField();
        usernameBox = new JTextField();
        send = new JButton("Send");
        submit = new JButton("Submit");

        layout = new BoxLayout(usersPanel, BoxLayout.Y_AXIS);
        layout2 = new BoxLayout(messagesPanel, BoxLayout.Y_AXIS);

        usersPanel.setLayout(layout);
        messagesPanel.setLayout(layout2);

        messagesPanel.add(messageBox);
        messagesPanel.add(send);

        send.addActionListener(twooter);
        submit.addActionListener(twooter);
        buttons[0].addActionListener(twooter); //GARBO

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
        window.add(loginPanel);
        loginPanel.setLayout(grid);
        loginPanel.add(usernameBox);
        loginPanel.add(submit);
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
        messagesPanel.remove(messages[i]);
        messages[i].setText(i + ") " + username + ": " + message);
        messages[i].setLineWrap(true);
        //Border border = BorderFactory.createLineBorder(Color.BLUE, 5);
        messages[i].setBorder(BorderFactory.createLineBorder(Color.BLACK));
        messagesPanel.add(messages[i]);
    }

    public void getButton(int index)
    {
        return buttons(i);
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