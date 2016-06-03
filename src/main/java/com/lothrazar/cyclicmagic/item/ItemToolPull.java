package com.lothrazar.cyclicmagic.item;

import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.util.UtilPlaceBlocks;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemToolPull  extends BaseTool implements  IHasRecipe{

private static final int durability = 5000;
	
	public ItemToolPull(){
		super(durability);
	}
	
	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World worldObj, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
 
		BlockPos resultPosition;
		if(player.isSneaking()){
			resultPosition = UtilPlaceBlocks.pushBlock(worldObj, player, pos, side);
		}
		else{
			resultPosition = UtilPlaceBlocks.pullBlock(worldObj, player, pos, side);
		}

		onUse(stack, player, worldObj, hand);
		return super.onItemUse(stack, player, worldObj, resultPosition, hand, side, hitX, hitY, hitZ); 
	}

	@Override
	public void addRecipe() { 
		GameRegistry.addRecipe(new ItemStack(this),
				" gp", 
				" bg", 
				"b  ", 
			'b',Items.BLAZE_ROD, 
			'g',Items.GHAST_TEAR, 
		    'p',Blocks.STICKY_PISTON);  
	}
}
