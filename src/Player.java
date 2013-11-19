import java.util.*;

/**
 * один игрок
 * у него есть имя.
 * и открытые и закрытые карты
 */
class Player
{
    private LinkedList<Card> openedCards, closedCards;
    private String name;

    /**
     * конструктор.
     * Открытых карт нет
     * @param playerName имя игрока
     * */
    public Player(String playerName){
        name = playerName;
        openedCards = new LinkedList<>();
        closedCards = new LinkedList<>();
    }


    /**
     * заносим закрытые карты
     * @param cards часть allCards, которая будет передана в качестве закрытых
     */
    public void setCardsToPlayer(LinkedList<Card> cards){
        closedCards.addAll(cards);
    }

    /**
     * на всякий случай пока. вывод информации о игроке в консоль
     */
/*    public void printInformation(){
        System.out.printf("Player %s:\nClosed card: ", name);
        for (Card card : closedCards){
            System.out.printf("%d ", card.getCardNumber());
        }
        System.out.printf("\nOpened card: ");
        for (Card card : openedCards){
            System.out.printf("%d ", card.getCardNumber());
        }
        System.out.printf("\n\n");
    }
*/
    public String getName(){
    	return name;    
    }    
    public int getCardsCount()
    {
        return openedCards.size() + closedCards.size();
    }
    public int getOpenCardsCount()
    {
        return openedCards.size();
    }
    public int getCloseCardsCount()
    {
        return closedCards.size();
    }
    
    public Card openNextCard()
    {
        if (closedCards.size() == 0){
            return openedCards.get(openedCards.size() - 1);
        }
        Card card = closedCards.pollFirst();
        openedCards.addLast(card);
        return card;
    }
    
    public Card getTopOpenedCard()
    {
        if (getOpenCardsCount() == 0){
            return null;
        }
        return openedCards.getLast();
    }

    public LinkedList<Card> pickUpAllOpenedCards(){
        LinkedList<Card> result = new LinkedList<>(openedCards);
        openedCards.clear();
        
        return result;
    }
    
}
