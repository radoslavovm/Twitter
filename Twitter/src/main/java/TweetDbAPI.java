
public interface TweetDbAPI {
//    /**
//     * Set connection settings
//     * @param url
//     * @param user
//     * @param password
//     */
    // void authenticate(String url, String user, String password);

    void flush();

    /**
     * Insert a user
     * @param user_id
     * @param follower_id
     */
    void insert_user(int user_id, int follower_id);

    /**
     * Insert a tweet
     * @param tweet_text
     * @param user_id
     */
    void insert_tweet(String tweet_text, int user_id);

    /**
     * make a homescreen
     * @param user_id
     */
    void home_screen(int user_id);

//    /**
//     * Close the connection when application finishes
//     */
//    void closeConnection();
}
