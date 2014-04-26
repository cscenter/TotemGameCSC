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
    public static String SERVER_IP = "127.0.0.1";
    /**
     * максимальное количество игроков
     */
    private static final int MAX_NUMBER_OF_PLAYERS = 15;
    /**
     * время квантования
     */
    private static int timeToWait = 4000;
    /**
     * количество игроков, которых будет ждать сервер
     */
    public static int numberOfPlayers = 2;
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
    public static boolean isServer = false;

    /**
     * изменение дириктории, где лежат картинки
     * @param newDir новый путь
     */
    public static void ChangeDir(String newDir) {
        DIRECTORY = newDir;
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
     * @param whoDid кто походил
     * @param whatDid что сделал походивший
     */
    public static void decodeCommands(Queue<Byte> commands,
                                      Queue<Integer> whoDid, Queue<Game.WhatPlayerDid> whatDid) {
        whoDid.clear();
        whatDid.clear();
        while (!commands.isEmpty()) {
            byte current = commands.remove();
            WPDHolder what = new WPDHolder(Game.WhatPlayerDid.OPEN_NEW_CARD);
            Integer who = decodeOneCommand(current, what);
            whoDid.add(who);
            whatDid.add(what.getValue());
        }
    }

    /**
     * декодирование одной команды
     * последний бит отвечает за то, что сделал походивший
     * остальные - за номер походившего
     * @param command команда
     * @param what обёртка над тем, что сделал походивший
     * @return номер походившего
     */
    public static int decodeOneCommand(Byte command, WPDHolder what) {
        int who = command / 2;
        if (command - (command / 2) * 2 == 0) {
            what.setValue(Game.WhatPlayerDid.TOOK_TOTEM);
        } else {
            what.setValue(Game.WhatPlayerDid.OPEN_NEW_CARD);
        }
        return who;
    }

    /**
     * кодирование команд
     * последний бит отвечает за то, что сделал походивший
     * остальные - за номер походившего
     *
     * @param commands кодированные команды
     * @param whoDid кто походил
     * @param whatDid что сделал походивший
     */
    public static void codeCommands(Queue<Byte> commands,
                                    Queue<Integer> whoDid, Queue<Game.WhatPlayerDid> whatDid) {
        commands.clear();
        while (!whoDid.isEmpty()) {
            Integer who = whoDid.remove();
            Game.WhatPlayerDid what = whatDid.remove();
            Byte command = codeOneCommand(who, what);
            commands.add(command);
        }
    }

    /**
     * кодирование одной команды
     * последний бит отвечает за то, что сделал походивший
     * остальные - за номер походившего
     *
     * @param who кто походил
     * @param what что сделал походивший
     * @return команду в кодированном виде
     */
    public static byte codeOneCommand(Integer who, Game.WhatPlayerDid what) {
        byte command = (byte) (who * 2);
        if (what == Game.WhatPlayerDid.OPEN_NEW_CARD) {
            command++;
        }
        return command;
    }

    /**
     * класс-обёртка над тем, что сделал игрок
     * @see model.Game.WhatPlayerDid
     */
    public static class WPDHolder {
        /**
         * что сделал походивший
         */
        private Game.WhatPlayerDid value;

        /**
         * конструктор
         * @param initial что сделал походивший
         */
        public WPDHolder(Game.WhatPlayerDid initial) {
            value = initial;
        }

        /**
         * присвоение значения
         * @param newValue новое значение
         */
        public void setValue(Game.WhatPlayerDid newValue) {
            value = newValue;
        }

        /**
         *
         * @return что сделал походивший
         */
        public Game.WhatPlayerDid getValue() {
            return value;
        }
    }
}
