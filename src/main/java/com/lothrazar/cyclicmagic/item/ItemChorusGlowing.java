package com.lothrazar.cyclicmagic.item;
import java.util.List;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.data.Const;
import com.lothrazar.cyclicmagic.net.PacketSyncPlayerFlying;
import com.lothrazar.cyclicmagic.registry.CapabilityRegistry;
import com.lothrazar.cyclicmagic.registry.RecipeRegistry;
import com.lothrazar.cyclicmagic.registry.CapabilityRegistry.IPlayerExtendedProperties;
import com.lothrazar.cyclicmagic.util.UtilChat;
import com.lothrazar.cyclicmagic.util.UtilNBT;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemChorusGlowing extends ItemFood implements IHasRecipe {
  public static final int FLY_SECONDS = 5 * 60;//five minutes each
  public ItemChorusGlowing() {
    super(4, false);
    this.setAlwaysEdible();
  }
  @Override
  protected void onFoodEaten(ItemStack par1ItemStack, World world, EntityPlayer player) {
    setFlying(player);
    IPlayerExtendedProperties props = CapabilityRegistry.getPlayerProperties(player);
    props.setFlyingTimer(props.getFlyingTimer() + FLY_SECONDS * Const.TICKS_PER_SEC);
  }
  @Override
  public IRecipe addRecipe() {
    return RecipeRegistry.addShapedRecipe(new ItemStack(this, 8),
        "lll", 
        "lgl", 
        "lll",
        'g', "dustGlowstone",
        'l', Items.CHORUS_FRUIT);
  }
  private void setFlying(EntityPlayer player) {
    player.fallDistance = 0.0F;
    player.capabilities.allowFlying = true;
    player.capabilities.isFlying = true;
  }
  private void setNonFlying(EntityPlayer player) {
    player.capabilities.allowFlying = false;
    player.capabilities.isFlying = false;
    if (player instanceof EntityPlayerMP) { //force clientside  to  update
      ModCyclic.network.sendTo(new PacketSyncPlayerFlying(false), (EntityPlayerMP) player);
    }
  }
  @SubscribeEvent
  public void onPlayerUpdate(LivingUpdateEvent event) {
    if (event.getEntityLiving() instanceof EntityPlayer == false) { return; }
    EntityPlayer player = (EntityPlayer) event.getEntityLiving();
    IPlayerExtendedProperties props = CapabilityRegistry.getPlayerProperties(player);
    int flyingTicks = props.getFlyingTimer();//TICKS NOT SECONDS
    if (flyingTicks > 0) {
      props.setFlyingTimer(props.getFlyingTimer() - 1);
      setFlying(player);
    }
    else { //times up!
      props.setFlyingTimer(0);//in case negative
      setNonFlying(player);
    }
  }
  @Override
  @SideOnly(Side.CLIENT)
  public void addInformation(ItemStack stack, World playerIn, List<String> tooltips, net.minecraft.client.util.ITooltipFlag advanced) {
    tooltips.add(UtilChat.lang(this.getUnlocalizedName() + ".tooltip"));
  }
  @Override
  @SideOnly(Side.CLIENT)
  public boolean hasEffect(ItemStack stack) {
    return true;
  }
}
