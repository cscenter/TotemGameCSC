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

public class MyServer {
    private int whoDidThis;
    private int whatHeDid;
    private int numberOfPl = 1;
    private LinkedList<Socket> clients;
    private LinkedList<InputStream> clientInput;
    private LinkedList<OutputStream> clientOutput;
    private Queue<Byte> comands;
    public MyServer(){
        clientOutput = new LinkedList<>();
        clients = new LinkedList<>();
        clientInput = new LinkedList<>();
        comands = new SynchronousQueue<>();
        initServer();
    }
    private void initServer(){
        try (ServerSocket serverSocket = new ServerSocket(Configuration.getPort())) {
            //подключили всех пользователей
            for (int i=0; i<numberOfPl; i++){
                try {
                    Socket socket = serverSocket.accept();
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
}
