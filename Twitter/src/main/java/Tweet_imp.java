import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Random;
/**
 * This program exercises the API (MySQL implementation).
 * Notice that nothing other than the instantiation of the API shows us that
 * the underlying database is Relational, or MySQL.
 */
public class Tweet_imp {

    //private static TweetDbAPI api = new TweetDbSQL();
    //private static TweetDbAPI api = new strategy1();
    private static TweetDbAPI api = new strategy2();

    public static void main(String[] args) {
//        //Authenticate your access to the server.
//        String url =  "jdbc:mysql://localhost:3306/Twitter?serverTimezone=EST5EDT";
//        String user = "root";
//        String password = "Mir82605131!";
//        api.authenticate(url, user, password); // DON'T HARDCODE PASSWORDS!

        api.flush();

        //insert users
        try {
            //Time the execution
            long startTime = System.nanoTime();

            String line = "";
            String splitBy = ",";
            String[] users;

            //Parsing a CSV file into BufferedReader class constructor
            BufferedReader br = new BufferedReader(new FileReader("../../users.csv"));
            //BufferedReader br = new BufferedReader(new FileReader("users_test.csv"));
            br.readLine(); // this will read the first line

            while ((line = br.readLine()) != null)
            {
                users = line.split(splitBy);
                int u = Integer.parseInt(users[0]);
                int f = Integer.parseInt(users[1]);
                api.insert_user(u, f);
            }
            long endTime = System.nanoTime();
            // get difference of two nanoTime values
            long timeElapsed = endTime - startTime;

            System.out.println("Execution time of Insert User in milliseconds : " +
                    timeElapsed / 1000000);
        }  catch (Exception e) {
            e.printStackTrace();
        }
        //insert tweets
        try {
            long startTime = System.nanoTime();
            //read csv
            String line = "";
            String splitBy = ",";
            String[] tweet;

            //parsing a CSV file into BufferedReader class constructor
            BufferedReader br = new BufferedReader(new FileReader("../../tweets.csv"));
            //BufferedReader br = new BufferedReader(new FileReader("tweets_test.csv"));
            br.readLine(); // this will read the first line

            while ((line = br.readLine()) != null) //returns a Boolean value
            {
                tweet = line.split(splitBy); // use comma as separator
                String tweet_text = tweet[0];
                int user_id = Integer.parseInt(tweet[1]);
                api.insert_tweet(tweet_text, user_id);
            }
            long endTime = System.nanoTime();
            // get difference of two nanoTime values
            long timeElapsed = endTime - startTime;

            System.out.println("Execution time of Insert Tweet in milliseconds : " +
                    timeElapsed / 1000000);
        }  catch (Exception e) {
            e.printStackTrace();
        }
        //create home screens
        try{
            long startTime = System.nanoTime();
            Random r = new Random();
            int user_id;
////////////////////////////////////////////////////////////////////////CHANGE BACK TO 100///
////////////////////////////////////////////////////////////////////////CHANGE user_id///
            for (int i=0; i < 100; i++){
                //user_id = 35196;
                user_id =  (r.nextInt(99999) + 1);
                api.home_screen(user_id);
            }
            long endTime = System.nanoTime();
            // get difference of two nanoTime values
            long timeElapsed = endTime - startTime;

            System.out.println("Retrieval Execution time in milliseconds : " +
                    timeElapsed / 1000000);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        api.closeConnection();
    }
}
