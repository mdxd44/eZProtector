package ez.plugins.dan.Compatibility;

import java.util.HashMap;

import org.bukkit.entity.Player;

import ez.plugins.dan.ModBlockage.LabyMod.EnumLabyModFeature;

public abstract interface Compatibility {
	public abstract void sendBetterPvP(Player paramPlayer);
	public abstract void sendSchematica(Player paramPlayer );
	public abstract void sendReiMiniMap(Player paramPlayer );
	public abstract void sendDamageIndicators(Player paramPlayer );
	public abstract void sendVoxelMap(Player paramPlayer );
	public abstract void setLabyModFeature(Player paramPlayer, HashMap<EnumLabyModFeature, Boolean> paramHasMap);
	public abstract void sendSmartMove(Player paramPlayer);
}
