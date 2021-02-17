import redis.clients.jedis.Jedis;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;
import java.util.List;

public class strategy2 implements TweetDbAPI {
    public Jedis jedis = new Jedis("localhost");

    @Override
    public void flush() {
        jedis.flushAll();
        System.out.println("Connection to server sucessfully");
        //check whether server is running or not
        System.out.println("Server is running: " + jedis.ping());
        // an incrementer to give every tweet_text a tweet_id
        jedis.set("next_tweet_id", String.valueOf(1));
    }

    @Override
    public void insert_user(int user_id, int follower_id) {
        //A SET : key is user:id values are id (id of users that follow user:id)
        jedis.sadd("User:" + user_id, String.valueOf(follower_id));
    }

    @Override
    public void insert_tweet(String tweet_text, int user_id) {
        // key tweet:id val user:id
        // for this tweet , loop through list of people that follow user , call home_screen to add tweet to follower
        // key tweet:id val tweet:text
    }

    @Override
    public void home_screen(int user_id) {

        // key user , list tweet ids
        // system out user
        //system out tweets
    }
}