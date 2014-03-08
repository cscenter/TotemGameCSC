import java.net.ServerSocket.*;
import java.net.Socket.*;
import java.net.*;
import java.util.*;
import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Thread.sleep;

public class MyServer {
    public int whoDidThis;
    public int whatHeDid;
    int numberOfPl = 1;
    LinkedList<Socket> clients;
    LinkedList<InputStream> clientInput;
    LinkedList<OutputStream> clientOutput;
    Queue<Integer> comands;
    public static final int port = 6923;

    public MyServer(){
        clientOutput = new LinkedList<>();
        clients = new LinkedList<>();
        clientInput = new LinkedList<>();
        comands = new ArrayDeque<>(100);

        try (ServerSocket serverSocket = new ServerSocket(port)) {
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
            System.err.println("HEre");
            //пересылаем это всем
            for (OutputStream os : clientOutput){
                os.write(firstPlayer);
                os.write(cardSeed);
                os.flush();
            }
            System.err.println("HEre");

            for (Socket socket : clients){
                socket.close();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
