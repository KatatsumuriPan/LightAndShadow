package kpan.light_and_shadow.item;

import java.util.ArrayList;
import net.minecraft.item.Item;

public class ItemInit {

    public static final ArrayList<Item> ITEMS = new ArrayList<>();

    public static final ItemBase LIGHT = new ItemLight("light");
    public static final ItemBase SHADOW = new ItemShadow("shadow");
}
