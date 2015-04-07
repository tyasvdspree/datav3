import java.lang.reflect.Executable;

/**
 * Created by Arjen on 24-3-2015.
 */

public class Main
{
    public static void main(String[] args)throws Exception
    {
        Database database = new Database("test");
        NsApiReader nsApiReader = new NsApiReader();
        TwitterReader twitterReader = new TwitterReader();

        nsApiReader.getNsApiData(database);
        twitterReader.twitterSearch(database);


    }
}
