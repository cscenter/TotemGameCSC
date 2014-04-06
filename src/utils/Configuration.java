package utils;

import model.Game;

import java.io.*;
import java.util.ArrayList;
import java.util.Queue;

/**
 * Created by lavton on 09.03.14.
 */
public class Configuration {
    private static volatile Configuration instance;
    public static Configuration getInstance() {
        Configuration localInstance = instance;
        if (localInstance == null) {
            synchronized (Configuration.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new Configuration();
                    defaultSettings();
                    gallery = new Gallery();
                }
            }
        }
        return localInstance;
    }
    private static final int PORT = 6923;
    private static String DIRECTORY = "data/";
    private static final String SERVER_IP = "127.0.0.1";
    private static final int MAX_NUMBER_OF_PLAYERS=15;
    private static int timeToWait=1000;
    public static int numberOfPlayers=2;
    private static Gallery gallery;
    private static ArrayList<String> peopleNames;
    private static ArrayList<Character> peopleOpenKeys;
    private static ArrayList<Character> peopleCatchKeys;
    public static boolean isServer = false;

    public static void ChangeDir(String newDir){
        DIRECTORY = newDir;
    }
    public static int getMaxNumberOfPlayers(){
        return MAX_NUMBER_OF_PLAYERS;
    }
    public static int getTimeToWait(){
        return getInstance().timeToWait;
    }
    public static int getPort(){
        return getInstance().PORT;
    }
    public static String getDirectory(){
        return DIRECTORY;
    }
    public static String getServerIp(){
        return getInstance().SERVER_IP;
    }
    public static Gallery getGallery(){
        return getInstance().gallery;
    }
    public static int getNumberOfPlayers(){
        return getInstance().numberOfPlayers;
    }
    public static ArrayList<String> getPeopleNames(){
        return getInstance().peopleNames;
    }
    public static ArrayList<Character> getPeopleOpenKeys(){
        return getInstance().peopleOpenKeys;
    }
    public static ArrayList<Character> getPeopleCatchKeys(){
        return getInstance().peopleCatchKeys;
    }
    private static void defaultSettings(){

        //считываются станартные настройки из txt файла
        BufferedReader input;
        String classJar =
                Configuration.class.getResource("/MainTotemGame.class").toString();
        if (classJar.startsWith("jar:")) {
            InputStream in;

            in = Configuration.class.getResourceAsStream(DIRECTORY+"standardSettings.txt");

            input = new BufferedReader(new InputStreamReader(in));

        }else {
            try {
                input = new BufferedReader(new FileReader(DIRECTORY+"standardSettings.txt"));
            } catch (FileNotFoundException e) {
                System.err.println("Can't read standard settings. Life is pain(");
                throw new RuntimeException("Can't read standard settings. Life is pain(");
            }
        }

        String line;
        try {
            line = input.readLine();
            numberOfPlayers = Integer.parseInt(line);
            peopleNames = new ArrayList<>(numberOfPlayers);
            peopleCatchKeys = new ArrayList<>(numberOfPlayers);
            peopleOpenKeys = new ArrayList<>(numberOfPlayers);

            for (int i = 0; i < numberOfPlayers; i++){
                String name = input.readLine();
                char inputOpenCardKey=input.readLine().charAt(0);
                char inputCatchTotemKey=input.readLine().charAt(0);
                peopleOpenKeys.add(inputOpenCardKey);
                peopleCatchKeys.add(inputCatchTotemKey);
                peopleNames.add(name);

            }
            input.close();
        }catch (IOException e){
            throw new RuntimeException("settings are not correct");
        }

    }

    public static void decodeCommands(Queue<Byte> commands,
                                      Queue<Integer> whoDid, Queue<Game.WhatPlayerDid> whatDid){
        whoDid.clear();
        whatDid.clear();
        while (!commands.isEmpty()){
            byte current=commands.remove();
            WPDHolder what = new WPDHolder(Game.WhatPlayerDid.OPEN_NEW_CARD);
            Integer who = decodeOneCommand(current, what);
            whoDid.add(who);
            whatDid.add(what.getValue());
        }
    }
    public static int decodeOneCommand(Byte command, WPDHolder what){
        int who = command/2;
        if (command-(command/2)*2==0){
            what.setValue(Game.WhatPlayerDid.TOOK_TOTEM);
        }else {
            what.setValue(Game.WhatPlayerDid.OPEN_NEW_CARD);
        }
        return who;
    }


    public static void codeCommands(Queue<Byte> commands,
                                      Queue<Integer> whoDid, Queue<Game.WhatPlayerDid> whatDid){
        commands.clear();
        while (!whoDid.isEmpty()){
            Integer who = whoDid.remove();
            Game.WhatPlayerDid what = whatDid.remove();
            Byte command = codeOneCommand( who, what);
            commands.add(command);
        }
    }
    public static byte codeOneCommand(Integer who, Game.WhatPlayerDid what){
        byte command = (byte)(who*2);
        if (what == Game.WhatPlayerDid.OPEN_NEW_CARD){
            command++;
        }
        return command;
    }
    public static class WPDHolder {
        private Game.WhatPlayerDid value;
        public WPDHolder(Game.WhatPlayerDid initial) { value = initial; }
        public void setValue(Game.WhatPlayerDid newValue) { value = newValue; }
        public Game.WhatPlayerDid getValue() { return value; }
    }
}
