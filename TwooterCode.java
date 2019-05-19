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
            System.out.println(token + ": " + username);

            //client.postMessage(token, username, "???");
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
        //PrintWriter printFile = new PrintWriter(newFile);
        printFile.write(tokenString);

        printFile.close();
    }


    public void readMessages(TwooterClient client)
    {
        //https://apod.nasa.gov/apod/archivepix.html
        //https://apod.nasa.gov/apod/ap190519.html
        //https://apod.nasa.gov/apod/ap190512.html

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