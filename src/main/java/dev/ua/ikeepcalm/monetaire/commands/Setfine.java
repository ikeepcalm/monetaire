package dev.ua.ikeepcalm.monetaire.commands;

import dev.jorel.commandapi.annotations.Command;
import dev.jorel.commandapi.annotations.Default;
import dev.jorel.commandapi.annotations.Help;
import dev.jorel.commandapi.annotations.Permission;
import dev.jorel.commandapi.annotations.arguments.AIntegerArgument;
import dev.jorel.commandapi.annotations.arguments.APlayerArgument;
import dev.ua.ikeepcalm.monetaire.entities.transactions.SystemTx;
import dev.ua.ikeepcalm.monetaire.entities.transactions.source.ActionType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static dev.ua.ikeepcalm.monetaire.Monetaire.playerDao;
import static dev.ua.ikeepcalm.monetaire.Monetaire.systemTxDao;

@Command("setfine")
@Permission("monetaire.setfine")
@Help("Використання: /setfine <гравець> <к-сть діамантової руди>")
public class Setfine {

    @Default
    public static void setfine(Player finer, @APlayerArgument Player fined, @AIntegerArgument int amount) {
        dev.ua.ikeepcalm.monetaire.entities.Player foundFined = playerDao.findByNickname(fined.getName());
        foundFined.setFine(foundFined.getFine()+amount);
        playerDao.save(foundFined);
        SystemTx systemTx = new SystemTx();
        systemTx.setActionType(ActionType.SETFINE);
        systemTx.setSuccessful(true);
        systemTx.setTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern(("yyyy-MM-dd HH:mm"))));
        systemTx.setSender(fined.getName());
        systemTx.setAmount(amount);
        systemTxDao.save(systemTx);
        MiniMessage mm = MiniMessage.miniMessage();
        StringBuilder sb = new StringBuilder();
        sb.append("<bold><#5555FF>-----------------------------------------</bold>\n");
        sb.append("<bold><#5555FF>BANK</bold> <#FFFFFF>> Ви встановили штраф <#55FFFF>").append(amount).append(" ДР <#FFFFFF>гравцю <#FFAA00>").append(fined.getName()).append("\n");
        sb.append("<bold><#5555FF>-----------------------------------------</bold>\n");
        Component parsed = mm.deserialize(sb.toString());
        finer.sendMessage(parsed);
        StringBuilder sb1 = new StringBuilder();
        sb1.append("<bold><#5555FF>-----------------------------------------</bold>\n");
        sb1.append("<bold><#5555FF>BANK</bold> <#FFFFFF>> Ви отримали штраф сумою <#55FFFF>").append(amount).append(" ДР!\n");
        sb1.append("<bold><#5555FF>BANK</bold> <#FFFFFF>> Негайно виплатіть його, інакше ви втратите доступ до системи економіки!\n");
        sb1.append("<bold><#5555FF>-----------------------------------------</bold>");
        Component parsed1 = mm.deserialize(sb1.toString());
        fined.sendMessage(parsed1);
    }
}
