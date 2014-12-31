package com.bymarcin.binniefencerecipes;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

import binnie.extratrees.block.IPlankType;
import binnie.extratrees.block.PlankType;
import binnie.extratrees.block.decor.FenceType;

public class FenceRecipes implements IRecipe {

	public final int recipeWidth;
	public final int recipeHeight;
	private final FenceType type;
	private final char[][] pattern;

	public FenceRecipes(FenceType type, String... patterns) {
		this.type = type;
		recipeHeight = patterns.length;
		int width = 0;
		for (String p : patterns) {
			if (p.length() > width) {
				width = p.length();
			}
		}
		recipeWidth = width;
		pattern = new char[recipeHeight][recipeWidth];
		for (int i = 0; i < recipeHeight; i++) {
			String row = patterns[i];
			for (int j = 0; j < recipeWidth; j++) {
				pattern[i][j] = row.charAt(j);
			}
		}

	}

	public ItemStack getRecipeOutput() {
		return null;
	}

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
		Map<Character, IPlankType> planks = new HashMap<Character, IPlankType>();
		
		
		if(this.recipeHeight==2 && this.recipeWidth==3){
		int row = offsetj==0?2:0;
		for (int h = 0; h < 3; h++)
			if( inv.getStackInRowAndColumn(h, row)!=null) return false;
		}
		
		
		
		for (int i = 0; i < this.recipeWidth; i++) {
			for (int j = 0; j < this.recipeHeight; j++) {
				char c = pattern[j][i];
				ItemStack stack = inv.getStackInRowAndColumn(i + offseti, j + offsetj);
				if ((c == ' ' && stack != null) || (c != ' ' && stack == null))
					return false;
				if (c == ' ')
					continue;
				
				IPlankType curr = PlankType.get(stack);
				if (curr == null)
					return false;

				if (!planks.containsKey(c)) {
					planks.put(c, curr);
				} else if (planks.get(c) != curr) {
					return false;
				}
			}
		}
		
		return true;
	}

	public ItemStack getCraftingResult(InventoryCrafting inv) {
		IPlankType[] planks = new IPlankType[2];
		int n = 0;
		loop: for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (n == 2)
					break loop;
				ItemStack stack = inv.getStackInRowAndColumn(j, i);
				if (stack == null)
					continue;
				IPlankType plank = PlankType.get(stack);
				if (plank == null)
					continue;
				if(planks[0]==plank) continue;
				planks[n++] = plank;
			}
		}
		ItemStack out =  PlankType.getFence(planks[0], planks[1]!=null?planks[1]:planks[0], type);
		out.stackSize = 4;
		return out;
	}

	public int getRecipeSize() {
		return this.recipeWidth * this.recipeHeight;
	}

}
