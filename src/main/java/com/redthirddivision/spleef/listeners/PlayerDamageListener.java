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
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

/**
 * <strong>Project:</strong> Spleef <br>
 * <strong>File:</strong> PlayerDamageListener.java
 *
 * @author <a href="http://jpeter.redthirddivision.com">TheJeterLP</a>
 */
public class PlayerDamageListener implements Listener {
    
    @EventHandler(ignoreCancelled = true)
    public void onDamage(final EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player)) {
            return;
        }
        if (e.getCause() == EntityDamageEvent.DamageCause.LAVA || e.getCause() == EntityDamageEvent.DamageCause.FIRE || e.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK) {
            Player p = (Player) e.getEntity();
            Game g = Main.getInstance().getGameManager().getArena(p);
            if (g == null) {
                return;
            }
            g.setSpectator(p);
            e.setCancelled(true);
            e.setDamage(0.0);
        } else {
            e.setCancelled(true);
            e.setDamage(0.0);
        }
    }
    
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        Game g = Main.getInstance().getGameManager().getArena(e.getEntity());
        if (g == null || !g.isStarted()) {
            return;
        }
        g.removePlayer(e.getEntity());
    }
    
}
