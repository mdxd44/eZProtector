/*
 * Copyright (C) 2021 Elytrium, DoNotSpamPls
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.elytrium.ezprotector.shared.config;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import net.elytrium.ezprotector.BuildConstants;

public class Settings extends Config {

  @Ignore
  public static final Settings IMP = new Settings();

  public String PREFIX = "&7[&aeZProtector&7]&f";

  @Create
  public TAB_COMPLETION TAB_COMPLETION;
  @Create
  public HIDDEN_SYNTAXES HIDDEN_SYNTAXES;
  @Create
  public CUSTOM_PLUGINS CUSTOM_PLUGINS;
  @Create
  public CUSTOM_VERSION CUSTOM_VERSION;
  @Create
  public CUSTOM_COMMANDS CUSTOM_COMMANDS;
  @Create
  public MODS MODS;

  @Comment({
      "eZProtector | Version " + BuildConstants.VERSION,
      "To see what are the placeholders available to you, read the information page here: https://git.io/vpDio\n",

      "This module blocks certain commands from being tab completed."
  })
  public static class TAB_COMPLETION {
    public boolean BLOCKED = true;
    @Comment("If this is set to true, the commands in the list will be THE ONLY ones to be tab completed.")
    public boolean WHITELIST = false;
    public List<String> COMMANDS = Arrays.asList("?", "about", "bukkit:?", "bukkit:about", "bukkit:help", "bukkit:pl", "bukkit:plugins", "bukkit:ver", "bukkit:version", "help", "icanhasbukkit", "pl", "plugins", "ver", "version");
  }

  @Comment("This blocks players from using commands such as /bukkit:help or /essentials:warp, since they be used to bypass blocks.")
  public static class HIDDEN_SYNTAXES {
    public boolean BLOCKED = true;
    public String ERROR_MESSAGE = "{PRFX} That command syntax is forbidden!";
    @Comment("If you add a command to this list, it will not be blocked.")
    public List<String> WHITELISTED = Arrays.asList("whitelisted:commands", "with:hidden", "syntaxes:here");

    @Create
    public NOTIFY_ADMINS NOTIFY_ADMINS;
    public static class NOTIFY_ADMINS {
      public boolean ENABLED = true;
      @Comment({
          "{0} - Player nickname",
          "{1} - Used command"
      })
      public String MESSAGE = "{PRFX} &e{0} &ftried to use a command with a hidden syntax. &4({1}})";
    }

    @Create
    public PUNISH_PLAYER PUNISH_PLAYER;
    public static class PUNISH_PLAYER {
      public boolean ENABLED = false;
      @Comment({
          "{0} - Player nickname",
          "{1} - error-message"
      })
      public String COMMAND = "kick {0} {1}";
    }
  }

  @Comment("This option allows you to set a custom list of plugins that will be shown if players do /plugins")
  public static class CUSTOM_PLUGINS {
    public boolean ENABLED = true;
    public String PLUGINS = "Fully, Custom, Plugins";

    @Create
    public Settings.CUSTOM_PLUGINS.NOTIFY_ADMINS NOTIFY_ADMINS;
    @Comment("The player will not see an error message, but admins can still be notified.")
    public static class NOTIFY_ADMINS {
      public boolean ENABLED = true;
      @Comment("{0} - Player nickname")
      public String MESSAGE = "{PRFX} &e{0} &ftried to see the server plugins.";
    }
  }

  @Comment("Same deal as with the custom plugins above, but for the /version command!")
  public static class CUSTOM_VERSION {
    public boolean ENABLED = true;
    @Comment("It shows up as \"This server is running server version Custom Version\"")
    public String VERSION = "Custom Version";

    @Create
    public NOTIFY_ADMINS NOTIFY_ADMINS;
    @Comment("The player will not see an error message, but admins can still be notified.")
    public static class NOTIFY_ADMINS {
      public boolean ENABLED = true;
      @Comment("{0} - Player nickname")
      public String MESSAGE = "{PRFX} &e{0} &ftried to see the server version.";
    }
  }

  @Comment("This option allows you to block any command you wish by adding it to the list!")
  public static class CUSTOM_COMMANDS {
    public boolean BLOCKED = true;
    @Comment("{0} - Used command")
    public String ERROR_MESSAGE = "{PRFX} You don't have the permission to use this command! &4({0})";
    public List<String> COMMANDS = Arrays.asList("a", "about", "icanhasbukkit", "?", "command_without_slash");
    @Comment({
        " - pl",
        " - plugins",
        " - ver",
        " - version\n"
    })

    @Create
    public NOTIFY_ADMINS NOTIFY_ADMINS;
    public static class NOTIFY_ADMINS {
      public boolean ENABLED = true;
      @Comment({
          "{0} - Player nickname",
          "{1} - Used command"
      })
      public String MESSAGE = "{PRFX} &e{0} &ftried to use a command with a hidden syntax. &4({1}})";
    }

    @Create
    public PUNISH_PLAYER PUNISH_PLAYER;
    public static class PUNISH_PLAYER {
      public boolean ENABLED = false;
      @Comment({
          "{0} - Player nickname",
          "{1} - error-message"
      })
      public String COMMAND = "kick {0} {1}";
    }
  }

  @Comment("This section allows you to block various mods (and even punish players for some of them!).")
  public static class MODS {
    @Create
    public FIVE_ZIG FIVE_ZIG;
    public static class FIVE_ZIG {
      public boolean BLOCK = false;
    }

    @Create
    public BETTERPVP BETTERPVP;
    @Comment("This option also blocks Xaero's Minimap.")
    public static class BETTERPVP {
      public boolean BLOCK = false;
    }

    @Create
    public BETTERSPRINTING BETTERSPRINTING;
    public static class BETTERSPRINTING {
      public boolean BLOCK = false;
    }

    @Create
    public FABRIC FABRIC;
    public static class FABRIC {
      public boolean BLOCK = false;
      @Comment({
          "{0} - Player nickname"
      })
      public String WARNING_MESSAGE = "{PRFX} &e{0} &ftried to join while having Fabric installed.";
      public String PUNISH_COMMAND = "kick {0} &4Fabric usage is not allowed.";
    }

    @Create
    public FORGE FORGE;
    public static class FORGE {
      public boolean BLOCK = false;
      @Comment({
          "{0} - Player nickname"
      })
      public String WARNING_MESSAGE = "{PRFX} &e{0} &ftried to join while having Forge installed.";
      public String PUNISH_COMMAND = "kick {0} &4Forge usage is not allowed.";
    }

    @Create
    public LITELOADER LITELOADER;
    public static class LITELOADER {
      public boolean BLOCK = false;
      @Comment({
          "{0} - Player nickname"
      })
      public String WARNING_MESSAGE = "{PRFX} &e{0} &ftried to join while having LiteLoader installed.";
      public String PUNISH_COMMAND = "kick {0} &4LiteLoader usage is not allowed.";
    }

    @Create
    public RIFT RIFT;
    public static class RIFT {
      public boolean BLOCK = false;
      @Comment({
          "{0} - Player nickname"
      })
      public String WARNING_MESSAGE = "{PRFX} &e{0} &ftried to join while having Rift installed.";
      public String PUNISH_COMMAND = "kick {0} &4Rift usage is not allowed.";
    }

    @Create
    public SCHEMATICA SCHEMATICA;
    public static class SCHEMATICA {
      public boolean BLOCK = false;
    }

    @Create
    public VOXELMAP VOXELMAP;
    public static class VOXELMAP {
      public boolean BLOCK = false;
    }

    @Create
    public WDL WDL;
    public static class WDL {
      public boolean BLOCK = false;
    }
  }

  public void reload(File file) {
    if (this.load(file, this.PREFIX)) {
      this.save(file, this.PREFIX);
    } else {
      this.save(file, this.PREFIX);
      this.load(file, this.PREFIX);
    }
  }
}
