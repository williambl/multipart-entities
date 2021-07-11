package com.williambl.multipartentities.mixin;

import com.williambl.multipartentities.MultipartEntity;
import com.williambl.multipartentities.PartEntity;
import com.williambl.multipartentities.PartsHolder;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.boss.dragon.EnderDragonPart;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "net/minecraft/server/world/ServerWorld$ServerEntityHandler")
public class ServerEntityHandlerMixin {
    @SuppressWarnings("ShadowTarget")
    @Shadow ServerWorld field_26936;

    @Redirect(method = "Lnet/minecraft/server/world/ServerWorld$ServerEntityHandler;startTracking(Lnet/minecraft/entity/Entity;)V", at=@At(value = "CONSTANT", args = "classValue=net/minecraft/entity/boss/dragon/EnderDragonEntity", ordinal = 0))
    private boolean multipartentities$cancelStartTrackingParts(Object targetObject, Class<?> classValue) {
        return false;
    }


    @Inject(method = "Lnet/minecraft/server/world/ServerWorld$ServerEntityHandler;startTracking(Lnet/minecraft/entity/Entity;)V", at=@At("TAIL"))
    private void multipartentities$startTrackingParts(Entity entity, CallbackInfo ci) {
        if (entity instanceof MultipartEntity mpE) {
            PartEntity<?>[] parts = mpE.getParts();

            for (PartEntity<?> part : parts) {
                ((PartsHolder)field_26936).getParts().put(((Entity)part).getId(), (Entity) part);
            }
        }
    }

    @Redirect(method = "Lnet/minecraft/server/world/ServerWorld$ServerEntityHandler;stopTracking(Lnet/minecraft/entity/Entity;)V", at=@At(value = "CONSTANT", args = "classValue=net/minecraft/entity/boss/dragon/EnderDragonEntity", ordinal = 0))
    private boolean multipartentities$cancelStopTrackingParts(Object targetObject, Class<?> classValue) {
        return false;
    }


    @Inject(method = "Lnet/minecraft/server/world/ServerWorld$ServerEntityHandler;stopTracking(Lnet/minecraft/entity/Entity;)V", at=@At("TAIL"))
    private void multipartentities$stopTrackingParts(Entity entity, CallbackInfo ci) {
        if (entity instanceof MultipartEntity mpE) {
            PartEntity<?>[] parts = mpE.getParts();

            for (PartEntity<?> part : parts) {
                ((PartsHolder)field_26936).getParts().remove(((Entity)part).getId());
            }
        }
    }
}
