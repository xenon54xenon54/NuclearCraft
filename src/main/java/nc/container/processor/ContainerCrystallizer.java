package nc.container.processor;

import nc.container.SlotSpecificInput;
import nc.recipe.NCRecipes;
import nc.tile.processor.TileEnergyItemFluidProcessor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnaceOutput;

public class ContainerCrystallizer extends ContainerEnergyItemFluidProcessor {

	public ContainerCrystallizer(EntityPlayer player, TileEnergyItemFluidProcessor tileEntity) {
		super(tileEntity, NCRecipes.CRYSTALLIZER_RECIPES);
		
		addSlotToContainer(new SlotFurnaceOutput(player, tileEntity, 0, 116, 35));
		
		addSlotToContainer(new SlotSpecificInput(tileEntity, 1, 132, 64, speedUpgrade));
		addSlotToContainer(new SlotFurnaceOutput(player, tileEntity, 2, 152, 64));
		
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 9; j++) {
				addSlotToContainer(new Slot(player.inventory, j + 9*i + 9, 8 + 18*j, 84 + 18*i));
			}
		}
		
		for (int i = 0; i < 9; i++) {
			addSlotToContainer(new Slot(player.inventory, i, 8 + 18*i, 142));
		}
	}
}
