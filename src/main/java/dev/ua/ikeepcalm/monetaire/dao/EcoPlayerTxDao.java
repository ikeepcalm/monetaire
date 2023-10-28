package dev.ua.ikeepcalm.monetaire.dao;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import dev.ua.ikeepcalm.monetaire.entities.transactions.EcoPlayerTx;

import java.sql.SQLException;

public class EcoPlayerTxDao extends BaseDaoImpl<EcoPlayerTx, Long> {

    public EcoPlayerTxDao(ConnectionSource connectionSource) throws SQLException {
        super(connectionSource, EcoPlayerTx.class);
    }

    private void init() {
        try {
            TableUtils.createTableIfNotExists(connectionSource, EcoPlayerTx.class);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void save(EcoPlayerTx ecoPlayerTx){
        init();
        try {
            create(ecoPlayerTx);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
