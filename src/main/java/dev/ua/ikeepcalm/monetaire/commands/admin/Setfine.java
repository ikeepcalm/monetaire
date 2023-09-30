package dev.ua.ikeepcalm.monetaire.commands.admin;

import dev.jorel.commandapi.annotations.Command;
import dev.jorel.commandapi.annotations.Default;
import dev.jorel.commandapi.annotations.Help;
import dev.jorel.commandapi.annotations.Permission;
import dev.jorel.commandapi.annotations.arguments.AIntegerArgument;
import dev.jorel.commandapi.annotations.arguments.APlayerArgument;
import dev.ua.ikeepcalm.monetaire.entities.User;
import dev.ua.ikeepcalm.monetaire.entities.transactions.SystemTx;
import dev.ua.ikeepcalm.monetaire.entities.transactions.source.ActionType;
import dev.ua.ikeepcalm.monetaire.utils.ChatUtil;
import org.bukkit.entity.Player;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static dev.ua.ikeepcalm.monetaire.Monetaire.playerDao;
import static dev.ua.ikeepcalm.monetaire.Monetaire.systemTxDao;

@Command("setfine")
@Permission("monetaire.admin")
@Help("Використання: /setfine <гравець> <к-сть діамантової руди>")
public class Setfine {

    @Default
    public static void setfine(Player finer, @APlayerArgument Player fined, @AIntegerArgument int amount) {
        if (amount > 0) {
            User foundFined = playerDao.findByNickname(fined);
            if (foundFined.getCard() != null) {
                foundFined.getCard().setFine(foundFined.getCard().getFine() + amount);
                playerDao.save(foundFined);
                SystemTx systemTx = new SystemTx();
                systemTx.setActionType(ActionType.SETFINE);
                systemTx.setSuccessful(true);
                systemTx.setTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern(("yyyy-MM-dd HH:mm"))));
                systemTx.setSender(fined.getName());
                systemTx.setAmount(amount);
                systemTx.setMomentBalance("MainBalance: " + foundFined.getCard().getBalance()
                        + " | Credits: " + foundFined.getCard().getLoan() + " | Fines: " + foundFined.getCard().getFine());
                systemTxDao.save(systemTx);
                ChatUtil.sendMessage(finer,
                        "Ви встановили штраф <#55FFFF>" + amount + " ДР <#FFFFFF> гравцю " + fined.getName());

                ChatUtil.sendMessage(fined,
                        "Вам встановлено штраф на суму <#55FFFF>" + amount + " ДР <#FFFFFF>",
                        "Негайно виплатіть його, інакше ви втратите доступ до системи економіки!!");
            } else {
                ChatUtil.sendMessage(finer,
                        "У цього гравця немає картки!");
            }
        } else {
            ChatUtil.sendMessage(finer,
                    "Штраф не може бути від'ємним!");
        }
    }
}
