import twooter.*;

public class TwooterCode
{
    public TwooterCode(TwooterClient client)
    {
        String token;
        String name;
        System.out.println("Hello world");

        client.enableLiveFeed();

        double rand = Math.random() * 100000;
        int no = (int) rand;
        name = "UserNo" + no;
        
        
        try
        {
            token = client.registerName(name);
            System.out.println(token + ": " + name);

            client.postMessage(token, name, "???");
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
        
        
        client.disableLiveFeed();

        System.out.println("We get this far");
    }
}