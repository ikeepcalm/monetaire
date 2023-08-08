package dev.ua.ikeepcalm.monetaire.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import dev.ua.ikeepcalm.monetaire.dao.PlayerDao;

@DatabaseTable(tableName = "players", daoClass = PlayerDao.class)
public class Player {

    @DatabaseField(generatedId = true)
    private Long id;

    @DatabaseField(canBeNull = false, unique = true)
    private String nickname;

    @DatabaseField(unique = true)
    private String uuid;

    @DatabaseField
    private Long balance;

    @DatabaseField
    private Long onlineb;

    @DatabaseField
    private Long loan;

    @DatabaseField
    private Long fine;

    @DatabaseField(defaultValue = "0")
    private Long sponsored;

    @DatabaseField(defaultValue = "false")
    private boolean autoDeposit;

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

    public Long getLoan() {
        return loan;
    }

    public void setLoan(Long loan) {
        this.loan = loan;
    }

    public Long getFine() {
        return fine;
    }

    public void setFine(Long fine) {
        this.fine = fine;
    }

    public Long getSponsored() {
        return sponsored;
    }

    public void setSponsored(Long sponsored) {
        this.sponsored = sponsored;
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

    public Long getOnlineb() {
        return onlineb;
    }

    public void setOnlineb(Long onlineb) {
        this.onlineb = onlineb;
    }
}
