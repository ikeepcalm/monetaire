package dev.ua.ikeepcalm.monetaire.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import dev.ua.ikeepcalm.monetaire.dao.AdvertiserDao;

@DatabaseTable(tableName = "advertisers", daoClass = AdvertiserDao.class)
public class Advertiser {

    @DatabaseField(generatedId = true)
    private Long id;

    @DatabaseField(canBeNull = false, unique = true)
    private String nickname;

    @DatabaseField(unique = true)
    private String uuid;

    @DatabaseField
    private Long balance;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Long getBalance() {
        return balance;
    }

    public void setBalance(Long balance) {
        this.balance = balance;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

}
