package dev.ua.ikeepcalm.monetaire.listeners;

import dev.ua.ikeepcalm.monetaire.entities.EcoUser;
import dev.ua.ikeepcalm.monetaire.utils.ChatUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import static dev.ua.ikeepcalm.monetaire.Monetaire.ecoPlayerDao;

public class LoginListener implements Listener {

    @EventHandler
    public void onPlayerLogin(PlayerJoinEvent event) {
        org.bukkit.entity.Player player = event.getPlayer();
        EcoUser loggedEcoUser = ecoPlayerDao.findByNickname(player);
        if (loggedEcoUser.getCard() != null) {
            if (loggedEcoUser.getCard().getFine() > 0) {
                ChatUtil.sendMessage(player,
                        "Ви маєте сплатити штраф!",
                        "Інакше ви не зможете повноцінно користуватися банківською системою!",
                        "Сума штрафів: <#55FFFF>" + loggedEcoUser.getCard().getFine() + " ДР"
                );
            } else if (loggedEcoUser.getCard().getLoan() > 0) {
                ChatUtil.sendMessage(player,
                        "Ви маєте певну заборгованість!",
                        "Щоб позбутися і виплатити її - /paycredit!",
                        "Сума боргу: <#55FFFF>" + loggedEcoUser.getCard().getLoan() + " ДР"
                );
            }
        }
    }
}
