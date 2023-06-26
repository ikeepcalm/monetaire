package dev.ua.ikeepcalm.monetaire.dao;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import dev.ua.ikeepcalm.monetaire.entities.transactions.PlayerTx;

import java.sql.SQLException;

public class PlayerTxDao extends BaseDaoImpl<PlayerTx, Long> {

    public PlayerTxDao(ConnectionSource connectionSource) throws SQLException {
        super(connectionSource, PlayerTx.class);
    }

    private void init() {
        try {
            TableUtils.createTableIfNotExists(connectionSource, PlayerTx.class);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void save(PlayerTx playerTx){
        init();
        try {
            create(playerTx);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
