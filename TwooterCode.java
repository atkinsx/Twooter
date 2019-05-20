import twooter.*;
import java.io.*;

public class TwooterCode
{
    private String token;
    private String name;
    private String txtFile;

    public TwooterCode(TwooterClient client)
    {
        
        System.out.println("Hello world");

        client.enableLiveFeed();

        // double rand = Math.random() * 100000;
        // int no = (int) rand;
        // name = "UserNo" + no;
        
        readMessages(client);
    }

    public void setName(String input)
    {
        name = input;
    }

    public String createUser(TwooterClient client, String username)
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
                System.out.println("The username is already taken or is invalid.");
            }

            return token;
        }
        catch(Exception e)
        {
            System.out.println(e);
            return null;
        }
    }

    public String readTokenFromFile(String username) throws IOException
    {
        txtFile = username + ".txt";
        FileReader fileReader = new FileReader(txtFile);
        BufferedReader textReader = new BufferedReader(fileReader);
        String outputToken = textReader.readLine();

        textReader.close();
        return outputToken;
    }

    public void writeTokenToFile(String username, String tokenString) throws IOException
    {
        txtFile = username + ".txt";
        
        FileWriter newFile = new FileWriter(txtFile, false);
        BufferedWriter printFile = new BufferedWriter(newFile);
        printFile.write(tokenString);

        printFile.close();
    }

    public void postMessage(TwooterClient client)
    {
        //client.postMessage(token, name, "Please don't read this... :(");
    }

    public void readMessages(TwooterClient client)
    {
        try
        {
            Message[] test = client.getMessages();

            for (int i = 0; i < test.length; i++)
                System.out.println(test[i].message);
        }
        catch (IOException event)
        {
            System.out.println("Error: " + event.getMessage());
        }
    }

    public void close(TwooterClient client)
    {
        client.disableLiveFeed();
    }
}