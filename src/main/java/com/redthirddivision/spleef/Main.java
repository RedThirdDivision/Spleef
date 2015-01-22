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
package com.redthirddivision.spleef;

import com.redthirddivision.bukkitgamelib.Minigame;
import com.redthirddivision.bukkitgamelib.command.CommandManager;
import com.redthirddivision.bukkitgamelib.database.Database;
import com.redthirddivision.bukkitgamelib.database.MySQL;
import com.redthirddivision.bukkitgamelib.database.SQLite;
import com.redthirddivision.spleef.commands.CreateCommands;
import com.redthirddivision.spleef.listeners.PlayerBlockEvent;
import com.redthirddivision.spleef.listeners.PlayerDamageListener;
import com.redthirddivision.spleef.utils.Config;
import com.redthirddivision.spleef.utils.DBHandler;
import de.thejeterlp.bukkit.updater.Updater;
import java.io.File;
import java.sql.SQLException;

/**
 * <strong>Project:</strong> Spleef <br>
 * <strong>File:</strong> Main.java
 *
 * @author <a href="http://jpeter.redthirddivision.com">TheJeterLP</a>
 */
public class Main extends Minigame {
    
    private static Main INSTANCE;
    private static Database db;
    
    @Override
    public void onPluginStart() {
        INSTANCE = this;
        Config.load(this, "config.yml");
        
        if (Config.USE_MYSQL.getBoolean()) {
            db = new MySQL(Config.MYSQL_HOST.getString(), Config.MYSQL_USER.getString(), Config.MYSQL_PASSWORD.getString(), Config.MYSQL_DATABASE.getString(), Config.MYSQL_PORT.getInt());
        } else {
            File dbFile = new File(getDataFolder(), "database.db");
            db = new SQLite(dbFile);
        }
        
        DBHandler.setup();
        
        getServer().getPluginManager().registerEvents(new PlayerBlockEvent(), this);
        getServer().getPluginManager().registerEvents(new PlayerDamageListener(), this);
        
        getCommandManager().registerClass(CreateCommands.class);
        
        Updater u = new Updater(this, -1, "r3d-spleef");
        u.search();
    }
    
    @Override
    public void onPluginStop() {
        try {
            db.closeConnection();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    public static Main getInstance() {
        return INSTANCE;
    }
    
    public static Database getDB() {
        return db;
    }
    
}
