package dev.ua.ikeepcalm.monetaire.commands.forusers;

import dev.jorel.commandapi.annotations.Command;
import dev.jorel.commandapi.annotations.Default;
import dev.jorel.commandapi.annotations.Help;
import dev.jorel.commandapi.annotations.Permission;
import dev.ua.ikeepcalm.monetaire.entities.Advertiser;
import dev.ua.ikeepcalm.monetaire.utils.ChatUtil;
import org.bukkit.entity.Player;

import static dev.ua.ikeepcalm.monetaire.Monetaire.advertiserDao;

@Command("employ")
@Permission("monetaire.shop")
@Help("Використання: /employ")
public class Employ {

    @Default
    public static void employ(Player player) {
        Advertiser advertiser = advertiserDao.findByNickname(player);
        if (advertiser == null){
            advertiser = new Advertiser();
            advertiser.setBalance(0L);
            advertiser.setNickname(player.getName());
            advertiser.setUuid(String.valueOf(player.getUniqueId()));
            advertiserDao.save(advertiser);
            ChatUtil.sendMessage(player,
                    "Ви успішно зареєстрували свій рекламний рахунок!");
        } else {
            ChatUtil.sendMessage(player,
                    "Ви вже зареєстровані як рекламне лице!");
        }
    }
}
