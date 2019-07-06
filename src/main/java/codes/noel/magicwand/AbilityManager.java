package codes.noel.magicwand;

import java.util.HashMap;

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
	
	public AbilityManager addAbility(Class<Ability> clazz) throws InstantiationException, IllegalAccessException
	{
		Ability ability = clazz.newInstance();
		this.abilities.put(clazz.getName(), ability);
		
		return this;
	}
}
