package dev.ua.ikeepcalm.monetaire.commands.common.diamonds;

import dev.jorel.commandapi.annotations.Command;
import dev.jorel.commandapi.annotations.Default;
import dev.jorel.commandapi.annotations.Permission;
import dev.jorel.commandapi.annotations.arguments.AIntegerArgument;
import dev.ua.ikeepcalm.monetaire.entities.MinFin;
import dev.ua.ikeepcalm.monetaire.entities.EcoUser;
import dev.ua.ikeepcalm.monetaire.entities.transactions.SystemTx;
import dev.ua.ikeepcalm.monetaire.entities.transactions.source.ActionType;
import dev.ua.ikeepcalm.monetaire.utils.ChatUtil;
import org.bukkit.entity.Player;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static dev.ua.ikeepcalm.monetaire.Monetaire.*;

@Command("sponsor")
@Permission("monetaire.player")
public class Sponsor {

    @Default
    public static void sponsor(Player player, @AIntegerArgument int amount){
        if (amount> 0 && amount < 25000){
            EcoUser sponsorEcoUser = ecoPlayerDao.findByNickname(player);
            if (sponsorEcoUser.getCard() == null) {
                ChatUtil.sendMessage(player,
                        "У вас немає картки!",
                        "Спочатку виконайте ➜ /card");
            } else {
                if (sponsorEcoUser.getCard().getFine() > 0){
                    ChatUtil.sendMessage(player,
                            "Ви маєте сплатити штраф!",
                            "Інакше ви не зможете повноцінно користуватися банківською системою!",
                            "Сума штрафів: <#55FFFF>" + sponsorEcoUser.getCard().getFine() + " ДР"
                            );
                } else {
                    if (sponsorEcoUser.getCard().getBalance() >= amount){
                        sponsorEcoUser.getCard().setBalance((sponsorEcoUser.getCard().getBalance() - amount));
                        sponsorEcoUser.getCard().setSponsored(sponsorEcoUser.getCard().getSponsored()+amount);
                        ecoPlayerDao.save(sponsorEcoUser);
                        SystemTx systemTx = new SystemTx();
                        systemTx.setActionType(ActionType.SPONSOR);
                        systemTx.setSender(sponsorEcoUser.getNickname());
                        systemTx.setAmount(amount);
                        systemTx.setTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern(("yyyy-MM-dd HH:mm"))));
                        systemTx.setSuccessful(true);
                        systemTx.setMomentBalance("MainBalance: " + sponsorEcoUser.getCard().getBalance()
                                + " | Credits: "+ sponsorEcoUser.getCard().getLoan() +" | Fines: " + sponsorEcoUser.getCard().getFine());
                        systemTxDao.save(systemTx);
                        MinFin minFin = minfinDao.getMinfin();
                        minFin.setBalance(minFin.getBalance()+amount);
                        minfinDao.save(minFin);
                        ChatUtil.broadcastMessage(
                                "Гравець <#FFAA00>" + player.getName() + "<#FFFFFF> проспонсорував МінФін на <#55FFFF>" + amount +" ДР!"
                        );
                    } else {
                        SystemTx systemTx = new SystemTx();
                        systemTx.setActionType(ActionType.SPONSOR);
                        systemTx.setSender(sponsorEcoUser.getNickname());
                        systemTx.setAmount(amount);
                        systemTx.setTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern(("yyyy-MM-dd HH:mm"))));
                        systemTx.setSuccessful(false);
                        systemTxDao.save(systemTx);
                        ChatUtil.sendMessage(player,
                                "Недостатня кількість коштів на балансі!",
                                "Актуальний рахунок: <#55FFFF>" + sponsorEcoUser.getCard().getBalance() + " ДР"
                        );
                    }
                }
            }
        } else {
            ChatUtil.sendMessage(player,
                    "Сума спонсорування не може бути меньше 1-го, та більше 2500 ДР за один раз ДР!!"
            );
        }

    }
}
