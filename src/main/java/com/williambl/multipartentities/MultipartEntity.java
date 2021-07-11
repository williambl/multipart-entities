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

import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;

/**
 * An entity with sub-entities, known as 'parts'.
 *
 * Part entities are an extension of Vanilla's system for ender dragon part entities. These entities are not saved,
 * ticked, or handled like regular entities.
 *
 * Because they are not synced from server->client, they will have different entity IDs on both sides. See
 * {@link net.minecraft.entity.boss.dragon.EnderDragonEntity#onSpawnPacket(EntitySpawnS2CPacket)} for how to fix this.
 */
public interface MultipartEntity {
    /**
     * Gets the individual sub parts that make up this entity.
     *
     * The entities returned by this method are NOT saved to the world in nay way, they exist as an extension
     * of their host entity. The child entity does not track its server-side(or client-side) counterpart, and
     * the host entity is responsible for moving and managing these children.
     *
     * See {@link net.minecraft.entity.boss.dragon.EnderDragonEntity} for an example implementation.
     * @return The child parts of this entity. The value to be returned here should be cached.
     */
    default PartEntity<?>[] getParts() {
        return new PartEntity[0];
    }
}
