package nc.tile;

import nc.block.tile.IActivatable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

public abstract class NCTile extends TileEntity implements ITickable {
	
	public boolean isAdded;
	public boolean isMarkedDirty;
	
	public NCTile() {
		super();
	}
	
	@Override
	public void update() {
		if (!isAdded) {
			onAdded();
			isAdded = true;
		}
		if (isMarkedDirty) {
			markDirty();
			isMarkedDirty = false;
		}
	}
	
	public void onAdded() {
		if (world.isRemote) {
			getWorld().markBlockRangeForRenderUpdate(pos, pos);
			getWorld().getChunkFromBlockCoords(getPos()).markDirty();
		}
		markDirty();
	}
	
	@Override
	public ITextComponent getDisplayName() {
		if (getBlockType() != null) return new TextComponentTranslation(getBlockType().getLocalizedName()); else return null;
	}
	
	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate) {
		String oldName = oldState.getBlock().getUnlocalizedName().toString();
		String newName = newSate.getBlock().getUnlocalizedName().toString();
		if (oldName.contains("_idle") || oldName.contains("_active")) {
			return !oldName.replace("_idle","").replace("_active","").equals(newName.replace("_idle","").replace("_active",""));
		}
		return oldState.getBlock() != newSate.getBlock();
	}
	
	public IBlockState getDefaultBlockState() {
		return world.getBlockState(pos).getBlock().getDefaultState();
	}
	
	public void setState(boolean isActive) {
		if (getBlockType() instanceof IActivatable) ((IActivatable)getBlockType()).setState(isActive, world, pos);
	}
	
	// NBT
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		writeAll(nbt);
		return nbt;
	}
	
	public NBTTagCompound writeAll(NBTTagCompound nbt) {
		return nbt;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		readAll(nbt);
	}
	
	public void readAll(NBTTagCompound nbt) {}
	
	/*public NBTTagCompound getTileData() {
		NBTTagCompound nbt = new NBTTagCompound();
		writeToNBT(nbt);
		return nbt;
	}*/
	
	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		NBTTagCompound nbt = new NBTTagCompound();
		writeToNBT(nbt);
		int metadata = getBlockMetadata();
		return new SPacketUpdateTileEntity(pos, metadata, nbt);
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {
		readFromNBT(packet.getNbtCompound());
	}

	@Override
	public NBTTagCompound getUpdateTag() {
		NBTTagCompound nbt = new NBTTagCompound();
		writeToNBT(nbt);
		return nbt;
	}

	@Override
	public void handleUpdateTag(NBTTagCompound tag) {
		super.handleUpdateTag(tag);
	}
}
