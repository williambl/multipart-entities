package com.williambl.multipartentities.mixin;

import com.williambl.multipartentities.MultipartEntity;
import com.williambl.multipartentities.PartEntity;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.TypeFilter;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.stream.Stream;

@SuppressWarnings("UnresolvedMixinReference")
@Mixin(World.class)
public abstract class WorldMixin implements WorldAccess, AutoCloseable {
    @Redirect(method = "method_31593", at=@At(value = "CONSTANT", args = "classValue=net/minecraft/entity/boss/dragon/EnderDragonEntity", ordinal = 0))
    private static boolean multipartentities$cancelGetOtherEntitiesParts(Object targetObject, Class<?> classValue) {
        return false;
    }


    @Inject(method = "method_31593", at=@At(value = "CONSTANT", args = "classValue=net/minecraft/entity/boss/dragon/EnderDragonEntity", ordinal = 0))
    private static void multipartentities$getOtherEntitiesParts(Entity except, Predicate<? super Entity> predicate, List<Entity> list, Entity entity2, CallbackInfo ci) {
        if (entity2 instanceof MultipartEntity mpE) {
            PartEntity<?>[] var11 = mpE.getParts();

            for (PartEntity<?> part : var11) {
                if (part instanceof Entity partE) {
                    if (entity2 != except && predicate.test(partE)) {
                        list.add(partE);
                    }
                }
            }
        }
    }

    @Redirect(method = "method_31596", at=@At(value = "CONSTANT", args = "classValue=net/minecraft/entity/boss/dragon/EnderDragonEntity", ordinal = 0))
    private static boolean multipartentities$cancelGetEntitiesByTypeParts(Object targetObject, Class<?> classValue) {
        return false;
    }


    @Inject(method = "method_31596", at=@At(value = "CONSTANT", args = "classValue=net/minecraft/entity/boss/dragon/EnderDragonEntity", ordinal = 0))
    private static <T extends Entity> void multipartentities$getEntitiesByTypeParts(Predicate<? super Entity> predicate, List<Entity> list, TypeFilter<Entity, T> typeFilter, Entity entity, CallbackInfo ci) {
        if (entity instanceof MultipartEntity mpE) {
            PartEntity<?>[] var11 = mpE.getParts();

            for (PartEntity<?> part : var11) {
                if (part instanceof Entity partE) {
                    T entity2 = typeFilter.downcast(partE);
                    if (entity2 != null && predicate.test(entity2)) {
                        list.add(entity2);
                    }
                }
            }
        }
    }

    @Override
    public Stream<VoxelShape> getEntityCollisions(@Nullable Entity entity, Box box, Predicate<Entity> predicate) {
        if (entity instanceof MultipartEntity mpE) {
            var parts = Arrays.asList(mpE.getParts());
            //noinspection SuspiciousMethodCalls
            return Arrays.stream(mpE.getParts()).flatMap(it -> WorldAccess.super.getEntityCollisions((Entity) it, box, predicate.and(other -> !parts.contains(other))));
        }
        return WorldAccess.super.getEntityCollisions(entity, box, predicate);
    }
}
