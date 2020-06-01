package gg.scenarios.terra.managers.twitter;
import gg.scenarios.terra.Terra;
import gg.scenarios.terra.managers.TeamState;
import gg.scenarios.terra.scenarios.Scenario;
import lombok.Getter;
import lombok.Setter;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

@Getter
public class PostTweet {

    private Terra uhc = Terra.getInstance();

    private int mins;
    private int slots;
    private String teamSize;
    private  String host;
    private String description;
    private String linkUrl;
    private Status status;

    public PostTweet(int mins) throws TwitterException {
        this.mins = mins;
        slots = 100;
        this.teamSize = teamSizeToString();
        this.host = uhc.getGameManager().getHost().getPlayer().getName();
        this.description= " ";
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey("MbQzRmEe8FBmHrX3cKOn1BXRb")
                .setOAuthConsumerSecret("RpawT97PXdxucf5hz2udtdn4aSEwo6zX3ZGIItuTVnViU6jdkx")
                .setOAuthAccessToken("1224195613811601408-BarvuEcIX6DZL8uMKF1CZSVJEq8izA")
                .setOAuthAccessTokenSecret("OFbMOSsrbQ4Lkj4yyxSoGinUzPM8OilrNnEv65NZ2Rnqo");
        TwitterFactory tf = new TwitterFactory(cb.build());
        Twitter twitter = tf.getInstance();
        status = twitter.updateStatus(createTweet());
        linkUrl= "https://twitter.com/" + status.getUser().getScreenName()
                + "/status/" + status.getId();
    }

    private String teamSizeToString() {
        if (uhc.getGameManager().getTeamState() == TeamState.SOLO) {
            return "FFA";
        }else if (uhc.getGameManager().getTeamState() == TeamState.SLAVEMARKET){
            return "Slave Market";
        }else {
            return "To" + uhc.getGameManager().getTeamSize();
        }
    }

    private String createScenarios(){
        StringBuilder stringBuilder = new StringBuilder();
        for(Scenario scenario : uhc.getScenarioManager().getScenarios()){
            if (scenario.isEnabled()) {
                stringBuilder.append(scenario.getName());
                stringBuilder.append(", ");
            }
        }
        stringBuilder.deleteCharAt(stringBuilder.length() - 2);
        return stringBuilder.toString();
    }

    private String createTweet() {
        return "Upcoming Scenarios Match\n" +
                "\n" +
                "- " + teamSize + " " +createScenarios() + "\n" +
                "- Opens at: " + uhc.getGameManager().getMatch().getMatchTime().getHour() + ":" + uhc.getGameManager().getMatch().getMatchTime().getMinute() + " ("+ mins+"m)\n" +
                "\n" +
                "- Matchpost: " + uhc.getGameManager().getMatchPost() + "\n" +
                "\n" +
                "- IP: na.scenarios.gg\n" +
                "- Region: NA\n" +
                "\n" +
                "- Host: " + host +"\n";
    }
}