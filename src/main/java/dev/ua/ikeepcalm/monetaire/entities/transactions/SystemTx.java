package dev.ua.ikeepcalm.monetaire.entities.transactions;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import dev.ua.ikeepcalm.monetaire.dao.SystemTxDao;
import dev.ua.ikeepcalm.monetaire.entities.transactions.source.ActionType;
@DatabaseTable(tableName = "systemtxs", daoClass = SystemTxDao.class)
public class SystemTx{

    @DatabaseField(generatedId = true)
    private Long id;

    @DatabaseField
    private String sender;

    @DatabaseField(dataType = DataType.ENUM_STRING)
    private ActionType actionType;

    @DatabaseField
    private Integer amount;

    @DatabaseField
    private String time;


    @DatabaseField(dataType = DataType.BOOLEAN)
    private boolean successful;

    @DatabaseField
    private String momentBalance;

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public ActionType getActionType() {
        return actionType;
    }

    public void setActionType(ActionType actionType) {
        this.actionType = actionType;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isSuccessful() {
        return successful;
    }

    public String getMomentBalance() {
        return momentBalance;
    }

    public void setMomentBalance(String momentBalance) {
        this.momentBalance = momentBalance;
    }

    public void setSuccessful(boolean successful) {
        this.successful = successful;
    }
}
