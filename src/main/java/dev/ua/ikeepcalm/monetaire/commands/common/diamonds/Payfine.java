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

@Command("payfine")
@Permission("monetaire.player")
@Help("Використання: /payfine")
public class Payfine {

    @Default
    public static void payfine(Player player) {
        EcoUser finedEcoUser = ecoPlayerDao.findByNickname(player);
        if (finedEcoUser.getCard() == null) {
            ChatUtil.sendMessage(player,
                    "У вас немає картки!",
                    "Спочатку виконайте ➜ /card");
        } else {
            if (finedEcoUser.getCard().getFine() == 0){
                ChatUtil.sendMessage(player,
                        "Нічого виплачувати, у вас немає активних штрафів!");
            } else {
                if (finedEcoUser.getCard().getBalance()>= finedEcoUser.getCard().getFine()){
                    finedEcoUser.getCard().setBalance(finedEcoUser.getCard().getBalance()- finedEcoUser.getCard().getFine());
                    finedEcoUser.getCard().setFine(0L);
                    MinFin minFin = minfinDao.getMinfin();
                    minFin.setBalance(minFin.getBalance()+ finedEcoUser.getCard().getFine());
                    minfinDao.save(minFin);
                    SystemTx systemTx = new SystemTx();
                    systemTx.setActionType(ActionType.PAYFINE);
                    systemTx.setSuccessful(true);
                    systemTx.setTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern(("yyyy-MM-dd HH:mm"))));
                    systemTx.setSender(player.getName());
                    systemTx.setAmount(Math.toIntExact(finedEcoUser.getCard().getFine()));
                    systemTx.setMomentBalance("MainBalance: " + finedEcoUser.getCard().getBalance()
                            + " | Credits: "+ finedEcoUser.getCard().getLoan() +" | Fines: " + finedEcoUser.getCard().getFine());
                    systemTxDao.save(systemTx);
                    ecoPlayerDao.save(finedEcoUser);
                    ChatUtil.sendMessage(player,
                            "Ви успішно виплатили свій штраф!");
                } else {
                    SystemTx systemTx = new SystemTx();
                    systemTx.setActionType(ActionType.PAYFINE);
                    systemTx.setSuccessful(false);
                    systemTx.setTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern(("yyyy-MM-dd HH:mm"))));
                    systemTx.setSender(player.getName());
                    systemTx.setAmount(Math.toIntExact(finedEcoUser.getCard().getFine()));
                    systemTx.setMomentBalance("MainBalance: " + finedEcoUser.getCard().getBalance()
                            + " | Credits: "+ finedEcoUser.getCard().getLoan() +" | Fines: " + finedEcoUser.getCard().getFine());
                    systemTxDao.save(systemTx);
                    ChatUtil.sendMessage(player,
                            "Недостатньо коштів на балансі!",
                            "Поповніть свій рахунок і спробуйте ще раз!");
                }
            }
        }
    }
}
