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

/**
 * серверная часть приложения
 * в отдельных потоках крутятся ридеры для клиентов
 * и раз в некое кол-во времени
 * @see utils.Configuration#timeToWait
 * получившаяся очередь рассылается игрокам
 */
public class MyServer extends TimerTask {
    /**
     * Таймер
     */
    private Timer timer;
    /**
     * количество игроков
     */
    private int numberOfPl;

    /**
     * список сокетов от клиентов
     */
    private ArrayList<Socket> clients;
    /**
     * список соединений клиент-сервер
     */
    private ArrayList<InputStream> clientInput;
    /**
     * список соединений сервер-клиент
     */
    private ArrayList<OutputStream> clientOutput;
    /**
     * закодированные команды
     * @see controller.MyClient#commands
     */
    private Queue<Byte> commands;
    /**
     * список классов - чтецов
     */
    private ArrayList<Reader> readers;

    /**
     * конструктор. инициализирует все переменные,
     * запускает сервер на соединение
     * даёт таймеру расписание
     */
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

    /**
     * сетевое соединение сервера.
     * Ждёт входящих,
     * пересылает им инфу о первом игроке, кем этот игрок будет и основании для рандома
     * запускает потоки на прослушку
     */
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
            //+ кто за какого игрока ответственен
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
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * run у Таймера
     * берёт часть очереди, которая была на начало выполнения функции
     * загоняет её в массив байтов
     * пересылает всем игрокам
     */
    @Override
    public void run() {
        int queueSize = commands.size();
        byte[] flushing = new byte[queueSize];
        int curNum = 0;
        while (curNum < queueSize) {
            flushing[curNum++] = commands.remove();
        }
        for (int i = 0; i < flushing.length; i++) {
            System.out.print(flushing[i] + " ");
        }
        System.out.println("\nso get " + flushing.length + " commands. Now flush them to users");
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

    /**
     * класс чтенца, работающий в своём потоке
     */
    private class Reader extends Thread {
        /**
         * игрок, которого слушаем
         */
        int player;

        /**
         * инициализация простая: сопоставляем пришедшего из вне игрока с тем, которого будем слушать
         * @param myPlayerNumber номер игрока, которого будем слушать
         */
        public Reader(int myPlayerNumber) {
            player = myPlayerNumber;
        }

        /**
         * в бесконечном цикле слушаем.
         * Если пришла какая-нибудь команда (1 байт)
         * дописываем его в конец очереди
         */
        @Override
        public void run() {
            while (true) {
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
