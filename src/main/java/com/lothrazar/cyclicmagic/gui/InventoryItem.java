package com.lothrazar.cyclicmagic.gui;

import com.lothrazar.cyclicmagic.ItemRegistry;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.IChatComponent;

public class InventoryItem implements IInventory {
	public static final int INV_SIZE = 8;
	private ItemStack[] inventory = new ItemStack[INV_SIZE];
	private final ItemStack invItem;

	public InventoryItem(ItemStack stack) {
		invItem = stack;
		if (!invItem.hasTagCompound()) {
			invItem.setTagCompound(new NBTTagCompound());
		}

		readFromNBT(invItem.getTagCompound());
	}

	private void readFromNBT(NBTTagCompound compound) {

		System.out.println("read");
		NBTTagList items = compound.getTagList("ItemInventory", INV_SIZE);

		for (int i = 0; i < items.tagCount(); ++i) {
			// 1.7.2+ change to items.getCompoundTagAt(i)

			NBTTagCompound item = (NBTTagCompound) items.getCompoundTagAt(i);
			int slot = item.getInteger("Slot");

			// Just double-checking that the saved slot index is within our
			// inventory array bounds
			if (slot >= 0 && slot < getSizeInventory()) {
				inventory[slot] = ItemStack.loadItemStackFromNBT(item);
			}
		}
	}

	public void writeToNBT(NBTTagCompound compound) {
		System.out.println("writeToNBT");
		// Create a new NBT Tag List to store itemstacks as NBT Tags
		NBTTagList items = new NBTTagList();

		for (int i = 0; i < getSizeInventory(); ++i) {
			System.out.println(i + "__writeToNBT");
			// Only write stacks that contain items
			if (getStackInSlot(i) != null) {
				// Make a new NBT Tag Compound to write the itemstack and slot
				// index to
				NBTTagCompound item = new NBTTagCompound();
				item.setInteger("Slot", i);
				// Writes the itemstack in slot(i) to the Tag Compound we just
				// made
				getStackInSlot(i).writeToNBT(item);

				// add the tag compound to our tag list
				System.out.println(i + "__appendTag");
				items.appendTag(compound);
			}
		}
		// Add the TagList to the ItemStack's Tag Compound with the name
		// "ItemInventory"
		compound.setTag("ItemInventory", items);
	}

	@Override
	public String getName() {

		return "Wand Inventory";
	}

	@Override
	public boolean hasCustomName() {
		return false;
	}

	@Override
	public IChatComponent getDisplayName() {
		return null;
	}

	@Override
	public int getSizeInventory() {
		return INV_SIZE;
	}

	@Override
	public ItemStack getStackInSlot(int index) {
		return inventory[index];
	}

	@Override
	public ItemStack decrStackSize(int slot, int amount) {
		ItemStack stack = getStackInSlot(slot);
		if (stack != null) {
			if (stack.stackSize > amount) {
				stack = stack.splitStack(amount);
				// Don't forget this line or your inventory will not be saved!
				//markDirty();
			}
			else {
				// this method also calls onInventoryChanged, so we don't need
				// to call it again
				setInventorySlotContents(slot, null);
			}
		}
		return stack;
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack stack) {
		inventory[slot] = stack;

		if (stack != null && stack.stackSize > getInventoryStackLimit()) {
			stack.stackSize = getInventoryStackLimit();
		}

		//markDirty();

	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public void markDirty() {

		System.out.println("markDirty");
		// TODO: this has nbt.copy issue somewhere.doesnt happen if this is
		// gone?

		for (int i = 0; i < getSizeInventory(); ++i) {
			if (getStackInSlot(i) != null && getStackInSlot(i).stackSize == 0) {
				inventory[i] = null;
			}
		}

		// This line here does the work:
		writeToNBT(invItem.getTagCompound());

	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return true;
	}

	@Override
	public void openInventory(EntityPlayer player) {
	}

	@Override
	public void closeInventory(EntityPlayer player) {
		System.out.println("close event dirty");
		markDirty();
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		// only placeable blocks, not any old item
		return stack.getItem() != ItemRegistry.cyclic_wand && Block.getBlockFromItem(stack.getItem()) != null;
	}

	@Override
	public int getField(int id) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setField(int id, int value) {
		// TODO Auto-generated method stub

	}

	@Override
	public int getFieldCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub

	}

}
