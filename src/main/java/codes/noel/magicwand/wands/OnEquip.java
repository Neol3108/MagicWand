package codes.noel.magicwand.wands;

import org.bukkit.entity.Player;

import codes.noel.magicwand.MagicWand;

public interface OnEquip {
	
	public void onEquip(Player player, MagicWand wand);
	
	public void onDequip(Player player, MagicWand wand);
}
