import redis.clients.jedis.Jedis;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/*
    This is the Broadcasting Strategy. As tweets are inserted, the timelines of users are populated with tweet ids
    The users and their tweet timelines are then printed.
    The user.csv is user id & id of person that follows said user
 */
public class strategy2 implements TweetDbAPI {
    public Jedis jedis = new Jedis("localhost");

    //clear any data already in redis server
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
        //create uuid for tweets || generated data does not contain tweet_ids
        String tweet_id = UUID.randomUUID().toString();
        //create timestamp for tweet as it is inserted
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());

        // A STRING : key tweet:id & value tweet:text
        jedis.set("Tweet_id:" + tweet_id, tweet_text);
        // A LIST : key user:id & value list of tweet:id that the user is author of
        jedis.lpush("User_id:"+user_id, String.valueOf(tweet_id));

        // instantiate cursor
        String cur = ScanParams.SCAN_POINTER_START;
        List<String> followers = null;
        //all the followers that need to have timelines populated (all followers that follow user_id)
        ScanResult<String> scanFollowers = jedis.sscan("User:"+ user_id, cur);
        followers = scanFollowers.getResult();

        for (int u=0; u < followers.size(); u++) {
            //Create/populate Timeline for each follower
            jedis.lpush("Timeline:"+followers.get(u), tweet_id);
        }
    }

    @Override
    public void home_screen(int user_id) {
        // Create list of the first 10 tweet ids in the timeline
        List<String> timeline_id = jedis.lrange("Timeline:"+user_id, 0, 11);
        List<String> timeline_text = new ArrayList<>();
        for (int i=0; i < timeline_id.size(); i++) {
            // create list of the tweet text that will be outputted for timeline
            timeline_text.add(jedis.get("Tweet_id:"+timeline_id.get(i)));
        }
        System.out.println("Timeline for:"+user_id+":"+timeline_text);
    }
}