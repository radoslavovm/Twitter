
public class Tweet {

    private String tweet_text;
    private int user_id;
    private String tweet_ts;


    public Tweet(String tweet_text, int user_id, String tweet_ts) {

        this.tweet_text = tweet_text;
        this.user_id = user_id;
        this.tweet_ts = tweet_ts;
    }
}