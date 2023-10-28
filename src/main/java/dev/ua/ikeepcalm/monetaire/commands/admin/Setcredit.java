package dev.ua.ikeepcalm.monetaire.commands.admin;

import dev.jorel.commandapi.annotations.Command;
import dev.jorel.commandapi.annotations.Default;
import dev.jorel.commandapi.annotations.Help;
import dev.jorel.commandapi.annotations.Permission;
import dev.jorel.commandapi.annotations.arguments.AIntegerArgument;
import dev.jorel.commandapi.annotations.arguments.APlayerArgument;
import dev.jorel.commandapi.annotations.arguments.AStringArgument;
import dev.ua.ikeepcalm.monetaire.entities.MinFin;
import dev.ua.ikeepcalm.monetaire.entities.EcoUser;
import dev.ua.ikeepcalm.monetaire.entities.transactions.SystemTx;
import dev.ua.ikeepcalm.monetaire.entities.transactions.source.ActionType;
import dev.ua.ikeepcalm.monetaire.utils.ChatUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static dev.ua.ikeepcalm.monetaire.Monetaire.*;

@Command("setcredit")
@Permission("monetaire.admin")
@Help("Використання: /setfine <гравець> <к-сть діамантової руди> <тариф>")
public class Setcredit {

    @Default
    public static void setcredit(Player crediter, @APlayerArgument Player credited, @AIntegerArgument int amount, @AStringArgument String tarif) {
        if (amount > 0) {
            if (tarif.equals("oneweek") || tarif.equals("twoweek") || tarif.equals("threeweek")) {
                EcoUser creditedEcoUser = ecoPlayerDao.findByNickname(credited);
                if (creditedEcoUser.getCard() == null) {
                    MinFin minFin = minfinDao.getMinfin();
                    minFin.setBalance(minFin.getBalance() - amount);
                    long finalAmount = 0;
                    switch (tarif) {
                        case "oneweek" -> {
                            finalAmount = (long) (amount + amount * 0.02);
                            creditedEcoUser.getCard().setLoan(finalAmount);
                        }
                        case "twoweek" -> {
                            finalAmount = (long) (amount + amount * 0.03);
                            creditedEcoUser.getCard().setLoan(finalAmount);
                        }
                        case "threeweek" -> {
                            finalAmount = (long) (amount + amount * 0.04);
                            creditedEcoUser.getCard().setLoan(finalAmount);
                        }
                    }
                    minFin.setWaitCredits(minFin.getWaitCredits() + finalAmount);
                    minfinDao.save(minFin);
                    if ((creditedEcoUser.getCard().getBalance() + amount) <= 1344) {
                        creditedEcoUser.getCard().setBalance(creditedEcoUser.getCard().getBalance() + amount);
                    } else {
                        creditedEcoUser.getCard().setBalance(1344L);
                        int exceededAmount = (int) (creditedEcoUser.getCard().getBalance() + amount - 1344);
                        ItemStack itemStack = new ItemStack(Material.DEEPSLATE_DIAMOND_ORE, exceededAmount);
                        credited.getWorld().dropItemNaturally(credited.getLocation().add(0, 1, 0), itemStack);
                    }

                    ecoPlayerDao.save(creditedEcoUser);
                    SystemTx systemTx = new SystemTx();
                    systemTx.setActionType(ActionType.SETCREDIT);
                    systemTx.setSuccessful(true);
                    systemTx.setTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern(("yyyy-MM-dd HH:mm"))));
                    systemTx.setSender(credited.getName());
                    systemTx.setAmount(amount);
                    systemTx.setMomentBalance("MainBalance: " + creditedEcoUser.getCard().getBalance()
                            + " | Credits: " + creditedEcoUser.getCard().getLoan() + " | Fines: " + creditedEcoUser.getCard().getFine());

                    systemTxDao.save(systemTx);
                    ChatUtil.sendMessage(crediter,
                            "Ви надали позику на суму <#55FFFF>" + creditedEcoUser.getCard().getLoan() + " ДР <#FFFFFF>гравцю <#FFAA00>" + creditedEcoUser.getNickname());

                    ChatUtil.sendMessage(credited,
                            "Вам надано позику на суму <#55FFFF>" + amount + " ДР <#FFFFFF>",
                            "Ви маєте сплатити <#55FFFF>" + finalAmount + " ДР <#FFFFFF>, щоб виплатити борг!");
                } else {
                    ChatUtil.sendMessage(crediter,
                            "У цього гравця немає картки!");
                }
            } else {
                ChatUtil.sendMessage(crediter,
                        "Помилка у написанні тарифу!");
            }
        } else {
            ChatUtil.sendMessage(crediter,
                    "Позика не може бути від'ємною!");
        }
    }
}
