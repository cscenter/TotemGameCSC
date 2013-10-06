class Game
{
    ArrayList<Player> players;
    ArrayList<Card> allCards;
    ArrayList<Card> cardsUnderTotem;
    
    View gameView;
    
    int turnNumber;
    
    // Не вижу смысла для перечисления - ситуации "стрелки наружу" и "стрелки внутрь" можно обрабатывать
    // проверяя карту и запуская соответствующие методы
    boolean isColorModeEnabled;
    
    public Game(int numberOfPlayers)
    {
        
    }
    
    public boolean isGameEnded()
    {
        for (Player player : players)
            if (player.getCardsCount() == 0)
                return true;
        
        return false;
    }
    
    public boolean isRoundEnded()
    {
        return false;
    }
    
    boolean checkDuel()
    {
        return false;
    }
    
    public void makeMove(int playerIndex, boolean tookTotem)
    {
        
    }
    
    public int getPlayersCount()
    {
        return players.size();
    }
    
    public Player getPlayer(int playerIndex)
    {
        try {
            return players.get(playerIndex);
        } catch (IndexOutOfBoundsException e) {
            System.out.println(e.getCause() + ":" + e.getMessage());
            return null;
        }
    }
    
    public void generateCards()
    {
        
    }
    
}