package dev.ua.ikeepcalm.monetaire.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import dev.ua.ikeepcalm.monetaire.dao.MinfinDao;

@DatabaseTable(tableName = "minfin", daoClass = MinfinDao.class)
public class MinFin {

    @DatabaseField(generatedId = true)
    private Long id;

    @DatabaseField
    private Long balance;

    @DatabaseField
    private Long spentProjects;

    @DatabaseField
    private Long waitCredits;

    @DatabaseField
    private Long waitFines;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBalance() {
        return balance;
    }

    public void setBalance(Long balance) {
        this.balance = balance;
    }

    public Long getSpentProjects() {
        return spentProjects;
    }

    public void setSpentProjects(Long spentProjects) {
        this.spentProjects = spentProjects;
    }

    public Long getWaitCredits() {
        return waitCredits;
    }

    public void setWaitCredits(Long waitCredits) {
        this.waitCredits = waitCredits;
    }

    public Long getWaitFines() {
        return waitFines;
    }

    public void setWaitFines(Long waitFines) {
        this.waitFines = waitFines;
    }
}
