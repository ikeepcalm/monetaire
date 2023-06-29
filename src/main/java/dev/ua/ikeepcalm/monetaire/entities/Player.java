package dev.ua.ikeepcalm.monetaire.entities;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import dev.ua.ikeepcalm.monetaire.dao.PlayerDao;

@DatabaseTable(tableName = "players", daoClass = PlayerDao.class)
public class Player {

    @DatabaseField(generatedId = true)
    private Long id;

    @DatabaseField(canBeNull = false, unique = true)
    private String nickname;
    @DatabaseField
    private Long balance;

    @DatabaseField
    private Long loan;

    @DatabaseField
    private Long fine;

    @DatabaseField
    private Long sponsored;

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
}
