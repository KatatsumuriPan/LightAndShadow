package kpan.light_and_shadow.item;

import kpan.light_and_shadow.ModMain;
import kpan.light_and_shadow.util.interfaces.IMetaName;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public abstract class ItemSubTypes extends ItemBase implements IMetaName {

    public ItemSubTypes(String name, CreativeTabs tabs) {
        super(name, tabs);
        setHasSubtypes(true);
        setMaxDamage(0);
    }

    abstract protected int metaMax();

    @Override
    public void registerItemModels() {
        for (int i = 0; i <= metaMax(); i++) {
            ModMain.proxy.registerSingleModel(this, i, "inventory");
        }
    }
    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (isInCreativeTab(tab)) {
            for (int i = 0; i <= metaMax(); i++) {
                items.add(new ItemStack(this, 1, i));
            }
        }
    }

}
