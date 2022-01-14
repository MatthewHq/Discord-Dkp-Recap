package DiscordDkp;
import java.util.ArrayList;
import java.util.HashMap;
import net.dv8tion.jda.api.JDA;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Portalz
 */
public class Raid {

    public HashMap<String, User> attending = new HashMap(); //inRAID
    public HashMap<User, Integer> bids = new HashMap(); //INRAID
    public String bidItem = ""; //INRAID
    public boolean bidding = false; //INDRAID
    public boolean attend = false; //INRAID
    public String attendKey = ""; //INRAID
    public ArrayList<User> highBidders = new ArrayList();//INRAID
    public HashMap<String, String> statuses = new HashMap(); //inraid
    public String winnerz = ""; //inraid
    String id;
    String channel;
    boolean reset=false;

    String channelName;

    public Raid(String channelName) {
        //statuses
        channel=channelName;
        statuses.put("attend", "Attendance is Open"); //0
        statuses.put("bidding", "-- BIDS ARE OPEN --"); //1
    }

}
