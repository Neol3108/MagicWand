package codes.noel.magicwand;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import codes.noel.magicwand.wands.OnEquip;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ComponentBuilder;

public class MagicWand implements OnEquip {

	public static final String abilityKey = "abilities";
	
	protected static AbilityManager abilityManager;

	private Player player;
	private Plugin plugin;
	private ItemStack itemStack;
	private ArrayList<String> abilities = new ArrayList<String>();
	private Class<? extends Ability> activeAbility;
	
	protected MagicWand(Plugin plugin, Player player, ItemStack itemStack) throws Exception {
		this(plugin, player, itemStack, false);
	}

	@SuppressWarnings("unchecked")
	protected MagicWand(Plugin plugin, Player player, ItemStack itemStack, boolean create) throws Exception {
		this.player = player;
		this.plugin = plugin;
		this.itemStack = itemStack;
		
		ItemMeta meta = itemStack.getItemMeta();

		PersistentDataContainer pdc = meta.getPersistentDataContainer();
		
		if (create) {
			pdc.set(plugin.getMetaKey("magicWand"), PersistentDataType.STRING, getClass().getName());
			this.itemStack.setItemMeta(meta);
		} else {
			String wandClassName = pdc.get(plugin.getMetaKey("magicWand"), PersistentDataType.STRING);
			
			Exception notAWand = new Exception("This is not a Magic Wand!");
			
			if (wandClassName == null) {
				throw notAWand;
			}
			
			Class<?> wandClass = Class.forName(wandClassName);
			
			if (!MagicWand.class.isAssignableFrom(wandClass)) {
				throw notAWand;
			}
		}

		String abilities = pdc.get(
				plugin.getMetaKey("abilities"),
				PersistentDataType.STRING
				);

		String activeAbility = pdc.get(plugin.getMetaKey("activeAbility"), PersistentDataType.STRING);

		if (abilities != null) {
			JsonParser parser = new JsonParser();
			JsonElement json = parser.parse(abilities);
			for (JsonElement ability : json.getAsJsonArray()) {
				this.abilities.add(ability.getAsString());
			}
		}

		if (activeAbility != null) {
			try {
				Class<?> act = Class.forName(activeAbility);
				if (Ability.class.isAssignableFrom(act)) {
					if (abilityManager.hasAbility(((Class<? extends Ability>) act))) {
						this.activeAbility = (Class<Ability>) act;
					}
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	public ItemStack getItemStack()
	{
		return this.itemStack;
	}

	public ArrayList<String> getAbilities()
	{
		return this.abilities;
	}

	public MagicWand updateItemMeta()
	{
		ItemMeta meta = this.itemStack.getItemMeta();
		PersistentDataContainer pdc = meta.getPersistentDataContainer();

		pdc.set(
				this.plugin.getMetaKey("abilities"),
				PersistentDataType.STRING,
				new Gson().toJson(this.getAbilities())
				);
		
		if (this.activeAbility != null) {
			pdc.set(
					plugin.getMetaKey("activeAbility"),
					PersistentDataType.STRING,
					this.activeAbility.getName()
					);
		}

		this.itemStack.setItemMeta(meta);
		
		return this;
	}

	public MagicWand addAbility(@SuppressWarnings("unchecked") Class<? extends Ability>... clazzes)
	{
		for (Class<? extends Ability> clazz : clazzes) {
			if (!this.abilities.contains(clazz.getName())) {
				this.abilities.add(clazz.getName());
			}
		}

		this.updateItemMeta();
		
		return this;
	}

	public MagicWand removeAbility(@SuppressWarnings("unchecked") Class<? extends Ability>... clazzes)
	{
		for (Class<? extends Ability> clazz : clazzes) {
			this.abilities.remove(clazz.getName());
		}
		this.updateItemMeta();

		return this;
	}
	
	public Class<? extends Ability> activeAbility()
	{
		return this.activeAbility;
	}
	
	public MagicWand setActiveAbility(Class<? extends Ability> clazz)
	{
		if (this.abilities.contains(clazz.getName())) {
			this.activeAbility = clazz;
			
			this.updateItemMeta();
		}
		
		return this;
	}
	
	@SuppressWarnings("unchecked")
	public MagicWand selectNext()
	{
		int index = 0;
		
		if (this.abilities.contains(this.activeAbility.getName())) {
			index = this.abilities.indexOf(this.activeAbility.getName());
			
			if (++index >= this.abilities.size()) {
				index = 0;
			}
		}
		
		try {
			this.setActiveAbility((Class<? extends Ability>) Class.forName(this.abilities.get(index)));
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		

		this.player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new ComponentBuilder(this.activeAbility.getName()).color(ChatColor.AQUA).create());
		
		return this;
	}
	
	public static AbilityManager getAbilityManager()
	{
		return abilityManager;
	}
	
	public static MagicWand fromItemStack(Plugin plugin, Player player, ItemStack itemStack)
	{
		try {
			return new MagicWand(plugin, player, itemStack);
		} catch (Exception e) {
			return null;
		}
	}
	
	@SafeVarargs
	public static MagicWand build(Plugin plugin, Player player, Class<? extends Ability>... abilities)
	{
		ItemStack itemStack = new ItemStack(Material.BLAZE_ROD);
		ItemMeta meta = itemStack.getItemMeta();
		
		meta.setDisplayName("Magic Wand");
		
		itemStack.setItemMeta(meta);
		
		try {
			return new MagicWand(plugin, player, itemStack, true)
					.addAbility(abilities)
					.setActiveAbility(abilities[0])
					.updateItemMeta();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void onEquip(Player player, MagicWand wand) {
		plugin.broadcast("Equiped " + player.getName());
	}

	@Override
	public void onDequip(Player player, MagicWand wand) {
		plugin.broadcast("Dequiped " + player.getName());
		
	}
}
