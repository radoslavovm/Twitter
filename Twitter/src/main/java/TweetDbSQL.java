import database.dbUtils;
import java.sql.*;
import java.sql.Timestamp;
import java.util.Date;

public class TweetDbSQL implements TweetDbAPI {
    // For demonstration purposes. Better would be a constructor that takes a file path
    // and loads parameters dynamically.
    dbUtils dbu;

    public void authenticate(String url, String user, String password) {
        dbu = new dbUtils(url, user, password);
    }
    /**
     * insert a tweet into db
     */
    public void insert_tweet(int tweet_id, String tweet_text, int user_id) {
        Date date= new Date();
        long time = date.getTime();
        Timestamp ts = new Timestamp(time);
        String sql;

        try {
            // get connection and initialize statement
            Connection con = dbu.getConnection(); // get the active connection
            Statement stmt = con.createStatement();

                sql = "INSERT INTO Twitter.tweet (tweet_text,user_id,tweet_ts) VALUES" +
                        "('"+tweet_text+"','"+user_id+"','"+ts+"')";

                stmt.executeUpdate(sql);
            // Cleanup
            stmt.close();

        } catch (SQLException e) {
            System.err.println("ERROR: Could not insert record: ");
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }
    /**
     * insert a user_id and follows_id into db
     */
    public void insert_user(int user_id, int follower_id) {
        try {
            // get connection and initialize statement
            Connection con = dbu.getConnection(); // get the active connection
            Statement stmt = con.createStatement();
            String sql;

                sql = "INSERT INTO Twitter.followers (user_id,follows_id) VALUES" +
                        "('"+user_id+"','"+follower_id+"')";
                stmt.executeUpdate(sql);
            // Cleanup
            stmt.close();

        } catch (SQLException e) {
            System.err.println("ERROR: Could not insert record: ");
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }
    /**
     * create a homescreen for a user with user_id
     */
    public void home_screen(int user_id) {
        try {
            // get connection and initialize statement
            Connection con = dbu.getConnection(); // get the active connection
            Statement stmt = con.createStatement();
            String sql;

            sql = "WITH tab(follows_id) AS " +
                    "(SELECT follows_id FROM Twitter.followers where user_id = " + "('"+user_id+"')" +")" +
                    "SELECT tweet_text, user_id " +
                    "FROM tab " +
                    "JOIN Twitter.tweet " +
                    "ON tweet.user_id = tab.follows_id ORDER BY tweet_ts LIMIT 10";

            ResultSet rs = stmt.executeQuery(sql);

            System.out.println("tweet_text" + ", " + "user_author");
            while (rs.next()) {
                String tweet = rs.getString("tweet_text");
                int user = rs.getInt("user_id");
                System.out.println(tweet + ", " + user);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public void flush() {

    }

    /**
     * Close the connection when application finishes
     */
    public void closeConnection() {
        dbu.closeConnection();
    }
}