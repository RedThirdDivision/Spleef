/*   Copyright 2015 Joey Peter
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
package com.redthirddivision.spleef.utils;

import java.io.File;
import java.io.IOException;
import net.minecraft.util.com.google.common.base.Joiner;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * <strong>Project:</strong> Spleef <br>
 * <strong>File:</strong> Config.java
 *
 * @author <a href="http://jpeter.redthirddivision.com">TheJeterLP</a>
 */
public enum Config {

    USE_MYSQL("SQL.UseMySQL", false, "Do we use MySQL or SQLite?"),
    SQL_TABLE_PREFIX("SQL.TablePrefix", "r3d_spleef_", "The prefix for every table we create"),
    MYSQL_HOST("SQL.MySQLHost", "localhost", "The MySQL host. (Not needed if you use SQLite)"),
    MYSQL_PORT("SQL.MySQLPort", 3306, "The MySQL port. (Not needed if you use SQLite)"),
    MYSQL_DATABASE("SQL.MySQLDatabase", "database", "The MySQL database. (Not needed if you use SQLite)"),
    MYSQL_USER("SQL.MySQLUser", "root", "The MySQL user. (Not needed if you use SQLite)"),
    MYSQL_PASSWORD("SQL.MySQLPassword", "root", "The MySQL password. (Not needed if you use SQLite)"),
    SETTINGS_PLAYER_TOOL("Settings.Tool", Material.DIAMOND_SPADE.toString(), "The item which will be used to dig");

    private final Object value;
    private final String path;
    private final String comment;
    private static YamlConfiguration cfg;
    private static File f;

    private Config(String path, Object val, String comment) {
        this.path = path;
        this.value = val;
        this.comment = comment;
    }

    public int getInt() {
        return cfg.getInt(path);
    }

    public boolean getBoolean() {
        return cfg.getBoolean(path);
    }

    public double getDouble() {
        return cfg.getDouble(path);
    }

    public String getString() {
        return cfg.getString(path);
    }

    @Override
    public String toString() {
        return getString();
    }

    public static void load(JavaPlugin pl, String cfgFile) {
        pl.getDataFolder().mkdirs();
        f = new File(pl.getDataFolder(), cfgFile);
        cfg = YamlConfiguration.loadConfiguration(f);
        String header = pl.getDescription().getName() + " plugin by " + Joiner.on(", ").join(pl.getDescription().getAuthors()) + "\n";
        for (Config c : values()) {
            header += c.path + ": " + c.comment + "\n";
            if (!cfg.contains(c.path)) {
                cfg.set(c.path, c.value);
            }
        }
        cfg.options().header(header);
        try {
            cfg.save(f);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
