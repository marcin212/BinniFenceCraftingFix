package com.bymarcin.binniefencerecipes;

import net.minecraft.block.Block;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import forestry.arboriculture.gadgets.BlockArbFence;
import forestry.core.config.ForestryBlock;
import binnie.extratrees.block.PlankType;
import binnie.extratrees.block.decor.BlockFence;
import binnie.extratrees.block.decor.BlockMultiFence;
import binnie.extratrees.block.decor.FenceType;

public class FenceConnectorRecipes implements IRecipe{
	public final int recipeWidth = 3;
	public final int recipeHeight = 2;
	
	@Override
	public boolean matches(InventoryCrafting inv, World world) {
		for (int i = 0; i <= 3 - this.recipeWidth; ++i) {
			for (int j = 0; j <= 3 - this.recipeHeight; ++j) {
				if (this.checkMatch(inv, i, j)) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean checkMatch(InventoryCrafting inv, int offseti, int offsetj){
		ItemStack found = null;
		int row = offsetj==0?2:0;
		for (int h = 0; h < 3; h++)
			if( inv.getStackInRowAndColumn(h, row)!=null) return false;
		
		
		for (int i = 0; i < this.recipeWidth; i++) {
			for (int j = 0; j < this.recipeHeight; j++) {
				ItemStack curr = inv.getStackInRowAndColumn(offseti + i, offsetj + j);
				if(curr==null) return false;
				if(found!=null)
				if(found!=null && !curr.isItemEqual(found)) return false;
				found = curr;
			}
		}
		
		if(found==null) return false;
		
		Block b = Block.getBlockFromItem(found.getItem());
		if(b instanceof BlockMultiFence || b instanceof BlockFence) {
			NBTTagCompound nbt = found.getTagCompound();
			if(nbt!=null && nbt.hasKey("meta")){
				if( (nbt.getInteger("meta") & (1<<10)) ==0){
					return true;
				}
			}
		}else if(b instanceof BlockArbFence ){
			return true;
		}
		return false;
	}
	
	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv) {
		ItemStack stack = inv.getStackInRowAndColumn(1, 1);
		
		int damage = stack.getItemDamage();
		Block b =  Block.getBlockFromItem(stack.getItem());
		if(b instanceof BlockArbFence ){
			ItemStack plank;
			if(((BlockArbFence) b).getWoodType(damage).ordinal()<16){
				plank = ForestryBlock.planks1.getItemStack(1,damage);
			}else{
				plank = ForestryBlock.planks2.getItemStack(1,damage);
			}
			ItemStack fenceout = PlankType.getFence(PlankType.get(plank), new FenceType(0, true, false));
			if(fenceout!=null){
				fenceout.stackSize = 5;
			}
			return fenceout;
		}else{
			ItemStack out = stack.copy();
			NBTTagCompound nbt = out.getTagCompound();
			if(nbt!=null && nbt.hasKey("meta")){
				damage = nbt.getInteger("meta");
				damage = damage ^ (1<<10);
				if(damage< 16388)
					out.setItemDamage(damage);
				nbt.setInteger("meta", damage);
			}
			out.stackSize = 5;
			return out;
		}
	}

	@Override
	public int getRecipeSize() {
		return this.recipeWidth * this.recipeHeight;
	}

	@Override
	public ItemStack getRecipeOutput() {
		return null;
	}

}
