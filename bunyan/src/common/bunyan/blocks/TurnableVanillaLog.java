/**
 * Copyright (c) Scott Killen, 2012
 * 
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license
 * located in /MMPL-1.0.txt
 */

package bunyan.blocks;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;
import bunyan.api.Direction;
import bunyan.api.DirectionalBlock;
import bunyan.api.TurnableLog;

public class TurnableVanillaLog extends TurnableLog {

	public static final int		metaOak			= 0;
	public static final int		metaPine		= 1;
	public static final int		metaBirch		= 2;
	public static final int		metaJungle		= 3;

	private static final int	BARK_OFFSETS[]	= { -1, 95, 96, 132 };

	public TurnableVanillaLog(int id) {
		super(id, 21);
	}

	@Override
	public void addCreativeItems(ArrayList itemList) {
		// itemList.add(new ItemStack(blockID, 1, metaOak));
		// itemList.add(new ItemStack(blockID, 1, metaPine));
		// itemList.add(new ItemStack(blockID, 1, metaBirch));
		// itemList.add(new ItemStack(blockID, 1, metaJungle));
	}

	@Override
	protected void dropBlockAsItem_do(World world, int x, int y, int z,
			ItemStack itemstack)
	{
		itemstack.itemID = new ItemStack(wood).itemID;
		super.dropBlockAsItem_do(world, x, y, z, itemstack);
	}

	@Override
	public int getTextureOffsetFromFacingSideAndMetadata(
			Direction facing, Direction side, int metadata)
	{
		int offset = 0;
		final int species = getDataFromMetadata(metadata);

		switch (side) {
			case DOWN:
			case UP:
				offset = BARK_OFFSETS[species];
				break;
			case NORTH:
			case SOUTH:
				if (facing == Direction.WEST
						|| facing == Direction.EAST)
					offset = BARK_OFFSETS[species];
				break;
			case WEST:
			case EAST:
				if (facing == Direction.NORTH
						|| facing == Direction.SOUTH)
					offset = BARK_OFFSETS[species];
		}
		return offset;
	}

	@Override
	public int idDropped(int metadata, Random random, int alwaysZero) {
		return wood.blockID;
	}

	@Override
	public void onLogTurner(EntityPlayer player, World world, int x,
			int y, int z, Direction side)
	{
		if (side == Direction.UP || side == Direction.DOWN) {
			final int metadata = DirectionalBlock
					.getDataFromMetadata(world
							.getBlockMetadata(x, y, z));
			world.setBlockAndMetadataWithNotify(x, y, z, wood.blockID,
					metadata);
		} else
			DirectionalBlock.setFacing(world, x, y, z, side, true);
	}

}
