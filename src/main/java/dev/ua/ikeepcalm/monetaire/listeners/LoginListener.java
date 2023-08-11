package dev.ua.ikeepcalm.monetaire.listeners;

import dev.ua.ikeepcalm.monetaire.entities.User;
import dev.ua.ikeepcalm.monetaire.utils.ChatUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import static dev.ua.ikeepcalm.monetaire.Monetaire.playerDao;

public class LoginListener implements Listener {

    @EventHandler
    public void onPlayerLogin(PlayerJoinEvent event) {
        org.bukkit.entity.Player player = event.getPlayer();
        User loggedUser = playerDao.findByNickname(player);
        if (loggedUser.getCard() != null) {
            if (loggedUser.getCard().getFine() > 0) {
                ChatUtil.sendMessage(player,
                        "Ви маєте сплатити штраф!",
                        "Інакше ви не зможете повноцінно користуватися банківською системою!",
                        "Сума штрафів: <#55FFFF>" + loggedUser.getCard().getFine() + " ДР"
                );
            } else if (loggedUser.getCard().getLoan() > 0) {
                ChatUtil.sendMessage(player,
                        "Ви маєте певну заборгованість!",
                        "Щоб позбутися і виплатити її - /paycredit!",
                        "Сума боргу: <#55FFFF>" + loggedUser.getCard().getLoan() + " ДР"
                );
            }
        }
    }
}
