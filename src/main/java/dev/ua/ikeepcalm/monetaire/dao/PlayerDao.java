package dev.ua.ikeepcalm.monetaire.dao;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import dev.ua.ikeepcalm.monetaire.entities.User;

import java.sql.SQLException;
import java.util.List;

import static dev.ua.ikeepcalm.monetaire.Monetaire.cardDao;

public class PlayerDao extends BaseDaoImpl<User, Long> {

    public PlayerDao(ConnectionSource connectionSource) throws SQLException {
        super(connectionSource, User.class);
    }

    private void init() {
        try {
            TableUtils.createTableIfNotExists(connectionSource, User.class);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public User findByNickname(org.bukkit.entity.Player input) {
        init();
        try {
            List<User> userList = super.queryForEq("nickname", input.getName());
            if (userList.size() == 0) {
                User user = new User();
                user.setNickname(input.getName());
                user.setUuid(input.getUniqueId().toString());
                create(user);
                return user;
            } else {
                User user = userList.get(0);
                if (user.getUuid() == null) {
                    user.setUuid(input.getUniqueId().toString());
                    save(user);
                    return user;
                } else {
                    return user;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void save(User user) {
        init();
        try {
            List<User> userList = super.queryForEq("nickname", user.getNickname());
            if (userList.size()==0){
                create(user);
            } else {
                cardDao.save(user.getCard());
                update(user);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
