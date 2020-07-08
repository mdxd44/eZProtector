/*
 * eZProtector - Copyright (C) 2018-2020 DoNotSpamPls
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.github.donotspampls.ezprotector.velocity.listeners;

import com.moandjiezana.toml.Toml;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.ServerConnectedEvent;
import com.velocitypowered.api.proxy.Player;
import net.kyori.text.TextComponent;

public class PlayerJoinListener {

    private final Toml config;
    public PlayerJoinListener(Toml config) {
        this.config = config;
    }

    @Subscribe
    @SuppressWarnings("unused")
    public void onPlayerJoin(final ServerConnectedEvent event) {
        final Player p = event.getPlayer();

        if (config.getBoolean("mods.betterpvp") && !p.hasPermission("ezprotector.bypass.mod.betterpvp"))
            p.sendMessage(TextComponent.of(" §c §r§5 §r§1 §r§f §r§0 "));

        if (config.getBoolean("mods.voxelmap") && !p.hasPermission("ezprotector.bypass.mod.voxelmap"))
            p.sendMessage(TextComponent.of(" §3 §6 §3 §6 §3 §6 §e "));
    }

}