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
package de.jeter.spleef;

import de.jeter.bukkitgamelib.Minigame;
import de.jeter.spleef.commands.CreateCommands;
import de.jeter.spleef.commands.SpleefCommands;
import de.jeter.spleef.listeners.PlayerBlockEvent;
import de.jeter.spleef.listeners.PlayerDamageListener;
import de.jeter.spleef.utils.Config;
import de.jeter.spleef.utils.DBHandler;
import de.jeter.spleef.utils.Locales;
import java.sql.SQLException;

/**
 * <strong>Project:</strong> Spleef <br>
 * <strong>File:</strong> Main.java
 *
 * @author <a href="https://jeter.de">TheJeterLP</a>
 */
public class Main extends Minigame {
    
    private static Main INSTANCE;
   
    
    @Override
    public void onPluginStart() {
        INSTANCE = this;
        
        Config.load(this, "config.yml");
        Locales.load();
                       
        DBHandler.setup();
        DBHandler.loadArenasFromDB();
        
        getServer().getPluginManager().registerEvents(new PlayerBlockEvent(), this);
        getServer().getPluginManager().registerEvents(new PlayerDamageListener(), this);
        
        getCommandManager().registerClass(CreateCommands.class);
        getCommandManager().registerClass(SpleefCommands.class);
    }
    
    @Override
    public void onPluginStop() {
        try {
            DBHandler.getDatabase().closeConnection();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    public static Main getInstance() {
        return INSTANCE;
    }   
}
