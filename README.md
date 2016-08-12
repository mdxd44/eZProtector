# eZProtector

[​IMG]
[​IMG]​
Add custom plugins! Both commands /pl and /plugins are replaced with fake plugins, which are highly customizable and toggable in the config.yml.
/ver , /version , /icanhasbukkit, /a, and /about are disabled. You can set the access-denied message in the config.yml.
Every, (I mean EVERY) auto-completion command is disabled for people who don't have the correct permission: /<TAB>, /a <TAB>, /about <TAB>, /icanhasbukkit <TAB>, /ver <TAB>, /version <TAB>, and /? <TAB>. They are all disabled, allowing tab completing for usernames!
You can add any command you want to block! eZProtector can also block every listed command in the config, just add the desired commands to blocked-cmds in the config. This will give the access-denied message. You can set the access-denied message in the config.yml.
The /help command is also disabled, but this one has a toggle whether if it's disabled or not. You can toggle it in the config.yml. Also, if it's disabled, the message will be the one at access-denied option in the config.yml.
Blocks bukkit's hidden syntaxes! '/bukkit:help', 'essentials:help', and every command that includes ":" in it. Doesn't matter if the command exists or not, it will block it. (This feature is toggable)
Protection against the sign exploit (that one with json stuff)!
Can block commands for opped players (this doesn't have a permission). This feature is only bypassable for specific players that are in the config. Also, the bypassed players specified in the config, are the only ones that get notified if another opped player does a blocked command.
Notify an administrator with the permission ezprotector.notify.* if someone uses a blocked command, abuse the sign exploit, tried to join with the world downloader mod, the Schematica mod, or any other mod. This applies to the customizable blocked commands either for normal players and opped players! This also applies to default blocked commands and bukkit's hidden syntaxes. In conclusion, this applies to everything.
Toggle almost anything! eZProtector was made to be fully customizable.
99.9% customizable. eZProtector allows you to change every notify message! The plugin can't be more customizable. Plus there are additional placeholders to make the plugin even more customizable: %player% and %command%.
Block the world downloader mod! Players will be punished if they try to join with this mod.
Block the Schematica mod! Players can't be punished. But if they join with the mod, it will be disabled.
Schematica is a very similar mod to the World Downloader mod. This mod downloads parts of your world, unlike the World Downloader that downloads your worlds (duh). The Schematica mod works like this: it displays a ghost image of a schematic file inside Minecraft to rebuild a structure or export any part of your world into a schematic file.
Block the Forge mod. Players will be punished if they try to join with this mod.
Block the Damage Indicators mod. Players can't be punished. But if they join with the mod, it will be disabled.
Block the Rei's Minimap mod. Players can't be punished. But if they join with the mod, it will be disabled.
Block the VoxelMap mod. Players can't be punished. But if they join with the mod, it will be disabled.
Block the 5zig Mod. Players can't be punished. But if they join with the mod, it will be disabled.
Block the BetterPvP Mod. Players can't be punished. But if they join with the mod, it will be disabled.
Block the LiteLoader mod. Players will be punished if they try to join with this mod.
Block the Better Sprinting mod. Players can't be punished. But if they join with the mod, it will be disabled.
Block the LabyMod. Players can't be punished. But if they join with the mod, it will be disabled.
[​IMG]​
ezprotector.info
ezprotector.reload
ezprotector.notify.*
.notify.update
.notify.signhack
.notify.blockedcommands
.notify.hiddensyntaxes
.notify.tab-completion
.notify.command.*
.command.plugins
.command.version
.command.help
.command.questionmark (?)
.command.about
.notify.mod.*
.notify.mod.wdl
.notify.mod.forge
.notify.mod.liteloader
ezprotector.bypass.*
.bypass.hiddensyntaxes
.bypass.blockedcommands
.bypass.signhack
.bypass.tab-completion
.bypass.command:
.command.plugins
.command.version
.command.help
.command.questionmark (?)
.command.about
.bypass.mod:
.mod.wdl (World downloader mod)
.mod.schematica
.mod.voxelmap
.mod.damageindicators
.mod.reiminimap
.mod.forge
.mod.5zig
.mod.betterpvp
.mod.liteloader
.mod.bettersprinting
.mod.labymod
Example permission: ezprotector.bypass.mod.wdl - the player can join the server with the world downloader mod.

[​IMG]​
Code (Config with explanation and comments):
#############################
# eZProtector by Dvargas135 #
#############################
#
# Plugin prefix for warns/notifies
prefix: '&3[&beZProtector&3] &b'
#
# Update notifier
updater: true
#
# If you have all the mods enabled, your console will be spammed by logging all the mods.
log-blocked-mods: true
#
# The default notify message, will only be used for the commands
# /plugins, /pl, /a, /about, /?, /ver, /version, /help, /icanhasbukkit,
# and the ones specified in block-commands
default-notify-message: '&e%player% tried to do the command %command%'
#
# Customised plugin list, commas are imperative.
# Showed if the 'custom-plugins' option is set to true.
custom-plugins:
  enabled: true
  plugins: Fully, Custom, Plugins
  # This message will only show if the above option is set to false.
  message: '&4Your abuse has been sent to the admins.'
  # Notify to all the players with the permission ezprotector.notify.command.plugins
  notify: true
#
# Block the tab completion or not. (permission ezprotector.bypass.tab-completion)
tab-completion:
  # disabled = blocked; if it's true, the tab completion will be blocked. (dumb explanation)
  disable: true
  # Warning: these are disabled by default since some people may spam this
  # event and it will log every single press of the tab key.
  log-console:
    enabled: false
    # If you prefer the log to be a warning, this is the option.
    # Warnings are shown with red color in some consoles.
    # Log will show as [INFO] and warn will show as [WARN]
    warn-instead-of-log: false
    message: '%player% triggered the tab event: %message%'
  # Permission for the admins to be notified: ezprotector.notify.tab-completion
  notify-admins:
    enabled: false
    message: '%player% triggered the tab event: %message%'
  warn-player:
    enabled: true
    message: '&cYou don''t have permission to use this.'
#
# The default blocked commands by eZProtector:
# /plugins, /pl, /help, /?, /version, /icanhasbukkit, /a, /ver
# Please remember to add the commands you want to block if you disable this feature.
default-blocked-commands: true
#
# Error message displayed to the players without any access of the plugin.
error-message: '&cNo permission.'
#
# Message showed for every command including ':' in it.
# An example could be "/bukkit:help", "/essentials:warp", etc.
# This will only work if the option "block-hidden-syntaxes" is set to true.
block-hidden-syntaxes:
  enabled: true
  message: '&cThat syntax is forbidden.'
  # Notify to all the players with the permission ezprotector.notify.hiddensyntaxes
  notify: true
  # For YAML, if you want to add a message with an apostrophe such as "don't",
  # you have to type double apostrophe: don''t.
  notify-message: '&e%player% tried to do a command with bukkit''s hidden syntax. (%command%)'
#
# Disable /help command (there isn't much to explain here).
disable-help-command:
  enabled: false
  message: '&cYou are not allowed to do that.'
  # Notify to all the players with the permission ezprotector.notify.command.help
  notify: false
#
# Will deny the listed commands showing the message set in 'error-message'.
block-commands:
  enabled: true
  message: '&cYou don''t have permission.'
  # Notify to all the players with the permission ezprotector.notify.blockedcommands
  notify: true
  # Command list for regular players.
  commands:
  - op
  - plugman list
#
# Blockage of commands for opped players
block-op-commands:
  enabled: true
  # Error message for opped players that are not in the bypassed-players list
  message: '&cThis command can only be executed from console.'
  # Notify ONLY to the bypassed players (see below).
  notify: true
  notify-message: '&eOpped player %player% tried to do %command%'
  # Command list (Only blocks for opped players)
  commands:
  - ezp
  - reload
  - featherboard resetconfig
  - command_without_slash
  # Who can bypass this option?
  # This only applies to the op commands section.
  bypassed-players:
  - Notch
  # Will op the above players (Bypassed players)
  op-bypassed-players-on-startup: false
#
# Sign exploit blockage
block-anti-sign-hack:
  enabled: true
  # If a player tries to abuse the sign exploit, a broadcasted message
  # will be sent to all the players with the permission ezprotector.notify.signhack
  warning-message: '&4WARN &c- &f%player% attempted to do the sign exploit, but failed.'
  # The command that will be executed if someone attempts to do the sign exploit.
  punish-command: kick %player% &4This exploit is forbidden!
#
# Mod blocker
mods:
  wdl:
    block: false
    warning-message: '&4WARN &c- &f%player% tried to join with the world downloader mod, but failed.'
    punish-command: kick %player% &4The World Downloader Mod is not allowed.
  schematica:
    block: false
  voxelmap:
    block: false
  damageindicators:
    block: false
  reiminimap:
    block: false
  forge:
    block: false
    warning-message: '&4WARN &c- &f%player% tried to join with the Forge mod, but failed.'
    punish-command: kick %player% &4The Forge mod is not allowed.
  5zig:
    block: false
  betterpvp:
    block: false
  liteloader:
    block: false
    warning-message: '&4WARN &c- &f%player% tried to join with the LiteLoader mod, but failed.'
    punish-command: kick %player% &4The LiteLoader mod is not allowed.
  bettersprinting:
    block: false
  labymod:
    block: false
#
# Thanks for using eZProtector, don't forget to leave a review! :)
[​IMG]​
Anti Sign Crash
Block commands for opped players
Add notify messages
Book Exploit Fix (This is fixed on Spigot 1.8.8, please update).
Anti World Downloader Mod
Updater
Bypassable players for blocked op commands
Anti Schematica Mod
Anti Damage Indicators Mod
Anti VoxelMap Mod
Anti ReiMiniMap Mod
Anti Forge Mod
Anti 5zig Mod
Anti BetterPvP Mod
Anti LiteLoader Mod
Fix the tab blocker not working for "/? a<tab>"
Anti BetterSprint Mod
Anti LabyMod Mod
Completely fix the PlayerJoinEvent errors
Fix the reload command not working for some config sections
Add a file to log abuses (super low priority)
Make the plugin even more customizable (how???)
Comment on the plugin discussion if you have any suggestions!

[​IMG]​
ProtocolLib
Vault (optional)
Java 8
A Spigot server running on 1.7, 1.8, 1.9 or 1.10 versions.
[​IMG]​
Download and Install ProtocolLib.
Download and Install Vault (optional).
Download eZProtector.
Drag and drop eZProtector to the plugins folder.
Configure eZProtector at your liking.
Enjoy!
[​IMG]

World Downloader Mod:
[​IMG]

Commands:
[​IMG]
[​IMG]

LabyMod:
[​IMG] 

5zig Mod:
[​IMG] 

Better Sprinting Mod:
[​IMG] ​

[​IMG]
​
All rights are reserved.
You're not allowed to decompile my plugin.
"Why? It's free... Should be open source..." Well, no. The plugin has special methods to block mods. I don't want people decompiling my plugin to see how can they stop the mod detection.

[​IMG]
Liking my resource? Consider donating to support me!
(Click the donate button)

[​IMG]
​
eZProtector is now compatible with 1.7 (yay) but please note that for this version some of the mods (probably all) won't be blocked. Why? Because of the simple fact that the developers decide not to implement the block feature for server compatibility on the first release of the mod.

Please use the plugin discussion or PM me to report bugs (if there are any), don't review my plugin with 1 star because it has a bug.

For the blockage of commands for opped players, you need to be on the bypassed list to be notified. Only those players will be notified if other player (that is not on that list, of course) tries a blocked command.

Also, the default config comes with the command "/ezp" blocked for opped players, so you first need to execute it from the console if you want to add yourself to the bypassed list.

Some mods use block codes to disable themselves, this is why your players will receive those blank messages. Sorry, I can't do anything about it. If you really get annoyed by that, you may disable the blockage for the mods. 

eZProtector can't block hacked clients. The fact that Minecraft is Java-based, there simply isn't any reasonable way of detecting changes to a client, because any feature you add to detect changes to the client, can just be removed or disabled by the client. Same applies to mods; I can't just add a mod name to the blocked list, it doesn't work like that. The ways I use to block mods are because their developers added this to support server owners, a mod which wants to remain hidden could just hide itself anyway, just because Minecraft is Java-based.

I can't add whatever mod you suggest me to. There is a long process for me to add a mod which consists on myself contacting the mod author/developer for them to implement the blockage and compatibility with Bukkit/Spigot servers so I can add it to my plugin. I can't just add your mod to a list (would be awesome, wouldn't it?) and make magic blocking your mod. Although some mods I've blocked did implement these server compatibility features, I did contact several times mod authors for them to implement/improve these.
