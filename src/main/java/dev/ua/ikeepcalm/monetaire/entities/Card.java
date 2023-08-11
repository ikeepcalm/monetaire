package dev.ua.ikeepcalm.monetaire.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import dev.ua.ikeepcalm.monetaire.dao.CardDao;

@DatabaseTable(tableName = "cards", daoClass = CardDao.class)
public class Card {


    @DatabaseField(generatedId = true)
    private Long id;

    @DatabaseField
    private String number;

    @DatabaseField
    private int cvv;

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


    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private User holder;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getCvv() {
        return cvv;
    }

    public void setCvv(int cvv) {
        this.cvv = cvv;
    }

    public User getHolder() {
        return holder;
    }

    public void setHolder(User holder) {
        this.holder = holder;
    }

    public Long getBalance() {
        return balance;
    }

    public void setBalance(Long balance) {
        this.balance = balance;
    }

    public Long getOnlineb() {
        return onlineb;
    }

    public void setOnlineb(Long onlineb) {
        this.onlineb = onlineb;
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
