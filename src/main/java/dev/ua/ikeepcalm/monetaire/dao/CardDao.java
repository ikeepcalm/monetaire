package dev.ua.ikeepcalm.monetaire.dao;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import dev.ua.ikeepcalm.monetaire.entities.Card;
import dev.ua.ikeepcalm.monetaire.entities.EcoUser;
import dev.ua.ikeepcalm.monetaire.utils.CardUtil;

import java.sql.SQLException;
import java.util.List;

import static dev.ua.ikeepcalm.monetaire.Monetaire.ecoPlayerDao;

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


    public Card create(EcoUser ecoUser) {
        init();
        try {
            String cardNumber;
            do {
                cardNumber = CardUtil.generateCardNumber();
            } while (numberExists(cardNumber));

            Card card = new Card();
            card.setHolder(ecoUser);
            card.setNumber(cardNumber);
            card.setCvv(CardUtil.generateCVV());
            card.setBalance(0L);
            card.setLoan(0L);
            card.setFine(0L);
            card.setOnlineb(0L);
            card.setCoins(0L);
            ecoUser.setCard(card);

            create(card);
            ecoPlayerDao.save(ecoUser);
            return card;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
