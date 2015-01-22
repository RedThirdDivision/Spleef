/*
 * Copyright 2015 Joey Peter.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.redthirddivision.spleef.commands;

import com.redthirddivision.bukkitgamelib.command.BaseCommand;
import com.redthirddivision.bukkitgamelib.command.CommandArgs;
import com.redthirddivision.bukkitgamelib.command.CommandHandler;
import com.redthirddivision.bukkitgamelib.command.CommandResult;
import com.redthirddivision.bukkitgamelib.command.HelpPage;
import com.redthirddivision.bukkitgamelib.utils.SelectionManager;
import com.redthirddivision.bukkitgamelib.utils.Utils;
import com.redthirddivision.spleef.Main;
import com.redthirddivision.spleef.utils.ArenaStore;
import com.redthirddivision.spleef.utils.DBHandler;
import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

/**
 * <strong>Project:</strong> Spleef <br>
 * <strong>File:</strong> CreateCommands.java
 *
 * @author <a href="http://jpeter.redthirddivision.com">TheJeterLP</a>
 */
@CommandHandler
public class CreateCommands {

    private final HelpPage create = new HelpPage("createspleef");

    public CreateCommands() {
        create.addPage(" <name>", "Start the creator mode with the given arena name.");
        create.addPage(" set selection", "Sets the selection to the current worldEdit selection.");
        create.addPage(" set maxplayers <number>", "Sets the maxPlayer to <number>");
        create.addPage(" set minplayers <number>", "Sets the minPlayer to <number>");
        create.addPage(" set sign", "Sets the sign");
        create.addPage(" set lobby", "Sets the lobby to your location");
        create.addPage(" set spawn", "Sets the spawn to your location");
        create.addPage(" set spectator", "Sets the spectator to your location");
        create.addPage(" finish", "Finishes the current arena");

        create.prepare();
    }

    @BaseCommand(command = "createspleef", sender = BaseCommand.Sender.PLAYER, permission = "spleef.admin")
    public CommandResult executeBasic(Player sender, CommandArgs args) {
        if (create.sendHelp(sender, args)) return CommandResult.SUCCESS;
        if (args.getLength() != 1) return CommandResult.ERROR;

        if (!ArenaStore.isDefault()) {
            Utils.sendMessage(sender, Utils.MessageType.ERROR, "You have to finish your current arena before you can start a new one!", Main.getInstance());
            return CommandResult.SUCCESS;
        }

        ArenaStore.name = args.getString(0);
        Utils.sendMessage(sender, Utils.MessageType.INFO, "The name has been set! You can continue the proccess of arena creation now!", Main.getInstance());
        return CommandResult.SUCCESS;
    }

    @BaseCommand(command = "createspleef", sender = BaseCommand.Sender.PLAYER, permission = "spleef.admin", subCommand = "set")
    public CommandResult executeSet(Player sender, CommandArgs args) {
        if (create.sendHelp(sender, args)) return CommandResult.SUCCESS;
        if (args.getLength() <= 1) return CommandResult.ERROR;

        if (ArenaStore.isDefault()) {
            Utils.sendMessage(sender, Utils.MessageType.ERROR, "You have to enter the creation mode first! Use §a/" + args.getBase() + " help§7 to receive help.", Main.getInstance());
            return CommandResult.SUCCESS;
        }

        if (args.getString(0).equalsIgnoreCase("selection")) {
            if (args.getLength() != 1) return CommandResult.ERROR;

            Location[] sel = SelectionManager.getSelection(sender);
            ArenaStore.sel = sel;
            Utils.sendMessage(sender, Utils.MessageType.INFO, "Stored the current WorldEdit selection.", Main.getInstance());
            return CommandResult.SUCCESS;
        } else if (args.getString(0).equalsIgnoreCase("minplayers")) {
            if (args.getLength() != 2) return CommandResult.ERROR;
            if (!args.isInteger(1)) return CommandResult.NOT_A_NUMBER;

            int number = args.getInt(1);

            ArenaStore.minplayers = number;
            Utils.sendMessage(sender, Utils.MessageType.INFO, "Stored the minPlayers.", Main.getInstance());
            return CommandResult.SUCCESS;
        } else if (args.getString(0).equalsIgnoreCase("maxplayers")) {
            if (args.getLength() != 2) return CommandResult.ERROR;

            if (!args.isInteger(1)) return CommandResult.NOT_A_NUMBER;

            int number = args.getInt(1);

            ArenaStore.maxplayers = number;
            Utils.sendMessage(sender, Utils.MessageType.INFO, "Stored the maxPlayers.", Main.getInstance());
            return CommandResult.SUCCESS;
        } else if (args.getString(0).equalsIgnoreCase("sign")) {
            if (args.getLength() != 1) return CommandResult.ERROR;

            Location looking = Utils.getLocationLooking(sender, 10);
            if (looking == null) {
                Utils.sendMessage(sender, Utils.MessageType.ERROR, "Sign cannot be null.", Main.getInstance());
                return CommandResult.SUCCESS;
            }

            if (!(looking.getBlock().getState() instanceof Sign)) {
                Utils.sendMessage(sender, Utils.MessageType.ERROR, "The block you are looking at is not a sign!", Main.getInstance());
                return CommandResult.SUCCESS;
            }

            Sign sign = (Sign) looking.getBlock().getState();
            ArenaStore.sign = sign;
            Utils.sendMessage(sender, Utils.MessageType.INFO, "Stored the sign", Main.getInstance());
            return CommandResult.SUCCESS;
        } else if (args.getString(0).equalsIgnoreCase("lobby")) {
            if (args.getLength() != 1) return CommandResult.ERROR;

            Location loc = sender.getLocation();
            ArenaStore.lobbyPoint = loc;
            Utils.sendMessage(sender, Utils.MessageType.INFO, "Stored the lobby.", Main.getInstance());
            return CommandResult.SUCCESS;
        } else if (args.getString(0).equalsIgnoreCase("spawn")) {
            if (args.getLength() != 1) return CommandResult.ERROR;

            Location loc = sender.getLocation();
            ArenaStore.spawnPoint = loc;
            Utils.sendMessage(sender, Utils.MessageType.INFO, "Stored the spawn.", Main.getInstance());
            return CommandResult.SUCCESS;
        } else if (args.getString(0).equalsIgnoreCase("spectator")) {
            if (args.getLength() != 1) return CommandResult.ERROR;

            Location loc = sender.getLocation();
            ArenaStore.spectatorPoint = loc;
            Utils.sendMessage(sender, Utils.MessageType.INFO, "Stored the spectator spawn.", Main.getInstance());
            return CommandResult.SUCCESS;
        } else {
            return CommandResult.ERROR;
        }
    }

    @BaseCommand(command = "createspleef", sender = BaseCommand.Sender.PLAYER, permission = "spleef.admin", subCommand = "finish")
    public CommandResult executeFinish(Player sender, CommandArgs args) {
        if (create.sendHelp(sender, args)) return CommandResult.SUCCESS;
        if (!args.isEmpty()) return CommandResult.ERROR;

        if (ArenaStore.isDefault()) {
            Utils.sendMessage(sender, Utils.MessageType.ERROR, "You have to enter the creation mode first! Use §a/" + args.getBase() + " help§7 to receive help.", Main.getInstance());
            return CommandResult.SUCCESS;
        }

        if (!ArenaStore.isComplete()) {
            Utils.sendMessage(sender, Utils.MessageType.ERROR, "You have to use every set command first! Use §a/" + args.getBase() + " help§7 to receive help.", Main.getInstance());
            return CommandResult.SUCCESS;
        }

        DBHandler.createArena();
        Utils.sendMessage(sender, Utils.MessageType.INFO, "The game has been loaded.", Main.getInstance());

        ArenaStore.reset();
        return CommandResult.SUCCESS;
    }
}
