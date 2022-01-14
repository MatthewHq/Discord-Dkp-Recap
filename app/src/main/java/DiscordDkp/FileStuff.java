package DiscordDkp;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Portalz
 */
public class FileStuff {

    public static final String dataDir = "app" + File.separator+"data" + File.separator;

    public static void checkDataDir() {
        File dir = new File(dataDir);
        if (!dir.exists()) {
            dir.mkdir();
        }
    }

    public static void WriteToCSVFile(String s, String File) throws IOException {
        File f = new File(dataDir + File + ".txt");
        if (!f.exists()) {
            f.createNewFile();
        }
        BufferedWriter writer = new BufferedWriter(new FileWriter(f));
        writer.append(s);

        writer.close();
    }

    public static void WriteToLog(String s) throws IOException {
        File f = new File(dataDir + "log.txt");
        if (!f.exists()) {
            f.createNewFile();
        }
        BufferedWriter writer = new BufferedWriter(new FileWriter(f, true));
        SimpleDateFormat formatter = new SimpleDateFormat("YY-MM-dd HH:mm:ss z");
        Date date = new Date(System.currentTimeMillis());
        String time = formatter.format(date);
        writer.append(time + " " + s);
        writer.close();
    }

    // Java Program to illustrate reading from
    // FileReader using FileReader
    public static HashMap<String, User> ReadCSV() throws FileNotFoundException, IOException {
        BufferedReader reader;
        HashMap<String, User> ret = new HashMap();
        try {
            reader = new BufferedReader(new FileReader(dataDir + "CSV.txt"));
            String line = reader.readLine();
            while (line != null) {
                // System.out.println(line);
                User user = CSVtoUser(line);
                ret.put(App.norm(user.Username), user);
                // read next line
                line = reader.readLine();
            }
            reader.close();
            // System.out.println(MessageListener.users.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ret;
    }

    public static ArrayList<String> ReadCSVAttend(String file) throws FileNotFoundException, IOException {
        BufferedReader reader;
        ArrayList<String> ret = new ArrayList();
        try {
            reader = new BufferedReader(new FileReader(dataDir + file + ".txt"));
            String line = reader.readLine();
            while (line != null) {
                // System.out.println(line);
                ret.add(line);
                // read next line
                line = reader.readLine();
            }
            reader.close();
            // System.out.println(MessageListener.users.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ret;
    }

    public static User CSVtoUser(String entry) {

        int tabat = entry.indexOf("\t");
        // System.out.println(entry);

        String str = App.norm(entry.substring(0, tabat));
        String num = entry.substring(tabat + 1, entry.length());
        // System.out.println(num);

        int dkp = Integer.parseInt(num);
        User user = new User(str, dkp);

        return user;
    }

    public static HashMap<String, User> ReadCSVKeys() throws FileNotFoundException, IOException {
        BufferedReader reader;
        HashMap<String, User> ret = new HashMap();
        try {
            reader = new BufferedReader(new FileReader(dataDir + "CSVK.txt"));
            String line = reader.readLine();
            while (line != null) {
                // System.out.println(line);
                User user = CSVtoUserKeys(line);
                ret.put(App.norm(user.Username), user);
                // read next line
                line = reader.readLine();
            }
            reader.close();
            // System.out.println(MessageListener.users.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ret;
    }

    public static User CSVtoUserKeys(String entry) {

        int tabat = entry.indexOf("\t");
        // System.out.println(entry);

        String str = App.norm(entry.substring(0, tabat));
        String key = entry.substring(tabat + 1, entry.length());
        // System.out.println(num);

        User user = new User(str, 0);
        user.setKey(key);

        return user;
    }

    public static void dataToJson(HashMap<String, User> json) throws IOException {
        Gson gson = new Gson();
        File x = new File(dataDir + "JSON.txt");
        x.delete();
        x.createNewFile();
        // System.out.println(gson.toJson(MessageListener.users));
        // gson.toJson(MessageListener.users, new FileWriter(x.getName()));

        String str = gson.toJson(json);

        System.out.println("saving" + gson.toJson(json));
        BufferedWriter writer = new BufferedWriter(new FileWriter(x));
        writer.write(str);
        writer.close();
    }

    public static File dataToJsonCustomFile(HashMap<String, User> json, String text) throws IOException {
        Gson gson = new Gson();
        File x = new File(text + ".JSON");
        System.out.println(x.getAbsoluteFile() + " path");
        try {
            x.delete();
        } catch (Exception e) {
        }
        x.createNewFile();
        // System.out.println(gson.toJson(MessageListener.users));
        // gson.toJson(MessageListener.users, new FileWriter(x.getName()));

        String str = gson.toJson(json);

        System.out.println("msgJSON" + gson.toJson(json));
        BufferedWriter writer = new BufferedWriter(new FileWriter(x));
        writer.write(str);
        writer.close();

        return x;
    }

    public static HashMap<String, User> JSONtoData() throws IOException {
        Gson gson = new Gson();
        File x = new File(dataDir + "JSON.txt");

        if (!x.exists()) {
            x.createNewFile();
        }

        // Deep clone
        BufferedReader reader;
        reader = new BufferedReader(new FileReader(dataDir + "JSON.txt"));
        String line = reader.readLine();
        // System.out.println(gson.toJson(MessageListener.users));

        Type type = new TypeToken<HashMap<String, User>>() {
        }.getType();
        HashMap<String, User> clonedMap = gson.fromJson(line, type);
        reader.close();
        return clonedMap;

    }

}
