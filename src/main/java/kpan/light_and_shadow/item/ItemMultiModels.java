package kpan.light_and_shadow.item;

import kpan.light_and_shadow.ModMain;
import net.minecraft.creativetab.CreativeTabs;

public abstract class ItemMultiModels extends ItemSubTypes {

    public ItemMultiModels(String name, CreativeTabs tabs) { super(name, tabs); }

    abstract protected String getItemFileName(int i);

    @Override
    public void registerItemModels() {
        for (int i = 0; i <= metaMax(); i++) {
            ModMain.proxy.registerMultiItemModel(this, i, getItemFileName(i), "inventory");
        }
    }

}
