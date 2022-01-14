package DiscordDkp;

import com.google.gson.Gson;

import org.checkerframework.checker.units.qual.s;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.security.auth.login.LoginException;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class App extends ListenerAdapter {

    long msgOutput = 0;
    long backOutput = 0;
    public HashMap<String, Raid> raids = new HashMap();
    public HashMap<String, User> users = new HashMap();
    ArrayList<User> notify = new ArrayList();
    public ArrayList<String> channels = new ArrayList();
    public ArrayList<String> admins = new ArrayList();
    String mainChan = Props.getProp("mainChannel", true);
    // public HashMap<String, User> attending = new HashMap(); //inRAID
    public HashMap<String, User> discordUsers = new HashMap();
    // public HashMap<User, Integer> bids = new HashMap(); //INRAID
    // public String bidItem = ""; //INRAID
    // public boolean bidding = false; //INDRAID
    // public boolean attend = false; //INRAID
    // public String attendKey = ""; //INRAID
    public static JDA jda;
    // public ArrayList<User> highBidders = new ArrayList();//INRAID
    public static boolean started = false;
    public static String[] classes = { "Warrior", "Priest", "Mage", "Warlock", "Rogue", "Druid", "Paladin", "Shaman",
            "Hunter" };
    // public HashMap<String, String> statuses = new HashMap(); //inraid
    // public String winnerz = ""; //inraid

    String helpMsg = "``HI WELCOME TO PORTALZ BOT \n"
            + "===Commands===\n\n"
            + "FORMAT - !command <required> [optional]\n"
            + "example: !register Portalz Mage myKey \n\n"
            + "!dkp\n"
            + "!bid <##> (Denominations of 5)\n"
            + "!notify (toggle bid notifications) ~~~NEW COMMAND~~~\n"
            + "!attend <key> (key announced in raid chat)\n"
            + "!check <Class> example: !check mage\n"
            + "!register <character> <class> <key>\n"
            + "!key [newKey]~~~NEW COMMAND~~~\n"
            + "!help \n\n"
            + "initial word LOWER CASE``";

    public static void main(String[] args) throws LoginException {
        try {
            Props.propLoading();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        jda = JDABuilder.createDefault(Props.getProp("JDA-Key", true)).build();
        jda.addEventListener(new App());

    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (!started) {
            System.out.println("starting");
            onStart();
            started = true;
            Gson gson = new Gson();
            System.out.println(gson.toJson(users));
        }
        // System.out.println(event.getMessage().getContentDisplay());
        if (event.getMessage().getContentDisplay().length() > 0
                && event.getMessage().getContentDisplay().charAt(0) == '!') {
            if (event.isFromType(ChannelType.TEXT)) {
                System.out.printf("[%s][%s] %#s: %s%n", event.getGuild().getName(), event.getChannel().getName(),
                        event.getAuthor(), event.getMessage().getContentDisplay());
                if (event.getAuthor().getAsTag().equals(Props.getProp("Owner", true))) {
                    if (event.getMessage().getContentDisplay().startsWith("!test")) {

                    }
                }
            } else if (event.isFromType(ChannelType.PRIVATE)) {
                if (admins.contains(event.getAuthor().getAsTag())) {
                    if (event.getMessage().getContentDisplay().startsWith("!importCSV")
                            || event.getMessage().getContentDisplay().startsWith("!ic")) {
                        importCSV(1);
                    }
                    if (event.getMessage().getContentDisplay().startsWith("!importdkp")
                            || event.getMessage().getContentDisplay().startsWith("!idkp")) {
                        importCSV(2);
                    } else if (event.getMessage().getContentDisplay().startsWith("!importCSVK")
                            || event.getMessage().getContentDisplay().startsWith("!ick")) {
                        importCSVKeys();
                    } else if (event.getMessage().getContentDisplay().startsWith("!exportAttend")
                            || event.getMessage().getContentDisplay().startsWith("!ea")) {
                        exportCSVAttend(event);
                    } else if (event.getMessage().getContentDisplay().startsWith("!importAttend")
                            || event.getMessage().getContentDisplay().startsWith("!ia")) {
                        readCSVAttend(event);
                    } else if (event.getMessage().getContentDisplay().startsWith("!giveDKP")
                            || event.getMessage().getContentDisplay().startsWith("!gdkp")) {
                        giveDKP(event);
                    } else if (event.getMessage().getContentDisplay().startsWith("!attendKey")
                            || event.getMessage().getContentDisplay().startsWith("!ak")) {
                        attendKey(event);
                    } else if (event.getMessage().getContentDisplay().startsWith("!attendClear")
                            || event.getMessage().getContentDisplay().startsWith("!ac")) {
                        attendClear(event);
                    } else if (event.getMessage().getContentDisplay().startsWith("!attendOff")
                            || event.getMessage().getContentDisplay().startsWith("!ao")) {
                        attendOff(event);
                    } else if (event.getMessage().getContentDisplay().startsWith("!attendManual")
                            || event.getMessage().getContentDisplay().startsWith("!am")) {
                        attendManual(event);
                    } else if (event.getMessage().getContentDisplay().startsWith("!endbid")
                            || event.getMessage().getContentDisplay().startsWith("!eb")) {
                        endBid(event);
                    } else if (event.getMessage().getContentDisplay().startsWith("!finalize")
                            || event.getMessage().getContentDisplay().startsWith("!fz")) {
                        finalizeBid(event);
                    } else if (event.getMessage().getContentDisplay().startsWith("!writecsv")
                            || event.getMessage().getContentDisplay().startsWith("!wc")) {
                        writeCSV();
                    } else if (event.getMessage().getContentDisplay().startsWith("!exportcsv")
                            || event.getMessage().getContentDisplay().startsWith("!ec")) {
                        exportCSV();
                    } else if (event.getMessage().getContentDisplay().startsWith("!exportjson")
                            || event.getMessage().getContentDisplay().startsWith("!ej")) {
                        exportJSON(event);
                    } else if (event.getMessage().getContentDisplay().startsWith("!msgJSON")
                            || event.getMessage().getContentDisplay().startsWith("!mj")) {
                        msgJSON(event);
                    } else if (event.getMessage().getContentDisplay().startsWith("!msgJSON")
                            || event.getMessage().getContentDisplay().startsWith("!bj")) {
                        backupJSON(event);
                    } else if (event.getMessage().getContentDisplay().startsWith("!importjson")
                            || event.getMessage().getContentDisplay().startsWith("!ij")) {
                        importJSON();
                    } else if (event.getMessage().getContentDisplay().startsWith("!keyreg")
                            || event.getMessage().getContentDisplay().startsWith("!kr")) {
                        keyReg(event);
                    } else if (event.getMessage().getContentDisplay().startsWith("!startBid")
                            || event.getMessage().getContentDisplay().startsWith("!sb")) {
                        startBid(event);
                    } else if (event.getMessage().getContentDisplay().startsWith("!manualReg")
                            || event.getMessage().getContentDisplay().startsWith("!mr")) {
                        manualReg(event);
                    } else if (event.getMessage().getContentDisplay().startsWith("!addPlayer")
                            || event.getMessage().getContentDisplay().startsWith("!ap")) {
                    } else if (event.getMessage().getContentDisplay().startsWith("!removePlayer")
                            || event.getMessage().getContentDisplay().startsWith("!rp")) {
                        removePlayer(event);
                    } else if (event.getMessage().getContentDisplay().startsWith("!normall")
                            || event.getMessage().getContentDisplay().startsWith("!na")) {
                        normAll();
                        fb(event, "All Usernames Normalized");
                        // } else if (event.getMessage().getContentDisplay().startsWith("!norm") ||
                        // event.getMessage().getContentDisplay().equals("!n")) {
                        // normReply(event);
                    } else if (event.getMessage().getContentDisplay().startsWith("!decay")) {
                        decay(event);
                    } else if (event.getMessage().getContentDisplay().startsWith("!test")) {

                    }
                }
                if (event.getMessage().getContentDisplay().startsWith("!register")) {
                    register(event);
                } else if (event.getMessage().getContentDisplay().startsWith("!bid")) {
                    bid(event);
                } else if (event.getMessage().getContentDisplay().startsWith("!dkp")) {
                    dkp(event);
                } else if (event.getMessage().getContentDisplay().startsWith("!notify")) {
                    notify(event);
                } else if (event.getMessage().getContentDisplay().startsWith("!attend")) {
                    attend(event);
                } else if (event.getMessage().getContentDisplay().startsWith("!key")) {
                    String[] args = event.getMessage().getContentDisplay().split(" ");
                    if (args[0].equals("!key")) {
                        key(event);
                    }
                } else if (event.getMessage().getContentDisplay().startsWith("!check")) {
                    check(event);
                } else if (event.getMessage().getContentDisplay().startsWith("!help")) {
                    helpPlayer(event);
                }
            }

            // System.out.printf("[PM] %#s: %s%n", event.getAuthor(),
            // event.getMessage().getContentDisplay());
        }
    }

    public void helpPlayer(MessageReceivedEvent event) {
        // editBlock("BIDS ARE OPEN", 2);
        fb(event, helpMsg);
    }

    public void startBid(MessageReceivedEvent event) {
        // editBlock("BIDS ARE OPEN", 2);

        String[] args = event.getMessage().getContentDisplay().split(" ");
        if (args.length < 2) {
            System.out.println(args.length + " LEEENGH");
            fb(event, "Usage: 'sb <raidId> <item>");
        } else if (raids.containsKey(args[1])) {
            Raid r = raids.get(args[1]);
            r.bids = new HashMap();
            r.bidding = true;
            if (args.length > 2) {
                r.bidItem = event.getMessage().getContentDisplay()
                        .substring(event.getMessage().getContentDisplay().indexOf(" ") + 2);
            } else {
                r.bidItem = "";
            }
            fb(event, "Bid started on " + (r.bidItem.equals("") ? "IDK" : r.bidItem));

            toUsers(event, notify, "Bid started on " + (r.bidItem.equals("") ? "IDK" : r.bidItem), args[1]);
            try {
                FileStuff.WriteToLog("\nBid started on " + (r.bidItem.equals("") ? "IDK" : r.bidItem));
            } catch (IOException ex) {
                Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
            }
            editBlock("", args[1]);

        } else {
            fb(event, "That raid id was not found D:");
        }

    }

    public void readCSVAttend(MessageReceivedEvent event) {
        String[] args = event.getMessage().getContentDisplay().split(" ");
        if (args.length != 4) {
            fb(event, "Usage: 'ia <raidId> <file>");
        } else if (raids.containsKey(args[1])) {
            Raid r = raids.get(args[1]);
            try {
                // editBlock("BIDS ARE OPEN", 2);
                for (String s : FileStuff.ReadCSVAttend(args[2])) {
                    r.attending.put(s, users.get(s));
                }
                editBlock("", args[1]);
            } catch (IOException ex) {
                Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
                fb(event, "Error: Most likely on file ");
            }
        } else {
            fb(event, "That raid id was not found D:");
        }

    }

    public void attendKey(MessageReceivedEvent event) {

        String[] args = event.getMessage().getContentDisplay().split(" ");
        System.out.println(args.toString());
        if (args.length != 4) {
            fb(event, "Usage: 'ak <key> <raidChannel> <raidId>");
        } else if (channels.contains(args[2])) {
            Raid raid = new Raid(args[2]);
            raid.attend = true;
            boolean repeat = false;
            boolean repeatId = false;
            boolean repeatChannel = false;
            boolean idChannel = false;

            for (Raid r : raids.values()) {
                if (r.attendKey.equals(args[1])) {
                    repeat = true;
                }
            }

            if (!raids.isEmpty()) {
                for (String id : raids.keySet()) {
                    if (args[3].equals(id)) {
                        repeatId = true;
                    }
                    if (raids.get(id).channel.equals(args[2])) {
                        repeatChannel = true;
                    }
                }
            }

            for (String s : channels) {
                if (args[3].equals(s)) {
                    idChannel = true;
                }
            }
            if (!idChannel) {
                if (!repeatChannel) {
                    if (!repeatId) {
                        if (!repeat) {
                            raid.attendKey = args[1];
                            raid.id = args[3];
                            raids.put(args[3], raid);
                            editBlock("", args[3]);
                            fb(event, "Raid " + args[3] + " Initiated in channel: " + args[2]);
                        } else {
                            fb(event, "That raid key is already in use");
                        }
                    } else {
                        fb(event, "That raid id is already in use");
                    }
                } else {
                    fb(event, "That channel is already in use by a raid.");
                }
            } else {
                fb(event, "Raid ids cannot be the same as channel names.");
            }

        } else {
            fb(event, "That channel isn't registered! D:");
        }
        // attending = new HashMap();
        // editBlock("Attendance is Open", 1);

    }

    public void attendOff(MessageReceivedEvent event) {
        String[] args = event.getMessage().getContentDisplay().split(" ");
        if (args.length != 2) {
            fb(event, "Usage: 'ao <raidId>");
        } else if (raids.containsKey(args[1])) {
            Raid r = raids.get(args[1]);
            r.attend = false;
            // editBlock("Attendance Closed", 2);
            editBlock("", args[1]);
        } else {
            fb(event, "That raid id was not found D:");
        }

    }

    public void attendClear(MessageReceivedEvent event) {
        String[] args = event.getMessage().getContentDisplay().split(" ");
        if (args.length != 2) {
            fb(event, "Usage: 'ac <raidId>");
        } else if (raids.containsKey(args[1])) {
            raids.get(args[1]).reset = true;
            if (raids.size() == 0) {
                editBlock("Attendance Cleared", "all");
            } else {
                editBlock("Attendance Cleared", args[1]);
            }
            raids.remove(args[1]);
        } else {
            fb(event, "That raid id was not found D:");
        }

    }

    public void endBid(MessageReceivedEvent event) {
        String[] args = event.getMessage().getContentDisplay().split(" ");
        if (args.length != 2) {
            fb(event, "Usage: 'eb <raidId>");
        } else if (raids.containsKey(args[1])) {
            Raid r = raids.get(args[1]);

            TextChannel chan = null;

            r.highBidders = new ArrayList();
            r.bidding = false;
            int topBid = 0;
            for (User bidder : r.bids.keySet()) {
                if (r.bids.get(bidder) > topBid) {
                    topBid = r.bids.get(bidder);
                }
            }
            for (User bidder : r.bids.keySet()) {
                if (r.bids.get(bidder) == topBid) {
                    r.highBidders.add(bidder);
                }
            }
            String winners = "";
            if (!r.bidItem.equals("")) {
                winners += "\n" + "| " + r.bidItem + " |\n";
            }
            winners += "---HighBidders---\n";
            for (User hbidder : r.highBidders) {
                winners = winners + hbidder.Username + " for " + r.bids.get(hbidder) + "\n";
            }
            winners = winners + "---theRest---\n";

            for (User bidder : r.bids.keySet()) {
                if (r.bids.get(bidder) < topBid) {
                    winners = winners + bidder.Username + " for " + r.bids.get(bidder) + "\n";
                }
            }
            r.winnerz = winners;

            fb(event, winners);
            toUsers(event, notify, winners, args[1]);
            editBlock("", args[1]);
            try {
                FileStuff.WriteToLog("\n=====================================================\n" + winners
                        + "\n=====================================================\n");

                // for (Message m : chan.getHistory().retrievePast(1).complete()) {
                // if (m.getAuthor().getAsTag().equals("Portbot#8824")) {
                // if (m.getContentDisplay().startsWith("!!!\nPortalz Lame DKP")) {
                // MessageBuilder mb = new MessageBuilder();
                // mb.appendCodeBlock("Portalz Lame DKP\n" + getTable(), "");
                // m.editMessage(mb.build()).queue();
                // }
                // }
                // }
            } catch (IOException ex) {
                Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else {
            fb(event, "That raid id was not found D:");
        }

    }

    public void finalizeBid(MessageReceivedEvent event) {
        // String[] args = event.getMessage().getContentDisplay().split(" ");
        // if (args.length != 2) {
        // fb(event, "Usage: 'fz <raidId>");
        // } else if (raids.containsKey(args[1])) {
        // Raid r = raids.get(args[1]);
        //
        //
        //
        //
        // } else {
        // fb(event, "That raid id was not found D:");
        // }

        String[] args = event.getMessage().getContentDisplay().split(" ");
        if (args.length != 2) {
            fb(event, "Usage: 'fz <raidId>");
        } else if (raids.containsKey(args[1])) {
            Raid r = raids.get(args[1]);

            System.out.println(r.bids.get(r.highBidders.get(0)));
            if (r.highBidders.size() == 1) {
                int oldDkp = r.highBidders.get(0).dkp;
                r.highBidders.get(0).dkp = r.highBidders.get(0).dkp - r.bids.get(r.highBidders.get(0));
                fb(event,
                        r.highBidders.get(0).Username + " dkp set to " + r.highBidders.get(0).dkp + " from " + oldDkp);
                try {
                    FileStuff.WriteToLog("\n" + r.highBidders.get(0).Username + " dkps set to "
                            + r.highBidders.get(0).dkp + " from " + oldDkp + " by " + event.getAuthor().getAsTag());
                } catch (IOException ex) {
                    Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                fb(event, "HighBidder list not singular");
            }

        } else {
            fb(event, "That raid id was not found D:");
        }

    }

    public void exportCSV() {
        try {
            String print = "";
            for (User x : users.values()) {
                print += x.Username + "\t" + x.dkp + "\n";
            }
            FileStuff.WriteToCSVFile(print, "CSVW");
            // editBlock("", 1);
        } catch (IOException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void exportCSVAttend(MessageReceivedEvent event) {

        String[] args = event.getMessage().getContentDisplay().split(" ");
        if (args.length != 3) {
            fb(event, "Usage: 'ea <raidId> <filename>");
        } else if (raids.containsKey(args[1])) {
            Raid r = raids.get(args[1]);
            try {
                String print = "";
                for (User x : r.attending.values()) {
                    print += x.Username + "\n";
                }
                FileStuff.WriteToCSVFile(print, args[2]);
                fb(event, "Attendace exported to" + args[2] + ".txt");
            } catch (IOException ex) {
                Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            fb(event, "That raid id was not found D:");
        }

    }

    public void importCSV(int type) {// 1 = overwrite users | 2 == add
        try {
            HashMap<String, User> imported = FileStuff.ReadCSV();
            if (type == 1) {
                users = imported;
            }
            FileStuff.WriteToLog("=====Import DKP=====");
            for (User x : imported.values()) {
                if (x.Discord != null) {
                    discordUsers.put(x.Discord, x);
                }
                if (type == 2) {
                    if (users.containsKey(x.Username)) {
                        FileStuff.WriteToLog(x.Username + " dkp " + users.get(x.Username).dkp + " to "
                                + (users.get(x.Username).dkp + x.dkp));
                        users.get(x.Username).dkp = users.get(x.Username).dkp + x.dkp;
                    } else {
                        users.put(x.Username, x);
                    }
                }
            }
            FileStuff.WriteToLog("=====Import DKP END=====");
            editBlock("", "all");
        } catch (IOException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void importCSVKeys() {
        try {
            HashMap<String, User> keyUsers = FileStuff.ReadCSVKeys();
            for (User x : keyUsers.values()) {
                if (!users.containsKey(x.Username)) {
                    users.put(x.Username, x);
                    users.get(x.Username).setKey(x.key);
                } else {
                    users.get(x.Username).setKey(x.key);
                }
            }
            for (User x : users.values()) {
                if (x.Discord != null) {
                    discordUsers.put(x.Discord, x);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void writeCSV() {
        try {
            HashMap<String, User> rusers = FileStuff.ReadCSV();
            for (User x : rusers.values()) {
                if (!users.containsKey(x.Username)) {
                    users.put(x.Username, x);
                    users.get(x.Username).setDKP(x.dkp);
                } else {
                    System.out.println(x.Username);
                    users.get(x.Username).setDKP(x.dkp);
                }

            }
            for (User x : users.values()) {
                if (x.Discord != null) {
                    discordUsers.put(x.Discord, x);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }
        editBlock("", "all");
    }

    public void exportJSON(MessageReceivedEvent event) {
        try {
            FileStuff.dataToJson(users);
            // fb(event, "JSON File");
        } catch (IOException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void msgJSON(MessageReceivedEvent event) {

        if (msgOutput != 0 && System.currentTimeMillis() - msgOutput < 30000) {
            fb(event, "Backup can be msged in " + (30 - Math.round(((System.currentTimeMillis() - msgOutput) / 1000)))
                    + "s");
        } else {

            SimpleDateFormat formatter = new SimpleDateFormat("YY-MM-dd HH-mm-ss z");
            Date date = new Date(System.currentTimeMillis());
            System.out.println();

            File file = new File(FileStuff.dataDir + File.separator + "backupMsgs");
            for (File f : file.listFiles()) {
                f.delete();
            }
            String endTitle = " backup";
            String title = formatter.format(date) + endTitle;

            try {
                File theFile = FileStuff.dataToJsonCustomFile(users,
                        FileStuff.dataDir + File.separator + "backupMsgs" + File.separator + title);
                msgOutput = System.currentTimeMillis();
                event.getChannel().sendFile(theFile, title + ".JSON").queue();
            } catch (IOException ex) {
                Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public void backupJSON(MessageReceivedEvent event) {

        if (backOutput != 0 && System.currentTimeMillis() - backOutput < 30000) {
            fb(event, "Backup can be made in " + (30 - Math.round(((System.currentTimeMillis() - backOutput) / 1000)))
                    + "s");
        } else {

            SimpleDateFormat formatter = new SimpleDateFormat("YY-MM-dd HH-mm-ss z");
            Date date = new Date(System.currentTimeMillis());
            System.out.println();

            String endTitle = " backup";
            String title = formatter.format(date) + endTitle;

            try {
                FileStuff.dataToJsonCustomFile(users,
                        FileStuff.dataDir + File.separator + "backups" + File.separator + title);
                backOutput = System.currentTimeMillis();
                fb(event, "Backup Complete");
            } catch (IOException ex) {
                Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

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

    public void keyReg(MessageReceivedEvent event) {
        String[] args = event.getMessage().getContentDisplay().split(" ");
        if (args.length < 2) {
            fb(event, "Usage: 'keyReg <name> <key>");
        } else if (args.length == 3) {
            String key = args[2];
            if (!users.containsKey(args[1])) {
                users.put(args[1], new User(args[1], 0));
            }
            users.get(args[1]).setKey(key);
            fb(event, "Key registered \n"
                    + "!register " + users.get(args[1]).Username + " CLASS " + users.get(args[1]).key);
            exportJSON(event);
            Gson gson = new Gson();
            System.out.println(gson.toJson(users));
        }
        try {
            FileStuff.dataToJson(users);
        } catch (IOException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void removePlayer(MessageReceivedEvent event) {
        String[] args = event.getMessage().getContentDisplay().split(" ");
        if (args.length != 2) {
            fb(event, "Usage: 'removePlayer <name>");
        } else if (args.length == 2) {
            String name = norm(args[1]);
            if (users.containsKey(name)) {

                if (discordUsers.containsKey(users.get(name).Discord)) {
                    discordUsers.remove(users.get(name).Discord);
                }
                users.remove(args[1]);
                fb(event, "User Removed");
                exportJSON(event);
                editBlock("", "all");
            } else {
                fb(event, "User Not Found");
            }

        }
        try {
            FileStuff.dataToJson(users);
        } catch (IOException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void normReply(MessageReceivedEvent event) {
        String[] args = event.getMessage().getContentDisplay().split(" ");
        if (args.length != 2) {
            fb(event, "Usage: 'norm <name>");
        } else if (args.length == 2) {
            String name = norm(args[1]);
            fb(event, norm(name));

        }

    }

    public void register(MessageReceivedEvent event) {
        String[] args = event.getMessage().getContentDisplay().split(" ");
        String name = event.getAuthor().getAsTag();

        System.out.println("register0 " + args.length + args[0]);
        if (args.length < 4) {
            System.out.println("register1");
            fb(event, "Usage: !register <character> <class> <key>");
        } else {
            if (discordUsers.containsKey(name)) {
                System.out.println("register2");
                discordUsers.get(name).Discord = "unbound";

                // fb(event, "This discord account is already registered to " +
                // discordUsers.get(name).Username + " " + discordUsers.get(name).dkp + " dkp");
                fb(event, "This discord account has been unregistered from " + discordUsers.get(name).Username
                        + " - key: " + discordUsers.get(name).key);
                // discordUsers.remove(name);
            }
            String user = norm(args[1]);
            if (users.containsKey(user) && args.length == 4) {
                if (users.containsKey(user)) {
                    Gson gson = new Gson();
                    System.out.println(gson.toJson(users));
                    System.out.println("2" + users.get(user).key + " " + users.get(user).dkp);
                    String pClass = "";
                    for (String s : classes) {
                        if (s.equalsIgnoreCase(args[2])) {
                            pClass = s;
                        }
                    }
                    if (!pClass.equals("")) {
                        if (users.get(user).getKey().equals(args[3])) {
                            users.get(user).setDiscord(event.getAuthor().getAsTag());
                            users.get(user).Main = user;
                            users.get(user).characters.put(user, pClass);
                            discordUsers.put(event.getAuthor().getAsTag(), users.get(user));
                            exportJSON(event);
                            fb(event, "Registered!");
                        } else {
                            fb(event, "Incorrect Key D:.");
                        }
                    } else {
                        fb(event, "Error: Class " + args[2] + " not found.");
                    }

                } else {
                    fb(event, "Could not find that name.");
                }

            }
        }
    }

    public void bid(MessageReceivedEvent event) {

        String[] args = event.getMessage().getContentDisplay().split(" ");
        if (!discordUsers.containsKey(event.getAuthor().getAsTag())) {
            fb(event, "Your discord account is not registered. Get a key from Portalz then type\"!register\"");
        } else if (args.length != 2) {
            fb(event, "Usage: !bid <##> (Denominations of 5)");
        } else {
            Raid contained = null;
            for (Raid raid : raids.values()) {
                if (raid.attending.containsKey(discordUsers.get(event.getAuthor().getAsTag()).Username)) {
                    contained = raid;
                }
            }
            if (contained != null) {
                if (contained.bidding) {
                    try {
                        int bid = Integer.parseInt(args[1]);
                        if (bid % 5 == 0) {
                            if (discordUsers.get(event.getAuthor().getAsTag()).dkp >= bid) {
                                contained.bids.put(discordUsers.get(event.getAuthor().getAsTag()), bid);
                                System.out.println(discordUsers.get(event.getAuthor().getAsTag()).Username
                                        + " bid has been set to: "
                                        + contained.bids.get(discordUsers.get(event.getAuthor().getAsTag()))
                                        + (contained.bidItem.equals("") ? "" : " for " + contained.bidItem));
                                fb(event,
                                        "Your bid has been set to: "
                                                + contained.bids.get(discordUsers.get(event.getAuthor().getAsTag()))
                                                + (contained.bidItem.equals("") ? "" : " for " + contained.bidItem)
                                                + "\nYou can overwrite your bid before it closes by !bid <##> again");
                                FileStuff.WriteToLog("\n" + discordUsers.get(event.getAuthor().getAsTag()).Username
                                        + " bid has been set to: "
                                        + contained.bids.get(discordUsers.get(event.getAuthor().getAsTag())));
                            } else {
                                fb(event, "You don't have enough DKP");
                            }
                        } else {
                            fb(event, "Only denominations of 5 please (5,10,15....)");
                        }
                    } catch (Exception e) {
                        fb(event, "Thanks not a number -.-");
                    }
                } else {
                    fb(event, "Bids aren't open right now!");
                }
            } else {
                fb(event, "You aren't attending any raids");
            }
        }

    }

    public void check(MessageReceivedEvent event) {
        String[] args = event.getMessage().getContentDisplay().split(" ");
        ArrayList<User> list = new ArrayList();
        if (!discordUsers.containsKey(event.getAuthor().getAsTag())) {
            fb(event, "Your discord account is not registered. Get a key from Portalz then type\"!register\"");
        } else if (args.length != 2) {
            // fb(event, "Usage: !check <Class | ArmorType> \n example: !check mage");
            fb(event, "Usage: !check <Class> \n example: !check mage");
        } else {
            String pClass = "";
            for (String s : classes) {
                if (s.equalsIgnoreCase(norm(args[1]))) {
                    pClass = s;
                }
            }
            System.out.println(pClass + " = PCLASS");

            if (!pClass.equals("")) {
                Collection<User> use;
                Raid contained = null;
                for (Raid raid : raids.values()) {
                    if (raid.attending.containsKey(discordUsers.get(event.getAuthor().getAsTag()).Username)) {
                        contained = raid;
                    }
                }
                if (contained != null) {
                    use = contained.attending.values();
                } else {
                    use = users.values();
                }

                for (User u : use) {
                    if (u.characters != null && u.Main != null && u.characters.get(u.Main).equals(pClass)) {
                        list.add(u);
                    }
                }
                fb(event, sortTable(list));
            } else {
                fb(event, "Error: Class " + norm(args[1]) + " not found.");
            }
        }

    }

    public void attendManual(MessageReceivedEvent event) {
        String[] args = event.getMessage().getContentDisplay().split(" ");
        if (args.length != 4) {
            fb(event, "Usage: !am <name> <type> <raidId> (types: r-remove a-add)");
        } else if (raids.containsKey(args[3])) {
            Raid r = raids.get(args[3]);

            String name = args[1];
            if (!users.containsKey(name)) {
                fb(event, "That player isn't registered");
            } else if (args[2].equals("r")) {
                if (r.attending.containsKey(name)) {
                    r.attending.remove(name);
                    editBlock("", args[3]);
                    fb(event, "Player removed from attendance");
                } else {
                    fb(event, "That player isn't attending");
                }
            } else if (args[2].equals("a")) {
                if (!r.attending.containsKey(name)) {
                    r.attending.put(name, users.get(name));
                    editBlock("", args[3]);
                    fb(event, "Player added to attendance.");
                } else {
                    fb(event, "That player is already attending");
                }
            } else {
                fb(event, "wrong type");
                fb(event, "Usage: !am <name> <type> (types: r-remove a-add)");
            }

        } else {
            fb(event, "That raid id was not found D:");
        }

    }

    public void attend(MessageReceivedEvent event) {
        String[] args = event.getMessage().getContentDisplay().split(" ");
        if (args.length != 2) {
            fb(event, "Usage: !attend <key> (key announced in raid chat)");
        } else if (!discordUsers.containsKey(event.getAuthor().getAsTag())) {
            fb(event, "Your discord account is not registered. Get a key from Portalz then type\"!register\"");
        } else {
            Raid contained = null;
            for (Raid raid : raids.values()) {
                if (raid.attendKey.equals(args[1])) {
                    contained = raid;
                }
            }
            if (contained != null) {
                if (contained.attend) {

                    contained.attending.put(discordUsers.get(event.getAuthor().getAsTag()).Username,
                            discordUsers.get(event.getAuthor().getAsTag()));
                    fb(event, "You are in attendance!");
                    fb(event, helpMsg);
                    // editBlock("Attendance is Open", 2);
                    editBlock("", contained.id);

                } else {
                    fb(event, "Attendance isn't being taken right now");
                }
            } else {
                fb(event, "No raid has that key!");
            }

        }

    }

    public void key(MessageReceivedEvent event) {
        String[] args = event.getMessage().getContentDisplay().split(" ");
        if (args.length != 2) {
            fb(event, "Usage: !key [key] (less than 20 characters)");
            if (discordUsers.containsKey(event.getAuthor().getAsTag())) {
                fb(event, "Current Key for " + discordUsers.get(event.getAuthor().getAsTag()).Username + " is ["
                        + discordUsers.get(event.getAuthor().getAsTag()).key + "]");
            }
        } else if (!discordUsers.containsKey(event.getAuthor().getAsTag())) {
            fb(event, "Your discord account is not registered. Get a key from Portalz then type\"!register\"");
        } else if (args[1].length() > 20) {
            fb(event, "Usage: !key <key> (less than 20 characters)");
        } else {
            discordUsers.get(event.getAuthor().getAsTag()).key = args[1];
            fb(event, "Key for " + discordUsers.get(event.getAuthor().getAsTag()).Username + " set to: ["
                    + discordUsers.get(event.getAuthor().getAsTag()).key + "]");
        }
        exportJSON(event);
    }

    public void giveDKP(MessageReceivedEvent event) {
        String[] args = event.getMessage().getContentDisplay().split(" ");
        if (args.length != 3) {
            fb(event, "Usage: !giveDKP <Username> <Amount>. (Can replace username with attending or all)");
        }
        boolean isInt = false;
        try {
            int reward = Integer.parseInt(args[2]);
            isInt = true;
        } catch (Exception e) {
            fb(event, "Thanks not a number -.-");
            System.out.println("A3");
        }
        if (isInt) {

            int reward = Integer.parseInt(args[2]);
            if (users.keySet().contains(norm(args[1]))) {
                int oldDKP = users.get(norm(args[1])).dkp;
                users.get(norm(args[1])).dkp = users.get(norm(args[1])).dkp + reward;
                fb(event, args[1] + " dkps set to " + users.get(norm(args[1])).dkp + " from " + oldDKP);
                try {
                    FileStuff.WriteToLog("\n" + args[1] + " dkps set to " + users.get(norm(args[1])).dkp + " from "
                            + oldDKP + " by " + event.getAuthor().getAsTag());
                } catch (IOException ex) {
                    Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
                }
                exportJSON(event);

                Raid contained = null;
                for (Raid raid : raids.values()) {
                    if (raid.attending.containsKey(discordUsers.get(event.getAuthor().getAsTag()).Username)) {
                        contained = raid;
                    }
                }
                if (contained != null) {
                    editBlock("", contained.id);
                } else {
                    editBlock("", "all");
                }

            } else if (args[1].equalsIgnoreCase("all")) {
                for (User x : users.values()) {
                    x.dkp = x.dkp + reward;

                    try {
                        FileStuff.WriteToLog("\n" + x.Username + " " + x.dkp);
                    } catch (IOException ex) {
                        Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
                fb(event, "All awarded " + reward);
                try {
                    FileStuff.WriteToLog("\n" + "All awarded " + reward);
                } catch (IOException ex) {
                    Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
                }
                editBlock("", "all");
            } else {
                Raid raid = null;
                for (String s : raids.keySet()) {
                    if (args[1].equalsIgnoreCase(s)) {
                        raid = raids.get(s);
                    }
                }
                if (raid != null) {
                    for (User x : raid.attending.values()) {
                        x.dkp = x.dkp + reward;

                        try {
                            FileStuff.WriteToLog("\n" + x.Username + " " + x.dkp);
                        } catch (IOException ex) {
                            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
                        }

                    }
                    try {
                        FileStuff.WriteToLog("\n" + "All attending raid " + raid.id + " awarded " + reward);
                    } catch (IOException ex) {
                        Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    fb(event, "All attending raid " + raid.id + " awarded " + reward);
                    editBlock("", raid.id);
                } else {
                    fb(event, "Incorrect <Username> field");
                }
            }

            int type;

            exportJSON(event);
            System.out.println("E333");
        }
    }

    public void dkp(MessageReceivedEvent event) {
        if (!discordUsers.containsKey(event.getAuthor().getAsTag())) {
            fb(event, "Your discord account is not registered. Get a key from Portalz then type\"!register\"");
        } else {
            fb(event, discordUsers.get(event.getAuthor().getAsTag()).dkp + "");
        }
    }

    public void notify(MessageReceivedEvent event) {
        System.out.println(event.getAuthor().getAsTag());
        if (!discordUsers.containsKey(event.getAuthor().getAsTag())) {
            fb(event, "Your discord account is not registered. Get a key from Portalz then type\"!register\"");
        } else if (notify.contains(discordUsers.get(event.getAuthor().getAsTag()))) {
            notify.remove(discordUsers.get(event.getAuthor().getAsTag()));
            fb(event, "Auction notifications disabled");
        } else {
            notify.add(discordUsers.get(event.getAuthor().getAsTag()));
            fb(event, "Auction notifications enabled");
        }
    }

    public void onStart() {
        FileStuff.checkDataDir();
        Props.processListProp("Admins", Props.admins);
        Props.processListProp("moreChannels", Props.channels);

        channels.add(Props.getProp("mainChannel", true));
        for (String s : Props.channels) {
            channels.add(s);
        }

        admins.add(Props.getProp("Owner", true));
        for (String s : Props.admins) {
            admins.add(s);
        }

        File fi = new File(FileStuff.dataDir + "JSON.txt");
        if (fi.exists()) {
            importJSON();
        }

        boolean texted = false;
        TextChannel chan = null;
        String test = "";
        String entryId = "";
        System.out.println("AA");
        /// find dkp channel
        // jda.gettext
        System.out.println(channels);
        System.out.println(jda.getTextChannels());

        for (String eChan : channels) {
            for (TextChannel x : jda.getTextChannelsByName(eChan, true)) {
                System.out.println(x.getGuild().getName());
                test = x.getName() + "\n";
                if (x.getGuild().getName().contains(Props.getProp("Server", true))) {
                    System.out.println("found channel");
                    chan = x;
                }
            }

            // get table message for editingi
            try {
                System.out.println("BB");
                // find and edit===============================
                for (Message m : chan.getHistory().retrievePast(1).complete()) {
                    if (m.getAuthor().getAsTag().equals("Portbot#8824")) {
                        if (m.getContentDisplay().startsWith("```\nPortalz Lame DKP")) {
                            MessageBuilder mb = new MessageBuilder();
                            mb.appendCodeBlock("Portalz Lame DKP\n" + getTable("all"), "");
                            m.editMessage(mb.build()).queue();
                            texted = true;
                        }
                    }
                }

                // ===================================
                if (!texted) {
                    MessageBuilder mb = new MessageBuilder();
                    mb.appendCodeBlock("Portalz Lame DKP\n" + getTable("all"), "");
                    chan.sendMessage(mb.build()).queue();
                }
            } catch (Exception e) {
                System.out.println("C");
                MessageBuilder mb = new MessageBuilder();
                mb.appendCodeBlock("Portalz Lame DKP\n" + getTable("all"), "");
                chan.sendMessage(mb.build()).queue();

            }
        }

    }

    // 1 ALL | 2 Attendance
    public String getTable(String raidId) {
        System.out.println("D");
        String table = "Dkp List " + ((raidId.equals("all")) ? "" : raids.get(raidId).attending.size()) + "\n\n";
        ArrayList<String> list = new ArrayList();
        Collection<User> vals = new ArrayList();

        if (raidId.equals("all") || raids.get(raidId).reset) {
            vals.addAll(users.values());
        } else {
            vals.addAll(raids.get(raidId).attending.values());
        }

        HashMap<String, Integer> tab = new HashMap();
        ArrayList<String> tabOrder = new ArrayList();

        while (vals.size() != 0) {
            User remove = null;
            int max = -1;
            for (User u : vals) {
                if (u.dkp > max) {
                    max = u.dkp;
                    remove = u;
                    // System.out.println(vals.size());
                }
            }
            // System.out.println(vals.size());
            tabOrder.add(remove.Username);
            tab.put(remove.Username, remove.dkp);
            vals.remove(remove);
        }
        for (int j = 0; j < tabOrder.size(); j++) {
            String dkp = tab.get(tabOrder.get(j)) + "";
            for (int i = 0; i < 3 - dkp.length(); i++) {
                dkp = dkp + " ";
            }

            list.add(dkp + "  |  " + tabOrder.get(j) + "\n");

        }

        for (int i = 0; i < list.size(); i++) {
            table = table + list.get(i);
        }

        return table;
    }

    public String sortTable(Collection x) {
        System.out.println("D");
        String table = "Dkp List\n\n";
        ArrayList<String> list = new ArrayList();
        Collection<User> vals = new ArrayList();

        vals.addAll(x);

        HashMap<String, Integer> tab = new HashMap();
        ArrayList<String> tabOrder = new ArrayList();

        while (vals.size() != 0) {
            User remove = null;
            int max = -1;
            for (User u : vals) {
                if (u.dkp > max) {
                    max = u.dkp;
                    remove = u;
                    // System.out.println(vals.size());
                }
            }
            // System.out.println(vals.size());
            tabOrder.add(remove.Username);
            tab.put(remove.Username, remove.dkp);
            vals.remove(remove);
        }
        for (int j = 0; j < tabOrder.size(); j++) {
            String dkp = tab.get(tabOrder.get(j)) + "";
            for (int i = 0; i < 3 - dkp.length(); i++) {
                dkp = dkp + " ";
            }

            list.add(dkp + "  |  " + tabOrder.get(j) + "\n");

        }

        for (int i = 0; i < list.size(); i++) {
            table = table + list.get(i);
        }

        return table;
    }

    public String statBlock(Raid r) {
        String stat = "";
        if (r != null) {

            if (r.reset) {
                return "";
            }
            if (r.bidding) {
                stat += "\n" + r.statuses.get("bidding");
                if (!r.bidItem.equals("")) {
                    stat += "\n" + "| " + r.bidItem + " |\n";
                }
            }

            if (r.attend) {
                stat += "\n" + r.statuses.get("attend");
            }
            if (!r.winnerz.equals("")) {
                stat += "\n" + r.winnerz;
            }
        }
        return stat;
    }

    public void editBlock(String s, String Raid) {

        HashMap<String, String> raidMessages = new HashMap();

        Raid tarRaid = null;
        Raid mainChanRaid = null;
        for (Raid rcheck : raids.values()) {
            if (rcheck.id.equals(Raid)) {
                tarRaid = rcheck;
            }
            if (rcheck.channel.equals(mainChan)) {
                mainChanRaid = rcheck;
            }
        }

        if (tarRaid != null) {// specific raid
            raidMessages.put(tarRaid.id, "");
        } else if (Raid.equals("all")) {
            if (raids.size() != 0) {
                for (Raid r : raids.values()) {
                    raidMessages.put(r.id, "");
                }
            }
            if (mainChanRaid == null) {
                raidMessages.put("all", "");
            }
        }

        for (String raidId : raidMessages.keySet()) {
            String form = statBlock(raids.get(raidId));
            System.out.println(raids.get(raidId));
            // boolean texted = false;
            TextChannel chan = null;
            String test = "";
            String entryId = "";

            if (!form.equals("")) {
                form = "\n=======================" + form + "\n=======================\n";
            }

            String channel;
            if (raidId.equals("all")) {
                channel = mainChan;
            } else {
                channel = raids.get(raidId).channel;
            }

            for (TextChannel x : jda.getTextChannelsByName(channel, true)) {
                System.out.println(x.getGuild().getName());
                test = x.getName() + "\n";
                if (x.getGuild().getName().contains(Props.getProp("Server", true))) {
                    System.out.println("found channel");
                    chan = x;
                }
            }
            Boolean found = false;
            System.out.println(chan.getHistory().retrievePast(1).complete());
            for (Message m : chan.getHistory().retrievePast(1).complete()) {
                // System.out.println("retreived");
                if (m.getAuthor().getAsTag().equals("Portbot#8824")) {
                    // System.out.println("ByPortbot");
                    if (m.getContentDisplay().startsWith("```\nPortalz Lame DKP")) {
                        // System.out.println("EDITING SOON");
                        MessageBuilder mb = new MessageBuilder();
                        System.out.println(form);
                        mb.appendCodeBlock("Portalz Lame DKP\n" + form + getTable(raidId), "");
                        m.editMessage(mb.build()).queue();
                        found = true;
                    }
                }
            }
            if (!found) {
                MessageBuilder mb = new MessageBuilder();
                System.out.println(form);
                mb.appendCodeBlock("Portalz Lame DKP\n" + form + getTable(raidId), "");
                chan.sendMessage(mb.build()).queue();
            }
        }

    }

    // feedback
    public void fb(MessageReceivedEvent event, String s) {
        MessageBuilder mb = new MessageBuilder();
        mb.appendCodeLine(s);
        event.getChannel().sendMessage(mb.build()).queue();
    }

    public void toUsers(MessageReceivedEvent event, ArrayList<User> users, String s, String raidId) {

        for (User u : notify) {
            boolean send = true;
            if (!raidId.equals("all") && !raids.isEmpty()) {
                if (!raids.get(raidId).attending.containsKey(u.Main)) {
                    send = false;
                }
            }
            if (send) {
                MessageBuilder mb = new MessageBuilder();
                mb.appendCodeLine(s);
                jda.getUserByTag(u.Discord).openPrivateChannel().complete().sendMessage(mb.build()).queue();
            }

        }

    }

    public static String norm(String s) {
        String name = s.toLowerCase();
        if (name.charAt(0) >= 97 && name.charAt(0) <= 122) {
            name = (name.charAt(0) + "").toUpperCase() + name.substring(1, name.length());
        }
        return name;
    }

    public void normAll() {
        ArrayList<String> removeUsers = new ArrayList();
        ArrayList<String> removeCharacters = new ArrayList();

        for (String s : users.keySet()) {
            users.get(s).Username = norm(users.get(s).Username);
            if (users.get(s).Main != null) {
                users.get(s).Main = norm(users.get(s).Main);
            }
            if (users.get(s).characters != null) {
                for (String alt : users.get(s).characters.keySet()) {
                    removeCharacters.add(alt);
                }
            }

            for (String alt : removeCharacters) {
                users.get(s).characters.put(norm(alt), users.get(s).characters.get(alt));
                users.get(s).characters.remove(alt);
            }

        }
        for (String s : removeUsers) {
            users.put(norm(s), users.get(s));
            users.remove(s);
        }
        Gson gson = new Gson();
        System.out.println(gson.toJson(users));

    }

    public void decay(MessageReceivedEvent event) {

        try {
            FileStuff.WriteToLog("=====DECAY=====");
            for (User u : users.values()) {
                int dkp = u.dkp;
                double output = dkp;
                System.out.println(dkp * .95 + " " + (dkp * .95) % 5);

                output = (dkp * .95) - ((dkp * .95) % 5);
                if ((dkp * .95) % 5 >= 2.5) {
                    output += 5;
                }

                FileStuff.WriteToLog(u.Username + " dkp from [" + u.dkp + "]" + " then " + dkp * .95 + " "
                        + (dkp * .95) % 5 + " then finally [" + output + "]");
                FileStuff.WriteToLog(u.Username + " ");

                u.dkp = (int) output;
            }
            FileStuff.WriteToLog("=====DECAY END=====");

        } catch (IOException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }
        editBlock("", "all");
        fb(event, "All players decayed by 5%");

    }

    public void manualReg(MessageReceivedEvent event) {

    }
}
