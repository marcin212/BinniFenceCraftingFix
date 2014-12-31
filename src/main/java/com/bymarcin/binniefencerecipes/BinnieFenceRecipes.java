package com.bymarcin.binniefencerecipes;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;

import binnie.extratrees.block.decor.FenceType;

@Mod(modid = BinnieFenceRecipes.MODID, version = BinnieFenceRecipes.VERSION, dependencies = "after:ExtraTrees")
public class BinnieFenceRecipes
{
    public static final String MODID = "binniefencerecipes";
    public static final String VERSION = "1.0";
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {

    }
    
    @EventHandler
    public void postinit(FMLPostInitializationEvent event)
    {
        GameRegistry.addRecipe(new FenceRecipes( new FenceType(0, false, false), "#Y#", "# #"));
        GameRegistry.addRecipe(new FenceRecipes( new FenceType(1, false, false), "#Y#", "# #", " Y "));
        GameRegistry.addRecipe(new FenceRecipes( new FenceType(2, false, false), " Y ", "# #", "#Y#"));
        GameRegistry.addRecipe(new FenceConnectorRecipes());
        GameRegistry.addRecipe(new FenceConnectorRecipesSecond());
    }
}
