package dev.ua.ikeepcalm.monetaire.commands.common;

import dev.jorel.commandapi.annotations.Command;
import dev.jorel.commandapi.annotations.Default;
import dev.jorel.commandapi.annotations.Help;
import dev.jorel.commandapi.annotations.Permission;
import dev.ua.ikeepcalm.monetaire.entities.EcoUser;
import dev.ua.ikeepcalm.monetaire.utils.ChatUtil;
import org.bukkit.entity.Player;

import static dev.ua.ikeepcalm.monetaire.Monetaire.cardDao;
import static dev.ua.ikeepcalm.monetaire.Monetaire.ecoPlayerDao;

@Command("card")
@Permission("monetaire.player")
@Help("Використання: /card")
public class CardIssue {

    @Default
    public static void card(Player player) {
        EcoUser foundEcoUser = ecoPlayerDao.findByNickname(player);
        if (foundEcoUser.getCard() != null) {
            ChatUtil.sendMessage(player,
                    "У вас вже є картка ➜ " + foundEcoUser.getCard().getNumber());
        } else {
//            Location location = player.getLocation();
//            int x = location.getBlockX();
//            int y = location.getBlockY();
//            int z = location.getBlockZ();
//            if ((x > 24 && x < 39)  && (y > 60  && y < 70) && (z > -25 && z < -16)){
                dev.ua.ikeepcalm.monetaire.entities.Card card = cardDao.create(foundEcoUser);
                ChatUtil.sendMessage(player,
                        "Вітаємо з реєстрацією картки! Ваш номер картки ➜ " + card.getNumber(),
                        "Ваш унікальний код картки: (" + card.getCvv() + "). Щоб не загубити його натисніть на ➜ F2"
                        );
//            } else {
//                ChatUtil.sendMessage(player,
//                "Ця команда дійсна лише у будівлі банку. Вона знаходиться на координатах ➜ 41, 65, -17 ( Спавн )");
//            }
        }
    }
}
