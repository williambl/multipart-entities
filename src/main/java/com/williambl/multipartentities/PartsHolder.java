package com.williambl.multipartentities;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.entity.Entity;

public interface PartsHolder {
    Int2ObjectMap<Entity> getParts();
}
