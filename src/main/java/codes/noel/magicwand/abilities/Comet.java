package codes.noel.magicwand.abilities;

import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;

import codes.noel.magicwand.Ability;
import codes.noel.magicwand.MagicWand;

public class Comet extends Ability {

	@Override
	public void execute(Player player, MagicWand magicWand) {
		Fireball fireball = player.launchProjectile(Fireball.class);
		fireball.setVelocity(player.getLocation().getDirection().multiply(4));
	}

}
