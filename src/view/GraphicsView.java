package view;

import controller.BasicClient;
import controller.MyClient;
import controller.TotemClient;
import model.Game;
import utils.Configuration;

import javax.swing.*;
import java.awt.*;
import java.util.*;

/**
 * Класс, отвечающий за работу с игроками
 * В графическом режиме
 */
public class GraphicsView extends JFrame {
    private static int FRAME_SIZE;
    private MyPanel myPanel;
    boolean isServer;
    TotemClient client;

    /**
     * Поля, которые нужно знать View о игроках
     */
    /**
     * ввести число игроков, а так же кнопки управления
     * по умолчанию:
     * 4 игрока, клавиши
     * q, w
     * e, r
     * t, y
     * u, i
     */
    private char getNewChar(String inputMessage, String errorOutputMessage) {
        String inputString;
        char inputChar;
        Scanner scan = new Scanner(System.in);
        do {
            try {
                System.out.println(inputMessage);
                inputString = scan.nextLine();
                inputChar = inputString.charAt(0);
            } catch (StringIndexOutOfBoundsException e) {
                System.out.printf("%s\n", errorOutputMessage);
                continue;
            }
            break;
        } while (true);
        return inputChar;
    }

    private void defaultSettings(ArrayList<String> names, ArrayList<Character> openKeys, ArrayList<Character> catchKeys,
                                 ArrayList<Double> angles) {
        for (int i = 0; i < Configuration.getNumberOfPlayers(); i++) {
            names.add(Configuration.getPeopleNames().get(i));
            openKeys.add(Configuration.getPeopleOpenKeys().get(i));
            catchKeys.add(Configuration.getPeopleCatchKeys().get(i));
            angles.add(360.0 * i / Configuration.getNumberOfPlayers());
        }
    }


    private int setNumberOfPlayers() {
        int numberOfPeople;
        Scanner scan = new Scanner(System.in);
        do {
            try {
                System.out.println("Please, insert number of players");
                numberOfPeople = scan.nextInt();
                scan.nextLine();
            } catch (InputMismatchException | StringIndexOutOfBoundsException e) {
                System.out.println("Integer must be integer, you, clever user! try again\n");
                scan.nextLine();
                continue;
            }
            System.out.printf("\n");
            if (numberOfPeople <= 1) {
                System.out.printf("%d isn't correct number of players! try again\n", numberOfPeople);
            } else {
                break;
            }
        } while (true);
        return numberOfPeople;
    }

    private void notDefaultSettings(ArrayList<String> rezultStrings) {
        int numberOfPeople = setNumberOfPlayers();
        Scanner scan = new Scanner(System.in);
        String inputString;
        char inputCatchTotemKey;
        char inputOpenCardKey;
        for (int i = 0; i < numberOfPeople; i++) {
            String inputS;
            do {
                System.out.printf("player %d: insert your name\n", i + 1);
                inputS = scan.nextLine();
                if (inputS.length() != 0) {
                    break;
                } else {
                    System.out.println("Name can't be empty string! try again");
                }
            } while (true);
            rezultStrings.add(inputS);
            label:
            do {
                try {
                    System.out.printf("%s: insert key to open first card\n", inputS);
                    inputString = scan.nextLine();
                    inputOpenCardKey = inputString.charAt(0);
                    Character tmp = inputOpenCardKey;
                    inputOpenCardKey = tmp.toString().toLowerCase().charAt(0);
                    break;
                } catch (StringIndexOutOfBoundsException e) {
                    System.out.printf("%s, you know, what you just did?? You may drop the game!" +
                            " Thing about your behaviour and try once more!\n", inputS);
                }

            } while (true);
        }

    }

    private ArrayList<String> startView(ArrayList<Character> openKeys, ArrayList<Character> catchKeys,
                                        ArrayList<Double> angles) {
        ArrayList<String> names = new ArrayList<>();
        boolean flag = true;
        int numberOfPeople = 4;
        do {
            Character inputChar;
            inputChar = getNewChar("Use the default settings? (Y/N)", "No-No-NO, %username% ! Char means char, not empty string!");
            switch (inputChar.toString().toUpperCase().charAt(0)) { /* использовать параметры по умолчанию?*/
                case 'Y':           /*да*/
                    flag = false;
                    defaultSettings(names, openKeys, catchKeys, angles);
                    break;
                case 'N':               /*нет*/
                    flag = false;
                    notDefaultSettings(names);
                    break;
                default:
                    System.out.println("What are you doing?? Try once more!");
            }
        } while ((flag));

        /*показываем то, что получаем в итоге*/
        System.out.println("So, we have:");
        System.out.printf("Number of people: %d\n", numberOfPeople);
        return names;
    }

    /**
     * функция перерисовки после того, как сервер прислал информацию.
     * @param whoDid кто ходил
     * @param whatDid что сделал. Тут всё, кроме, возможно, последнего, должны быть захваты тотема
     * @param isSmbdCatch флаг отвечающий тому, был ли хотя бы один захват тотема
     * @param isSmbOpen флаг, отвечающий тому, была ли открыта новая карта
     */
    public void repaintView(Queue<Integer> whoDid, Queue<Game.WhatPlayerDid> whatDid,
                            boolean isSmbOpen,boolean isSmbdCatch) {
        myPanel.repaintModel(whoDid, whatDid, isSmbOpen, isSmbdCatch);
    }

    public GraphicsView() {
        isServer = Configuration.getIsServer();
        ArrayList<Character> openKeys = new ArrayList<>();
        ArrayList<Character> catchKeys = new ArrayList<>();
        ArrayList<Double> angles = new ArrayList<>();
        FRAME_SIZE = (Toolkit.getDefaultToolkit().getScreenSize().getHeight() > Toolkit.getDefaultToolkit().getScreenSize().getWidth()) ?
                (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth() : (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
        setPreferredSize(new Dimension(FRAME_SIZE, FRAME_SIZE));

        //если не хотим каждый раз выбирать по умолчанию - берём этот кусок. Если хотим - откоммичиваем что внизу
        ArrayList<String> names = new ArrayList<>();
        defaultSettings(names, openKeys, catchKeys, angles);
//        myGame = new Game(names, CardView.getCardsNumbers());
        client = isServer ? new MyClient(names, CardView.getCardsNumbers()) : new BasicClient(names, CardView.getCardsNumbers());
        System.out.println("Generate GUI");
        client.setGraphicsView(this);
        //myGame = new Game(startView(openKeys, catchKeys, angles), graphics.CardView.getCardsNumbers(gallery));
        myPanel = new MyPanel();
        myPanel.initiation(client, catchKeys, openKeys, angles);
        add(myPanel);
        pack();
        addKeyListener(myPanel.initMyKeyListener());
        addMouseListener(myPanel.initMyMouseListener());
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
    }
}