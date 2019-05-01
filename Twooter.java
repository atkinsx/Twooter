import twooter.*;
import javax.swing.*;

public class Twooter
{
    public static void main(String[] args)
    {
        TwooterClient test = new TwooterClient();
        String idk;
        System.out.println("Hello world");

        test.enableLiveFeed();
        

        try
        {
            idk = test.registerName("helpmewhatisgoingon");
            System.out.println(idk);
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }
}