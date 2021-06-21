package com.williambl.multipartentities.mixin;

import com.williambl.multipartentities.MultipartEntity;
import com.williambl.multipartentities.PartEntity;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {
    @Redirect(method = "attack", at=@At(value = "CONSTANT", args = "classValue=net/minecraft/entity/boss/dragon/EnderDragonPart", ordinal = 0))
    private boolean multipartentities$cancelRenderHitbox(Object targetObject, Class<?> classValue) {
        return false;
    }


    @Inject(method = "attack", at=@At(value = "CONSTANT", args = "classValue=net/minecraft/entity/boss/dragon/EnderDragonPart", ordinal = 0), locals = LocalCapture.CAPTURE_FAILHARD)
    private void multipartentities$renderHitbox(Entity target, CallbackInfo ci, float f, float h, boolean bl, boolean bl2, int j, boolean bl3, boolean bl4, float k, boolean bl5, int l, ItemStack itemStack2, Entity entity) {
        if (target instanceof PartEntity<?> mpE) {
            target = mpE.getParent();
        }
    }
}
