package dev.ua.ikeepcalm.monetaire.dao;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import dev.ua.ikeepcalm.monetaire.entities.MinFin;

import java.sql.SQLException;

public class MinfinDao extends BaseDaoImpl<MinFin, Long> {

    public MinfinDao(ConnectionSource connectionSource) throws SQLException {
        super(connectionSource, MinFin.class);
    }

    private void init(){
        try {
            TableUtils.createTableIfNotExists(connectionSource, MinFin.class);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public MinFin getMinfin(){
        init();
        try {
            return queryForId(1L);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void save(MinFin minFin){
        init();
        try {
            MinFin bdMinfin = getMinfin();
            bdMinfin.setBalance(minFin.getBalance());
            bdMinfin.setSpentProjects(minFin.getSpentProjects());
            bdMinfin.setWaitCredits(minFin.getWaitCredits());
            bdMinfin.setWaitFines(minFin.getWaitFines());
            update(bdMinfin);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
