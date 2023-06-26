package dev.ua.ikeepcalm.monetaire.commands;

import dev.jorel.commandapi.annotations.Command;
import dev.jorel.commandapi.annotations.Default;
import dev.jorel.commandapi.annotations.Help;
import dev.jorel.commandapi.annotations.Permission;
import dev.jorel.commandapi.annotations.arguments.AIntegerArgument;
import dev.ua.ikeepcalm.monetaire.entities.transactions.SystemTx;
import dev.ua.ikeepcalm.monetaire.entities.transactions.source.ActionType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static dev.ua.ikeepcalm.monetaire.Monetaire.playerDao;
import static dev.ua.ikeepcalm.monetaire.Monetaire.systemTxDao;

@Command("deposit")
@Permission("monetaire.deposit")
@Help("Використання: /deposit <к-сть діамантової руди>")
public class Deposit {

    @Default
    public static void deposit(Player player, @AIntegerArgument int amount) {
        if (amount> 0 && amount < 3000){
            dev.ua.ikeepcalm.monetaire.entities.Player depositPlayer = playerDao.findByNickname(player.getName());
            if (player.getInventory().containsAtLeast(new ItemStack(Material.DIAMOND_ORE), amount)){
                player.getInventory().removeItemAnySlot(new ItemStack(Material.DIAMOND_ORE, amount));
                depositPlayer.setBalance((depositPlayer.getBalance() + amount));
                playerDao.save(depositPlayer);
                SystemTx systemTx = new SystemTx();
                systemTx.setActionType(ActionType.DEPOSIT);
                systemTx.setSender(depositPlayer.getNickname());
                systemTx.setAmount(amount);
                systemTx.setTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern(("yyyy-MM-dd HH:mm"))));
                systemTx.setSuccessful(true);
                systemTxDao.save(systemTx);
                MiniMessage mm = MiniMessage.miniMessage();
                StringBuilder sb = new StringBuilder();
                sb.append("<bold><#5555FF>-----------------------------------------</bold>\n");
                sb.append("<bold><#5555FF>BANK</bold> <#FFFFFF>> Успішне поповнення на <#55FFFF>").append(amount).append(" ДР\n");
                sb.append("<bold><#5555FF>BANK</bold> <#FFFFFF>> Актуальний рахунок: <#55FFFF>").append(depositPlayer.getBalance()).append(" ДР\n");
                sb.append("<bold><#5555FF>-----------------------------------------</bold>");
                Component parsed = mm.deserialize(sb.toString());
                player.sendMessage(parsed);
            } else if (player.getInventory().containsAtLeast(new ItemStack(Material.DEEPSLATE_DIAMOND_ORE), amount)){
                player.getInventory().removeItemAnySlot(new ItemStack(Material.DEEPSLATE_DIAMOND_ORE, amount));
                depositPlayer.setBalance((depositPlayer.getBalance() + amount));
                playerDao.save(depositPlayer);
                SystemTx systemTx = new SystemTx();
                systemTx.setActionType(ActionType.DEPOSIT);
                systemTx.setSender(depositPlayer.getNickname());
                systemTx.setAmount(amount);
                systemTx.setTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern(("yyyy-MM-dd HH:mm"))));
                systemTx.setSuccessful(true);
                systemTxDao.save(systemTx);
                MiniMessage mm = MiniMessage.miniMessage();
                StringBuilder sb = new StringBuilder();
                sb.append("<bold><#5555FF>-----------------------------------------</bold>\n");
                sb.append("<bold><#5555FF>BANK</bold> <#FFFFFF>> Успішне поповнення на <#55FFFF>").append(amount).append(" ДР\n");
                sb.append("<bold><#5555FF>BANK</bold> <#FFFFFF>> Актуальний рахунок: <#55FFFF>").append(depositPlayer.getBalance()).append(" ДР\n");
                sb.append("<bold><#5555FF>-----------------------------------------</bold>");
                Component parsed = mm.deserialize(sb.toString());
                player.sendMessage(parsed);
            } else {
                SystemTx systemTx = new SystemTx();
                systemTx.setActionType(ActionType.DEPOSIT);
                systemTx.setSender(depositPlayer.getNickname());
                systemTx.setAmount(amount);
                systemTx.setTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern(("yyyy-MM-dd HH:mm"))));
                systemTx.setSuccessful(false);
                systemTxDao.save(systemTx);
                MiniMessage mm = MiniMessage.miniMessage();
                StringBuilder sb = new StringBuilder();
                sb.append("<bold><#5555FF>BANK</bold> <#AAAAAA> > <#FF5555>Недостатня кількість ДР у інвентарі! Спробуйте із меншою сумою");
                Component parsed = mm.deserialize(sb.toString());
                player.sendMessage(parsed);
            }
        } else {
            MiniMessage mm = MiniMessage.miniMessage();
            StringBuilder sb = new StringBuilder();
            sb.append("<bold><#5555FF>BANK</bold> <#AAAAAA> > <#FF5555>Сума поповнення не може бути меньше 1-го, та більше 3000 ДР! Спробуйте ще раз, але з правильними даними!");
            Component parsed = mm.deserialize(sb.toString());
            player.sendMessage(parsed);
        }
    }
}
