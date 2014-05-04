package model;

import view.GraphicsView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;

/**
 * Главный класс модели. В нём содержится список игроков, список всех карт и карт под тотемом
 * а так же функции выполняемые во время хода
 */
public class Game {
    /**
     * список игроков
     */
    private ArrayList<Player> players;
    /**
     * список всех карт
     */
    private LinkedList<Card> allCards;
    /**
     * ссылка на тотем
     */
    private Totem totem;
    /**
     * общее количество карт
     */
    private static int NUMBER_OF_CARDS;
    /**
     * номер хода
     */
    private int turnNumber;
    /**
     * игрок,который ожидается будет ходить следующим
     */
    private int playerWhoWillGo;

    /**
     * режим игры
     */
    private GameMode gameMode;
    /**
     *
     * @return ссылку на все карты
     */
    public LinkedList<Card> getAllCards() {
        return allCards;
    }

    /**
     * информация о тотеме
     */
    public class Totem {
        /**
         * карты под тотемом
         */
        private LinkedList<Card> cards;

        /**
         * По правилам лишние карты (которые остаются от деления всех карт поровну)
         * кладутся под тотем
         *
         * @param numberOfPlayers количество играющих людей
         */
        public Totem(int numberOfPlayers) {
            cards = new LinkedList<>(allCards.subList((NUMBER_OF_CARDS / numberOfPlayers) * numberOfPlayers,
                    NUMBER_OF_CARDS));
        }

        /**
         * отдаёт все карты из-под тотема.
         * @return список карт, которые под тотемом лежили
         */
        public LinkedList<Card> pickUpAllCards() {
            LinkedList<Card> result = new LinkedList<>(cards);
            cards.clear();
            return result;
        }

        /**
         * @return количество карт под тотемом
         */
        public int getCardsCount() {
            return cards.size();
        }
    }

    /**
     * @return ссылка на тотем
     */
    public Totem getTotem() {
        return totem;
    }

    /**
     * режим игры: обычный, по цветам, когда надо сейчас хватать тотем, когда надо открыть верхнюю карту
     */
    public enum GameMode {
        NORMAL_MODE,
        COLOR_MODE,
        CATCH_TOTEM_MODE,
        OPEN_CARD_MODE
    }

    /**
     * @return режим игры
     */
    public GameMode getGameMode() {
        return gameMode;
    }
    /*
     * Блок генерирования: создание карт, игроков, конструктор Игры
     */

    /**
     * Конструктор игры. Создаём список всех карт, список игроков, список карт под тотемом
     * говорим что пойдёт первый раунд
     *
     * @param playersNames имена играющих людей
     * @param cardNumbers номера карт
     */
    public Game(ArrayList<String> playersNames, ArrayList<Integer> cardNumbers) {
        turnNumber = 1;
        playerWhoWillGo = 0;
        generateAllCards(cardNumbers);
        int numberOfPlayers = playersNames.size();
        generatePlayers(playersNames);
        totem = new Totem(numberOfPlayers);
        gameMode = GameMode.NORMAL_MODE;
    }

    /**
     * коструктор, в котором мы задаём первого игрока и основание для перемешки карт
     * @param playersNames имена играющих людей
     * @param cardNumbers номера карт
     * @param firstPerson кто ходит первым
     * @param cardSeed основание для перемешки карт
     */
    public Game(ArrayList<String> playersNames, ArrayList<Integer> cardNumbers, int firstPerson, int cardSeed) {
        turnNumber = 1;
        playerWhoWillGo = firstPerson;
        generateAllCards(cardNumbers, cardSeed);
        int numberOfPlayers = playersNames.size();
        generatePlayers(playersNames);
        totem = new Totem(numberOfPlayers);
        gameMode = GameMode.NORMAL_MODE;
    }

    /**
     * генерируем случайный порядок для карт используя
     * @see java.util.Collections#shuffle(java.util.List)
     * @param names номера карт
     */
    void generateAllCards(ArrayList<Integer> names) {
        allCards = new LinkedList<>();
        NUMBER_OF_CARDS = utils.Configuration.getNumOfCards();
        for (int i = 0; i < NUMBER_OF_CARDS; i++) {
            allCards.add(new Card(names.get(i)));
        }
        Collections.shuffle(allCards);
    }

    /**
     * генерируем случайный порядок с фиксируемым основанием, используя
     * @see java.util.Collections#shuffle(java.util.List, java.util.Random)
     * @param names номера карт
     * @param cardSeed основание для перемешки карт
     */
    void generateAllCards(ArrayList<Integer> names, int cardSeed) {
        allCards = new LinkedList<>();
        NUMBER_OF_CARDS = utils.Configuration.getNumOfCards();
        for (int i = 0; i < NUMBER_OF_CARDS; i++) {
            allCards.add(new Card(names.get(i)));
        }
        Collections.shuffle(allCards, new Random(cardSeed));
    }

    /**
     * создаёт список игроков.
     * Поскольку allCards уже в случайном порядке, можно просто разбить их на одинаковые блоки
     * по количеству людей
     * и записать в закрытые карты
     *
     * @param playersNames имена людей, которые будут играть
     */
    void generatePlayers(ArrayList<String> playersNames) {
        int numberOfPlayers = playersNames.size();
        players = new ArrayList<>(numberOfPlayers);
        int numberOfCardsForPlayers = NUMBER_OF_CARDS / numberOfPlayers;
        for (int i = 0; i < numberOfPlayers; i++) {
            players.add(new Player(playersNames.get(i), i));
            players.get(i).setCardsToPlayer(new LinkedList<>(allCards.subList(i * numberOfCardsForPlayers,
                    (i + 1) * numberOfCardsForPlayers)));
        }
    }


    /**
     * Конец блока генерирования
     */

    /**
     *
     * @return номер хода
     */
    public int getTurnNumber() {
        return turnNumber;
    }

    /**
     *
     * @return номер человека, чей будет ход
     */
    public int getPlayerWhoWillGo() {
        return playerWhoWillGo;
    }

    /**
     * увеличивает по модулю номер игрока, чей будет ход
     * @return номер человека, чей будет ход увеличенный
     */
    private int incPlayerThatWillGo() {
        playerWhoWillGo = (playerWhoWillGo + 1) % getPlayersCount();
        return playerWhoWillGo;
    }

    /**
     * проверяет, кончилась ли игра. А именно есть ли игрок без карт
     * @return флаг конца игры
     */
    public boolean isGameEnded() {
        for (Player player : players)
            if (player.getCardsCount() == 0) {
                return true;
            }
        return false;
    }


    /**
     * проверяем наличие дуэли с человеком. потребность проверять возникает лишь с человеком
     * который схватил тотем
     * @param playerTookTotem номер игрока, с которым проверяем
     * @return список номеров игроков, с которыми была дуэль у данного
     */
    public ArrayList<Integer> checkDuelWithPlayer(Player playerTookTotem) {
        if (playerTookTotem.getOpenCardsCount() == 0) {
            return null;
        }
        ArrayList<Integer> result = new ArrayList<>();
        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            if ((player != playerTookTotem) && (player.getOpenCardsCount() > 0)) {
                if (gameMode == GameMode.COLOR_MODE) {
                    if (player.getTopOpenedCard().getCardColor() == playerTookTotem.getTopOpenedCard().getCardColor()) {
                        result.add(i);
                    }
                } else {
                    if (player.getTopOpenedCard().getCardFormNumber() == playerTookTotem.getTopOpenedCard().getCardFormNumber()) {
                        result.add(i);
                    }
                }
            }
        }
        if (result.size() == 0) {
            return null;
        }
        return result;
    }

    /**
     * ошибшийся человек забирает все открытые карты
     * @param looser игрок, который берёт все открытые карты и карты под тотемом
     */
    private void takeAllCardsOnTheTable(Player looser) {
        looser.setCardsToPlayer(totem.pickUpAllCards());
        for (Player player : players) {
            looser.setCardsToPlayer(player.pickUpAllOpenedCards());
        }
    }

    /**
     * варианты результатов хода
     */
    public enum ResultOfMakeMove {
        INCORRECT,
        TOTEM_WAS_CATCH_CORRECT,
        TOTEM_WAS_CATCH_INCORRECT,
        CARD_OPENED,
        NOT_DEFINED_CATCH,
        ALL_CARDS_OPENED
    }

    /**
     * что сделал человек: открыл карту или схватил тотем
     */
    public enum WhatPlayerDid {
        TOOK_TOTEM,
        OPEN_NEW_CARD;
        public int whoWasIt;
    }

    /**
     * функция хода.
     *
     * @param whatPlayerDid что сделал походивший
     * @return результат хода
     */
    public ResultOfMakeMove makeMove(WhatPlayerDid whatPlayerDid) {
        int playerIndex = whatPlayerDid.whoWasIt;
        switch (whatPlayerDid) {
            case TOOK_TOTEM:
                ArrayList<Integer> result = checkDuelWithPlayer(players.get(playerIndex));
                if (gameMode == GameMode.CATCH_TOTEM_MODE) {
                    if (result == null) {
                        arrowsInMakeMove(playerIndex);
                        gameMode = GameMode.NORMAL_MODE;
                        return ResultOfMakeMove.TOTEM_WAS_CATCH_CORRECT;
                    } else {
                        return ResultOfMakeMove.NOT_DEFINED_CATCH;
                    }
                } else {
                    if (result == null) {
                        takeAllCardsOnTheTable(players.get(playerIndex));
                        gameMode = GameMode.NORMAL_MODE;
                        return ResultOfMakeMove.TOTEM_WAS_CATCH_INCORRECT;
                    } else if (result.size() == 1) {
                        afterDuelMakeMove(playerIndex, result.get(0));
                        gameMode = GameMode.NORMAL_MODE;
                        return ResultOfMakeMove.TOTEM_WAS_CATCH_CORRECT;
                    } else {
                        return ResultOfMakeMove.NOT_DEFINED_CATCH;
                    }
                }
            case OPEN_NEW_CARD:
                if (playerIndex == playerWhoWillGo) {
                    turnNumber++;

                    if ((players.get(playerWhoWillGo).getOpenCardsCount() > 0) &&
                            (players.get(playerWhoWillGo).getTopOpenedCard().getCardType() == Card.CardType.ARROWS_COLORED)) {
                        gameMode = GameMode.NORMAL_MODE;
                    }
                    switch (players.get(playerWhoWillGo).openNextCard().getCardType()) {
                        case ARROWS_COLORED:
                            gameMode = GameMode.COLOR_MODE;
                            break;
                        case ARROWS_IN:
                            gameMode = GameMode.CATCH_TOTEM_MODE;
                            break;
                        case ARROWS_OUT:
                            gameMode = GameMode.OPEN_CARD_MODE;
                            return ResultOfMakeMove.ALL_CARDS_OPENED;
                    }
                    int flagOfEnd = playerWhoWillGo; //На случай, если вдруг у всех кончались закрытые карты
                    do {
                        incPlayerThatWillGo();
                        if (flagOfEnd == playerWhoWillGo) {
                            return ResultOfMakeMove.CARD_OPENED;
                        }
                    } while (players.get(playerWhoWillGo).getCloseCardsCount() == 0);
                } else {
                    return ResultOfMakeMove.INCORRECT;
                }
        }
        return ResultOfMakeMove.CARD_OPENED;
    }

    /**
     * ход после спец. карты "стрелки во внутрь"
     *
     * @param winner кто схватил тотем
     */
    public void arrowsInMakeMove(int winner) {
        totem.cards.addAll(players.get(winner).pickUpAllOpenedCards());
        gameMode = GameMode.NORMAL_MODE;
    }


    /**
     * Если была неразрешимость, (в виде дуэли в несколько игроков) дохаживает после её устранения
     *
     * @param winner кто выиграл дуэль
     * @param looser кого выигрывший посчитал проигравшим
     */
    public void afterDuelMakeMove(int winner, int looser) {
        players.get(looser).setCardsToPlayer(totem.pickUpAllCards());
        players.get(looser).setCardsToPlayer(players.get(winner).pickUpAllOpenedCards());
        players.get(looser).setCardsToPlayer(players.get(looser).pickUpAllOpenedCards());
        playerWhoWillGo = looser;
        gameMode = GameMode.NORMAL_MODE; //после дуэли карта "цветные стрелки" перестаёт действовать

    }

    /**
     * ход после спец. карты "стрелки наружу". Все открывают верхнюю
     */
    public void openAllTopCards() {
        for (Player player : players) {
            switch (player.openNextCard().getCardType()) {
                case ARROWS_COLORED:
                    gameMode = GameMode.COLOR_MODE;
                case ARROWS_IN:
                    gameMode = GameMode.CATCH_TOTEM_MODE;
                    break;
            }
        }
    }

    /**
     * считает количество игроков
     *
     * @return количество игроков
     */
    public int getPlayersCount() {
        return players.size();
    }

    /**
     * возвращает ссылку на конкретного игрока
     *
     * @param playerIndex номер игрока
     * @return ссылка на него
     */
    public Player getPlayer(int playerIndex) {
        return players.get(playerIndex);
    }
}
