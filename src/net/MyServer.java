package net;

import utils.Configuration;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.SynchronousQueue;

public class MyServer extends TimerTask {
    private int whoDidThis;
    private int whatHeDid;
    private Timer timer;
    private int numberOfPl;
    private ArrayList<Socket> clients;
    private ArrayList<InputStream> clientInput;
    private ArrayList<OutputStream> clientOutput;
    private Queue<Byte> commands;
    private ArrayList<Reader> readers;

    public MyServer() {
        clientOutput = new ArrayList<>();
        numberOfPl = Configuration.getNumberOfPlayers();
        clients = new ArrayList<>();
        clientInput = new ArrayList<>();
        commands = new ConcurrentLinkedQueue<>();
        readers = new ArrayList<>(numberOfPl);
        initServer();
        timer = new Timer();
        timer.schedule(this, 10000, Configuration.getTimeToWait());
    }

    private void initServer() {
        try (ServerSocket serverSocket = new ServerSocket(Configuration.getPort())) {
            //подключили всех пользователей
            System.out.println("wait for " + numberOfPl + " users");
            for (int i = 0; i < numberOfPl; i++) {
                try {
                    Socket socket = serverSocket.accept();
                    clients.add(socket);
                    InputStream inputStream = socket.getInputStream();
                    clientInput.add(inputStream);
                    clientOutput.add(socket.getOutputStream());
                    System.out.println(numberOfPl - i - 1 + " left");
                } catch (Exception e) {
                    throw e;
                }
            }

            //генерим кто первый ходит и зерно для случайного набора карт
            byte firstPlayer = (byte) (Math.abs((new Random()).nextInt()) % numberOfPl);
            byte cardSeed = (byte) ((new Random()).nextInt());

            //пересылаем это всем
            //+ кто каким ходит??
            System.out.println("start to give them initialize information: first Player is #" +
                    firstPlayer + ", card Seed is number " + cardSeed);
            for (byte i = 0; i < clientOutput.size(); i++) {
                OutputStream os = clientOutput.get(i);
                os.write(firstPlayer);
                os.write(cardSeed);
                os.write(i);
                os.flush();
                readers.add(new Reader(i));
                readers.get(i).start();

            }
            System.out.println("init finish. lets go!");
/*            for (OutputStream os : clientOutput){
                os.write(firstPlayer);
                os.write(cardSeed);
                os.flush();
            }*/

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void run() {
        int queueSize = commands.size();
        byte[] flushing = new byte[queueSize];
        int curNum = 0;
        while (curNum < queueSize) {
            flushing[curNum++] = commands.remove();
        }
        for (int i = 0; i < flushing.length; i++){
            System.out.print(flushing[i]+ " ");
        }
        System.out.println("\nso get "+ flushing.length+" commands. Now flush them to users");
        for (OutputStream stream : clientOutput) {
            try {
                stream.write(queueSize);
                stream.write(flushing);
                stream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class Reader extends Thread {
        int player;

        public Reader(int myPlayerNumber) {
            player = myPlayerNumber;
        }

        @Override
        public void run() {
            while (true){
                int getting = -1;
                try {
                    getting = clientInput.get(player).read();
                } catch (SocketTimeoutException e) {
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (getting != -1) {
                    System.out.println("get information from user #" + player);
                    commands.add((byte) getting);
                }
            }
        }
    }
}
