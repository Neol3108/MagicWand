package codes.noel.magicwand;

import org.bukkit.entity.Player;

public abstract class Ability {
	protected static Plugin plugin;
	
	public abstract void execute(Player player, MagicWand magicWand);
}
