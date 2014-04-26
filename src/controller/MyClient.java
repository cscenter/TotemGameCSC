package controller;

import model.Card;
import model.Game;
import model.Player;
import utils.Configuration;
import view.GraphicsView;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * сетевая имплиментация контроллера
 * @see controller.TotemClient
 *
 */
public class MyClient implements TotemClient {
    /**
     * ссылка на модель
     */
    private Game myGame;
    /**
     * свой сокет
     */
    private Socket socket;
    /**
     * поток передачи от сервера к клиенту
     */
    private InputStream inputStream;
    /**
     * поток передачи от клиента к серверу
     */
    private OutputStream outputStream;
    /**
     * команды в зашифрованном виде
     * @see utils.Configuration#decodeCommands(java.util.Queue, java.util.Queue, java.util.Queue)
     */
    private Queue<Byte> commands;
    /**
     * список людей, которые ходили
     */
    private Queue<Integer> whoDid;
    /**
     * список того, что сделали люди, которые ходили
     */
    private Queue<Game.WhatPlayerDid> whatDid;
    /**
     * какому игроку соответствует этот контроллер
     */
    private int whatPlayer;
    /**
     * мы можем совершить лишь один ход за промежуток, в котором сервер получает команды.
     * вопрос,совершили ли мы его уже?
     */
    private boolean canWeGo;
    /**
     * какому игроку соответвует этот контроллер
     * @return номер игрока
     */
    /**
     * ссылка на вид. он обновляется после появления инфы с сервера
     */
    private GraphicsView graphicsView;
    @Override
    public int getWhatPlayer() {
        return whatPlayer;
    }

    /**
     * конструктор.
     * создаёт списки все.
     * Соединяется с сервером
     * запускает в отдельном потоке на приём
     * @param playersNames имена игроков. Одновременно по именам узнаём количество
     * @param cardNumbers номера карт в порядке прочтения
     */
    public MyClient(ArrayList<String> playersNames, ArrayList<Integer> cardNumbers) {
        String ip = Configuration.getServerIp();
        commands = new ConcurrentLinkedQueue<>();
        whatDid = new LinkedList<>();
        whoDid = new LinkedList<>();
        canWeGo = false;
        int port = Configuration.getPort();
        initConnection(ip, port, playersNames, cardNumbers);
        new ToClient().start();
    }

    /**
     * соединение с сервером.
     * приём сгененрированного сервером номера первого игрока
     * и основания random для перетасовки карт
     * @see model.Game#generateAllCards(java.util.ArrayList, int)
     * @param ip IP-адрес сервера
     * @param port порт, на котором сервер слушает
     * @param playersNames имена игроков. Одновременно по именам узнаём количество
     * @param cardNumbers номера карт в порядке прочтения
     */
    private void initConnection(String ip, int port, ArrayList<String> playersNames, ArrayList<Integer> cardNumbers) {
        try {

            /* соединяемся */
            System.out.println("connect to server");
            socket = new Socket(ip, port);
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
            if (socket.isConnected()) {
                System.out.println("connection done. Wait for other players to start initializing");
            }

            /*
            получаем номер первого игрока
            и основание для random
            и то, какому игроку будет принадлежать этот контроллер
             */
            int firstPlayer = inputStream.read();
            int cardSeed = inputStream.read();
            whatPlayer = inputStream.read();
            System.out.println("initializing complete");

            /*
            запускаем контруктор модели с известным игроком и seed
             */
            myGame = new Game(playersNames, cardNumbers, firstPlayer, cardSeed);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
     * функция хода. Используется для непосредственного занесения данных в модель
     *
     * @param playerIndex   кто походил
     * @param whatPlayerDid что сделал походивший
     * @return результат хода
     * @see controller.TotemClient#moveWithoutAnswer(int, model.Game.WhatPlayerDid)
     */
    @Override
    public Game.ResultOfMakeMove makeMove(int playerIndex, Game.WhatPlayerDid whatPlayerDid) {

        return myGame.makeMove(playerIndex, whatPlayerDid);
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
     * пока НЕ ДОПИСАНА
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
     * пока НЕ ДОПИСАНА
     *
     * @param winner кто схватил тотем
     */
    @Override
    public void arrowsInMakeMove(int winner) {
        myGame.arrowsInMakeMove(winner);
    }

    /**
     * ход после спец. карты "стрелки наружу". Все открывают верхнюю
     *
     * пока НЕ ДОПИСАНА
     *
     */
    @Override
    public void openAllTopCards() {
        myGame.openAllTopCards();
    }

    /**
     * возвращает список из ссылок на все карты в игре
     *
     * @return список из ссылок на все карты в игре
     */
    @Override
    public LinkedList<Card> getAllCards() {
        return myGame.getAllCards();
    }

    /**
     * функция будет слита с
     * @see controller.TotemClient#makeMove(int, model.Game.WhatPlayerDid)
     * кодирует последний ход и отправляет данные на сервер.
     *
     * @param playerIndex   кто походил
     * @param whatPlayerDid что сделал походивший
     *
     */
    @Override
    public void moveWithoutAnswer(int playerIndex, Game.WhatPlayerDid whatPlayerDid) {
        if (canWeGo){ //если ещё не ходили в этом кванте
            canWeGo = false;
            try {
                if (playerIndex == whatPlayer) {
                    outputStream.write(Configuration.codeOneCommand(playerIndex, whatPlayerDid));
                }
    //            getInformationFromServer();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("send action to server");
        }
    }

    /**
     * создаёт ссылку на вид
     *
     * @param view ссылка на вид
     */
    @Override
    public void setGraphicsView(GraphicsView view) {
        graphicsView = view;
    }

    /**
     * класс, запускающийся в отдельном потоке. Передача от сервера к клиенту
     */
    private class ToClient extends Thread {

        /**
         * собираем информацию с сервера: список кодированных команд
         * и сразу их декодируем
         */
        public void getInformationFromServer() {
            try {
                int length;
                int res;
                commands.clear();
                canWeGo = true;
                length = (inputStream.read());
                if (length > 0) {
                    System.out.println("will read " + length + " commands");
                }
                for (int i = 0; i < length; i++) {
                    res = inputStream.read();
                    commands.add((byte) (res));
                }
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Configuration.decodeCommands(commands, whoDid, whatDid);
        }

        /**
         * в бесконечном цикле ждём информации с сервера
         * и передаём её в модель
         *
         * открытие карты можно перенести по событиям вконец.
         * т.к. все действовали, ещё не видя эту карту
         *
         *
         * формируем очереди из одних захватов тотема + (возможно) одной открытой карты для последнего игрока
         *
         * Во view - вся последовательность захватов тотема. Передаём ему.
         */
        @Override
        public void run() {
            while (!isGameEnded()) {
                getInformationFromServer();
                Queue<Integer> newWhoDid = new LinkedList<>();
                Queue<Game.WhatPlayerDid> newWhatDid = new LinkedList<>();
                boolean hasOneTotem = false;
                int whoTookTotemFirst = -1;
                boolean hasOpenCard = false;
                int whoOpenCard = -1;
                if (whatDid.size() > 0) {
                    System.out.println("get information from server");
                }
                while (!whoDid.isEmpty()) {
                    Integer who = whoDid.remove();
                    Game.WhatPlayerDid what = whatDid.remove();
                    if ((what == Game.WhatPlayerDid.TOOK_TOTEM)){
                        if (!hasOneTotem){
                            whoTookTotemFirst = who;
                            hasOneTotem = true;
                        }
                        newWhatDid.add(what);
                        newWhoDid.add(who);
                    } else {
                        if (!hasOpenCard){
                            hasOpenCard = true;
                            whoOpenCard = who;
                        }
                    }
                }
                if(hasOpenCard){
                    newWhatDid.add(Game.WhatPlayerDid.OPEN_NEW_CARD);
                    newWhoDid.add(whoOpenCard);
                }
                graphicsView.repaintView(newWhoDid, newWhatDid, hasOpenCard, hasOneTotem);
            }
        }

    }
}
