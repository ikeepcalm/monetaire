package dev.ua.ikeepcalm.monetaire.dao;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import dev.ua.ikeepcalm.monetaire.entities.Player;

import java.sql.SQLException;
import java.util.List;

public class PlayerDao extends BaseDaoImpl<Player, Long> {

    public PlayerDao(ConnectionSource connectionSource) throws SQLException {
        super(connectionSource, Player.class);
    }

    private void init(){
        try {
            TableUtils.createTableIfNotExists(connectionSource, Player.class);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Player findByNickname(String nickname){
        init();
        try {
            List<Player> playerList = super.queryForEq("nickname", nickname);
            if (playerList.size()==0){
                Player player = new Player();
                player.setNickname(nickname);
                player.setBalance(0L);
                player.setLoan(0L);
                player.setFine(0L);
                create(player);
                return player;
            } else {
                return playerList.get(0);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void save(Player player){
        init();
        try {
            List<Player> playerList = super.queryForEq("nickname", player.getNickname());
            if (playerList.size()==0){
                player.setBalance(0L);
                player.setLoan(0L);
                player.setFine(0L);
                create(player);
            } else {
                update(player);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
