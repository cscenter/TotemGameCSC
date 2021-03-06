package model;

import java.util.LinkedList;

/**
 * один игрок
 * у него есть имя.
 * и открытые и закрытые карты
 */
public class Player {
    /**
     * карты игрока
     */
    private LinkedList<Card> openedCards, closedCards;
    /**
     * имя
     */
    private String name;
    /**
     * конструктор.
     * Открытых карт нет
     *
     * @param playerName имя игрока
     */
    public Player(String playerName, int ID) {
        name = playerName;
        id = ID;
        openedCards = new LinkedList<>();
        closedCards = new LinkedList<>();
    }

    private int id;
    public int getId() {
        return id;
    }
    /**
     * заносим закрытые карты
     *
     * @param cards часть allCards, которая будет передана в качестве закрытых
     */
    public void setCardsToPlayer(LinkedList<Card> cards) {
        closedCards.addAll(cards);
    }

    /**
     *
     * @return имя игрока
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @return общее количество карт
     */
    public int getCardsCount() {
        return openedCards.size() + closedCards.size();
    }

    /**
     *
     * @return количество открытых карт
     */
    public int getOpenCardsCount() {
        return openedCards.size();
    }

    /**
     *
     * @return количество закрытых карт
     */
    public int getCloseCardsCount() {
        return closedCards.size();
    }

    /**
     * открываем новую карту:
     * переносим из закрытых в открытые в начало
     * @return открытая карта
     */
    public Card openNextCard() {
        if (closedCards.size() == 0) {
            return openedCards.get(openedCards.size() - 1);
        }
        Card card = closedCards.pollFirst();
        openedCards.addLast(card);
        return card;
    }

    /**
     * возвращает верхнюю открытую карту
     * @return верхняя открытая карта или null в случае отсутствия
     */
    public Card getTopOpenedCard() {
        if (getOpenCardsCount() == 0) {
            return null;
        }
        return openedCards.getLast();
    }

    /**
     * возвращает тип открытой карты. Если верхней карты нет - вернёт обычный тип
    */
    public Card.CardType getTopCardType() {
        if (getOpenCardsCount() == 0) {
            return Card.CardType.NORMAL;
        }
        return getTopOpenedCard().getCardType();
    }
    /**
     * забираем все открытые карты
     * @return список из всех карт
     */
    public LinkedList<Card> pickUpAllOpenedCards() {
        LinkedList<Card> result = new LinkedList<>(openedCards);
        openedCards.clear();
        return result;
    }
}
