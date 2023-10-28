package dev.ua.ikeepcalm.monetaire.dao;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import dev.ua.ikeepcalm.monetaire.entities.EcoUser;

import java.sql.SQLException;
import java.util.List;

import static dev.ua.ikeepcalm.monetaire.Monetaire.cardDao;

public class EcoPlayerDao extends BaseDaoImpl<EcoUser, Long> {

    public EcoPlayerDao(ConnectionSource connectionSource) throws SQLException {
        super(connectionSource, EcoUser.class);
    }

    private void init() {
        try {
            TableUtils.createTableIfNotExists(connectionSource, EcoUser.class);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public EcoUser findByNickname(org.bukkit.entity.Player input) {
        init();
        try {
            List<EcoUser> ecoUserList = super.queryForEq("nickname", input.getName());
            if (ecoUserList.isEmpty()) {
                EcoUser ecoUser = new EcoUser();
                ecoUser.setNickname(input.getName());
                ecoUser.setUuid(input.getUniqueId().toString());
                create(ecoUser);
                return ecoUser;
            } else {
                EcoUser ecoUser = ecoUserList.get(0);
                if (ecoUser.getUuid() == null) {
                    ecoUser.setUuid(input.getUniqueId().toString());
                    save(ecoUser);
                    return ecoUser;
                } else {
                    return ecoUser;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void save(EcoUser ecoUser) {
        init();
        try {
            List<EcoUser> ecoUserList = super.queryForEq("nickname", ecoUser.getNickname());
            if (ecoUserList.isEmpty()){
                create(ecoUser);
            } else {
                cardDao.save(ecoUser.getCard());
                update(ecoUser);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
