package com.williambl.multipartentities.mixin;

import com.williambl.multipartentities.MultipartEntity;
import com.williambl.multipartentities.PartEntity;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.util.TypeFilter;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.entity.EntityLookup;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
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
    @Shadow protected abstract EntityLookup<Entity> getEntityLookup();
    //FIXME: THIS REALLY SHOULD MIXIN TO THE LAMBDAS BUT THAT DOESN'T WORK

    @Inject(method = "getOtherEntities", at=@At(value = "RETURN"))
    private void multipartentities$getOtherEntitiesParts(Entity except, Box box, Predicate<? super Entity> predicate, CallbackInfoReturnable<List<Entity>> cir) {
        getEntityLookup().forEachIntersects(box, (entity2) -> {
            if (entity2 instanceof MultipartEntity mpE && !(entity2 instanceof EnderDragonEntity)) {
                PartEntity<?>[] var11 = mpE.getParts();

                for (PartEntity<?> part : var11) {
                    if (part instanceof Entity partE) {
                        if (entity2 != except && predicate.test(partE)) {
                            cir.getReturnValue().add(partE);
                        }
                    }
                }
            }
        });
    }

    @Inject(method = "getEntitiesByType", at=@At(value = "RETURN"))
    private <T extends Entity> void multipartentities$getEntitiesByTypeParts(TypeFilter<Entity, T> filter, Box box, Predicate<? super T> predicate, CallbackInfoReturnable<List<T>> cir) {
        this.getEntityLookup().forEachIntersects(filter, box, (entity) -> {
            if (entity instanceof MultipartEntity mpE && !(entity instanceof EnderDragonEntity)) {
                PartEntity<?>[] var11 = mpE.getParts();

                for (PartEntity<?> part : var11) {
                    if (part instanceof Entity partE) {
                        T entity2 = filter.downcast(partE);
                        if (entity2 != null && predicate.test(entity2)) {
                            cir.getReturnValue().add(entity2);
                        }
                    }
                }
            }
        });
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
