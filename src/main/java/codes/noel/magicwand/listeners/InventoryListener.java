package codes.noel.magicwand.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import codes.noel.magicwand.MagicWand;
import codes.noel.magicwand.Plugin;
import codes.noel.magicwand.wands.OnEquip;

public class InventoryListener implements Listener
{
	private Plugin plugin;
	private static final int offHandSlot = 40;
	
	public InventoryListener(Plugin plugin)
	{
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onWandLocationChange(InventoryClickEvent event)
	{
		if (event.getInventory().getHolder() instanceof Player) {
			Player player = (Player) event.getInventory().getHolder();
			PlayerInventory pInventory = player.getInventory();

			if (event.getCurrentItem().getType() != Material.AIR) {
				ItemStack cursor = event.getCurrentItem();
				
				if (pInventory.getItemInMainHand().equals(cursor)) {
					this.onEquipedItemChange(player, cursor, null);
				} else if (pInventory.getItemInOffHand().equals(cursor)) {
					this.onEquipedItemChange(player, cursor, null);
				}
				
			} else if (event.getCursor() != null) {
				ItemStack cursor = event.getCursor();
				
				if (event.getSlot() == pInventory.getHeldItemSlot()) {
					this.onEquipedItemChange(player, null, cursor);
				} else if (event.getSlot() == offHandSlot) {
					this.onEquipedItemChange(player, null, cursor);
				}
			}
		}
	}
	
	@EventHandler
	public void onSelectedItemChange(PlayerItemHeldEvent event)
	{
		Player player = event.getPlayer();
		PlayerInventory pInventory = player.getInventory();
		ItemStack next = pInventory.getItem(event.getNewSlot());
		ItemStack previous = pInventory.getItem(event.getPreviousSlot());
		
		this.onEquipedItemChange(player, previous, next);
	}
	
	@EventHandler
	public void onItemDrop(PlayerDropItemEvent event)
	{
		plugin.broadcast(event.getPlayer().getInventory().getItemInMainHand().getType().toString());
		ItemStack itemStack = event.getItemDrop().getItemStack();
		if (itemStack.getAmount() == 1) {
			this.onEquipedItemChange(event.getPlayer(), itemStack, null);
		}
	}
	
	@EventHandler
	public void onItemPickup(EntityPickupItemEvent event)
	{
		if (event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			PlayerInventory pInventory = player.getInventory();
			ItemStack itemStack = event.getItem().getItemStack();

			if (itemStack.getAmount() == 1) {
				int slot = pInventory.firstEmpty();
				if (slot == pInventory.getHeldItemSlot() || slot == offHandSlot) {
					this.onEquipedItemChange(player, null, itemStack);
				}
			}
		}
	}
	
	public void onEquipedItemChange(Player player , ItemStack previous, ItemStack next)
	{
		if (next != null) {
			MagicWand nextWand = MagicWand.fromItemStack(plugin, player, next);
			
			if (nextWand != null) {
				if (OnEquip.class.isAssignableFrom(nextWand.getClass())) {
					nextWand.onEquip(player, nextWand);
				}
			}
		}
		
		if (previous != null) {
			MagicWand previousWand = MagicWand.fromItemStack(plugin, player, previous);
			
			if (previousWand != null) {
				if (OnEquip.class.isAssignableFrom(previousWand.getClass())) {
					previousWand.onDequip(player, previousWand);
				}
			}
		}
	}
}
