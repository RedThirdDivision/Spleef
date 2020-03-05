package de.jeter.spleef.commands;

import com.google.common.base.Joiner;
import de.jeter.bukkitgamelib.command.BaseCommand;
import de.jeter.bukkitgamelib.command.CommandArgs;
import de.jeter.bukkitgamelib.command.CommandHandler;
import de.jeter.bukkitgamelib.command.CommandResult;
import de.jeter.bukkitgamelib.command.Sender;
import de.jeter.bukkitgamelib.utils.Utils;
import de.jeter.spleef.Main;
import org.bukkit.command.CommandSender;

@CommandHandler
public class SpleefCommands {
    
    @BaseCommand(command = "spleef", sender = Sender.ALL, helpArguments = {""})
    public CommandResult executeBasic(CommandSender sender, CommandArgs args) {        
        if (!args.isEmpty()) {
            return CommandResult.ERROR;
        }

        Utils.sendMessage(sender, Utils.MessageType.INFO, "Spleef minigame version " + Main.getInstance().getDescription().getVersion() + " by " + Joiner.on(", ").join(Main.getInstance().getDescription().getAuthors().toArray()), Main.getInstance());
        return CommandResult.SUCCESS;
    }

}
