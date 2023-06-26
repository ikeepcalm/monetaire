package dev.ua.ikeepcalm.monetaire.dao;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import dev.ua.ikeepcalm.monetaire.entities.transactions.SystemTx;

import java.sql.SQLException;

public class SystemTxDao extends BaseDaoImpl<SystemTx, Long> {

    public SystemTxDao(ConnectionSource connectionSource) throws SQLException {
        super(connectionSource, SystemTx.class);
    }

    private void init() {
        try {
            TableUtils.createTableIfNotExists(connectionSource, SystemTx.class);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void save(SystemTx systemTx){
        init();
        try {
            create(systemTx);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
