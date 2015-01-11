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
package com.redthirddivision.spleef.listeners;

import com.redthirddivision.bukkitgamelib.Game;
import com.redthirddivision.spleef.Main;
import com.redthirddivision.spleef.game.Spleef;
import com.redthirddivision.spleef.utils.Config;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * <strong>Project:</strong> Spleef <br>
 * <strong>File:</strong> PlayerBlockEvent.java
 *
 * @author <a href="http://jpeter.redthirddivision.com">TheJeterLP</a>
 */
public class PlayerBlockEvent implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onPlayerInterAct(PlayerInteractEvent e) {
        if (e.getAction() != Action.LEFT_CLICK_BLOCK) return;
        Game g = Main.getInstance().getGameManager().getArena(e.getPlayer());
        if (g == null) {
            return;
        }

        Spleef spleef = (Spleef) g;

        if (!spleef.containsBlock(e.getClickedBlock().getLocation())) e.setCancelled(true);

        if (e.getItem().getType() != Material.valueOf(Config.SETTINGS_PLAYER_TOOL.getString())) {
            e.setCancelled(true);
        }

        spleef.breakBlock(e.getClickedBlock());
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockPlace(final BlockPlaceEvent e) {
        if (e.getPlayer() == null) return;
        Game g = Main.getInstance().getGameManager().getArena(e.getPlayer());
        if (g == null) {
            return;
        }

        e.setCancelled(true);
    }

}