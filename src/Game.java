import java.util.*;

/**
 * Главный класс модели. В нём содержится список игроков, список всех карт и карт под тотемом
 * а так же функции выполняемые во время хода
 */
class Game{
    private ArrayList<Player> players;
    private LinkedList<Card> allCards;
    public class Totem{
        private LinkedList<Card> cards;
            /**
         * По правилам лишние карты (которые остаются от деления всех карт поровну)
         * кладутся под тотем
         * @param numberOfPlayers количество играющих людей
         */
        Totem (int numberOfPlayers){
            cards = new LinkedList<>(allCards.subList( (NUMBER_OF_CARDS / numberOfPlayers) * numberOfPlayers,
                    NUMBER_OF_CARDS));
        }
        public LinkedList<Card> pickUpAllCards(){
            LinkedList<Card> result = new LinkedList<>(cards);
            cards.clear();
            return result;
        }
        public int getCardsCount(){
            return cards.size();
        }


    }
    Totem totem;
    public static int NUMBER_OF_CARDS;
    private int turnNumber;
    private int playerWhoWillGo;
    public enum GameMode{
        NORMAL_MODE,
        COLOR_MODE,
        CATCH_TOTEM_MODE,
        OPEN_CARD_MODE
    }
    private GameMode gameMode;
    public GameMode getGameMode(){
        return gameMode;
    }
    /*
     * Блок генерирования: создание карт, игроков, конструктор Игры
     */
    /**
     * Конструктор игры. Создаём список всех карт, список игроков, список карт под тотемом
     * говорим что пойдёт первый раунд
     * @param playersNames имена играющих людей
     */
    public Game(ArrayList<String> playersNames, ArrayList<Integer> names){
        turnNumber = 1;
        playerWhoWillGo = 0;
        generateAllCards(names);
        int numberOfPlayers = playersNames.size();
        generatePlayers(playersNames);
        totem = new Totem(numberOfPlayers);
        gameMode = GameMode.NORMAL_MODE;
    }

    /**
     * генерирование карт произходит следующим образом:
     * сначала создаётся последовательность в прямом порядке с картами
     * Потом генерируется массив такой же длины и заполняется случайными числами
     * Он сортируется.
     * Карты переставляются в таком же порядке, в котором сортируется созданный случайным массив
     */
    void generateAllCards(ArrayList<Integer> names){
        allCards = new LinkedList<>();
        NUMBER_OF_CARDS = names.size();
        for (int i = 0; i < NUMBER_OF_CARDS; i++){
            allCards.add(new Card(names.get(i)));
        }
        Collections.shuffle(allCards);
    }

    /**
     * создаёт список игроков. Имена пока не задаются.
     * Поскольку allCards уже в случайном порядке, можно просто разбить их на одинаковые блоки
     *                                              по количеству людей
     *  и записать в закрытые карты
     *  @param playersNames имена людей, которые будут играть
     */
    void generatePlayers(ArrayList<String> playersNames){
        int numberOfPlayers = playersNames.size();
        players = new ArrayList<>(numberOfPlayers);
        int numberOfCardsForPlayers = NUMBER_OF_CARDS / numberOfPlayers;
        for (int i = 0; i < numberOfPlayers; i++){
            players.add(new Player(playersNames.get(i)));
            players.get(i).setCardsToPlayer(new LinkedList<>(allCards.subList(i * numberOfCardsForPlayers,
                    (i + 1) * numberOfCardsForPlayers)));
        }
    }



    /**
     * Конец блока генерирования
     */

    public int getTurnNumber(){
        return turnNumber;
    }

    public int getPlayerWhoWillGo(){
        return playerWhoWillGo;
    }
    private int incPlayerThatWillGo(){
        playerWhoWillGo = (playerWhoWillGo +1) % getPlayersCount();
        return playerWhoWillGo;
    }
    

    public boolean isGameEnded()
    {
        for (Player player : players)
            if (player.getCardsCount() == 0){
                return true;
            }
        return false;
    }
    

    /**
     *
     * @param playerTookTotem ищем лишь с тем человеком, который стащил тотем
     * @return номер игорока, с которым дуэль. -1, если таких игроков нет.
     * НЕ ПОНЯТНО, что делать, если совпадений больше чем два?
     */
    public ArrayList<Integer> checkDuelWithPlayer(Player playerTookTotem){
        if (playerTookTotem.getOpenCardsCount() ==0){
            return null;
        }
        ArrayList <Integer> result = new ArrayList<>();
        for (int i = 0; i < players.size(); i++){
            Player player = players.get(i);
            if ((player != playerTookTotem)&&(player.getOpenCardsCount() > 0)){
                if (gameMode == GameMode.COLOR_MODE){
                    if (player.getTopOpenedCard().getCardColor() == playerTookTotem.getTopOpenedCard().getCardColor()){
                        result.add(i);
                    }
                }else{
                    if (player.getTopOpenedCard().getCardFormNumber() == playerTookTotem.getTopOpenedCard().getCardFormNumber()){
                        result.add(i);
                    }
                }
            }
        }
        if (result.size() ==0){
            return null;
        }
        return result;
    }

    /**
     *
     * @param looser игрок, который берёт все открытые карты и карты под тотемом
     */
    private void takeAllCardsOnTheTable(Player looser){
        looser.setCardsToPlayer(totem.pickUpAllCards());
        for (Player player : players){
            looser.setCardsToPlayer(player.pickUpAllOpenedCards());
        }
    }
    public enum ResultOfMakeMove{
        INCORRECT,
        TOTEM_WAS_CATCH_CORRECT,
        TOTEM_WAS_CATCH_INCORRECT,
        CARD_OPENED,
        NOT_DEFINED_CATCH,
        ALL_CARDS_OPENED
    }
    public enum WhatPlayerDid{
        TOOK_TOTEM,
        OPEN_NEW_CARD
    }
    public ResultOfMakeMove makeMove(int playerIndex, WhatPlayerDid whatPlayerDid){
        switch (whatPlayerDid){
            case TOOK_TOTEM:
                ArrayList<Integer> result = checkDuelWithPlayer(players.get(playerIndex));
                if (gameMode == GameMode.CATCH_TOTEM_MODE){
                    if (result == null){
                        arrowsInMakeMove(playerIndex);
//                        takeAllCardsOnTheTable(players.get(playerIndex));
                        return ResultOfMakeMove.TOTEM_WAS_CATCH_CORRECT;
                    }else{
                        return ResultOfMakeMove.NOT_DEFINED_CATCH;
                    }
                }else{
                    if (result == null){
                        takeAllCardsOnTheTable(players.get(playerIndex));
                        return ResultOfMakeMove.TOTEM_WAS_CATCH_INCORRECT;
                    }else if (result.size() == 1){
                        afterDuelMakeMove(playerIndex,result.get(0));
                        return ResultOfMakeMove.TOTEM_WAS_CATCH_CORRECT;
                    }else{
                        return ResultOfMakeMove.NOT_DEFINED_CATCH;
                    }
                }
            case OPEN_NEW_CARD:
                if (playerIndex == playerWhoWillGo){
                    turnNumber++;
                    switch (players.get(playerWhoWillGo).openNextCard().getCardType()){
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
                    do{
                        incPlayerThatWillGo();
                        if (flagOfEnd == playerWhoWillGo){
                            return ResultOfMakeMove.CARD_OPENED;
                        }
                    }while(players.get(playerWhoWillGo).getCloseCardsCount() == 0);
                }else{
                    return ResultOfMakeMove.INCORRECT;
                }
        }
        return ResultOfMakeMove.CARD_OPENED;
    }
    public void arrowsInMakeMove(int winner){
        totem.cards.addAll(players.get(winner).pickUpAllOpenedCards());
        gameMode = GameMode.NORMAL_MODE;
    }
    public void afterDuelMakeMove(int winner, int looser){
        players.get(looser).setCardsToPlayer(totem.pickUpAllCards());
        players.get(looser).setCardsToPlayer(players.get(winner).pickUpAllOpenedCards());
        players.get(looser).setCardsToPlayer(players.get(looser).pickUpAllOpenedCards());
        playerWhoWillGo = looser;
        gameMode = GameMode.NORMAL_MODE; //после дуэли карта "цветные стрелки" перестаёт действовать

    }
    public void openAllTopCards(){
        for (Player player : players){
            switch (player.openNextCard().getCardType()){
                case ARROWS_COLORED:
                    gameMode = GameMode.COLOR_MODE;
                    break;
                case ARROWS_IN:
                    gameMode = GameMode.CATCH_TOTEM_MODE;
                    break;
                case ARROWS_OUT:
                    gameMode = GameMode.OPEN_CARD_MODE;
            }
        }
    }
    public int getPlayersCount(){
        return players.size();
    }
    
    public Player getPlayer(int playerIndex){
        return players.get(playerIndex);
    }
}
