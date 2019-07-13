package codes.noel.magicwand.abilities;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

import codes.noel.magicwand.Ability;
import codes.noel.magicwand.MagicWand;

public class Comet extends Ability {
	
	public static HashMap<UUID, Integer> comets = new HashMap<UUID, Integer>();

	@Override
	public void execute(Player player, MagicWand magicWand) {
		Fireball fireball = player.launchProjectile(Fireball.class);
		UUID uuid = fireball.getUniqueId();
		fireball.setVelocity(player.getLocation().getDirection().multiply(4));
		Ability self = this;
		BukkitScheduler scheduler = plugin.getServer().getScheduler();
		
		int id = scheduler.scheduleSyncRepeatingTask(plugin, new Runnable() {
			
			@Override
			public void run() {
				if (fireball.isDead() || fireball.getLocation().distance(player.getLocation()) > 60) {
					scheduler.cancelTask(comets.get(uuid));
					comets.remove(uuid);
				}
				self.playFirework(fireball.getLocation(), FireworkEffect.builder().withColor(Color.RED, Color.BLUE, Color.PURPLE).with(Type.BALL).withTrail().build());
			}
		}, 0L, 1L);
		comets.put(fireball.getUniqueId(), id);
		
		scheduler.scheduleSyncDelayedTask(plugin, new Runnable() {
			
			@Override
			public void run() {
				scheduler.cancelTask(id);
				comets.remove(uuid);
			}
		}, 120L);
	};

}
