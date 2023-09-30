package dev.ua.ikeepcalm.monetaire.dao;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import dev.ua.ikeepcalm.monetaire.entities.Card;
import dev.ua.ikeepcalm.monetaire.entities.User;
import dev.ua.ikeepcalm.monetaire.utils.CardUtil;

import java.sql.SQLException;
import java.util.List;

import static dev.ua.ikeepcalm.monetaire.Monetaire.playerDao;

public class CardDao extends BaseDaoImpl<Card, Long> {

    public CardDao(ConnectionSource connectionSource) throws SQLException {
        super(connectionSource, Card.class);
    }

    private void init() {
        try {
            TableUtils.createTableIfNotExists(connectionSource, Card.class);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean numberExists(String number) {
        try {
            List<Card> playerList = super.queryForEq("number", number);
            return !playerList.isEmpty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void save(Card card){
        try {
            update(card);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public Card create(User user) {
        init();
        try {
            String cardNumber;
            do {
                cardNumber = CardUtil.generateCardNumber();
            } while (numberExists(cardNumber));

            Card card = new Card();
            card.setHolder(user);
            card.setNumber(cardNumber);
            card.setCvv(CardUtil.generateCVV());
            card.setBalance(0L);
            card.setLoan(0L);
            card.setFine(0L);
            card.setOnlineb(0L);
            card.setCoins(0L);
            user.setCard(card);

            create(card);
            playerDao.save(user);
            return card;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
