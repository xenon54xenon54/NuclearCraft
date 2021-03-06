package nc.tile.energy;

import nc.ModCheck;
import nc.energy.EnumStorage.EnergyConnection;
import net.darkhax.tesla.capability.TeslaCapabilities;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.energy.CapabilityEnergy;

public abstract class TileEnergySidedInventory extends TileEnergyInventory implements ISidedInventory {

	public int[] topSlots;
	public int[] sideSlots;
	public int[] bottomSlots;
	
	public TileEnergySidedInventory(String name, int size, int capacity, EnergyConnection energyConnection) {
		super(name, size, capacity, energyConnection);
	}
	
	public TileEnergySidedInventory(String name, int size, int capacity, int maxTransfer, EnergyConnection energyConnection) {
		super(name, size, capacity, maxTransfer, energyConnection);
	}
			
	public TileEnergySidedInventory(String name, int size, int capacity, int maxReceive, int maxExtract, EnergyConnection energyConnection) {
		super(name, size, capacity, maxReceive, maxExtract, energyConnection);
	}
	
	// SidedInventory

	@Override
	public abstract int[] getSlotsForFace(EnumFacing side);

	@Override
	public abstract boolean canInsertItem(int slot, ItemStack stack, EnumFacing direction);

	@Override
	public abstract boolean canExtractItem(int slot, ItemStack stack, EnumFacing direction);
	
	// Capability
	
	net.minecraftforge.items.IItemHandler handlerTop = new net.minecraftforge.items.wrapper.SidedInvWrapper(this, net.minecraft.util.EnumFacing.UP);
	net.minecraftforge.items.IItemHandler handlerBottom = new net.minecraftforge.items.wrapper.SidedInvWrapper(this, net.minecraft.util.EnumFacing.DOWN);
	net.minecraftforge.items.IItemHandler handlerSide = new net.minecraftforge.items.wrapper.SidedInvWrapper(this, net.minecraft.util.EnumFacing.WEST);
	
	@Override
	@SuppressWarnings("unchecked")
	public <T> T getCapability(net.minecraftforge.common.capabilities.Capability<T> capability, @javax.annotation.Nullable net.minecraft.util.EnumFacing facing) {
		if (CapabilityEnergy.ENERGY == capability && energyConnection.canConnect()) {
			return (T) storage;
		}
		if (energyConnection != null && ModCheck.teslaLoaded() && energyConnection.canConnect()) {
			if ((capability == TeslaCapabilities.CAPABILITY_CONSUMER && energyConnection.canReceive()) || (capability == TeslaCapabilities.CAPABILITY_PRODUCER && energyConnection.canExtract()) || capability == TeslaCapabilities.CAPABILITY_HOLDER)
				return (T) storage;
		}
		if (facing != null && capability == net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			if (facing == EnumFacing.DOWN) {
				return (T) handlerBottom;
			} else if (facing == EnumFacing.UP) {
				return (T) handlerTop;
			} else {
				return (T) handlerSide;
			}
		}
		return super.getCapability(capability, facing);
	}
}
