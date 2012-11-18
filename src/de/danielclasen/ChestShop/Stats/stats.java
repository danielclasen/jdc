/**
 * 
 */
package de.danielclasen.ChestShop.Stats;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import com.Acrobot.Breeze.Utils.MaterialUtil;
import com.Acrobot.Breeze.Utils.MessageUtil;
import com.Acrobot.Breeze.Utils.StringUtil;
import com.Acrobot.ChestShop.ChestShop;
import com.Acrobot.ChestShop.Config.Language;
import com.Acrobot.ChestShop.DB.Transaction;
import com.Acrobot.ChestShop.Events.ItemInfoEvent;

/**
 * @author Daniel Clasen
 *
 */
public final class stats extends JavaPlugin {
	
	public void onEnable(){
		getLogger().info("onEnable has been invoked!");
	}
 
	public void onDisable(){
		getLogger().info("onDisable has been invoked!");
	}
	
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
        ItemStack item;

        if (args.length == 0) {
            if (!(sender instanceof HumanEntity)) {
                return false;
            }

            item = ((HumanEntity) sender).getItemInHand();
        } else {
            item = MaterialUtil.getItem(StringUtil.joinArray(args));
        }

        if (MaterialUtil.isEmpty(item)) {
            return false;
        }

        
        
        MessageUtil.sendMessage(sender, Language.iteminfo);
        sender.sendMessage(getNameAndID(item) + ChatColor.WHITE);
        
        String average = String.valueOf(getAveragePrice(item));
        sender.sendMessage(average + ChatColor.WHITE);
        ItemInfoEvent event = new ItemInfoEvent(sender, item);
        ChestShop.callEvent(event);

        return true;
    }

	private float getAveragePrice(ItemStack item) {
        float price = 0;
        int itemID = item.getTypeId();
		List<Transaction> prices = ChestShop.getDB().find(Transaction.class).where().eq("itemID", itemID).eq("buy", true).findList();

        for (Transaction t : prices) {
            price += t.getAveragePricePerItem();
        }

        float toReturn = price / prices.size();
        return (!Float.isNaN(toReturn) ? toReturn : 0);
		
	}

	private String getNameAndID(ItemStack item) {
        String itemName = MaterialUtil.getName(item);

        return ChatColor.GRAY + itemName + ChatColor.WHITE + "      " + item.getTypeId();
	}
	
	




}
