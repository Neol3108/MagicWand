package codes.noel.magicwand.abilities;

import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import codes.noel.magicwand.Ability;
import codes.noel.magicwand.MagicWand;

public class Leap extends Ability
{

	@Override
	public void execute(Player player, MagicWand magicWand)
	{
		player.setVelocity(player.getLocation().getDirection().multiply(2).add(new Vector(0, .5, 0)));
	}
	
}
