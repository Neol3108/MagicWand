package codes.noel.magicwand.abilities;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import codes.noel.magicwand.Ability;
import codes.noel.magicwand.MagicWand;

public class MobArmy extends Ability
{
	public static ArrayList<UUID> spawners = new ArrayList<UUID>();

	@Override
	public void execute(Player player, MagicWand magicWand)
	{
		BlockData bd = Material.SPAWNER.createBlockData("");
		FallingBlock fb = player.getWorld().spawnFallingBlock(player.getLocation().add(0, 1, 0), bd);
		fb.setVelocity(player.getLocation().getDirection().multiply(1.3).add(new Vector(0, .5, 0)));
		spawners.add(fb.getUniqueId());
	}

}
