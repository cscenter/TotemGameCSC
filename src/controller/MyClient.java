package controller;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import utils.*;
import model.*;
import view.*;

public class MyClient implements  TotemClient{
    Game myGame;
    Socket socket;
    InputStream inputStream;
    OutputStream outputStream;
    public MyClient(ArrayList<String> playersNames, ArrayList<Integer> cardNumbers){
        String ip = Configuration.getServerIp();
        int port = Configuration.getPort();
        try{
            socket= new Socket(ip, port);
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
            String outputStr;
            int firstPlayer = inputStream.read();
            int cardSeed = inputStream.read();
            myGame = new Game(playersNames,cardNumbers, firstPlayer, cardSeed);
        }catch(UnknownHostException e){e.printStackTrace();}
        catch(IOException e){e.printStackTrace();}
            }
    public MyClient(ArrayList<String> playersNames, ArrayList<Integer> cardNumbers, int firstPerson, int cardSeed){
        myGame = new Game(playersNames, cardNumbers, firstPerson, cardSeed);
    }

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
}
