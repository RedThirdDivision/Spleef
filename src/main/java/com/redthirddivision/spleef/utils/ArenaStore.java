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
package com.redthirddivision.spleef.utils;

import org.bukkit.Location;
import org.bukkit.block.Sign;

/**
 * <strong>Project:</strong> Spleef <br>
 * <strong>File:</strong> ArenaStore.java
 *
 * @author <a href="http://jpeter.redthirddivision.com">TheJeterLP</a>
 */
public class ArenaStore {

    public static String name = null;
    public static Location[] sel = null;
    public static int minplayers = -1, maxplayers = -1;
    public static Sign sign = null;
    public static Location spawnPoint = null;
    public static Location lobbyPoint = null;
    public static Location spectatorPoint = null;

    public static void reset() {
        name = null;
        sel = null;
        minplayers = -1;
        maxplayers = -1;
        sign = null;
        spawnPoint = null;
        lobbyPoint = null;
        spectatorPoint = null;
    }

    public static boolean isDefault() {
        return name == null
                && sel == null
                && minplayers == -1
                && maxplayers == -1
                && sign == null
                && spawnPoint == null
                && lobbyPoint == null
                && spectatorPoint == null;
    }

    public static boolean isComplete() {
        return name != null
                && sel != null
                && minplayers != -1
                && maxplayers != -1
                && sign != null
                && spawnPoint != null
                && lobbyPoint != null
                && spectatorPoint != null;
    }

}
