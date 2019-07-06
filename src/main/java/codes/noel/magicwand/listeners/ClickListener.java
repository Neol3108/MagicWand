package codes.noel.magicwand.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

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
			MagicWand magicwand = new MagicWand(plugin, event.getItem());
			for (String ability : magicwand.getAbilities()) {
				plugin.getServer().broadcastMessage(ability);
			}
		}
	}
	
	@EventHandler
	public void onLeftClick(PlayerInteractEvent event)
	{
		Action action = event.getAction();
		if (action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK) {
			MagicWand magicwand = new MagicWand(plugin, event.getItem());

			Class<? extends Ability> activeAbility = magicwand.activeAbility();
			this.plugin.broadcast(activeAbility != null ? activeAbility.getName() : "null");
		}
	}
}
