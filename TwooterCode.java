import twooter.*;

public class TwooterCode
{
    public TwooterCode(TwooterClient client)
    {
        String idk;
        System.out.println("Hello world");

        client.enableLiveFeed();

        double rand = Math.random() * 100000;
        int no = (int) rand;
        String name = "UserNo" + no;
        
        
        try
        {
            idk = client.registerName(name);
            System.out.println(idk + ": " + name);
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
        
        
        client.disableLiveFeed();

        System.out.println("We get this far");
    }
}