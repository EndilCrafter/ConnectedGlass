package com.supermartijn642.connectedglass;

import com.supermartijn642.connectedglass.model.CGConnectedBakedModel;
import com.supermartijn642.connectedglass.model.CGConnectedPaneBakedModel;
import com.supermartijn642.core.registry.ClientRegistrationHandler;
import net.minecraft.item.DyeColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;

/**
 * Created 5/7/2020 by SuperMartijn642
 */
@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ConnectedGlassClient {

    public static void register(){
        ClientRegistrationHandler handler = ClientRegistrationHandler.get("connectedglass");

        // Set render type for all the blocks
        for(CGGlassType type : CGGlassType.values()){
            if(type.isTinted)
                handler.registerBlockModelTranslucentRenderType(type::getBlock);
            else
                handler.registerBlockModelCutoutMippedRenderType(type::getBlock);
            if(type.hasPanes)
                handler.registerBlockModelCutoutMippedRenderType(type::getPane);

            // Register translucent render type for all the colored blocks
            for(DyeColor color : DyeColor.values()){
                handler.registerBlockModelTranslucentRenderType(() -> type.getBlock(color));
                if(type.hasPanes)
                    handler.registerBlockModelTranslucentRenderType(() -> type.getPane(color));
            }
        }
        handler.registerBlockModelTranslucentRenderType(() -> ConnectedGlass.tinted_glass);

        // Add overrides for the baked models
        for(CGGlassType type : CGGlassType.values()){
            handler.registerBlockModelOverwrite(type::getBlock, CGConnectedBakedModel::new);
            if(type.hasPanes)
                handler.registerBlockModelOverwrite(type::getPane, CGConnectedPaneBakedModel::new);
            for(DyeColor color : DyeColor.values()){
                handler.registerBlockModelOverwrite(() -> type.getBlock(color), CGConnectedBakedModel::new);
                if(type.hasPanes)
                    handler.registerBlockModelOverwrite(() -> type.getPane(color), CGConnectedPaneBakedModel::new);
            }
        }
    }
}
