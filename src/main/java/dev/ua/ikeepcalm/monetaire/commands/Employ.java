package dev.ua.ikeepcalm.monetaire.commands;

import dev.jorel.commandapi.annotations.Command;
import dev.jorel.commandapi.annotations.Default;
import dev.jorel.commandapi.annotations.Help;
import dev.jorel.commandapi.annotations.Permission;
import dev.ua.ikeepcalm.monetaire.entities.Advertiser;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
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
            MiniMessage mm = MiniMessage.miniMessage();
            StringBuilder sb = new StringBuilder();
            sb.append("<bold><#5555FF>ADS</bold> <#FFFFFF>> Ви успішно зареєстрували свій рекламний рахунок!");
            Component parsed = mm.deserialize(sb.toString());
            player.sendMessage(parsed);
        } else {
            MiniMessage mm = MiniMessage.miniMessage();
            StringBuilder sb = new StringBuilder();
            sb.append("<bold><#5555FF>ADS</bold> <#FFFFFF>> Ви вже зареєстровані як рекламне лице!");
            Component parsed = mm.deserialize(sb.toString());
            player.sendMessage(parsed);
        }
    }
}
