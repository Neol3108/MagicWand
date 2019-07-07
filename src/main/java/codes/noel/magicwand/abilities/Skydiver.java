package codes.noel.magicwand.abilities;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

import codes.noel.magicwand.Ability;
import codes.noel.magicwand.MagicWand;

public class Skydiver extends Ability {

	@Override
	public void execute(Player player, MagicWand magicWand) {
		player.setVelocity(player.getVelocity().setY(0));
		player.setFallDistance(0);
		player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_FLAP, 1, 0);
	}

}
