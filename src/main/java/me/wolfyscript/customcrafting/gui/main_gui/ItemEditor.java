package me.wolfyscript.customcrafting.gui.main_gui;

import me.wolfyscript.customcrafting.CustomCrafting;
import me.wolfyscript.customcrafting.data.CCCache;
import me.wolfyscript.customcrafting.gui.CCWindow;
import me.wolfyscript.customcrafting.gui.Setting;
import me.wolfyscript.utilities.api.inventory.gui.GuiCluster;
import me.wolfyscript.utilities.api.inventory.gui.GuiUpdate;
import me.wolfyscript.utilities.api.inventory.gui.button.ButtonState;
import me.wolfyscript.utilities.api.inventory.gui.button.buttons.ActionButton;
import me.wolfyscript.utilities.util.NamespacedKey;
import me.wolfyscript.utilities.util.inventory.PlayerHeadUtils;
import org.bukkit.Material;

public class ItemEditor extends CCWindow {

    public ItemEditor(GuiCluster<CCCache> cluster, CustomCrafting customCrafting) {
        super(cluster, "item_editor", 45, customCrafting);
    }

    @Override
    public void onInit() {
        registerButton(new ActionButton<>("back", new ButtonState<>("none", "back", PlayerHeadUtils.getViaURL("864f779a8e3ffa231143fa69b96b14ee35c16d669e19c75fd1a7da4bf306c"), (cache, guiHandler, player, inventory, slot, event) -> {
            if (cache.getSetting().equals(Setting.RECIPE_CREATOR)) {
                guiHandler.openCluster("recipe_creator");
            } else {
                guiHandler.openPreviousWindow();
            }
            return true;
        })));
        registerButton(new ActionButton<>("create_item", Material.ITEM_FRAME, (cache, guiHandler, player, inventory, slot, event) -> {
            guiHandler.openWindow(new NamespacedKey("item_creator", "main_menu"));
            return true;
        }));
    }

    @Override
    public void onUpdateSync(GuiUpdate<CCCache> guiUpdate) {

    }

    @Override
    public void onUpdateAsync(GuiUpdate<CCCache> event) {
        super.onUpdateAsync(event);
        event.setButton(0, "back");
        CCCache cache = event.getGuiHandler().getCustomCache();
        if (cache.getItems().isRecipeItem()) {
            event.setButton(21, new NamespacedKey("none", "item_list"));
            event.setButton(23, "create_item");
        }
    }
}
