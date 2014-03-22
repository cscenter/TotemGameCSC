package utils;

import java.io.*;
import java.util.ArrayList;
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
    private static final String DIRECTORY = "data/";
    private static final String SERVER_IP = "127.0.0.1";
    private static final int MAX_NUMBER_OF_PLAYERS=15;
    private static int timeToWait=1000;
    private static int numberOfPlayers;
    private static Gallery gallery;
    private static ArrayList<String> peopleNames;
    private static ArrayList<Character> peopleOpenKeys;
    private static ArrayList<Character> peopleCatchKeys;

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
        System.err.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
        System.err.println(classJar);
        if (classJar.startsWith("jar:")) {
            InputStream in;
            System.err.println("Here"+classJar);

            in = Configuration.class.getResourceAsStream(DIRECTORY+"standardSettings.txt");
            System.err.println("Here");

            input = new BufferedReader(new InputStreamReader(in));
            System.err.println("Here");

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
}
