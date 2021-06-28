package com.williambl.multipartentities.mixin;

import com.williambl.multipartentities.MultipartEntity;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MovementType;
import net.minecraft.util.collection.ReusableStream;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Mixin(Entity.class)
public abstract class EntityMixin {
    @Shadow public World world;

    @Shadow public float stepHeight;

    @Shadow protected boolean onGround;

    @Shadow public abstract void move(MovementType movementType, Vec3d movement);

    @Inject(method = "adjustMovementForCollisions(Lnet/minecraft/util/math/Vec3d;)Lnet/minecraft/util/math/Vec3d;", at = @At("HEAD"), cancellable = true)
    void multipartEntities$adjustMovementForAllBoxes(Vec3d movement, CallbackInfoReturnable<Vec3d> cir) {
        if (this instanceof MultipartEntity mpE) {
            for (var box : Arrays.stream(mpE.getParts()).map(it -> it instanceof Entity e ? e : null).filter(Objects::nonNull).map(Entity::getBoundingBox).collect(Collectors.toList())) {
                ShapeContext shapeContext = ShapeContext.of((Entity) (Object) this);
                VoxelShape voxelShape = world.getWorldBorder().asVoxelShape();
                Stream<VoxelShape> stream = VoxelShapes.matchesAnywhere(voxelShape, VoxelShapes.cuboid(box.contract(1.0E-7D)), BooleanBiFunction.AND) ? Stream.empty() : Stream.of(voxelShape);
                Stream<VoxelShape> stream2 = world.getEntityCollisions((Entity) (Object) this, box.stretch(movement), (entity) -> true);
                ReusableStream<VoxelShape> reusableStream = new ReusableStream<>(Stream.concat(stream2, stream));
                Vec3d vec3d = movement.lengthSquared() == 0.0D ? movement : Entity.adjustMovementForCollisions((Entity) (Object) this, movement, box, world, shapeContext, reusableStream);
                boolean bl = movement.x != vec3d.x;
                boolean bl2 = movement.y != vec3d.y;
                boolean bl3 = movement.z != vec3d.z;
                boolean bl4 = onGround || bl2 && movement.y < 0.0D;
                if (stepHeight > 0.0F && bl4 && (bl || bl3)) {
                    Vec3d vec3d2 = Entity.adjustMovementForCollisions((Entity) (Object) this, new Vec3d(movement.x, stepHeight, movement.z), box, world, shapeContext, reusableStream);
                    Vec3d vec3d3 = Entity.adjustMovementForCollisions((Entity) (Object) this, new Vec3d(0.0D, stepHeight, 0.0D), box.stretch(movement.x, 0.0D, movement.z), world, shapeContext, reusableStream);
                    if (vec3d3.y < (double) stepHeight) {
                        Vec3d vec3d4 = Entity.adjustMovementForCollisions((Entity) (Object) this, new Vec3d(movement.x, 0.0D, movement.z), box.offset(vec3d3), world, shapeContext, reusableStream).add(vec3d3);
                        if (vec3d4.horizontalLengthSquared() > vec3d2.horizontalLengthSquared()) {
                            vec3d2 = vec3d4;
                        }
                    }

                    if (vec3d2.horizontalLengthSquared() > vec3d.horizontalLengthSquared()) {
                        movement = vec3d2.add(Entity.adjustMovementForCollisions((Entity) (Object) this, new Vec3d(0.0D, -vec3d2.y + movement.y, 0.0D), box.offset(vec3d2), world, shapeContext, reusableStream));
                        continue;
                    }
                }

                movement = vec3d;
            }
            cir.setReturnValue(movement);
        }
    }
}
