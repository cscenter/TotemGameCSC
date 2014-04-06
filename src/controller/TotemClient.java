package controller;

import java.util.ArrayList;
import java.util.LinkedList;

import model.*;
import view.GraphicsView;

/**
 * Created with IntelliJ IDEA.
 * User: lavton
 * Date: 08.12.13
 * Time: 17:51
 * To change this template use File | Settings | File Templates.
 */
public interface TotemClient {
    public int getPlayersCount();
    public Player getPlayer(int i);
    public Game.Totem getTotem();
    public int getPlayerWhoWillGo();
    public int getTurnNumber();
    public boolean isGameEnded();
    public Game.ResultOfMakeMove makeMove(int playerIndex, Game.WhatPlayerDid whatPlayerDid);
    public ArrayList<Integer> checkDuelWithPlayer(Player playerTookTotem);
    public Game.GameMode getGameMode();
    public void afterDuelMakeMove(int winner, int looser);
    public void arrowsInMakeMove(int winner);
    public void openAllTopCards();
    public LinkedList<Card> getAllCards();
    public void moveWithoutAnswer(int playerIndex, Game.WhatPlayerDid whatPlayerDid);
    public void setGraphicsView(GraphicsView view);
//    public controller.TotemClient(ArrayList<String> playersNames, ArrayList<Integer> cardNumbers);
  //  public controller.TotemClient(ArrayList<String> playersNames, ArrayList<Integer> cardNumbers, int firstPerson, int cardSeed);


    }
