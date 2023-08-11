package dev.ua.ikeepcalm.monetaire.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import dev.ua.ikeepcalm.monetaire.dao.PlayerDao;

@DatabaseTable(tableName = "players", daoClass = PlayerDao.class)
public class User {

    @DatabaseField(generatedId = true)
    private Long id;

    @DatabaseField(canBeNull = false, unique = true)
    private String nickname;

    @DatabaseField(unique = true)
    private String uuid;

    @DatabaseField(defaultValue = "false")
    private boolean autoDeposit;

    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private Card card;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public boolean getAutoDeposit() {
        return autoDeposit;
    }

    public void setAutoDeposit(boolean autoDeposit) {
        this.autoDeposit = autoDeposit;
    }


    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }
}
