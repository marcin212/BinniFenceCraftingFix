package com.bymarcin.binniefencerecipes;

import net.minecraft.block.Block;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import forestry.arboriculture.gadgets.BlockArbFence;
import binnie.extratrees.block.IPlankType;
import binnie.extratrees.block.PlankType;
import binnie.extratrees.block.decor.BlockFence;
import binnie.extratrees.block.decor.BlockMultiFence;
import binnie.extratrees.block.decor.FenceType;

public class FenceConnectorRecipesSecond implements IRecipe{
	public final int recipeWidth = 1;
	public final int recipeHeight = 3;
	
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
	
	private boolean checkMatch(InventoryCrafting inv, int offseti, int offsetj) {
		ItemStack fence = inv.getStackInRowAndColumn(offseti, offsetj);
		ItemStack fence2 = inv.getStackInRowAndColumn(offseti, offsetj+2); 
		ItemStack plank = inv.getStackInRowAndColumn(offseti, offsetj+1);
		for(int i=0;i<3;i++){
			if(i==offseti) continue;
			for(int j =0; j<3; j++){
				if(inv.getStackInRowAndColumn(i,j)!=null) return false;
			}
		}
		
		if(fence!=null && fence2!=null && plank !=null){
			if(fence.isItemEqual(fence2)){
				IPlankType type = PlankType.get(plank);
				if(type !=null){
					Block b = Block.getBlockFromItem(fence.getItem());
					if(b instanceof BlockMultiFence || b instanceof BlockFence){
						NBTTagCompound nbt = fence.getTagCompound();
						if(nbt!=null && nbt.hasKey("meta")){
							if(type == BlockFence.getPlank(nbt.getInteger("meta"))){
								if( (nbt.getInteger("meta") & (1<<11)) ==0){
									return true;
								}
							}
						}
						
					}else if(b instanceof BlockArbFence){
						ItemStack pattern = PlankType.getFence(type, new FenceType(0));
						if(pattern!=null){
							return pattern.isItemEqual(fence);
						}
					}
				}
			}
		}
		
		return false;
		
	}
	
	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv) {
		ItemStack fence = null;
		ItemStack plank = null;
		
		for(int i=0; i<3; i++){
			if(inv.getStackInRowAndColumn(i, 0)!=null){
				fence = inv.getStackInRowAndColumn(i, 0);
			}
			if(inv.getStackInRowAndColumn(i, 1)!=null){
				plank = inv.getStackInRowAndColumn(i, 1);
			}
		}
		
		int damage = fence.getItemDamage();
		Block b = Block.getBlockFromItem(fence.getItem());
		if(b instanceof BlockArbFence){
			IPlankType t = PlankType.get(plank);
			FenceType type =  new FenceType(0, false, true);
			ItemStack stack = PlankType.getFence(t,type);
			if(stack!=null)
				stack.stackSize = 2;
			return stack;
		}else{
			ItemStack out = fence.copy();
			NBTTagCompound nbt = out.getTagCompound();
			if(nbt!=null && nbt.hasKey("meta")){
				damage = nbt.getInteger("meta");
				damage = damage ^ (1<<11);
				if(damage< 16388)
					out.setItemDamage(damage);
				nbt.setInteger("meta", damage);
			}
			out.stackSize = 2;
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
