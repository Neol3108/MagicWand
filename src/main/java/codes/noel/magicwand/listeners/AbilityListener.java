package codes.noel.magicwand.listeners;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;

import codes.noel.magicwand.Plugin;
import codes.noel.magicwand.abilities.MobArmy;

public class AbilityListener implements Listener {
	private Plugin plugin;
	private ArrayList<EntityType> mobTypes = new ArrayList<EntityType>();
	private ArrayList<Location> unbreakable = new ArrayList<Location>();
	
	public AbilityListener(Plugin plugin) {
		this.plugin = plugin;
		
		mobTypes.add(EntityType.ZOMBIE);
		mobTypes.add(EntityType.CREEPER);
		mobTypes.add(EntityType.CAVE_SPIDER);
		mobTypes.add(EntityType.SKELETON);
		mobTypes.add(EntityType.RAVAGER);
		mobTypes.add(EntityType.WITCH);
		mobTypes.add(EntityType.WITHER_SKELETON);
	}
	
	@EventHandler
	public void onFallingBlockLand(EntityChangeBlockEvent event)
	{
		Block landingBlock = event.getBlock();
		BlockData oldBlock = landingBlock.getBlockData().clone();
		if (event.getEntity() instanceof FallingBlock && (landingBlock.isPassable() || landingBlock.isLiquid())) {
			FallingBlock block = (FallingBlock) event.getEntity();
			
			if (MobArmy.spawners.contains(block.getUniqueId())) {
				plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
					
					@Override
					public void run() {
						landingBlock.setType(Material.SPAWNER);
						CreatureSpawner spawner = (CreatureSpawner) event.getBlock().getState();
						EntityType randomType = mobTypes.get(new Random().nextInt(mobTypes.size()));
						spawner.setDelay(10);
						spawner.setMinSpawnDelay(10);
						spawner.setMaxSpawnDelay(20);
						spawner.setSpawnedType(randomType);
						spawner.setRequiredPlayerRange(32);
						spawner.setSpawnCount(5);
						spawner.update();
						
						unbreakable.add(landingBlock.getLocation());
					}
				}, 5L);
				
				plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
					
					@Override
					public void run() {
						landingBlock.setBlockData(oldBlock);
						MobArmy.spawners.remove(block.getUniqueId());
						unbreakable.remove(landingBlock.getLocation());
						Location loc = landingBlock.getLocation();
						loc.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, loc.add(.5, .5, .5), 100);
						loc.getWorld().playSound(loc, Sound.BLOCK_ANVIL_LAND, 2, 1);
					}
				}, 120L);
			}
		}
	}
	
	@EventHandler
	public void onSpawnerBreak(BlockBreakEvent event)
	{
		if (unbreakable.contains(event.getBlock().getLocation())) {
			event.setCancelled(true);
		}
	}
}
