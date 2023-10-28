package dev.ua.ikeepcalm.monetaire.dao;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import dev.ua.ikeepcalm.monetaire.entities.RepUser;

import java.sql.SQLException;
import java.util.List;

public class RepPlayerDao extends BaseDaoImpl<RepUser, Long> {

    public RepPlayerDao(ConnectionSource connectionSource) throws SQLException {
        super(connectionSource, RepUser.class);
    }

    private void init() {
        try {
            TableUtils.createTableIfNotExists(connectionSource, RepUser.class);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateCredits(){
        try {
            List<RepUser> repUserList = queryForAll();
            for (RepUser repUser : repUserList){
                repUser.setCredits(2);
                update(repUser);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public RepUser findByNickname(org.bukkit.entity.Player input) {
        init();
        try {
            List<RepUser> repUserList = super.queryForEq("nickname", input.getName());
            if (repUserList.isEmpty()) {
                RepUser repUser = new RepUser();
                repUser.setNickname(input.getName());
                repUser.setUuid(input.getUniqueId().toString());
                repUser.setCredits(2);
                repUser.setReverence(0);
                if (input.hasPermission("monetaire.rep.admin")) {
                    repUser.setAdmin(true);
                }
                create(repUser);
                return repUser;
            } else {
                RepUser repUser = repUserList.get(0);
                if (repUser.isAdmin()) {
                    if (!input.hasPermission("monetaire.rep.admin")) {
                        repUser.setAdmin(false);
                        save(repUser);

                    }
                }
                return repUser;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void save(RepUser ecoUser) {
        init();
        try {
            List<RepUser> ecoUserList = super.queryForEq("nickname", ecoUser.getNickname());
            if (ecoUserList.isEmpty()) {
                create(ecoUser);
            } else {
                update(ecoUser);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
