import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class ServerController {
    public static String abort = "no! i want to exit!";

    void workAsServer(int port){
//		Socket socket;
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            try (Socket socket = serverSocket.accept()) {
                String inputStr;
                Scanner scanner = new Scanner(System.in);
                byte length;
                while (true){
                    OutputStream os = socket.getOutputStream();
                    inputStr = scanner.nextLine();
                    length = (byte)(inputStr.length());
                    if (inputStr.equals(abort)){
                        return;
                    }
                    byte[] requestBytes = inputStr.getBytes("UTF-8");
                    os.write(length);
                    os.write(requestBytes);
                    os.flush();
                }

            }
        }catch(IOException e){e.printStackTrace();}

    }
}
