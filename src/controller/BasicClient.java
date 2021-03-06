package controller;

import model.Card;
import model.Game;
import model.Player;
import view.GraphicsView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/**
 * простая имплиминтация контроллера (для одного компа)
 * все функции напрямую вызывают соотв. функцию у модели
 */
public class BasicClient implements TotemClient {
    /**
     * ссылка на модель
     */
    private Game myGame;
    private GraphicsView graphicsView;
    /**
     * считает количество игроков
     *
     * @return количество игроков
     */
    @Override
    public int getPlayersCount() {
        return myGame.getPlayersCount();
    }

    /**
     * возвращает ссылку на конкретного игрока
     *
     * @param i номер игрока
     * @return ссылка на него
     */
    @Override
    public Player getPlayer(int i) {
        return myGame.getPlayer(i);
    }

    /**
     * возвращает ссылку на тотем
     *
     * @return ссылка на тотем
     */
    @Override
    public Game.Totem getTotem() {
        return myGame.getTotem();
    }

    /**
     * предпологаемый следующий игрок
     *
     * @return номер следующего игрока
     */
    @Override
    public int getPlayerWhoWillGo() {
        return myGame.getPlayerWhoWillGo();
    }

    /**
     * номер хода
     *
     * @return номер хода
     */
    @Override
    public int getTurnNumber() {
        return myGame.getTurnNumber();
    }

    /**
     * проверка на то, что игра уже закончилась
     *
     * @return флаг конца игры
     */
    @Override
    public boolean isGameEnded() {
        return myGame.isGameEnded();
    }

    /**
     * функция хода. Используется для непосредственного занесение данных в модель
     *
     * @param whatPlayerDid что сделал походивший
     * @return результат хода
     * @see controller.TotemClient#moveWithoutAnswer(model.Game.WhatPlayerDid)
     */
    @Override
    public Game.ResultOfMakeMove makeMove(Game.WhatPlayerDid whatPlayerDid) {
        return myGame.makeMove(whatPlayerDid);
    }

    /**
     * Проверяет, возникла ли дуэль с кем-либо. Используется в случае неоднозначного хода
     *
     * @param playerTookTotem игрок, схвативший тотем - потенциальный дуэлянт
     * @return список номеров игроков, с которыми возникла дуэль
     */
    @Override
    public ArrayList<Integer> checkDuelWithPlayer(Player playerTookTotem) {
        return myGame.checkDuelWithPlayer(playerTookTotem);
    }

    /**
     * @return модификатор игры под действием спец. карты
     */
    @Override
    public Game.GameMode getGameMode() {
        return myGame.getGameMode();
    }

    /**
     * Если была неразрешимость, (в виде дуэли в несколько игроков) дохаживает после её устранения
     *
     * @param winner кто выиграл дуэль
     * @param looser кого выигрывший посчитал проигравшим
     */
    @Override
    public void afterDuelMakeMove(int winner, int looser) {
        myGame.afterDuelMakeMove(winner, looser);
    }

    /**
     * ход после спец. карты "стрелки во внутрь"
     *
     * @param winner кто схватил тотем
     */
    @Override
    public void arrowsInMakeMove(int winner) {
        myGame.arrowsInMakeMove(winner);
    }

    /**
     * ход после спец. карты "стрелки наружу". Все открывают верхнюю
     */
    @Override
    public void openAllTopCards() {
        myGame.openAllTopCards();
    }

    /**
     * возвращает список из ссылок на все карты в игре
     *
     * @return
     */
    @Override
    public LinkedList<Card> getAllCards() {
        return myGame.getAllCards();
    }

    /**
     *
     * @param whatPlayerDid что сделал походивший
     * @see controller.TotemClient#makeMove(model.Game.WhatPlayerDid)
     */
    @Override
    public void moveWithoutAnswer(Game.WhatPlayerDid whatPlayerDid) {
        Queue <Game.WhatPlayerDid> what = new LinkedList<>();
        what.add(whatPlayerDid);
        boolean hasOpenCard = (whatPlayerDid == Game.WhatPlayerDid.OPEN_NEW_CARD) ? true : false;
        graphicsView.repaintView(what, hasOpenCard, !hasOpenCard);
    }

    /**
     * передаёт модели ссылку на вид. Временное решение. будет добавлен листенер...
     *
     * @param view ссылка на вид
     */
    @Override
    public void setGraphicsView(GraphicsView view) {
        graphicsView = view;
        //myGame.setGraphicsView(view);
    }

    /**
     * возвращает кому принадлежит этот контроллер. фиктивно.
     *
     * @return кому принадлежит контроллер
     */
    @Override
    public int getWhatPlayer() {
        return 0;
    }

    /**
     * конструктор, в который передаётся имена и номера карт в порядке получения
     *
     * @param playersNames имена игроков
     * @param cardNumbers  номера карт
     */
    public BasicClient(ArrayList<String> playersNames, ArrayList<Integer> cardNumbers) {
        myGame = new Game(playersNames, cardNumbers);
    }

    /**
     * конструктор, которому ещё передаётся кто ходит и Card seed
     *
     * @param playersNames имена игроков
     * @param cardNumbers  номера карт
     * @param firstPerson  кто будет ходить первым
     * @param cardSeed     основание функции random
     */
    public BasicClient(ArrayList<String> playersNames, ArrayList<Integer> cardNumbers, int firstPerson, int cardSeed) {
        myGame = new Game(playersNames, cardNumbers, firstPerson, cardSeed);
    }

}
