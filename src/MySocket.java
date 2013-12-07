import java.net.ServerSocket.*;
import java.net.Socket.*;
import java.net.*;
import java.util.*;
import java.io.*;
public class MySocket {
    public int whoDidThis;
    public int whatHeDid;
    public static final int port = 692_324_999;
    public void workAsServer(){
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            try (Socket socket = serverSocket.accept()) {
                InputStream inputStream = socket.getInputStream();
                while (true){
                    whoDidThis = inputStream.read();
                    whatHeDid = inputStream.read();
                }

            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
