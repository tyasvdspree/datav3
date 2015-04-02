/**
 * Created by Arjen on 24-3-2015.
 */

import twitter4j.*;
import java.io.IOException;
import java.lang.reflect.Array;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TwitterReader {
private String[] attitude = {":)", ":("};
private String[] searchString = {"RdamCentraal", "RCentraal", "RotterdamCentraal", "Rotterdam+Centraal"};
private List<String> queryAttitude = Arrays.asList(attitude);
private List<String> searchQuery = Arrays.asList(searchString);

public void twitterSearch() throws TwitterException, IOException, InterruptedException, ClassNotFoundException, SQLException
    {
    TwitterDB twitterDB = new TwitterDB("test");
    for (int i = 0; i < queryAttitude.size(); i++)
        {
        for (int j = 0; j < searchQuery.size(); j++)
            {
            String queryString = searchQuery.get(j);// + "+" + queryAttitude.get(i);
            Twitter twitter = new TwitterFactory().getInstance();
            {
            try
                {
                Query query = new Query(queryString);
                QueryResult result;
                do
                    {
                    result = twitter.search(query);
                    List<Status> tweets = result.getTweets();
                    for (Status tweet : tweets)
                        {
                        System.out.print(tweet.getText());
                        twitterDB.updatedb(tweet.getId(), tweet.getUser().getScreenName(), tweet.getText(), tweet.getRetweetCount(), tweet.getFavoriteCount(), tweet.getCreatedAt(), queryAttitude.get(i));
                        }
                    }
                while ((query = result.nextQuery()) != null);
                System.exit(0);
                } catch (TwitterException te)
                {
                te.printStackTrace();
                System.out.print("failed to search tweets: " + te.getMessage());
                System.exit(-1);
                }
            }
            }
        }
    }
}
