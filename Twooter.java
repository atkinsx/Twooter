import twooter.*;

public class Twooter
{
    public static void main(String[] args)
    {
        TwooterClient test = new TwooterClient();
        TwooterGUI gui = new TwooterGUI();
        TwooterCode code = new TwooterCode(test);
    }
}