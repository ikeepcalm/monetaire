package dev.ua.ikeepcalm.monetaire.commands.common.reverence;

import dev.jorel.commandapi.annotations.*;
import dev.jorel.commandapi.annotations.arguments.APlayerArgument;
import dev.jorel.commandapi.annotations.arguments.AStringArgument;
import dev.ua.ikeepcalm.monetaire.entities.RepUser;
import dev.ua.ikeepcalm.monetaire.utils.ChatUtil;
import org.bukkit.entity.Player;

import static dev.ua.ikeepcalm.monetaire.Monetaire.repPlayerDao;

@Command("rep")
@Alias({"review"})
@Permission("monetaire.rep")
@Help("Використання: /review; /rep")
public class Rep {

    @Default
    public static void rep(Player repGivePlayer, @APlayerArgument Player repTakePlayer, @AStringArgument String action) {
        RepUser foundRepGivePlayer = repPlayerDao.findByNickname(repGivePlayer);
        RepUser foundRepTakePlayer = repPlayerDao.findByNickname(repTakePlayer);
        if (foundRepGivePlayer.isAdmin() || !foundRepTakePlayer.isAdmin()){
            ChatUtil.sendMessage(repGivePlayer, "<#55FFFF>Цю команду можуть використовувати лише звичайні гравці", "<#55FFFF>І тільки на представників адміністрації!");
        } else {
            switch (action){
                case "+" -> {
                    if (foundRepTakePlayer.getCredits() > 0){
                        foundRepTakePlayer.setReverence(foundRepTakePlayer.getReverence() + 1);
                        foundRepGivePlayer.setCredits(foundRepGivePlayer.getCredits() - 1);
                        repPlayerDao.save(foundRepGivePlayer);
                        repPlayerDao.save(foundRepTakePlayer);
                        ChatUtil.sendMessage(repGivePlayer, "<#55FFFF>Успішно збільшено повагу цього представника адміністрації!");
                        ChatUtil.sendMessage(repTakePlayer, "<#55FFFF>Хтось збільшив вам адміністраторську повагу. Гарна робота, так тримати!");
                    } else {
                        ChatUtil.sendMessage(repGivePlayer, "<#55FFFF>На жаль, у вас не залишилось кредитів поваги на сьогодні. Спробуйте через деякий час!");
                    }
                } case "-" -> {
                    if (foundRepTakePlayer.getCredits() > 0){
                        foundRepTakePlayer.setReverence(foundRepTakePlayer.getReverence() - 1);
                        foundRepGivePlayer.setCredits(foundRepGivePlayer.getCredits() - 1);
                        repPlayerDao.save(foundRepGivePlayer);
                        repPlayerDao.save(foundRepTakePlayer);
                        ChatUtil.sendMessage(repGivePlayer, "<#55FFFF>Успішно зменшено повагу цього представника адміністрації!");
                        ChatUtil.sendMessage(repTakePlayer, "<#55FFFF>Хтось зменшив вам адміністраторську повагу! Гарна причина задуматися над своєю поведінкою...");
                    } else {
                        ChatUtil.sendMessage(repGivePlayer, "<#55FFFF>На жаль, у вас не залишилось кредитів поваги на сьогодні. Спробуйте через деякий час!");
                    }
                } default -> ChatUtil.sendMessage(repGivePlayer, "<#55FFFF>Третім аргументом цієї команди може бути лише '+' або '-'!");
            }

        }
    }
}
