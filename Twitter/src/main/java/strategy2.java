import redis.clients.jedis.Jedis;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
    }

    @Override
    public void insert_user(int user_id, int follower_id) {
        //A SET : key is user:id values are id (id of users that follow user:id)
        jedis.sadd("User:" + user_id, String.valueOf(follower_id));
    }

    @Override
    public void insert_tweet(String tweet_text, int user_id) {

        String tweet_id = UUID.randomUUID().toString();;

        // key tweet:id val tweet:text
        jedis.set("Tweet_id/Text:"+ tweet_id, tweet_text);
        // key user:id val list of tweet:id the user is author of
        jedis.lpush("User_id:"+user_id, String.valueOf(tweet_id));

        String cur = ScanParams.SCAN_POINTER_START;
        List<String> followers = null;
        //all the followers that need to have timelines populated
        ScanResult<String> scanFollowers = jedis.sscan("User:"+ user_id, cur);
        followers = scanFollowers.getResult();

        for (int u=0; u < followers.size(); u++) {
            //Create Timeline
            jedis.lpush("Timeline:"+followers.get(u), tweet_id);
        }
    }

    @Override
    public void home_screen(int user_id) {
        List<String> timeline_id = jedis.lrange("Timeline:"+user_id, 0, 11);
        List<String> timeline_text = new ArrayList<>();
        for (int i=0; i < timeline_id.size(); i++) {
            timeline_text.add(jedis.get("Tweet_id/Text:"+timeline_id.get(i)));
        }
        System.out.println("Timeline:"+user_id+":"+timeline_text);
    }
}