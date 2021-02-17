import redis.clients.jedis.Jedis;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;
import java.util.List;

public class strategy1 implements TweetDbAPI {
    public Jedis jedis = new Jedis("localhost");
    public void flush() {
        jedis.flushAll();
        System.out.println("Connection to server sucessfully");
        //check whether server is running or not
        System.out.println("Server is running: " + jedis.ping());
        // an incrementer to give every tweet_text a tweet_id
        jedis.set("next_tweet_id", String.valueOf(1));
    }

    public void insert_user(int user_id, int follower_id) {
        //A SET : key is user:id values are id (id of users that user:id follows)
        jedis.sadd("User:" + user_id, String.valueOf(follower_id));
    }
    public void insert_tweet(String tweet_text, int user_id) {
         //A STRING : key is tweet:id value is a tweet text string
        jedis.set("Tweet_id:" + jedis.get("next_tweet_id"), "Tweet_text:" + tweet_text);
        //A LIST : key is user/tweet:id (just the user id) values are tweet_id (id of tweets that user/tweet:id created)
        jedis.lpush("User/Tweet:" + user_id, "Tweet_id:" + jedis.get("next_tweet_id"));
        //incr the tweet id generator
        jedis.incrBy("next_tweet_id", 1);
    }

    public void home_screen(int user_id) {
        String cur = ScanParams.SCAN_POINTER_START;
        List<String> result = null;
        ScanResult<String> scanResult = jedis.sscan("User:" + user_id, cur);
        // a list of the people the given user follows
        result = scanResult.getResult();
        System.out.println(result);

        for (int i=0; i < result.size(); i++) {
            // Create a list of the tweet_id that the user has created.
            // (this user is a person that the parameter user follows)
            List<String> t_id = jedis.lrange("User/Tweet:" + result.get(i), 0, 11);
            System.out.println("Author: "+ result.get(i));

            for (int t=0;  t < t_id.size(); t++) {
                String text = jedis.get(t_id.get(t));
                System.out.println(text);
            }
        }
    }
}
