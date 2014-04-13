package controller;

import model.Card;
import model.Game;
import model.Player;
import view.GraphicsView;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created with IntelliJ IDEA.
 * User: lavton
 * Date: 08.12.13
 * Time: 17:51
 * To change this template use File | Settings | File Templates.
 * <p/>
 * интерфейс контроллера - связь между View и Model
 */
public interface TotemClient {

    /**
     * считает количество игроков
     *
     * @return количество игроков
     */
    public int getPlayersCount();

    /**
     * возвращает ссылку на конкретного игрока
     *
     * @param i номер игрока
     * @return ссылка на него
     */
    public Player getPlayer(int i);

    /**
     * возвращает ссылку на тотем
     *
     * @return ссылка на тотем
     */
    public Game.Totem getTotem();

    /**
     * предпологаемый следующий игрок
     *
     * @return номер следующего игрока
     */
    public int getPlayerWhoWillGo();

    /**
     * номер хода
     *
     * @return номер хода
     */
    public int getTurnNumber();

    /**
     * проверка на то, что игра уже закончилась
     *
     * @return флаг конца игры
     */
    public boolean isGameEnded();

    /**
     * функция хода. На данный момент не используется, будет слита в другую
     *
     * @param playerIndex   кто походил
     * @param whatPlayerDid что сделал походивший
     * @return результат хода
     * @see controller.TotemClient#moveWithoutAnswer(int, model.Game.WhatPlayerDid)
     */
    public Game.ResultOfMakeMove makeMove(int playerIndex, Game.WhatPlayerDid whatPlayerDid);

    /**
     * Проверяет, возникла ли дуэль с кем-либо. Используется в случае неоднозначного хода
     *
     * @param playerTookTotem игрок, схвативший тотем - потенциальный дуэлянт
     * @return список номеров игроков, с которыми возникла дуэль
     */
    public ArrayList<Integer> checkDuelWithPlayer(Player playerTookTotem);

    /**
     * @return модификатор игры под действием спец. карты
     */
    public Game.GameMode getGameMode();

    /**
     * Если была неразрешимость, (в виде дуэли в несколько игроков) дохаживает после её устранения
     *
     * @param winner кто выиграл дуэль
     * @param looser кого выигрывший посчитал проигравшим
     */
    public void afterDuelMakeMove(int winner, int looser);

    /**
     * ход после спец. карты "стрелки во внутрь"
     *
     * @param winner кто схватил тотем
     */
    public void arrowsInMakeMove(int winner);

    /**
     * ход после спец. карты "стрелки наружу". Все открывают верхнюю
     */
    public void openAllTopCards();

    /**
     * возвращает список из ссылок на все карты в игре
     *
     * @return список из ссылок на все карты в игре
     */
    public LinkedList<Card> getAllCards();

    /**
     * функция будет слита с
     *
     * @param playerIndex   кто походил
     * @param whatPlayerDid что сделал походивший
     * @see controller.TotemClient#makeMove(int, model.Game.WhatPlayerDid)
     */
    public void moveWithoutAnswer(int playerIndex, Game.WhatPlayerDid whatPlayerDid);

    /**
     * ссылка на View, нужна будет для передачи результатов с сервера
     *
     * @param view ссылка на вид
     */
    public void setGraphicsView(GraphicsView view);

    /**
     * возвращает кому принадлежит этот контроллер. фиктивно для игры на одном компе
     * @return кому принадлежит контроллер
     */
    public int getWhatPlayer();
//    public controller.TotemClient(ArrayList<String> playersNames, ArrayList<Integer> cardNumbers);
    //  public controller.TotemClient(ArrayList<String> playersNames, ArrayList<Integer> cardNumbers, int firstPerson, int cardSeed);


}
