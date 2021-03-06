package view;

import controller.MyClient;
import controller.TotemClient;
import model.Game;
import utils.Configuration;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Класс, отвечающий за работу с игроками
 */
public class View {
    //    private Game myGame;
    private TotemClient client;
    /**
     * во время запуска инициализируем playersView, потом game а потом уже запускаем саму игру
     */

    /**
     * Поля, которые нужно знать graphics.View о игроках
     */
    private class PlayerView {
        public char openCardKey;
        public char catchTotemKey;
        public String playerViewName; //что делать с дублированием имени в graphics.PlayerView и Player?

        public PlayerView(char newOpenCardKey, char newCatchTotemKey, String name) {
            openCardKey = newOpenCardKey;
            catchTotemKey = newCatchTotemKey;
            playerViewName = name;
        }
    }

    ArrayList<PlayerView> playersView;

    private class CardsView {
        //        private ArrayList <File> cards;
        private ArrayList<String> cards;

        public CardsView() {
            cards = Configuration.getGallery().getCardsNames();
        }

        public ArrayList<Integer> getCardsNumbers() {
            ArrayList<Integer> rezult = new ArrayList<>();
            for (String card : cards) {
                rezult.add(getCardNumber(card));
            }
            return rezult;
        }

        private int getCardNumber(String str) {
//        System.err.println(str);
            Pattern numberPattern = Pattern.compile("[0-9]+");
            Matcher numberMatcher = numberPattern.matcher(str);
            numberMatcher.find();
            return Integer.parseInt(numberMatcher.group());
        }
    }

    private CardsView cardsView;

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

    private void defaultSettings(ArrayList<String> names) {
        for (int i = 0; i < Configuration.getNumberOfPlayers(); i++) {
            names.add(Configuration.getPeopleNames().get(i));
            playersView.add(new PlayerView(Configuration.getPeopleOpenKeys().get(i),
                    Configuration.getPeopleCatchKeys().get(i),
                    Configuration.getPeopleNames().get(i)));
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
        playersView.ensureCapacity(numberOfPeople);
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
                    for (PlayerView p : playersView) {
                        if ((inputOpenCardKey == p.catchTotemKey) || (inputOpenCardKey == p.openCardKey)) {
                            System.out.printf("player %s already use this key. Try another one\n", p.playerViewName);
                            continue label;
                        }
                    }
                    break;
                } catch (StringIndexOutOfBoundsException e) {
                    System.out.printf("%s, you know, what you just did?? You may drop the game!" +
                            " Thing about your behaviour and try once more!\n", inputS);
                }

            } while (true);
            label:
            do {
                try {
                    System.out.printf("%s: insert key to catch totem\n", inputS);
                    inputString = scan.nextLine();
                    inputCatchTotemKey = inputString.charAt(0);
                    Character tmp = inputCatchTotemKey;
                    inputCatchTotemKey = tmp.toString().toLowerCase().charAt(0);
                    if (inputCatchTotemKey == inputOpenCardKey) {
                        System.out.println("you already use this key for key that open last card! try another key");
                        continue;
                    }
                    for (PlayerView p : playersView) {
                        if ((inputCatchTotemKey == p.catchTotemKey) || (inputCatchTotemKey == p.openCardKey)) {
                            System.out.printf("player %s already use this key. Try another one\n", p.playerViewName);
                            continue label;
                        }
                    }
                    break;
                } catch (StringIndexOutOfBoundsException e) {
                    System.out.printf("%s, you know, what you just did?? You may drop the game!" +
                            " Thing about your behaviour and try once more!\n", inputS);
                }

            } while (true);
            playersView.add(new PlayerView(inputOpenCardKey, inputCatchTotemKey, inputS));
        }

    }

    private ArrayList<String> startView() {
        ArrayList<String> rezultStrings = new ArrayList<>();
        boolean flag = true;
        int numberOfPeople = 4;
        do {
            Character inputChar;
            playersView = new ArrayList<>();
            inputChar = getNewChar("Use the default settings? (Y/N)", "No-No-NO, %username% ! Char means char, not empty string!");
            switch (inputChar.toString().toUpperCase().charAt(0)) { /* использовать параметры по умолчанию?*/
                case 'Y':           /*да*/
                    flag = false;
                    defaultSettings(rezultStrings);
                    break;
                case 'N':               /*нет*/
                    flag = false;
                    notDefaultSettings(rezultStrings);
                    break;
                default:
                    System.out.println("What are you doing?? Try once more!");
            }
        } while ((flag));

        /*показываем то, что получаем в итоге*/
        System.out.println("So, we have:");
        System.out.printf("Number of people: %d\n", numberOfPeople);
        for (PlayerView p : playersView) {
            System.out.printf("Player %s has '%c' as key to open last card and '%c' as key to catch totem\n",
                    p.playerViewName, p.openCardKey, p.catchTotemKey);
        }
        return rezultStrings;
    }

    void printInformationAboutRound() {
        System.out.printf("name:            ");
        for (int i = 0; i < client.getPlayersCount(); i++) {
            System.out.printf("%15s", client.getPlayer(i).getName());
        }
        System.out.printf("    under totem: \nall cards:     ");
        for (int i = 0; i < client.getPlayersCount(); i++) {
            System.out.printf("%15s", client.getPlayer(i).getCardsCount());
        }

        System.out.printf("%15d\nclose cards    ", client.getTotem().getCardsCount());
        for (int i = 0; i < client.getPlayersCount(); i++) {
            System.out.printf("%15s", client.getPlayer(i).getCloseCardsCount());
        }
        System.out.printf("\nopen cards:    ");
        for (int i = 0; i < client.getPlayersCount(); i++) {
            System.out.printf("%15s", client.getPlayer(i).getOpenCardsCount());
        }

        System.out.printf("\nlast open card:");
        for (int i = 0; i < client.getPlayersCount(); i++) {
            if (client.getPlayer(i).getOpenCardsCount() > 0) {
                System.out.printf("%15s", client.getPlayer(i).getTopOpenedCard());
            } else {
                System.out.printf("              -");

            }
        }

        System.out.printf("\n\n");

        System.out.printf("Round %d, it's %s's turn\n", client.getTurnNumber(),
                client.getPlayer(client.getPlayerWhoWillGo()).getName());

    }

    int chooseOneOfPlayers(ArrayList<Integer> playersIndex) {
        int looser;
        Scanner scan = new Scanner(System.in);
        do {
            try {
                System.out.println("Please, choose a player from this list");
                System.out.printf("name: ");
                for (Integer i : playersIndex) {
                    System.out.printf("%15s", client.getPlayer(i).getName());
                }
                System.out.printf("\n");
                System.out.printf("type: ");
                for (int i = 0; i < playersIndex.size(); i++) {
                    System.out.printf("%15d", i);
                }
                System.out.printf("\n");
                looser = scan.nextInt();
                scan.nextLine();
            } catch (InputMismatchException | StringIndexOutOfBoundsException e) {
                System.out.println("Integer must be integer, you, clever user! try again\n");
                scan.nextLine();
                continue;
            }
            if ((looser < 0) || (looser >= playersIndex.size())) {
                System.out.printf("%d isn't correct number of player! try again\n", looser);
            } else {
                break;
            }
        } while (true);
        return looser;

    }

    public void run() {
        while (!(client.isGameEnded())) {
            String inputString;
            Scanner scan = new Scanner(System.in);
            char inputChar;
            printInformationAboutRound();
            System.out.printf("insert key:\n");
            try {
                inputString = scan.nextLine();
                inputChar = inputString.charAt(0);
                inputChar = (new Character(inputChar)).toString().toLowerCase().charAt(0);
            } catch (StringIndexOutOfBoundsException e) {
                System.out.printf("You can't use empty string!\n");
                continue;
            }
            boolean suchKeyHere = false;
            Game.ResultOfMakeMove resultOfMakeMove = Game.ResultOfMakeMove.INCORRECT;
            int whoPlayed = 0;
            for (int i = 0; i < client.getPlayersCount(); i++) {
                if (playersView.get(i).openCardKey == inputChar) {
                    Game.WhatPlayerDid what = Game.WhatPlayerDid.OPEN_NEW_CARD;
                    what.whoWasIt = i;
                    resultOfMakeMove = client.makeMove(what);
                    suchKeyHere = true;
                    whoPlayed = i;
                    break;
                }
                if (playersView.get(i).catchTotemKey == inputChar) {
                    Game.WhatPlayerDid what = Game.WhatPlayerDid.TOOK_TOTEM;
                    what.whoWasIt = i;
                    resultOfMakeMove = client.makeMove(Game.WhatPlayerDid.TOOK_TOTEM);
                    suchKeyHere = true;
                    whoPlayed = i;
                    break;
                }
            }
            if (!(suchKeyHere)) {
                System.out.println("Nobody have such key. Try again.\n");
                continue;
            }
            switch (resultOfMakeMove) {
                case INCORRECT:
                    System.out.printf("It's not your turn, %s. Don't hurry!\n",
                            client.getPlayer(whoPlayed).getName());
                    break;
                case TOTEM_WAS_CATCH_CORRECT:
                    System.out.printf("You won duel, %s! All your open cards and all cards under totem go to your opponent\n",
                            client.getPlayer(whoPlayed).getName());
                    break;
                case TOTEM_WAS_CATCH_INCORRECT:
                    System.out.printf("You mustn't take totem, %s! So you took all open cards!\n",
                            client.getPlayer(whoPlayed).getName());
                    break;
                case CARD_OPENED:

                    System.out.printf("%s open next card\n",
                            client.getPlayer(whoPlayed).getName());
                    break;
                case NOT_DEFINED_CATCH:
                    ArrayList<Integer> possibleLosers = client.checkDuelWithPlayer(client.getPlayer(whoPlayed));
                    if (client.getGameMode() == Game.GameMode.CATCH_TOTEM_MODE) {
                        label:
                        do {
                            inputChar = getNewChar("You catch totem while there were a duel with you AND card 'arrows in'. Do you" +
                                    "want to use effect of won duel or of card? type (D/C)", "Try again!");
                            inputChar = (new Character(inputChar)).toString().toLowerCase().charAt(0);
                            switch (inputChar) {
                                case 'd':
                                    if (possibleLosers.size() == 1) {
                                        System.out.printf("All cards go to your opponent, %s\n", client.getPlayer(possibleLosers.get(0)));
                                        client.afterDuelMakeMove(whoPlayed, possibleLosers.get(0));
                                    }
                                    break label;
                                case 'c':
                                    System.out.println("You put all cards under totem");
                                    client.arrowsInMakeMove(whoPlayed);
                                    break label;
                                default:
                                    System.out.println("try again");
                            }
                        } while (true);
                    }
                    int looser = chooseOneOfPlayers(possibleLosers);
                    client.afterDuelMakeMove(whoPlayed, looser);
                    System.out.printf("All cards go to your opponent, %s\n", client.getPlayer(looser).getName());
                    break;
                case ALL_CARDS_OPENED:
                    System.out.println("All players will open top cards. To do this, press Enter");
                    scan.nextLine();
                    client.openAllTopCards();
                default:
            }

//            }while (!(myGame.isRoundEnded));
        }
        for (int i = 0; i < client.getPlayersCount(); i++) {
            if (client.getPlayer(i).getCardsCount() == 0) {
                System.out.printf("Player %s won! It's very good :)\n", client.getPlayer(i).getName());
            }
        }
    }

    public View() {
        cardsView = new CardsView();
        client = new MyClient(startView(), cardsView.getCardsNumbers());
    }
}