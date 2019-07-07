package codes.noel.magicwand.listeners;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import codes.noel.magicwand.Ability;
import codes.noel.magicwand.MagicWand;
import codes.noel.magicwand.Plugin;

public class ClickListener implements Listener {
	
	private Plugin plugin;
	
	public ClickListener(Plugin plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onRightClick(PlayerInteractEvent event)
	{
		Action action = event.getAction();
		if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
			MagicWand magicWand = MagicWand.fromItemStack(plugin, event.getPlayer(), event.getItem());
			
			if (magicWand != null) {
				magicWand.selectNext();
				event.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onLeftClick(PlayerInteractEvent event)
	{
		Action action = event.getAction();
		if (action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK) {
			PlayerInventory pInventory = event.getPlayer().getInventory();
			ItemStack itemStack = pInventory.getItemInMainHand();
			if (itemStack.getType() == Material.AIR) {
				itemStack = pInventory.getItemInOffHand();
			}
			
			MagicWand magicWand = MagicWand.fromItemStack(plugin, event.getPlayer(), itemStack);


			if (magicWand != null) {
				Class<? extends Ability> activeAbility = magicWand.activeAbility();
				MagicWand.getAbilityManager().execute(activeAbility, event.getPlayer(), magicWand);
				
				event.setCancelled(true);
			}
		}
	}
}
