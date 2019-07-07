package codes.noel.magicwand;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import org.bukkit.entity.Player;

public class AbilityManager {
	private HashMap<String, Ability> abilities = new HashMap<String, Ability>();
	
	protected AbilityManager() {
		MagicWand.abilityManager = this;
	}
	
	public Ability getAbility(String key)
	{
		return this.abilities.get(key);
	}
	
	public Ability getAbility(Class<? extends Ability> clazz)
	{
		return this.getAbility(clazz.getName());
	}
	
	public boolean hasAbility(String key)
	{
		return this.abilities.containsKey(key);
	}
	
	public boolean hasAbility(Class<? extends Ability> clazz)
	{
		return this.hasAbility(clazz.getName());
	}
	
	public AbilityManager addAbility(Class<? extends Ability> clazz) throws IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException
	{
		Ability ability = clazz.getDeclaredConstructor().newInstance();
		this.abilities.put(clazz.getName(), ability);
		
		return this;
	}
	
	public void execute(Class<? extends Ability> ability, Player player, MagicWand magicWand)
	{
		this.getAbility(ability).execute(player, magicWand);
	}
}
