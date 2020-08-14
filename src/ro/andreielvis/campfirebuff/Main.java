package ro.andreielvis.campfirebuff;

import java.util.logging.Logger;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;


public class Main extends JavaPlugin implements Listener {

	private static final Logger log = Logger.getLogger("Minecraft");
	
	public int Effect_Duration = 0;
	public int Effect_Radius = 0;
	
	public void onEnable() {
		this.getServer().getPluginManager().registerEvents(this, this);
		loadConfig();
	}
	
	public void onDisable() {
		log.info(String.format("[%s] Plugin disabled", getDescription().getName()));
	}
	
	@EventHandler
	public void onPlayerRightClickCampfire(PlayerInteractEvent e) {
        EquipmentSlot ev = e.getHand();
        if (ev.equals(EquipmentSlot.HAND)) {
    		if(e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getClickedBlock().getType() == Material.CAMPFIRE) {
    			
    			Location blockLoc = e.getClickedBlock().getLocation();
    						
    			if(e.getPlayer().getInventory().getItemInMainHand().getType() == Material.PAPER) {
    				if(e.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName().equalsIgnoreCase("Night")) addPlayersEffect(e, "Night", PotionEffectType.NIGHT_VISION, blockLoc);
    				else if(e.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName().equalsIgnoreCase("Jump")) addPlayersEffect(e, "Jump", PotionEffectType.JUMP, blockLoc);
    				else if(e.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName().equalsIgnoreCase("Speed")) addPlayersEffect(e, "Speed", PotionEffectType.SPEED, blockLoc);
    				else if(e.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName().equalsIgnoreCase("Strength")) addPlayersEffect(e, "Strength", PotionEffectType.INCREASE_DAMAGE, blockLoc);
    			} else if(e.getPlayer().getInventory().getItemInMainHand().getType() == Material.DIAMOND) addPlayersEffect(e, "DIAMOND", PotionEffectType.REGENERATION, blockLoc);
    		}
        }
	}
	
	public void deletePaperNamebyEffect(Player p, String string) {
		if(string == "DIAMOND") {
			ItemStack it = new ItemStack(Material.DIAMOND, 1);
			
			p.getInventory().removeItem(it);
			
			return;
		}
		
		ItemStack it = new ItemStack(Material.PAPER, 1);
		ItemMeta it0 = it.getItemMeta();
		it0.setDisplayName(string);
		it.setItemMeta(it0);
		
		p.getInventory().removeItem(it);
	}
	
	public void addPlayersEffect(PlayerInteractEvent e, String name, PotionEffectType type, Location blockLoc) {
		if(!name.equals("DIAMOND")) e.getPlayer().sendMessage(String.format("§bYou have successfully enabled potion effect §a%s§b!", e.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName()));
		else e.getPlayer().sendMessage(String.format("§bYou have successfully enabled potion effect §aREGENERATION§b!"));
		
		for(Player p : getServer().getOnlinePlayers()) {
			if(p.getLocation().distance(blockLoc) <= Effect_Radius) {
				
				p.removePotionEffect(type);
				
				p.addPotionEffect(type.createEffect(20 * Effect_Duration, 0));
				
				if(!name.equals("DIAMOND")) p.sendMessage(String.format("§bYou got the potion effect §a%s§b from player §a%s§b!", e.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName(), e.getPlayer().getName()));
				else p.sendMessage(String.format("§bYou got the potion effect §a%s§b from player §aREGENERATION§b!", e.getPlayer().getName()));
			}
			
		}
		
		deletePaperNamebyEffect(e.getPlayer(), name);
	}
	
	public void loadConfig() {
		getConfig().options().copyDefaults(true);
		saveConfig();
		
		Effect_Duration = getConfig().getInt("Effect_Duration");
		Effect_Radius = getConfig().getInt("Effect_Radius");
	}
}
