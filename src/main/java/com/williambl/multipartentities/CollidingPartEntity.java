/*
 * Minecraft Forge
 * Copyright (c) 2016-2020.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation version 2.1
 * of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

package com.williambl.multipartentities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;

public class CollidingPartEntity<T extends Entity> extends Entity implements PartEntity<T> {
    private final T parent;
    private final EntityDimensions dimensions;

    public CollidingPartEntity(T parent, float width, float height) {
        super(parent.getType(), parent.world);
        this.parent = parent;
        this.dimensions = EntityDimensions.changing(width, height);
        this.calculateDimensions();
    }

    public T getParent() {
        return parent;
    }

    @Override
    protected void initDataTracker() {
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
    }

    @Override
    public boolean collides() {
        return true;
    }

    @Override
    public boolean isPartOf(Entity entity) {
        return this == entity || this.parent == entity;
    }

    @Override
    public Packet<?> createSpawnPacket() {
        throw new UnsupportedOperationException();
    }

    @Override
    public EntityDimensions getDimensions(EntityPose pose) {
        return this.dimensions;
    }

    @Override
    public boolean shouldSave() {
        return false;
    }
}