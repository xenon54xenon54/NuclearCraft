package nc.block.tile.generator;

import java.util.Random;

import nc.block.tile.processor.BlockProcessor;
import nc.config.NCConfig;
import nc.enumm.BlockEnums.ProcessorType;
import nc.handler.SoundHandler;
import nc.proxy.CommonProxy;
import nc.tile.generator.TileFissionController;
import nc.util.BlockFinder;
import nc.util.NCInventoryHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class BlockFissionController extends BlockProcessor {

	public BlockFissionController(boolean isActive) {
		super(ProcessorType.FISSION_CONTROLLER, isActive);
		if (!isActive) setCreativeTab(CommonProxy.TAB_FISSION_BLOCKS);
	}
	
	@Override
	public void dropItems(World world, BlockPos pos, IInventory tileentity) {
		NCInventoryHelper.dropInventoryItems(world, pos, tileentity, 0, 1);
	}
	
	@Override
	public int getComparatorInputOverride(IBlockState state, World world, BlockPos pos) {
		TileEntity tile = world.getTileEntity(pos);
		if (tile != null) {
			if (tile instanceof TileFissionController) return (int) MathHelper.clamp(1500D/(double)NCConfig.fission_comparator_max_heat*(double)((TileFissionController)tile).heat/(double)((TileFissionController)tile).getMaxHeat(), 0, 15);
		}
		return Container.calcRedstone(world.getTileEntity(pos));
	}
	
	@Override
	public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
		super.randomDisplayTick(state, world, pos, rand);
		
		TileEntity tile = world.getTileEntity(pos);
		if (tile != null) if (tile instanceof TileFissionController) {
			TileFissionController controller = (TileFissionController) tile;
			BlockFinder finder = new BlockFinder(pos, world, controller.getBlockMetadata());
			BlockPos position = finder.randomWithin(controller.minX, controller.maxX, controller.minY, controller.maxY, controller.minZ, controller.maxZ);
			double rate = Math.max(Math.sqrt(controller.cells)/NCConfig.fission_max_size, 1);
			
			if (controller.isGenerating()) if (rand.nextDouble() < rate) {
				world.playSound((double)position.getX(), (double)position.getY(), (double)position.getZ(), SoundHandler.geiger_tick, SoundCategory.BLOCKS, 1.6F, 1.0F + 0.12F*(rand.nextFloat() - 0.5F), false);
			}
		}
	}
}
