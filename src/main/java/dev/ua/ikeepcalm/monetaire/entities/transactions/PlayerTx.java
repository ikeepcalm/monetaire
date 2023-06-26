package dev.ua.ikeepcalm.monetaire.entities.transactions;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import dev.ua.ikeepcalm.monetaire.dao.PlayerTxDao;
import dev.ua.ikeepcalm.monetaire.entities.transactions.source.ActionType;

@DatabaseTable(tableName = "playertxs", daoClass = PlayerTxDao.class)
public class PlayerTx {

    @DatabaseField(generatedId = true)
    private Long id;

    @DatabaseField
    private String sender;

    @DatabaseField
    private String recipient;

    @DatabaseField(dataType = DataType.ENUM_STRING)
    private ActionType actionType;

    @DatabaseField
    private Integer amount;

    @DatabaseField
    private String time;

    @DatabaseField(dataType = DataType.BOOLEAN)
    private boolean successful;

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

    public void setSuccessful(boolean successful) {
        this.successful = successful;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }
}

