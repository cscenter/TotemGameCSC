package controller;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.*;
import java.util.concurrent.SynchronousQueue;

import utils.*;
import model.*;
import view.*;

public class MyClient implements  TotemClient{
    private Game myGame;
    private Socket socket;
    private InputStream inputStream;
    private OutputStream outputStream;
    private Queue<Byte> commands;
    private Queue<Integer> whoDid;
    private Queue<Game.WhatPlayerDid> whatDid;

    private int whatPlayer;
    public int getWhatPlayer(){
        return whatPlayer;
    }
    public MyClient(ArrayList<String> playersNames, ArrayList<Integer> cardNumbers){
        String ip = Configuration.getServerIp();
        commands = new SynchronousQueue<>();
        whatDid = new LinkedList<>();
        whoDid = new LinkedList<>();
        int port = Configuration.getPort();
        initConnection(ip, port, playersNames, cardNumbers);
        new ToClient().start();
    }

    private void initConnection(String ip,  int port, ArrayList<String> playersNames, ArrayList<Integer> cardNumbers){
        try{
            System.out.println("connect to server");
            socket= new Socket(ip, port);
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
            String outputStr;
            int firstPlayer = inputStream.read();
            int cardSeed = inputStream.read();
            whatPlayer = inputStream.read();
            System.out.println("initializing complite");
            myGame = new Game(playersNames,cardNumbers, firstPlayer, cardSeed);
        }catch(UnknownHostException e){e.printStackTrace();}
        catch(IOException e){e.printStackTrace();}
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
    @Override
    public LinkedList<Card> getAllCards(){
        return myGame.getAllCards();
    }
    @Override
    public void moveWithoutAnswer(int playerIndex, Game.WhatPlayerDid whatPlayerDid){
        try {
            if (playerIndex == whatPlayer){
                outputStream.write(Configuration.codeOneCommand(playerIndex, whatPlayerDid));
            }
//            getInformationFromServer();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void setGraphicsView(GraphicsView view){
        myGame.setGraphicsView(view);
    }
    private class ToClient extends Thread{
        public void getInformationFromServer(){
            try{
                String outputStr;
                int length;
                int res;
                boolean isReading=false;
                commands.clear();
                while (true){
                    length = (inputStream.read());
                    for (int i=0; i<length; i++){
                        res=inputStream.read();
                        commands.add((byte) (res));
                    }
                }
            }catch(UnknownHostException e){e.printStackTrace();}
            catch(IOException e){e.printStackTrace();}
            Configuration.decodeCommands(commands, whoDid, whatDid);
        }
        @Override
        public void run(){
            while (!isGameEnded()){
                getInformationFromServer();
                while (!whoDid.isEmpty()){
                    Integer who = whoDid.remove();
                    Game.WhatPlayerDid what = whatDid.remove();
                    myGame.moveWithoutAnswer(who, what);
                }

            }
        }

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
                        commands.add((byte)(res));
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
}
