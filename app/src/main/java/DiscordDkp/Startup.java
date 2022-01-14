package DiscordDkp;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Portalz
 */
public class Startup {

    public HashMap<String, User> discordUsers = new HashMap();
    public HashMap<String, User> users = new HashMap();


    public Startup(JDA jda) {
        importJSON();
        TextChannel chan = null;
        String test = "";
        String entryId = "";

        ///find dkp channel
        for (TextChannel x : jda.getTextChannelsByName("dkp", true)) {
            test = x.getName() + "\n";
            if (x.getGuild().getName().contains("Wheechair")) {
                chan = x;
            }
        }

        //get table message for editingi
        for (Message m : chan.getHistory().retrievePast(5).complete()) {
            System.out.println(jda.getUserByTag("Portbot#8824").getAsTag() + " vs " + m.getAuthor().getAsTag());
            if (m.getAuthor().getAsTag().equals("Portbot#8824")) {
                if (m.getContentDisplay().startsWith("Dkp List")) {
                    entryId = m.getId();
                }
            }
        }
        if (entryId.equals("")) {
            chan.sendMessage(getTable()).queue();
        } else {
            chan.getHistory().getMessageById(entryId).editMessage(getTable());
        }
    }

    public String getTable() {
        String table = "Dkp List\n";
        for (User u : users.values()) {
            String dkp = u.dkp + "";
            for (int i = 0; i < 3 - dkp.length(); i++) {
                dkp = dkp + " ";
            }
            table = table + dkp + " " + u.Username + "\n";
        }
        return table;
    }

    public void importJSON() {
        try {
            HashMap<String, User> clonedMap = FileStuff.JSONtoData();
            Gson gson = new Gson();
            System.out.println(gson.toJson(clonedMap));
            for (User r : clonedMap.values()) {

                if (r.Discord != null && !r.Discord.equals("")) {
                    discordUsers.put(r.Discord, r);
                }
                users.put(r.Username, r);
            }

        } catch (IOException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
