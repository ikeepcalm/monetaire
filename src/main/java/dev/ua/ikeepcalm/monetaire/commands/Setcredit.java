package dev.ua.ikeepcalm.monetaire.commands;

import dev.jorel.commandapi.annotations.Command;
import dev.jorel.commandapi.annotations.Default;
import dev.jorel.commandapi.annotations.Help;
import dev.jorel.commandapi.annotations.Permission;
import dev.jorel.commandapi.annotations.arguments.AIntegerArgument;
import dev.jorel.commandapi.annotations.arguments.APlayerArgument;
import dev.jorel.commandapi.annotations.arguments.AStringArgument;
import dev.ua.ikeepcalm.monetaire.entities.MinFin;
import dev.ua.ikeepcalm.monetaire.entities.transactions.SystemTx;
import dev.ua.ikeepcalm.monetaire.entities.transactions.source.ActionType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static dev.ua.ikeepcalm.monetaire.Monetaire.*;

@Command("setcredit")
@Permission("monetaire.setcredit")
@Help("Використання: /setfine <гравець> <к-сть діамантової руди> <тариф>")
public class Setcredit {

    @Default
    public static void setcredit(Player finer, @APlayerArgument Player credited, @AIntegerArgument int amount, @AStringArgument String tarif) {
        if (amount > 0) {
            if (tarif.equals("oneweek") || tarif.equals("twoweek") || tarif.equals("threeweek")) {
                dev.ua.ikeepcalm.monetaire.entities.Player foundCredited = playerDao.findByNickname(credited);
                MinFin minFin = minfinDao.getMinfin();
                minFin.setBalance(minFin.getBalance()- amount);
                long finalAmount = 0;
                switch (tarif) {
                    case "oneweek" -> {
                        finalAmount = (long) (amount + amount * 0.02);
                        foundCredited.setLoan(finalAmount);
                    }
                    case "twoweek" -> {
                        finalAmount = (long) (amount + amount * 0.03);
                        foundCredited.setLoan(finalAmount);
                    }
                    case "threeweek" -> {
                        finalAmount = (long) (amount + amount * 0.04);
                        foundCredited.setLoan(finalAmount);
                    }
                }
                minFin.setWaitCredits(minFin.getWaitCredits() + finalAmount);
                minfinDao.save(minFin);
                if ((foundCredited.getBalance() + amount) <= 1344) {
                    foundCredited.setBalance(foundCredited.getBalance() + amount);
                } else {
                    foundCredited.setBalance(1344L);
                    int exceededAmount = (int) (foundCredited.getBalance() + amount - 1344);
                    ItemStack itemStack = new ItemStack(Material.DEEPSLATE_DIAMOND_ORE, exceededAmount);
                    credited.getWorld().dropItemNaturally(credited.getLocation().add(0,1,0), itemStack);
                    credited.getWorld().spawnParticle(Particle.BUBBLE_POP, credited.getLocation(), 20);
                }

                playerDao.save(foundCredited);
                SystemTx systemTx = new SystemTx();
                systemTx.setActionType(ActionType.SETCREDIT);
                systemTx.setSuccessful(true);
                systemTx.setTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern(("yyyy-MM-dd HH:mm"))));
                systemTx.setSender(credited.getName());
                systemTx.setAmount(amount);
                systemTxDao.save(systemTx);
                MiniMessage mm = MiniMessage.miniMessage();
                StringBuilder sb = new StringBuilder();
                sb.append("<bold><#5555FF>-----------------------------------------</bold>\n");
                sb.append("<bold><#5555FF>BANK</bold> <#FFFFFF>> Ви надали позику на суму <#55FFFF>").append(foundCredited.getLoan()).append(" ДР <#FFFFFF>гравцю <#FFAA00>").append(credited.getName()).append("\n");
                sb.append("<bold><#5555FF>-----------------------------------------</bold>\n");
                Component parsed = mm.deserialize(sb.toString());
                finer.sendMessage(parsed);

                StringBuilder sb2 = new StringBuilder();
                sb2.append("<bold><#5555FF>-----------------------------------------</bold>\n");
                sb2.append("<bold><#5555FF>BANK</bold> <#FFFFFF>> Вам надано позику на суму <#55FFFF>").append(amount).append(" ДР <#FFFFFF>").append("\n");
                sb2.append("<bold><#5555FF>BANK</bold> <#FFFFFF>> Ви маєте сплатити <#55FFFF>").append(finalAmount).append(" ДР<#FFFFFF>, щоб позбутися заборгованності!").append("\n");
                sb2.append("<bold><#5555FF>-----------------------------------------</bold>\n");
                Component parsed2 = mm.deserialize(sb2.toString());
                credited.sendMessage(parsed2);
            } else {
                MiniMessage mm = MiniMessage.miniMessage();
                StringBuilder sb = new StringBuilder();
                sb.append("<bold><#5555FF>-----------------------------------------</bold>\n");
                sb.append("<bold><#5555FF>BANK</bold> <#FFFFFF>> Помилка у написанні тарифу! <#55FFFF>").append("\n");
                sb.append("<bold><#5555FF>-----------------------------------------</bold>\n");
                Component parsed = mm.deserialize(sb.toString());
                finer.sendMessage(parsed);
            }
        } else {
            MiniMessage mm = MiniMessage.miniMessage();
            StringBuilder sb = new StringBuilder();
            sb.append("<bold><#5555FF>-----------------------------------------</bold>\n");
            sb.append("<bold><#5555FF>BANK</bold> <#FFFFFF>> Позика не може бути від'ємною! <#55FFFF>").append("\n");
            sb.append("<bold><#5555FF>-----------------------------------------</bold>\n");
            Component parsed = mm.deserialize(sb.toString());
            finer.sendMessage(parsed);
        }
    }
}
