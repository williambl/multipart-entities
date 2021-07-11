package com.williambl.multipartentities.mixin;

import com.williambl.multipartentities.MultipartEntity;
import com.williambl.multipartentities.PartEntity;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.boss.dragon.EnderDragonPart;
import net.minecraft.util.math.MathHelper;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Group;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderDispatcher.class)
public class EntityRenderDispatcherMixin {

    @Group(name = "multipartentities$cancelRenderHitbox")
    @Redirect(method = "renderHitbox", at=@At(value = "CONSTANT", args = "classValue=net/minecraft/entity/boss/dragon/EnderDragonEntity", ordinal = 0))
    private static boolean multipartentities$cancelRenderHitbox$dev(Object targetObject, Class<?> classValue) {
        return false;
    }

    @Group(name = "multipartentities$cancelRenderHitbox")
    @Redirect(method = "renderHitbox", at=@At(value = "CONSTANT", args = "classValue=net/minecraft/class_1510", ordinal = 0))
    private static boolean multipartentities$cancelRenderHitbox$prod(Object targetObject, Class<?> classValue) {
        return false;
    }


    @Inject(method = "renderHitbox", at=@At(value = "INVOKE", target = "Lnet/minecraft/client/render/WorldRenderer;drawBox(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;Lnet/minecraft/util/math/Box;FFFF)V", ordinal = 0))
    private static void multipartentities$renderHitbox(MatrixStack matrices, VertexConsumer vertices, Entity entity, float tickDelta, CallbackInfo ci) {
        if (entity instanceof MultipartEntity mpE) {
            double d = -MathHelper.lerp(tickDelta, entity.lastRenderX, entity.getX());
            double e = -MathHelper.lerp(tickDelta, entity.lastRenderY, entity.getY());
            double f = -MathHelper.lerp(tickDelta, entity.lastRenderZ, entity.getZ());
            PartEntity<?>[] var11 = mpE.getParts();

            for (PartEntity<?> part : var11) {
                if (part instanceof Entity partE) {
                    matrices.push();
                    double g = d + MathHelper.lerp(tickDelta, partE.lastRenderX, partE.getX());
                    double h = e + MathHelper.lerp(tickDelta, partE.lastRenderY, partE.getY());
                    double i = f + MathHelper.lerp(tickDelta, partE.lastRenderZ, partE.getZ());
                    matrices.translate(g, h, i);
                    WorldRenderer.drawBox(matrices, vertices, partE.getBoundingBox().offset(-partE.getX(), -partE.getY(), -partE.getZ()), 0.25F, 1.0F, 0.0F, 1.0F);
                    matrices.pop();
                }
            }
        }

    }
}
