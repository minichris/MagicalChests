package chris.bukkitplugin.MagicalChests;


import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class PlayerClickChestListener implements Listener {

    public static String MagicChestName = "Magic Chest";

    @EventHandler
    public void blockChestInterract(PlayerInteractEvent event) {
        if(     !event.isCancelled()
                && (event.getAction() == Action.RIGHT_CLICK_BLOCK)
                && (event.getClickedBlock().getType() == Material.CHEST)
                && (blockHasRitual(event.getClickedBlock()))) { //if it is a right click on a chest which is set up correctly

            event.setCancelled(true); //cancel the real click
            Chest clickedChest = (Chest) event.getClickedBlock().getState(); //get the chest which has been clicked
            for (ItemStack itemStack : clickedChest.getInventory()) { //if there is anything in the chest, drop it into the world
                if(itemStack != null) clickedChest.getWorld().dropItemNaturally(clickedChest.getLocation(), itemStack);
            }
            clickedChest.getInventory().clear(); //clear the inventory from the chest

            event.getPlayer().openInventory(magicChest());
        }
        Bukkit.getConsoleSender().sendMessage("Action was " + event.getAction() + " material was " + event.getMaterial() + " clicking on " + event.getClickedBlock());
    }

    @EventHandler
    public void preventMagicChestItemClick(InventoryClickEvent event) {
        if(isMagicChestComponent(event.getCurrentItem())){
            event.setCancelled(true); //cancel the click
        }
    }

    @EventHandler
    public void preventMagicChestItemMove(InventoryMoveItemEvent event) {
        if(isMagicChestComponent(event.getItem())){
            event.setCancelled(true); //cancel the move
        }
    }

    public static boolean isMagicChestComponent(ItemStack item){
        if(item.getType() == Material.DIAMOND_HOE || item.getItemMeta().isUnbreakable()){ //if it is our chest graphic object
            return true;
        }
        else{
            return false;
        }
    }

    public static Inventory magicChest(){
        Inventory returnInventory = Bukkit.createInventory(null, 27, MagicChestName); //create a regular chest size inventory
        ItemStack magicChestTopItem = new ItemStack(Material.DIAMOND_HOE); //create a stack of 1 diamond hoe
        ItemMeta itemMeta = magicChestTopItem.getItemMeta(); //get the item meta of this diamond hoe
        itemMeta.setUnbreakable(true); //set it unbreakable
        magicChestTopItem.setItemMeta(itemMeta); //give the stack the item meta
        magicChestTopItem.setDurability((byte) 2); //give the item one damage to switch the image
        ItemStack magicChestBottomItem = magicChestTopItem.clone(); //clone it
        magicChestTopItem.setDurability((byte) 1); //give the item two damage to switch the image
        returnInventory.setItem(0, magicChestTopItem); //set the first position to the top item
        returnInventory.setItem(18, magicChestBottomItem); //set the other position to the bottom item
        return returnInventory;
    }

    private static boolean blockHasRitual(Block testBlock){
        boolean RitualComplete = true;

        //get the surrounding blocks of the chest
        Location blockLocation = testBlock.getLocation();
        Block[] surroundingblocks = new Block[8];
        surroundingblocks[0] = blockLocation.clone().add(1,0,1).getBlock();
        surroundingblocks[1] = blockLocation.clone().add(1,0,-1).getBlock();
        surroundingblocks[2] = blockLocation.clone().add(-1,0,1).getBlock();
        surroundingblocks[3] = blockLocation.clone().add(-1,0,-1).getBlock();
        surroundingblocks[4] = blockLocation.clone().add(0,0,1).getBlock();
        surroundingblocks[5] = blockLocation.clone().add(0,0,-1).getBlock();
        surroundingblocks[6] = blockLocation.clone().add(-1,0,0).getBlock();
        surroundingblocks[7] = blockLocation.clone().add(1,0,0).getBlock();

        for(int i = 0; i < 3; i++){ //for each of the corner blocks
            if( !(surroundingblocks[i].getType() == Material.REDSTONE_TORCH_ON) ){
                //check for each of the blocks - if they are NOT redstone torch
                RitualComplete = false; //fail the ritual
                Bukkit.getConsoleSender().sendMessage("Failing block at " + surroundingblocks[i].getLocation() + "of type " + surroundingblocks[i].getType());
            }
        }

        for(int i = 4; i < 7; i++){ //for each of the side blocks
            if( !(surroundingblocks[i].getType() == Material.REDSTONE_WIRE) ){
                //check for each of the blocks - if they are NOT redstone wire
                RitualComplete = false; //fail the ritual
                Bukkit.getConsoleSender().sendMessage("Failing block at " + surroundingblocks[i].getLocation() + "of type " + surroundingblocks[i].getType());
            }
        }

        return RitualComplete;
    }

}