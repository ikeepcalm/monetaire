package dev.ua.ikeepcalm.monetaire.dao;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import dev.ua.ikeepcalm.monetaire.entities.Advertiser;

import java.sql.SQLException;
import java.util.List;

public class AdvertiserDao extends BaseDaoImpl<Advertiser, Long> {

    public AdvertiserDao(ConnectionSource connectionSource) throws SQLException {
        super(connectionSource, Advertiser.class);
    }

    private void init(){
        try {
            TableUtils.createTableIfNotExists(connectionSource, Advertiser.class);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Advertiser findByNickname(org.bukkit.entity.Player input){
        init();
        try {
            List<Advertiser> playerList = super.queryForEq("nickname", input.getName());
            if (playerList.size()==0){
                return null;
            } else {
                Advertiser advertiser = playerList.get(0);
                if (advertiser.getUuid() == null){
                    advertiser.setUuid(input.getUniqueId().toString());
                    save(advertiser);
                    return advertiser;
                } else {
                   return advertiser;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void save(Advertiser advertiser){
        init();
        try {
            List<Advertiser> playerList = super.queryForEq("nickname", advertiser.getNickname());
            if (playerList.size()==0){
                advertiser.setBalance(0L);
                create(advertiser);
            } else {
                update(advertiser);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
