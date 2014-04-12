package controller;

import model.Card;
import model.Game;
import model.Player;
import view.GraphicsView;

import java.util.ArrayList;
import java.util.LinkedList;

public class BasicClient implements TotemClient {
    private Game myGame;

    @Override
    public int getPlayersCount() {
        return myGame.getPlayersCount();
    }

    @Override
    public Player getPlayer(int i) {
        return myGame.getPlayer(i);
    }

    @Override
    public Game.Totem getTotem() {
        return myGame.getTotem();
    }

    @Override
    public int getPlayerWhoWillGo() {
        return myGame.getPlayerWhoWillGo();
    }

    @Override
    public int getTurnNumber() {
        return myGame.getTurnNumber();
    }

    @Override
    public boolean isGameEnded() {
        return myGame.isGameEnded();
    }

    @Override
    public Game.ResultOfMakeMove makeMove(int playerIndex, Game.WhatPlayerDid whatPlayerDid) {
        return myGame.makeMove(playerIndex, whatPlayerDid);
    }

    @Override
    public ArrayList<Integer> checkDuelWithPlayer(Player playerTookTotem) {
        return myGame.checkDuelWithPlayer(playerTookTotem);
    }

    @Override
    public Game.GameMode getGameMode() {
        return myGame.getGameMode();
    }

    @Override
    public void afterDuelMakeMove(int winner, int looser) {
        myGame.afterDuelMakeMove(winner, looser);
    }

    @Override
    public void arrowsInMakeMove(int winner) {
        myGame.arrowsInMakeMove(winner);
    }

    @Override
    public void openAllTopCards() {
        myGame.openAllTopCards();
    }

    @Override
    public LinkedList<Card> getAllCards() {
        return myGame.getAllCards();
    }

    @Override
    public void moveWithoutAnswer(int playerIndex, Game.WhatPlayerDid whatPlayerDid) {
        myGame.moveWithoutAnswer(playerIndex, whatPlayerDid);
    }

    @Override
    public void setGraphicsView(GraphicsView view) {
        myGame.setGraphicsView(view);
    }

    public BasicClient(ArrayList<String> playersNames, ArrayList<Integer> cardNumbers) {
        myGame = new Game(playersNames, cardNumbers);
    }

    public BasicClient(ArrayList<String> playersNames, ArrayList<Integer> cardNumbers, int firstPerson, int cardSeed) {
        myGame = new Game(playersNames, cardNumbers, firstPerson, cardSeed);
    }

}
