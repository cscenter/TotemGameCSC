package utils;

import model.Game;

import java.io.*;
import java.util.ArrayList;
import java.util.Queue;

/**
 * Created by lavton on 09.03.14.
 *
 * класс, ответственный за конфигурацию. Синглтон.
 */
public class Configuration {
    /**
     * локальный объект класса
     */
    private static volatile Configuration instance;

    /**
     * инициализация, следящая за тем, чтобы был лишь один объект класса
     * @return
     */
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

    /**
     * порт, на котором слушает сервер
     */
    private static int PORT = 6923;
    /**
     * дириктория, в которой лежат картинки
     */
    private static String DIRECTORY = "data/";
    /**
     * IP сервера
     */
    private static String SERVER_IP = "127.0.0.1";
    /**
     * максимальное количество игроков
     */
    private static final int MAX_NUMBER_OF_PLAYERS = 8;
    /**
     * время квантования
     */
    private static int timeToWait = 4000;
    /**
     * количество игроков, которых будет ждать сервер
     */
    private static int numberOfPlayers = 2;
    /**
     * ссылка на Галерею, там хранятся все картинки
     */
    private static Gallery gallery;
    /**
     * имена игроков по умолчанию
     */
    private static ArrayList<String> peopleNames;
    /**
     * клавиши открывания карт по умолчанию
     */
    private static ArrayList<Character> peopleOpenKeys;
    /**
     * клавиши захвата тотема по умолчанию
     */
    private static ArrayList<Character> peopleCatchKeys;
    /**
     * выбор играем по сети или нет
     */
    private static boolean isServer = false;

    public static boolean getIsServer(){
        return isServer;
    }
    /**
     * изменение дириктории, где лежат картинки
     * @param newDir новый путь
     */
    public static void ChangeDir(String newDir) {
        DIRECTORY = newDir;
    }

    private static int numOfCards = 150;

    public static int getNumOfCards() {
        return numOfCards;
    }

    /**
     * задаёт параметры начальные
     * @param isServer сетевое ли взаимодействие
     * @param numOfPlayers количество игроков
     * @param port порт для сервера
     * @param IP IP сервера
     */
    public static void setNetworkConfig(boolean isServer, int numOfPlayers, int port, String IP, int numOfCards) {
        SERVER_IP = IP;
        PORT = port;
        numberOfPlayers = numOfPlayers;
        Configuration.isServer = isServer;
        Configuration.numOfCards = numOfCards;
    }
    /**
     *
     * @return максимальное количество игроков
     */
    public static int getMaxNumberOfPlayers() {
        return MAX_NUMBER_OF_PLAYERS;
    }

    /**
     *
     * @return время квантования
     */
    public static int getTimeToWait() {
        return getInstance().timeToWait;
    }

    /**
     *
     * @return порт, на котором слушает сервер
     */
    public static int getPort() {
        return getInstance().PORT;
    }

    /**
     *
     * @return директорию, где картинки лежат
     */
    public static String getDirectory() {
        return DIRECTORY;
    }

    /**
     *
     * @return IP сервера
     */
    public static String getServerIp() {
        return getInstance().SERVER_IP;
    }

    /**
     *
     * @return ссылку на галерею
     */
    public static Gallery getGallery() {
        return getInstance().gallery;
    }

    /**
     *
     * @return количество играющих
     */
    public static int getNumberOfPlayers() {
        return getInstance().numberOfPlayers;
    }

    /**
     *
     * @return имена играющих
     */
    public static ArrayList<String> getPeopleNames() {
        return getInstance().peopleNames;
    }

    /**
     *
     * @return клавиши открытия верхних карт
     */
    public static ArrayList<Character> getPeopleOpenKeys() {
        return getInstance().peopleOpenKeys;
    }

    /**
     *
     * @return клавиши захвата тотема
     */
    public static ArrayList<Character> getPeopleCatchKeys() {
        return getInstance().peopleCatchKeys;
    }

    /**
     * настройки по умолчанию. Пытаемся считать всё из файла, используя магию
     * для JAR не работает
     *
     * считываем кол-во игроков
     * их имена
     * и клавиши открытия верхней карты и захвата тотема
     */
    private static void defaultSettings() {
        //считываются станартные настройки из txt файла
        BufferedReader input;
        String classJar =
                Configuration.class.getResource("/MainTotemGame.class").toString();
        if (classJar.startsWith("jar:")) {
            InputStream in;

            in = Configuration.class.getResourceAsStream(DIRECTORY + "standardSettings.txt");

            input = new BufferedReader(new InputStreamReader(in));

        } else {
            try {
                input = new BufferedReader(new FileReader(DIRECTORY + "standardSettings.txt"));
            } catch (FileNotFoundException e) {
                System.err.println("Can't read standard settings. Life is pain(");
                throw new RuntimeException("Can't read standard settings. Life is pain(");
            }
        }

        String line;
        try {
            line = input.readLine();
            //      numberOfPlayers = Integer.parseInt(line);
            peopleNames = new ArrayList<>(numberOfPlayers);
            peopleCatchKeys = new ArrayList<>(numberOfPlayers);
            peopleOpenKeys = new ArrayList<>(numberOfPlayers);

            for (int i = 0; i < numberOfPlayers; i++) {
                String name = input.readLine();
                char inputOpenCardKey = input.readLine().charAt(0);
                char inputCatchTotemKey = input.readLine().charAt(0);
                peopleOpenKeys.add(inputOpenCardKey);
                peopleCatchKeys.add(inputCatchTotemKey);
                peopleNames.add(name);

            }
            input.close();
        } catch (IOException e) {
            throw new RuntimeException("settings are not correct");
        }

    }

    /**
     * декодирование команд.
     * последний бит отвечает за то, что сделал походивший
     * остальные - за номер походившего
     * @param commands кодированные команды
     * @param whatDid что сделал походивший
     */
    public static void decodeCommands(Queue<Byte> commands, Queue<Game.WhatPlayerDid> whatDid) {
        whatDid.clear();
        while (!commands.isEmpty()) {
            byte current = commands.remove();
            Game.WhatPlayerDid what = decodeOneCommand(current);
            whatDid.add(what);
        }
    }

    /**
     * декодирование одной команды
     * последний бит отвечает за то, что сделал походивший
     * остальные - за номер походившего
     * @param command команда
     * @return кто ходил и что сделал
     */
    public static Game.WhatPlayerDid decodeOneCommand(Byte command) {
        int who = command / 2;
        Game.WhatPlayerDid what;
        if (command - (command / 2) * 2 == 0) {
            what = Game.WhatPlayerDid.TOOK_TOTEM;
        } else {
            what = Game.WhatPlayerDid.OPEN_NEW_CARD;
        }
        what.whoWasIt = who;
        return what;
    }

    /**
     * кодирование команд
     * последний бит отвечает за то, что сделал походивший
     * остальные - за номер походившего
     *
     * @param commands кодированные команды
     * @param whatDid что сделал походивший
     */
    public static void codeCommands(Queue<Byte> commands, Queue<Game.WhatPlayerDid> whatDid) {
        commands.clear();
        while (!whatDid.isEmpty()) {
            Game.WhatPlayerDid what = whatDid.remove();
            Byte command = codeOneCommand(what);
            commands.add(command);
        }
    }

    /**
     * кодирование одной команды
     * последний бит отвечает за то, что сделал походивший
     * остальные - за номер походившего
     *
     * @param what что сделал походивший
     * @return команду в кодированном виде
     */
    public static byte codeOneCommand(Game.WhatPlayerDid what) {
        byte command = (byte) (what.whoWasIt * 2);
        if (what == Game.WhatPlayerDid.OPEN_NEW_CARD) {
            command++;
        }
        return command;
    }
}
