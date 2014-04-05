package net;

import java.net.ServerSocket.*;
import java.net.Socket.*;
import java.net.*;
import java.util.*;
import java.io.*;
import java.util.concurrent.SynchronousQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import utils.*;

public class MyServer extends TimerTask{
    private int whoDidThis;
    private int whatHeDid;
    private Timer timer;
    private int numberOfPl = 4;
    private ArrayList<Socket> clients;
    private ArrayList<InputStream> clientInput;
    private ArrayList<OutputStream> clientOutput;
    private Queue<Byte> commands;
    public MyServer(){
        clientOutput = new ArrayList<>();
        clients = new ArrayList<>();
        clientInput = new ArrayList<>();
        commands = new SynchronousQueue<>();
        initServer();
        timer = new Timer();
        timer.schedule(this, Configuration.getTimeToWait());
    }
    private void initServer(){
        try (ServerSocket serverSocket = new ServerSocket(Configuration.getPort())) {
            //подключили всех пользователей
            for (int i=0; i<numberOfPl; i++){
                try {
                    Socket socket = serverSocket.accept();
                    socket.setSoTimeout(Configuration.getTimeToWait());
                    clients.add(socket);
                    InputStream inputStream = socket.getInputStream();
                    clientInput.add(inputStream);
                    clientOutput.add(socket.getOutputStream());

                }catch (Exception e){
                    throw e;
                }
            }

            //генерим кто первый ходит и зерно для случайного набора карт
            byte firstPlayer = (byte)(((new Random()).nextInt())%numberOfPl);
            byte cardSeed = (byte)((new Random()).nextInt());

            //пересылаем это всем
            //+ кто каким ходит??
            for (byte i = 0; i < clientOutput.size(); i++){
                OutputStream os = clientOutput.get(i);
                os.write(firstPlayer);
                os.write(cardSeed);
                os.write(i);
                os.flush();

            }
/*            for (OutputStream os : clientOutput){
                os.write(firstPlayer);
                os.write(cardSeed);
                os.flush();
            }*/

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void run(){
        ArrayList<Reader> readers = new ArrayList<>(numberOfPl);
        for (int i=0; i< numberOfPl; i++){
            readers.add(new Reader(i));
        }
        for (Reader reader : readers){
            reader.start();
        }
        for (Reader reader : readers){
            try {
                reader.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        byte[] flushing = new byte[commands.size()];
        int curNum=0;
        while (!commands.isEmpty()){
            flushing[curNum++] = commands.remove();
        }
        for (OutputStream stream : clientOutput){
            try {
                stream.write((byte)commands.size());
                stream.write(flushing);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class Reader extends Thread{
        int player;
        public Reader(int myPlayerNumber){
            player = myPlayerNumber;
        }
        @Override
        public void run(){
            int getting=-1;
            try {
                getting = clientInput.get(player).read();
            } catch (SocketTimeoutException e){}
            catch (IOException e) {
                e.printStackTrace();
            }
            if (getting!=-1){
                commands.add((byte)getting);
            }
        }
    }
}
