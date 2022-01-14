package DiscordDkp;
import java.util.HashMap;

public class User {

    public String Username;
    public String Main;
    public String Discord;
    public String key;
    public HashMap<String, Integer> dkps;
    public HashMap<String, String> characters;

    public int dkp;

    public User(String Username, int dkp) {
        this.dkp = dkp;
        this.Username = Username;
        this.key = "";
        characters = new HashMap();
        dkps = new HashMap();
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setDKP(int dkp) {
        this.dkp = dkp;
    }

    public void setDiscord(String Discord) {
        this.Discord = Discord;
    }

    public String getDiscord() {
        return Discord;
    }

    public String getUsername() {
        return Username;
    }

    public String getKey() {
        return key;
    }

    public int getDKP() {
        return dkp;
    }

}
