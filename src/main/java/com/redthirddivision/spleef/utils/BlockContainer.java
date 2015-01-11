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
import org.bukkit.Material;
import org.bukkit.block.Block;

/**
 * <strong>Project:</strong> Spleef <br>
 * <strong>File:</strong> BlockContainer.java
 *
 * @author <a href="http://jpeter.redthirddivision.com">TheJeterLP</a>
 */
public class BlockContainer {

    private final Location loc;
    private final Material mat;
    private final byte data;

    public BlockContainer(Block b) {
        this.loc = b.getLocation();
        this.mat = b.getType();
        this.data = b.getData();
    }

    public Location getLoc() {
        return loc;
    }

    public Material getMat() {
        return mat;
    }

    public byte getData() {
        return data;
    }

}
