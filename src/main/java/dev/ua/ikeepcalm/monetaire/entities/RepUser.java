package dev.ua.ikeepcalm.monetaire.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import dev.ua.ikeepcalm.monetaire.dao.RepPlayerDao;


@DatabaseTable(tableName = "players", daoClass = RepPlayerDao.class)
public class RepUser {

    @DatabaseField(generatedId = true)
    private Long id;

    @DatabaseField(canBeNull = false, unique = true)
    private String nickname;

    @DatabaseField(unique = true)
    private String uuid;

    @DatabaseField(defaultValue = "0")
    private int reverence;

    @DatabaseField(defaultValue = "2")
    private int credits;

    @DatabaseField(defaultValue = "false")
    private boolean isAdmin;

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

    public int getCredits() {
        return credits;
    }

    public int getReverence() {
        return reverence;
    }

    public void setReverence(int reverence) {
        this.reverence = reverence;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }
}
