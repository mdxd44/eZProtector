/*
 * eZProtector - Copyright (C) 2018-2019 DoNotSpamPls
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.github.donotspampls.ezprotector.sponge.listeners;

import com.github.donotspampls.ezprotector.sponge.Main;
import com.moandjiezana.toml.Toml;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.text.Text;

public class PlayerJoinListener {

    /**
     * Sends out the mod blocks every time a player joins the server.
     *
     * @param event The join event from which other information is gathered.
     */
    @Listener
    public void onPlayerJoin(ClientConnectionEvent.Join event) {
        Player p = (Player) event.getSource();
        Toml config = Main.getConfig();

        // TODO: Maybe it's not used like this
        if (config.getBoolean("mods.betterpvp.block") && !p.hasPermission("ezprotector.bypass.mod.betterpvp")) p.sendMessage(Text.of(" §c §r§5 §r§1 §r§f §r§0 "));
        if (config.getBoolean("mods.voxelmap.block") && !p.hasPermission("ezprotector.bypass.mod.voxelmap")) p.sendMessage(Text.of(" §3 §6 §3 §6 §3 §6 §e "));
    }

}
