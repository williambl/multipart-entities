package com.williambl.multipartentities.mixin;

import com.williambl.multipartentities.MultipartEntity;
import com.williambl.multipartentities.AbstractPartEntity;
import com.williambl.multipartentities.PartEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.boss.dragon.EnderDragonPart;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(EnderDragonPart.class)
public class EnderDragonPartEntityMixin implements PartEntity<EnderDragonEntity> {
    @Shadow @Final public EnderDragonEntity owner;

    @Override
    public EnderDragonEntity getParent() {
        return owner;
    }
}
