class Player
{
    ArrayList<Card> openedCards, closedCards;
    String name;
    
    public Player(String name)
    {
        
    }
    
    public int getCardsCount()
    {
        return openedCards.size() + closedCards.size();
    }
    
    public Card openNextCard()
    {
        Card card = closedCards.get(0);
        closedCards.remove(0);
        openedCards.add(card);
        
        return card;
    }
    
    public Card getTopOpenedCard()
    {
        return openedCards.get(0);
    }
    
    public void pushCards(ArrayList<Card> cards)
    {
        
    }
    
    public ArrayList<Card> pickUpAllOpenedCards()
    {
        ArrayList<Card> result = new ArrayList<Card>(openedCards);
        openedCards.clear();
        
        return result;
    }
    
}
