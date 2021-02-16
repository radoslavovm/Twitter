import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;



public class csv_generate {

    //Delimiter used in CSV file
    public static String COMMA_DELIMITER = ",";
    public static String NEW_LINE_SEPARATOR = "\n";
    public static FileWriter fw;
    //Random num generator
    public static Random rand = new Random();

    //Generate user csv
    static void user_generate() throws IOException {
        // csv file header
        String FILE_HEADER = "user_id, follows_id";
        String filename = "users.csv";
        fw = new FileWriter(filename);
        // fields & variables
        int num_followers;
        int user;
        int follower;

        try {
            //Write the CSV file header
            fw.append(FILE_HEADER);
            //Add a new line separator after the header
            fw.append(NEW_LINE_SEPARATOR);

            //Write a new user to the CSV file
            for (user = 1; user < 100001; user++) {
                //Track the followers of each user to avoid duplicates
                ArrayList<Integer> followers = new ArrayList<>();
                //Assigns each user a number of followers
                if (user > 90000) {
                    num_followers = (rand.nextInt(15) + 1);
                } else if (user < 80000) {
                    num_followers = (rand.nextInt(5) + 1);
                } else {
                    num_followers = (rand.nextInt(8) + 1);
                }
                //Random id as the follower
                for (int i = 0; i < num_followers; i++) {
                    follower = (rand.nextInt(99999) + 1);
                    //Avoid duplicates
                    if (followers.contains(follower)) {

                    } else {
                        //Add row to csv
                        followers.add(follower);
                        fw.append(String.valueOf(user));
                        fw.append(COMMA_DELIMITER);
                        fw.append(String.valueOf(follower));
                        fw.append(NEW_LINE_SEPARATOR);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error in CsvFileWriter !!!");
            e.printStackTrace();
        } finally {
            try {
                fw.flush();
                fw.close();
            } catch (Exception e) {
                System.out.println("Error while flushing/closing fileWriter !!!");
                e.printStackTrace();
            }
        }
    }

    //Create random tweets
    private static String tweet_text() {
        // create a string of uppercase and lowercase characters and numbers
        String upperAlphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ ";
        String lowerAlphabet = "abcdefghijklmnopqrstuvwxyz";
        String numbers = "0123456789";
        // combine all strings
        String alphaNumeric = upperAlphabet + lowerAlphabet + numbers;
        // create random string builder
        StringBuilder sb = new StringBuilder();
                // specify length of random string
                int length = (rand.nextInt(140) + 1);

                for(int i = 0; i < length; i++) {
                    // generate random index number
                    int index = rand.nextInt(alphaNumeric.length());
                    // get character specified by index
                    // from the string
                    char randomChar = alphaNumeric.charAt(index);
                    // append the character to string builder
                    sb.append(randomChar);
                }
        return sb.toString();
    }

    //Generate csv of tweets
    static void tweet_generate() throws IOException {
        // csv file header
        String FILE_HEADER = "tweet_text, tweet_user";
        String filename = "tweets.csv";
        fw = new FileWriter(filename);
        // fields & variables
        String tweet_text;
        int tweet_user;

        try {
            //Write the CSV file header
            fw.append(FILE_HEADER);
            //Add a new line separator after the header
            fw.append(NEW_LINE_SEPARATOR);

            for (tweet_user = 1; tweet_user < 100001; tweet_user++) {
                for (int i = 1; i < 11; i++) {
                    tweet_text = tweet_text();

                    fw.append(tweet_text);
                    fw.append(COMMA_DELIMITER);
                    fw.append(String.valueOf(tweet_user));
                    fw.append(NEW_LINE_SEPARATOR);
                }
            }
        } catch (Exception e) {
            System.out.println("Error while flushing/closing fileWriter !!!");
            e.printStackTrace();
        } finally {
            try {
                fw.flush();
                fw.close();
            } catch (Exception e) {
                System.out.println("Error while flushing/closing fileWriter !!!");
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        user_generate();
        tweet_generate();
    }
}