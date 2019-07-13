package codes.noel.magicwand;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;

public abstract class Ability {
	protected static Plugin plugin;

	protected static Set<Material> transparents = new HashSet<Material>() {
		private static final long serialVersionUID = 5886228212643356887L;
	{
		add(Material.AIR);
		add(Material.WATER);
	}};
	
	public abstract void execute(Player player, MagicWand magicWand);
	
	public ArrayList<Entity> getTarget(LivingEntity entity, ArrayList<EntityType> lookFor, boolean bail, int range)
	{
		ArrayList<Block> sightBlock = (ArrayList<Block>) entity.getLineOfSight(transparents, 100);
		blockLoop:
		for (Block block : sightBlock) {
			if (sightBlock.indexOf(block) < 4) {
				continue;
			}
		
			ArrayList<Entity> entities = new ArrayList<Entity>();
			Collection<Entity> nearEntities = block.getWorld().getNearbyEntities(block.getLocation(), 1.2, 1.2, 1.2);
			
			for (Entity near : nearEntities) {
				if (lookFor.contains(near.getType())) {
					entities.add(near);
				}
			}
			
			if (bail && !entities.isEmpty()) {
				return entities;
			}
			
			for (Entity near : nearEntities) {
				if (near instanceof LivingEntity) {
					break blockLoop;
				}
			}
		}
		
		return new ArrayList<Entity>();
	}
	
	public void playFirework(Location location, FireworkEffect... fireworkEffects) {
		Firework firework = location.getWorld().spawn(location, Firework.class);
		FireworkMeta fireworkMeta = firework.getFireworkMeta();
		fireworkMeta.setPower(1);
		for (FireworkEffect fireworkEffect : fireworkEffects) {
			fireworkMeta.addEffect(fireworkEffect);
		}
		
		firework.setFireworkMeta(fireworkMeta);
		
		try {
			Object entityFirework = firework.getClass().getMethod("getHandle").invoke(firework);
			Field lifespan = entityFirework.getClass().getDeclaredField("expectedLifespan");
			lifespan.setAccessible(true);
			lifespan.set(entityFirework, 1);
			lifespan.setAccessible(false);
		} catch (NoSuchMethodException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | SecurityException | NoSuchFieldException e) {
			e.printStackTrace();
		}
	}
}
