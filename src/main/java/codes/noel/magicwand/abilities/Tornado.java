package codes.noel.magicwand.abilities;

import java.util.ArrayList;

import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.util.Vector;

import codes.noel.magicwand.Ability;
import codes.noel.magicwand.MagicWand;

public class Tornado extends Ability {

	private static int tnts = 10;
	private static float radius = 1.75F;
	private static Long firstDelay = 15L;
	private static Long secondDelay = 10L;
	
	@Override
	public void execute(Player player, MagicWand magicWand) {
		ArrayList<TNTPrimed> tntArray = new ArrayList<TNTPrimed>();
		
		for (int i = 0; i < tnts; i++) {
			TNTPrimed tnt = player.getWorld().spawn(player.getLocation(), TNTPrimed.class);
			tnt.setFuseTicks(40);
			tnt.setVelocity(new Vector(0, 2, 0));
			tntArray.add(tnt);
		}
		
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			@Override
			public void run() {
				float a = 0;
				
				for (TNTPrimed tntPrimed : tntArray) {
					double angle = Math.toRadians(a);
					double x = radius * Math.cos(angle);
					double z = radius * Math.sin(angle);
					
					tntPrimed.setVelocity(tntPrimed.getVelocity().add(new Vector(x, .5, z)));
					
					a += 360 / tnts;
				}
			}
		}, firstDelay);
		
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			@Override
			public void run() {
				for (TNTPrimed tntPrimed : tntArray) {
					tntPrimed.setVelocity(new Vector(0, -2, 0));
				}
			}
		}, firstDelay + secondDelay);
	}

}
