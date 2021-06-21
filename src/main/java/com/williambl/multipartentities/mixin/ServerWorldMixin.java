package com.williambl.multipartentities.mixin;

import com.williambl.multipartentities.PartEntity;
import com.williambl.multipartentities.PartsHolder;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.dragon.EnderDragonPart;
import net.minecraft.server.world.ServerWorld;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerWorld.class)
public class ServerWorldMixin implements PartsHolder {
    final Int2ObjectMap<Entity> parts = new Int2ObjectOpenHashMap<>();

    @Override
    public Int2ObjectMap<Entity> getParts() {
        return parts;
    }

    @Redirect(method = "getDragonPart", at = @At(value = "FIELD", target = "Lnet/minecraft/server/world/ServerWorld;dragonParts:Lit/unimi/dsi/fastutil/ints/Int2ObjectMap;", opcode = Opcodes.GETFIELD))
    private Int2ObjectMap<Entity> multipartEntities$getPart(ServerWorld world) {
        return parts;
    }
}
