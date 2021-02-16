
import redis.clients.jedis.Jedis;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;
import java.util.List;

public class strategy1 implements TweetDbAPI {
    public Jedis jedis = new Jedis("localhost");

    public void flush() {
        jedis.flushAll();
    }

    public void insert_user(int user_id, int follower_id) {
        //System.out.println("Connection to server sucessfully");
        //check whether server is running or not
        //System.out.println("Server is running: " + jedis.ping());

        jedis.sadd("User:" + user_id, String.valueOf(follower_id));
    }
    public void insert_tweet(int tweet_id, String tweet_text, int user_id) {
        Jedis jedis = new Jedis("localhost");
        //System.out.println("Connection to server sucessfully");
        //check whether server is running or not
        //System.out.println("Server is running: " + jedis.ping());

        jedis.append("Tweet_id:" + tweet_id, "Tweet_text:" + tweet_text);
        jedis.lpush("User/Tweet:" + user_id, "Tweet_id:" + tweet_id);
    }

    public void home_screen(int user_id) {
        Jedis jedis = new Jedis("localhost");
        //System.out.println("Connection to server sucessfully");
        //check whether server is running or not
        //System.out.println("Server is running: " + jedis.ping());

        String cur = ScanParams.SCAN_POINTER_START;
        boolean cycleIsFinished = false;
        List<String> result = null;
        while (!cycleIsFinished) {
            ScanResult<String> scanResult =
                    jedis.sscan("User:" + user_id, cur);
            // a list of the people the given user follows
            result = scanResult.getResult();
            System.out.println(result);

            cur = scanResult.getCursor();
            if (cur.equals("0")) {
                cycleIsFinished = true;
            }
        }
        for (int i=0; i < result.size(); i++) {
            List<String> t_id = jedis.lrange("User/Tweet:" + result.get(i), 0, 11);
            // System.out.println(t_id);

            for (int t=0;  t < t_id.size(); t++) {
                String text = jedis.get(t_id.get(t));
                System.out.println(text);
            }
        }
    }
}
