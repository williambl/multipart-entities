package com.williambl.multipartentities.mixin;

import com.williambl.multipartentities.MultipartEntity;
import com.williambl.multipartentities.PartEntity;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ThreadedAnvilChunkStorage;
import net.minecraft.util.TypeFilter;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Group;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.function.Predicate;

@Mixin(ThreadedAnvilChunkStorage.class)
public class ThreadedAnvilChunkStorageMixin {
    @Group(name = "multipartentities$changeLoadingPartClass")
    @Redirect(method = "loadEntity", at=@At(value = "CONSTANT", args = "classValue=net/minecraft/entity/boss/dragon/EnderDragonPart", ordinal = 0))
    private Class<?> multipartentities$changeLoadingPartClass$dev(Object targetObject, Class<?> classValue) {
        return PartEntity.class;
    }

    @Group(name = "multipartentities$changeLoadingPartClass")
    @Redirect(method = "loadEntity", at=@At(value = "CONSTANT", args = "classValue=net/minecraft/class_1508", ordinal = 0))
    private Class<?> multipartentities$changeLoadingPartClass$prod(Object targetObject, Class<?> classValue) {
        return PartEntity.class;
    }

}
