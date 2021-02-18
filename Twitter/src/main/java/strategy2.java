import redis.clients.jedis.Jedis;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;
import java.util.List;
/*
    This is the Broadcasting Strategy. As tweets are inserted, the timelines of users are populated with tweet ids
    The users and their tweet timelines are then printed.
    The user.csv is user id & id of person that follows said user
 */
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
        jedis.set("Tweet_id/User:"+ jedis.get("next_tweet_id"), String.valueOf(user_id));
        // key tweet:id val tweet:text
        jedis.set("Tweet_id/Text:"+ jedis.get("next_tweet_id"), tweet_text);
        // key user:id val list of tweet:id the user is author of
        jedis.lpush("User_id:"+user_id, jedis.get("next_tweet_id"));

        String cur = ScanParams.SCAN_POINTER_START;
        List<String> followers = null;
        //all the followers that need to have timelines populated
        ScanResult<String> scanFollowers = jedis.sscan("User:"+ user_id, cur);
        followers = scanFollowers.getResult();

        for (int u=0; u < followers.size(); u++) {
            jedis.set(followers.get(u), String.valueOf(user_id));
            home_screen(Integer.parseInt(followers.get(u)));
        }
        jedis.incrBy("next_tweet_id", 1);
    }

    @Override
    public void home_screen(int user_id) {
        String author = jedis.get(String.valueOf(user_id));
        List<String> tweets = jedis.lrange("User_id:"+author, 0, -1);

        for (int t=0; t < tweets.size(); t++) {
            jedis.lpush("TimeLine:"+user_id , tweets.get(t));
        }

        // system out user author, tweet
    }
}