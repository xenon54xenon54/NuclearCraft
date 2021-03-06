package nc.recipe.vanilla;

import nc.init.NCBlocks;
import nc.init.NCItems;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.IFuelHandler;

public class FurnaceFuelHandler implements IFuelHandler {
	
	@Override
	public int getBurnTime(ItemStack fuel) {
		if (fuel.isItemEqual(new ItemStack(NCItems.ingot, 1, 8))) return 1600;
		else if (fuel.isItemEqual(new ItemStack(NCItems.dust, 1, 8))) return 1600;
		else if (fuel.isItemEqual(new ItemStack(NCBlocks.ingot_block, 1, 8))) return 16000;
		return 0;
	}
	
}
