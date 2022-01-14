package DiscordDkp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

import net.dv8tion.jda.api.entities.templates.Template;

public class Props {
    public static Properties props;
    public static HashMap<String, String> propTemplate;
    public static ArrayList<String> admins=new ArrayList();
    public static ArrayList<String> channels=new ArrayList();

    public static void propLoading() throws IOException {
        File propsFile = new File(FileStuff.dataDir + File.separator + "DiscordDkp.properties");
        propsFile.createNewFile();
        props = new Properties();
        props.load(new FileInputStream(propsFile.getAbsolutePath()));

        propTemplate = new HashMap();
        propTemplate.put("JDA-Key", "JDA Key for discord bot Goes here");
        propTemplate.put("Owner", "Main owner discord tag");
        propTemplate.put("Admins", "Admins Discort tag, comma separated: Admin1#1234,Admin2#1244,Admin3#5412");
        propTemplate.put("Server", "Discord Server Name (must be unique to bot)");
        propTemplate.put("mainChannel", "main raid channel");
        propTemplate.put("moreChannels", "more raid channels comma separated: channel1,channel2,channel3");

        for (String key : propTemplate.keySet()) {
            if (props.getProperty(key) == null) {
                props.setProperty(key, propTemplate.get(key));
            }
            props.store(new FileWriter(propsFile), null);
        }

    }

    public static void processListProp(String property, ArrayList map) {
        if (props.getProperty(property).compareTo(propTemplate.get(property)) != 0) {
            for (String toList : props.getProperty(property).split(",")) {
                map.add(toList);
            }
        }
        System.out.println(map);
    }

    public static String getProp(String property, boolean required) {
        if (propTemplate.get(property).compareTo(props.getProperty(property)) != 0) {
            return props.getProperty(property);
        } else {
            if (required) {
                System.out.println("=== WARNING === no " + property + " set in DiscordDkp.properties");
            }
            return null;
        }
    }
}
