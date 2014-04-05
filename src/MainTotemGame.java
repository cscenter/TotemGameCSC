import controller.MyClient;
import view.*;
import net.MyServer;
import utils.Configuration;

/**
 * Created with IntelliJ IDEA.
 * User: anton
 * Date: 03.11.13
 * Time: 18:04
 * To change this template use File | Settings | File Templates.
 */

public class MainTotemGame {
    public static void main(String[] args){
        int i = 0;
        if (args.length ==0){
            new GraphicsView(); //.run();
        }else{
            switch (args[0].charAt(0)){
                case '1':
                    (new View()).run();
                    break;
                case '3':
                    Configuration.isServer=true;
                    new GraphicsView();
                    break;
                case '4':
                    Configuration.isServer=true;
                    new MyServer();
                    break;
                case '0':
                    Configuration.isServer=false;
                    new GraphicsView();
                    break;

                default:

                    (new View()).run();
            }
            if (args.length==2){
                Configuration.ChangeDir(args[1]);
            }
        }
    }

}



