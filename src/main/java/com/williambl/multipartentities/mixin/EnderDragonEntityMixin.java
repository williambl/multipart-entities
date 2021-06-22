package com.williambl.multipartentities.mixin;

import com.williambl.multipartentities.MultipartEntity;
import com.williambl.multipartentities.PartEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.boss.dragon.EnderDragonPart;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(EnderDragonEntity.class)
public class EnderDragonEntityMixin implements MultipartEntity {
    @Shadow @Final private EnderDragonPart[] parts;

    @Override
    public @Nullable PartEntity<?>[] getParts() {
        return (PartEntity<?>[]) parts;
    }
}
