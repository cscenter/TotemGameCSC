import net.MyServer;
import utils.Configuration;
import view.GraphicsView;
import view.View;

/**
 * Created with IntelliJ IDEA.
 * User: anton
 * Date: 03.11.13
 * Time: 18:04
 * To change this template use File | Settings | File Templates.
 */


public class MainTotemGame {
    private static void printHelp() {
        System.out.println("You are trying to run \"Totem Game\"");
        System.out.println("Usage: java -jar Totem.jar mode [options]");
        System.out.println("\nmode:");
        System.out.println("     --server     : run as server in network game");
        System.out.println("     --netclient  : run as client in network game");
        System.out.println("     --locclient  : run at local PC");
        System.out.println("\noptions:");
        System.out.println("     --playernum  : set number of players from 2 to 8");
        System.out.println("     --card       : set number of cards from playernum to 150");
        System.out.println("     --port       : (for net game only) set port to server and to client");
        System.out.println("     --IP         : (for net game only) give server's IP to client");
    }
    private static enum ResultOfParsing {
        INCORRECT,
        NOT_DEF,
        SERVER,
        CLIENT,
        LOCAL;
    }
    public static void main(String[] args) {
        ResultOfParsing resultOfParsing = ResultOfParsing.NOT_DEF;
        boolean isServer = false;
        int numOfPlayers = 4;
        int port = 6923;
        int cardNumber = 150;
        String IP = "127.0.0.1";
        if (args.length == 0) {
            resultOfParsing = ResultOfParsing.INCORRECT;
        }
        try {
            for (int i = 0; i < args.length; i++) {
                if (args[i].equals("--server")) {
                    isServer = true;
                    if (!resultOfParsing.equals(ResultOfParsing.NOT_DEF)) {
                        resultOfParsing = ResultOfParsing.INCORRECT;
                    } else {
                        resultOfParsing = ResultOfParsing.SERVER;
                    }

                }
                if (args[i].equals("--netclient")) {
                    isServer = true;
                    if (!resultOfParsing.equals(ResultOfParsing.NOT_DEF)) {
                        resultOfParsing = ResultOfParsing.INCORRECT;
                    } else {
                        resultOfParsing = ResultOfParsing.CLIENT;
                    }
                }
                if (args[i].equals("--locclient")) {
                    isServer = false;
                    if (!resultOfParsing.equals(ResultOfParsing.NOT_DEF)) {
                        resultOfParsing = ResultOfParsing.INCORRECT;
                    } else {
                        resultOfParsing = ResultOfParsing.LOCAL;
                    }

                }
                if (args[i].equals("--playernum")) {
                    numOfPlayers = Integer.parseInt(args[i+1]);
                    if ((numOfPlayers > 8) || (numOfPlayers <= 1)) {
                        numOfPlayers = 4;
                        resultOfParsing = ResultOfParsing.INCORRECT;
                        break;
                    }
                }
                if (args[i].equals("--card")) {
                    cardNumber = Integer.parseInt(args[i+1]);
                    if ((cardNumber > 150) || (cardNumber < 1)) {
                        cardNumber = 150;
                        resultOfParsing = ResultOfParsing.INCORRECT;
                        break;
                    }
                }
                if (args[i].equals("--port")) {
                    port = Integer.parseInt(args[i+1]);
                }
                if (args[i].equals("--IP")) {
                    IP = args[i+1];
                }
                if (args[i].equals("--help")){
                    resultOfParsing = ResultOfParsing.INCORRECT;
                    break;
                }
            }
        } catch (Exception e) {
            resultOfParsing = ResultOfParsing.INCORRECT;
        }

        Configuration.setNetworkConfig(isServer, numOfPlayers, port, IP, cardNumber);
        switch (resultOfParsing) {
            case SERVER:
                new MyServer();
                break;
            case CLIENT:
                new GraphicsView();
                break;
            case LOCAL:
                new GraphicsView();
                break;
            case NOT_DEF:
            case INCORRECT:
                printHelp();
                break;
        }
    }
}



