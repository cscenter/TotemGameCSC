package controller;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.*;
import java.util.concurrent.SynchronousQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import utils.*;
import model.*;
import view.*;

public class MyClient implements  TotemClient{
    private Game myGame;
    private Socket socket;
    private InputStream inputStream;
    private OutputStream outputStream;
    private boolean isHasServerAnswer;
    private Queue<Byte> comands;
    private Queue<Integer> whoDid;
    private Queue<Game.WhatPlayerDid> whatDid;

    private int whatPlayer;
    public int getWhatPlayer(){
        return whatPlayer;
    }
    public MyClient(ArrayList<String> playersNames, ArrayList<Integer> cardNumbers){
        String ip = Configuration.getServerIp();
        comands = new SynchronousQueue<>();
        int port = Configuration.getPort();
        try{
            socket= new Socket(ip, port);
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
            String outputStr;
            int firstPlayer = inputStream.read();
            int cardSeed = inputStream.read();
            whatPlayer = inputStream.read();
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
    public boolean haveWeGotAnswer() {
        return isHasServerAnswer;
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
    public LinkedList<Card> getAllCards(){
        return myGame.getAllCards();
    }
/*    private class ToServer extends Thread{
        @Override
        public void run(){

        }
    }
    private class ToClient extends Thread{
        public void getInformationFromServer(){
            try{
                String outputStr;
                int length;
                int res;
                boolean isReading=false;
                while (true){
                    length = (inputStream.read());
                    for (int i=0; i<length; i++){
                        res=inputStream.read();
                        comands.add((byte)(res));
                    }
                }
            }catch(UnknownHostException e){e.printStackTrace();}
            catch(IOException e){e.printStackTrace();}
            decodeCommands();
}

        @Override
        public void run(){


        }
    }                                       */
    private void decodeCommands(){
        whoDid.clear();
        whatDid.clear();
        while (!comands.isEmpty()){
            byte current=comands.remove();
            whoDid.add(current/2);
            if (current-(current/2)*2==0){
                whatDid.add(Game.WhatPlayerDid.TOOK_TOTEM);
            }else {
                whatDid.add(Game.WhatPlayerDid.OPEN_NEW_CARD);
            }
        }
    }
}
