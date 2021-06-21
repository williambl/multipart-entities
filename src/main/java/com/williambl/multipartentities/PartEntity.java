package com.williambl.multipartentities;

import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;

public interface PartEntity<T extends Entity> {
    T getParent();
}