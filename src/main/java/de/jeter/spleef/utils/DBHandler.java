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
package de.jeter.spleef.utils;

import de.jeter.bukkitgamelib.Game.ArenaState;
import de.jeter.bukkitgamelib.database.Database;
import de.jeter.bukkitgamelib.database.MySQL;
import de.jeter.bukkitgamelib.database.SQLite;
import de.jeter.bukkitgamelib.utils.Utils;
import de.jeter.spleef.Main;
import de.jeter.spleef.game.Spleef;
import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.bukkit.Location;

/**
 * <strong>Project:</strong> Spleef <br>
 * <strong>File:</strong> DBHandler.java
 *
 * @author <a href="http://jpeter.redthirddivision.com">TheJeterLP</a>
 */
public class DBHandler {

    private static Database db;

    public static Database getDatabase() {
        return db;
    }

    public static void setup() {
        try {

            if (Config.USE_MYSQL.getBoolean()) {
                db = new MySQL(Config.MYSQL_HOST.getString(), Config.MYSQL_USER.getString(), Config.MYSQL_PASSWORD.getString(), Config.MYSQL_DATABASE.getString(), Config.MYSQL_PORT.getInt());
            } else {
                File dbFile = new File(Main.getInstance().getDataFolder(), "database.db");
                db = new SQLite(dbFile);
            }

            if (db instanceof MySQL) {
                db.executeStatement("CREATE TABLE IF NOT EXISTS `" + Config.SQL_TABLE_PREFIX.getString() + "arenas` ("
                        + "`ID` INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT, "
                        + "`name` varchar(64) NOT NULL, "
                        + "`state` varchar(64) NOT NULL DEFAULT '" + ArenaState.WAITING.toString() + "', "
                        + "`min` varchar(128) NOT NULL, "
                        + "`max` varchar(128) NOT NULL, "
                        + "`minplayers` INTEGER NOT NULL, "
                        + "`maxplayers` INTEGER NOT NULL, "
                        + "`sign` varchar(128) NOT NULL, "
                        + "`spawnpoint` varchar(128) NOT NULL, "
                        + "`spectatorpoint` varchar(128) NOT NULL, "
                        + "UNIQUE KEY `name` (`name`)"
                        + ");");
            } else if (db instanceof SQLite) {
                db.executeStatement("CREATE TABLE IF NOT EXISTS `" + Config.SQL_TABLE_PREFIX.getString() + "arenas` ("
                        + "`ID` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
                        + "`name` varchar(64) NOT NULL, "
                        + "`state` varchar(64) NOT NULL DEFAULT '" + ArenaState.WAITING.toString() + "', "
                        + "`min` varchar(128) NOT NULL, "
                        + "`max` varchar(128) NOT NULL, "
                        + "`minplayers` INTEGER NOT NULL, "
                        + "`maxplayers` INTEGER NOT NULL, "
                        + "`sign` varchar(128) NOT NULL, "
                        + "`spawnpoint` varchar(128) NOT NULL, "
                        + "`spectatorpoint` varchar(128) NOT NULL"
                        + ");");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private static Spleef loadGame(int id) {
        try {
            PreparedStatement ps = db.getPreparedStatement("SELECT * FROM " + Config.SQL_TABLE_PREFIX.getString() + "arenas WHERE ID = ?;");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            Spleef game = null;
            if (rs.next()) {
                String name = rs.getString("name");
                ArenaState state = ArenaState.valueOf(rs.getString("state").toUpperCase());
                Location min = Utils.deserialLocation(rs.getString("min"));
                Location max = Utils.deserialLocation(rs.getString("max"));
                int minplayers = rs.getInt("minplayers");
                int maxplayers = rs.getInt("maxplayers");
                Location sign = Utils.deserialLocation(rs.getString("sign"));
                Location spawn = Utils.deserialLocation(rs.getString("spawnpoint"));
                Location spectator = Utils.deserialLocation(rs.getString("spectatorpoint"));

                game = new Spleef(id, name, Main.getInstance(), state, new Location[]{min, max}, minplayers, maxplayers, sign, "spleef.join." + name, spawn, spectator);
            }
            db.closeResultSet(rs);
            db.closeStatement(ps);
            return game;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private static Spleef loadGame(ResultSet rs) {
        try {
            int id = rs.getInt("ID");
            String name = rs.getString("name");
            ArenaState state = ArenaState.valueOf(rs.getString("state").toUpperCase());
            Location min = Utils.deserialLocation(rs.getString("min"));
            Location max = Utils.deserialLocation(rs.getString("max"));
            int minplayers = rs.getInt("minplayers");
            int maxplayers = rs.getInt("maxplayers");
            Location sign = Utils.deserialLocation(rs.getString("sign"));
            Location spawn = Utils.deserialLocation(rs.getString("spawnpoint"));
            Location spectator = Utils.deserialLocation(rs.getString("spectatorpoint"));

            if (min.isWorldLoaded() && max.isWorldLoaded() && sign.isWorldLoaded() && spawn.isWorldLoaded() && spectator.isWorldLoaded() && (min.getWorld() == max.getWorld())) {
                Spleef game = new Spleef(id, name, Main.getInstance(), state, new Location[]{min, max}, minplayers, maxplayers, sign, "spleef.join." + name, spawn, spectator);
                Main.getInstance().getLogger().info("Loaded arena " + game.getID() + " - " + game.getName());
                return game;
            } else {
                Main.getInstance().getLogger().info("Skipping arena " + id + " - " + name + " because one or more worlds are not loaded on this Server.");
                return null;
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static void loadArenasFromDB() {
        try {
            Statement s = db.getStatement();
            ResultSet rs = s.executeQuery("SELECT * FROM " + Config.SQL_TABLE_PREFIX.getString() + "arenas;");
            while (rs.next()) {
                loadGame(rs);
            }
            db.closeStatement(s);
            db.closeResultSet(rs);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void createArena() {
        try {
            int id = -1;
            PreparedStatement st = db.getConnection().prepareStatement("INSERT INTO `" + Config.SQL_TABLE_PREFIX.getString() + "arenas` (`name`, `min`, `max`, `minplayers`, `maxplayers`, `sign`, `spawnpoint`, `spectatorpoint`) VALUES (?, ?, ?, ?, ?, ?, ?, ?);", Statement.RETURN_GENERATED_KEYS);
            st.setString(1, ArenaStore.name);
            st.setString(2, Utils.serialLocation(ArenaStore.sel[0]));
            st.setString(3, Utils.serialLocation(ArenaStore.sel[1]));
            st.setInt(4, ArenaStore.minplayers);
            st.setInt(5, ArenaStore.maxplayers);
            st.setString(6, Utils.serialLocation(ArenaStore.sign.getLocation()));
            st.setString(7, Utils.serialLocation(ArenaStore.spawnPoint));
            st.setString(8, Utils.serialLocation(ArenaStore.spectatorPoint));

            int affectedRows = st.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating table failed, no rows affected.");
            }

            ResultSet generatedKeys = st.getGeneratedKeys();
            if (generatedKeys.next()) {
                id = generatedKeys.getInt(1);
            } else {
                String sql = Config.USE_MYSQL.getBoolean() ? "MySQL" : "SQLite";
                throw new SQLException("Creating Arena failed, no ID obtained. SQL type: " + sql);
            }

            db.closeStatement(st);
            loadGame(id);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

}
