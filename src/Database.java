/**
 * Created by Arjen on 24-3-2015.
 */

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import java.sql.*;
import java.util.*;
import java.util.Date;

import static java.sql.DriverManager.*;

public class Database
{
    private String userName = "root";
    private String password = "";
    private String driver = "com.mysql.jdbc.Driver";
    Connection conn = null;
    Statement statement = null;

    public Database(String dbName)
    {
        try
        {
            String dbConnectionString = "jdbc:mysql://localhost:3306/"+dbName;
            Class.forName(driver).newInstance();
            conn = DriverManager.getConnection(dbConnectionString, userName, password);
            statement = conn.createStatement();
        }
        catch (Exception e)
        {
            System.out.print(e);
        }
    }



    public void updatedb(Long tweetId, String tweetUser, String tweetText, int tweetRetweetCount, int tweetFavoriteCount, Date tweetCreatedAt, String tweetAttitude) throws ClassNotFoundException, SQLException
    {
        try
        {

            ResultSet resultSet = statement.executeQuery("SELECT tweetId FROM Tweet");
            while (resultSet.next())
            {
                if (tweetId != resultSet.getLong(0))
                {
                    PreparedStatement psInsert = conn.prepareStatement("INSERT INTO tweet(tweetId, tweetUser, tweetText, tweetRetweetCount, tweetFavoriteCount, tweetCreatedAt, tweetAttitude) " +
                            "VALUE (tweetId, tweetUser, tweetText, tweetRetweetCount, tweetFavoriteCount, tweetCreatedAt, tweetAttitude)");
                    psInsert.executeUpdate();
                }
            }
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

    public void closeConn()
    {
        try
        {
            statement.close();
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

    public void shutDownDB() throws ClassNotFoundException, SQLException
    {
        if (driver.equals("com.mysql.jdbc.Driver"))
        {
            boolean gotSQLExc = false;
            try
            {
                getConnection("jdbc:derby:;shutdown=true");
            }
            catch (SQLException se)
            {
                if ( se.getSQLState().equals("XJ015") )
                {
                    gotSQLExc = true;
                }
            }
            if (!gotSQLExc)
            {
                System.out.println("Database did not shut down normally");
            }
            else
            {
                System.out.println("Database shut down normally");
            }
        }
    }
}

