package com.lothrazar.cyclicmagic.spell;

import com.lothrazar.cyclicmagic.projectile.EntityShearingBolt; 
import com.lothrazar.cyclicmagic.util.UtilSound;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class SpellThrowShear extends BaseSpell implements ISpell {

	public SpellThrowShear(int id,String name){
		super.init(id,name);
		this.cost = 1;
	}
 
	@Override
	public boolean cast(World world, EntityPlayer player, BlockPos pos, EnumFacing side) {

		if(world.isRemote == false){
			world.spawnEntityInWorld(new EntityShearingBolt(world, player));
		}
		return true;
	}
	
	@Override
	public void payCost(World world, EntityPlayer player, BlockPos pos) {

		UtilSound.playSoundAt(player, UtilSound.Own.pew);
		super.payCost(world, player, pos);
	}

	@Override
	public void spawnParticle(World world, EntityPlayer player, BlockPos pos) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void playSound(World world, EntityPlayer player, BlockPos pos) {
		// TODO Auto-generated method stub
		
	}
}
