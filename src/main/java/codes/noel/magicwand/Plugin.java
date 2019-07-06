package codes.noel.magicwand;

import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import codes.noel.magicwand.abilities.Comet;
import codes.noel.magicwand.abilities.Leap;
import codes.noel.magicwand.listeners.ClickListener;

public class Plugin extends JavaPlugin {
	
	@Override
	public void onEnable() {
		this.getServer().getPluginManager().registerEvents(new ClickListener(this), this);
		this.getCommand("mw").setExecutor(this);
		
		AbilityManager abilityManager = new AbilityManager();
		try {
			abilityManager
			.addAbility(Leap.class)
			.addAbility(Comet.class);
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		switch (command.getName()) {
		case "mw":
			if (sender instanceof Player) {
				Player player = (Player) sender;
				player.getInventory().addItem(MagicWand.build(this, player, Leap.class, Comet.class).getItemStack());
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
