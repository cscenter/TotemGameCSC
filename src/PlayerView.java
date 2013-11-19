import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.ImageObserver;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlayerView{
    private static int scale;
//    private static int cardScale;
    CardView topCardView;
    public Player playerInfo;
    public int xCoordinate;
    public int yCoordinate;
    public double angle;
    public char openCardKey;
    public char catchTotemKey;
    public static void setScale(int size){
            scale = size;

    }
    public String playerViewName; //что делать с дублированием имени в PlayerView и Player?
    public PlayerView(char newOpenCardKey, char newCatchTotemKey, String name, double a){
        openCardKey = newOpenCardKey;
        catchTotemKey = newCatchTotemKey;
        playerViewName = name;
        angle = a;
        xCoordinate = (int)((scale/3.5) * Math.sin(angle*Math.PI/180) + scale / 2.2);
        yCoordinate = (int)((scale/3.5) * Math.cos(angle*Math.PI/180) + scale / 2.5);
    }
    public void connectWithInfo(Player player){
        playerInfo = player;
    }

    public PlayerView(char newOpenCardKey, char newCatchTotemKey, Player player, double a){
        openCardKey = newOpenCardKey;
        catchTotemKey = newCatchTotemKey;
        playerViewName = player.getName();
        playerInfo = player;
        angle = a;
        xCoordinate = (int)((scale/3.5) * Math.sin(angle*Math.PI/180) + scale / 2.2);
        yCoordinate = (int)((scale/3.5) * Math.cos(angle*Math.PI/180) + scale / 2.5);
    }


    public boolean isIn(Point p){
        if ((p.getX()<xCoordinate+CardView.cardSize)&&(p.getX()>xCoordinate-CardView.cardSize)){
            if ((p.getY()<yCoordinate+CardView.cardSize+40)&&(p.getY()>yCoordinate-40)){
                return true;
            }
        }
        return false;
    }
    public void clear(Graphics g){
        g.clearRect(xCoordinate-(int)(CardView.cardSize*2.1), yCoordinate, (int)(3.1*CardView.cardSize), (int)(CardView.cardSize*2.5));
    }
    public void drawPlayer(Graphics g, MyPanel panel){
        clear(g);
        g.drawChars(playerViewName.toCharArray(), 0, playerViewName.length(), xCoordinate - CardView.cardSize / 3, yCoordinate - scale / 30);
        String openCardsNumber = String.valueOf(playerInfo.getOpenCardsCount());
        g.drawChars(openCardsNumber.toCharArray(), 0, openCardsNumber.length(),xCoordinate+scale/60, yCoordinate+CardView.cardSize+scale/30);
        if (playerInfo.getOpenCardsCount()!=0){
//            topCardView = playerInfo.getTopOpenedCard();
            Image image = topCardView.cardImage;//.get(playerInfo.getTopOpenedCard().getCardNumber());
            g.drawImage(image, xCoordinate, yCoordinate, CardView.cardSize, CardView.cardSize, panel);
        }
        String closeCardsNumber = String.valueOf(playerInfo.getCloseCardsCount());
        g.drawChars(closeCardsNumber.toCharArray(), 0, closeCardsNumber.length(),  xCoordinate-CardView.cardSize+scale/60, yCoordinate+CardView.cardSize+scale/30);
        if (playerInfo.getCloseCardsCount()!=0){
            g.fillRect(xCoordinate - (int)(1.1 * CardView.cardSize), yCoordinate, CardView.cardSize, CardView.cardSize);
        }
        String catchKey=String.valueOf(openCardKey)+", "+String.valueOf(catchTotemKey);
        g.drawChars(catchKey.toCharArray(), 0, catchKey.length(), xCoordinate - (int)(CardView.cardSize*1.5), yCoordinate - scale/30);
    }
}
