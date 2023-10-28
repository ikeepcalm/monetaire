package dev.ua.ikeepcalm.monetaire.commands.common.diamonds;


import dev.jorel.commandapi.annotations.Command;
import dev.jorel.commandapi.annotations.Default;
import dev.jorel.commandapi.annotations.Help;
import dev.jorel.commandapi.annotations.Permission;
import dev.ua.ikeepcalm.monetaire.entities.MinFin;
import dev.ua.ikeepcalm.monetaire.entities.EcoUser;
import dev.ua.ikeepcalm.monetaire.entities.transactions.SystemTx;
import dev.ua.ikeepcalm.monetaire.entities.transactions.source.ActionType;
import dev.ua.ikeepcalm.monetaire.utils.ChatUtil;
import org.bukkit.entity.Player;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static dev.ua.ikeepcalm.monetaire.Monetaire.*;

@Command("paycredit")
@Permission("monetaire.player")
@Help("Використання: /paycredit")
public class Paycredit {

    @Default
    public static void paycredit(Player player) {
        EcoUser creditedEcoUser = ecoPlayerDao.findByNickname(player);
        if (creditedEcoUser.getCard() == null) {
            ChatUtil.sendMessage(player,
                    "У вас немає картки!",
                    "Спочатку виконайте ➜ /card");
        } else {
            if (creditedEcoUser.getCard().getLoan() == 0){
                ChatUtil.sendMessage(player,
                        "Нічого виплачувати, у вас немає заборгованності!");
            } else {
                if (creditedEcoUser.getCard().getBalance()>= creditedEcoUser.getCard().getLoan()){
                    MinFin minFin = minfinDao.getMinfin();
                    minFin.setBalance(minFin.getBalance() + creditedEcoUser.getCard().getLoan());
                    minFin.setWaitCredits(minFin.getWaitCredits() - creditedEcoUser.getCard().getLoan());
                    creditedEcoUser.getCard().setBalance(creditedEcoUser.getCard().getBalance() - creditedEcoUser.getCard().getLoan());
                    creditedEcoUser.getCard().setLoan(0L);
                    minfinDao.save(minFin);
                    ecoPlayerDao.save(creditedEcoUser);
                    SystemTx systemTx = new SystemTx();
                    systemTx.setActionType(ActionType.PAYCREDIT);
                    systemTx.setSuccessful(true);
                    systemTx.setTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern(("yyyy-MM-dd HH:mm"))));
                    systemTx.setSender(player.getName());
                    systemTx.setAmount(Math.toIntExact(creditedEcoUser.getCard().getFine()));
                    systemTx.setMomentBalance("MainBalance: " + creditedEcoUser.getCard().getBalance()
                            + " | Credits: "+ creditedEcoUser.getCard().getLoan() +" | Fines: " + creditedEcoUser.getCard().getFine());
                    systemTxDao.save(systemTx);
                    ChatUtil.sendMessage(player,
                            "Ви успішно позбулися своєї заборгованності!");
                } else {
                    SystemTx systemTx = new SystemTx();
                    systemTx.setActionType(ActionType.PAYCREDIT);
                    systemTx.setSuccessful(false);
                    systemTx.setTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern(("yyyy-MM-dd HH:mm"))));
                    systemTx.setSender(player.getName());
                    systemTx.setAmount(Math.toIntExact(creditedEcoUser.getCard().getFine()));
                    systemTx.setMomentBalance("MainBalance: " + creditedEcoUser.getCard().getBalance()
                            + " | Credits: "+ creditedEcoUser.getCard().getLoan() +" | Fines: " + creditedEcoUser.getCard().getFine());
                    systemTxDao.save(systemTx);
                    ChatUtil.sendMessage(player,
                            "Недостатньо коштів на балансі!",
                            "Поповніть свій рахунок і спробуйте ще раз!");
                }
            }
        }
    }
}
