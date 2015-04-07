/**
 * Created by Arjen on 24-3-2015.
 */

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import org.json.JSONObject;
import org.json.XML;
import java.io.*;
import java.sql.SQLException;

public class NsApiReader
    {
    String departTimes;
    String ritNumber;
    String departureTime;
    String delay;
    String delayText;
    String endDestination;
    String trainType;
    String routeText;
    String carrier;
    String trackChanced;
    String departureTrack;
    String travelTip;

    public void getNsApiData(Database database)
        {
        final String USERNAME = "christianlangejan@hotmail.com";
        final String PASSWORD = "APREZyc2aQ0I1viyFEMmhsD6-ciFxzNGXgA5NTLCkj2bq_aITYjxdQ";

        ClientConfig config = new DefaultClientConfig();
        Client client = Client.create(config);
        client.addFilter(new HTTPBasicAuthFilter(USERNAME, PASSWORD));

        // Get the protected web page and turn into string
        WebResource webResource = client.resource("http://webservices.ns.nl/ns-api-avt?station=RTD");
        //String receivedXML = webResource.accept(MediaType.APPLICATION_XML).get(String.class);

        departTimes = webResource.get(String.class);
        Stringrestucture();
        writeToDatabase(database);
        }

    private void Stringrestucture()
        {
        //remove irrelevant parts of string and reorganize to get consistent format(leaves only about 1/4 of the sting in the end)
        departTimes=departTimes.replaceAll("\t","");
        departTimes=departTimes.replaceAll("\n\n","\n");
        departTimes=departTimes.replaceAll("\n\n","\n");
        departTimes=departTimes.replaceAll("</VertrekTijd>\n<EindBestemming>","\n\n\n");
        departTimes=departTimes.replaceAll("</TreinSoort>\n<Vervoerder>","\n\n");
        departTimes=departTimes.replaceAll("<VertrekSpoor wijziging=\"false\">","false\n");
        departTimes=departTimes.replaceAll("<VertrekSpoor wijziging=\"true\">","true\n");
        departTimes=departTimes.replaceAll("</VertrekSpoor>\n</VertrekkendeTrein>","\n");
        departTimes=departTimes.replaceAll("</.*>","");
        departTimes=departTimes.replaceAll("<VertrekkendeTrein>\n",";");
        departTimes=departTimes.replaceAll("<.*>","");
        //System.out.println(departTimes);
        }

    private void writeToDatabase(Database database)
        {
        /*
        try {
            File file = new File("nsdb.txt");
            BufferedWriter output = new BufferedWriter(new FileWriter(file));
            output.write(departTimes);
            //ritNumber+","+departureTime+","+delay+","+delayText+","+endDestination+","+trainType+","+routeText+","+carrier+","+trackChanced+","+departureTrack+","+travelTip
            output.close();
        } catch ( IOException e ) {
            e.printStackTrace();
        }
        */
        int a=0;
        int part=1;
        while(true)
            {
            //find start of string (past s little bit of rubbish at the start)
            if(Character.toString(departTimes.charAt(a)).equals(";"))
                {
                break;
                }
            else
                {
                a++;
                }
            }
        for(int i=a;i<departTimes.length();i++)
            {
            //; should signal a new train with its information. clear all types
            if(Character.toString(departTimes.charAt(i)).equals(";"))
                {
                //remove if it works
                /*try
                {
                    NsDB nsdb = new NsDB("nsDB");
                }
                catch (ClassNotFoundException ce)
                {
                    System.out.println("ClassNotFoundException at NsApiReader");
                }
                catch (SQLException se)
                {
                    System.out.println("SQLException at NsApiReader");
                }*/

                //reset for next train
                part=1;
                ritNumber="";
                departureTime="";
                delay="";
                delayText="";
                endDestination="";
                trainType="";
                routeText="";
                carrier="";
                trackChanced="";
                departureTrack="";
                travelTip="";
                }
            // \n(newline) signals the next datathingy
            else if(Character.toString(departTimes.charAt(i)).equals("\n"))
                {
                part++;
                }
            else
                {
                switch (part)
                    {
                    case 1: ritNumber = ritNumber + Character.toString(departTimes.charAt(i)); break;
                    case 2: departureTime = departureTime + Character.toString(departTimes.charAt(i));break;
                    case 3: delay = delay + Character.toString(departTimes.charAt(i));break;
                    case 4: delayText = delayText + Character.toString(departTimes.charAt(i)); break;
                    case 5: endDestination = endDestination + Character.toString(departTimes.charAt(i));break;
                    case 6: trainType = trainType + Character.toString(departTimes.charAt(i));break;
                    case 7: routeText = routeText + Character.toString(departTimes.charAt(i));break;
                    case 8: carrier = carrier + Character.toString(departTimes.charAt(i));break;
                    case 9: trackChanced = trackChanced + Character.toString(departTimes.charAt(i));break;
                    case 10: departureTrack = departureTrack + Character.toString(departTimes.charAt(i));break;
                    case 11: travelTip = travelTip + Character.toString(departTimes.charAt(i));break;
                    }
                }
            }
        }

    }