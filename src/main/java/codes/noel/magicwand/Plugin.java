package codes.noel.magicwand;

import java.lang.reflect.InvocationTargetException;

import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import codes.noel.magicwand.abilities.Comet;
import codes.noel.magicwand.abilities.Leap;
import codes.noel.magicwand.abilities.Skydiver;
import codes.noel.magicwand.abilities.Tornado;
import codes.noel.magicwand.listeners.ClickListener;
import codes.noel.magicwand.listeners.InventoryListener;

public class Plugin extends JavaPlugin {

	protected static Plugin plugin;
	
	@Override
	public void onEnable() {
		Ability.plugin = this;
		
		this.getServer().getPluginManager().registerEvents(new ClickListener(this), this);
		this.getServer().getPluginManager().registerEvents(new InventoryListener(this), this);
		this.getCommand("mw").setExecutor(this);

		AbilityManager abilityManager = new AbilityManager();

		try {
			abilityManager
			.addAbility(Leap.class)
			.addAbility(Skydiver.class)
			.addAbility(Tornado.class)
			.addAbility(Comet.class);
		} catch (IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		switch (command.getName()) {
		case "mw":
			if (sender instanceof Player) {
				Player player = (Player) sender;
				player.getInventory().addItem(MagicWand.build(this, player, Leap.class, Skydiver.class, Tornado.class, Comet.class).getItemStack());
				return true;
			}
			break;
		}
		return false;
	}

	public NamespacedKey getMetaKey(String key)
	{
		return new NamespacedKey(this, key);
	}

	public void broadcast(String ...messages)
	{
		for (String message : messages) {
			this.getServer().broadcastMessage(message);
		}
	}
}
