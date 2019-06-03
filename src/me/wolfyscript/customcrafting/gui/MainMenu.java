package me.wolfyscript.customcrafting.gui;

import me.wolfyscript.customcrafting.CustomCrafting;
import me.wolfyscript.customcrafting.commands.CommandCC;
import me.wolfyscript.customcrafting.data.PlayerCache;
import me.wolfyscript.utilities.api.WolfyUtilities;
import me.wolfyscript.utilities.api.inventory.*;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import java.util.Locale;

public class MainMenu extends ExtendedGuiWindow {

    public MainMenu(InventoryAPI inventoryAPI) {
        super("main_menu", inventoryAPI, 45);
    }

    @Override
    public void onInit() {
        createItem("craft_recipe", Material.CRAFTING_TABLE);
        createItem("furnace_recipe", Material.FURNACE);

        if(WolfyUtilities.hasVillagePillageUpdate()){
            createItem("blast_furnace", Material.BLAST_FURNACE);
            createItem("smoker", Material.SMOKER);
            createItem("campfire", Material.CAMPFIRE);
            createItem("stonecutter", Material.STONECUTTER);
        }
        createItem("item_editor", Material.CHEST);
        createItem("recipe_list", Material.WRITTEN_BOOK);

        createItem("create_recipe", Material.ITEM_FRAME);
        createItem("edit_recipe", Material.REDSTONE);
        createItem("delete_recipe", Material.BARRIER);

        createItem("lockdown.enabled", Material.BARRIER);
        createItem("lockdown.disabled", Material.BARRIER);
    }

    @EventHandler
    public void onUpdate(GuiUpdateEvent event) {
        if (event.verify(this)) {
            if (event.getGuiHandler().isHelpEnabled()) {
                event.setItem(8, "gui_help_on", true);
            } else {
                event.setItem(8, "gui_help_off", true);
            }

            event.setItem(0, "glass_white", true);

            event.setItem(10, "craft_recipe");
            event.setItem(12, "furnace_recipe");
            if(WolfyUtilities.hasVillagePillageUpdate()){
                event.setItem(14, "blast_furnace");
                event.setItem(16, "smoker");
                event.setItem(20, "campfire");
                event.setItem(22, "stonecutter");
            }

            event.setItem(39, "item_editor");
            event.setItem(41, "recipe_list");

            event.setItem(0, CustomCrafting.getConfigHandler().getConfig().isLockedDown() ? "lockdown.enabled" : "lockdown.disabled");
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onUpdateGuis(GuiUpdateEvent event) {
        if (event.getWolfyUtilities().equals(CustomCrafting.getApi()) && event.getGuiHandler().getCurrentInv() != null && event.getGuiHandler().getCurrentInv().equals(event.getGuiWindow())) {
            for(int i = 0; i < 9; i++){
                event.setItem(i, "glass_white", true);
            }
            for (int i = 9; i < event.getGuiHandler().getCurrentInv().getSize()-9; i++) {
                event.setItem(i, "glass_gray", true);
            }
            for(int i = event.getGuiHandler().getCurrentInv().getSize()-9; i < event.getGuiHandler().getCurrentInv().getSize(); i++){
                event.setItem(i, "glass_white", true);
            }
            event.setItem(0, "back", true);
            if (event.getGuiHandler().getCurrentInv().getSize() > 8) {
                if (event.getGuiHandler().isHelpEnabled()) {
                    event.setItem(8, "gui_help_on", true);
                } else {
                    event.setItem(8, "gui_help_off", true);
                }
            }
        }
    }

    @Override
    public boolean onAction(GuiAction guiAction) {
        if (!super.onAction(guiAction)) {
            String action = guiAction.getAction();
            PlayerCache playerCache = CustomCrafting.getPlayerCache(guiAction.getPlayer());
            if(action.startsWith("lockdown.")){
                if (CommandCC.checkPerm(guiAction.getPlayer(), "customcrafting.cmd.lockdown")) {
                    CustomCrafting.getConfigHandler().getConfig().toggleLockDown();
                }
            }else{
                switch (action) {
                    case "item_editor":
                        playerCache.setSetting(Setting.ITEMS);
                        playerCache.getItems().setType("items");
                        playerCache.getItems().setSaved(false);
                        playerCache.getItems().setId("");
                        guiAction.getGuiHandler().changeToInv("item_editor");
                        break;
                    case "recipe_list":
                        guiAction.getGuiHandler().changeToInv("recipe_list");
                        playerCache.setSetting(Setting.RECIPE_LIST);
                        break;
                    case "blast_furnace":
                    case "smoker":
                    case "campfire":
                    case "craft_recipe":
                    case "furnace_recipe":
                    case "stonecutter":
                        playerCache.setSetting(Setting.valueOf(action.toUpperCase(Locale.ROOT)));
                        guiAction.getGuiHandler().changeToInv("recipe_editor");
                        break;
                }
            }
        }

        return true;
    }

}