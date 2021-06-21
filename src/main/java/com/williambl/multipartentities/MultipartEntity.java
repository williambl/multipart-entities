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

import org.jetbrains.annotations.Nullable;

public interface MultipartEntity {
    /**
     * This is used to specify that your entity has multiple individual parts, such as the Vanilla Ender Dragon.
     *
     * See {@link net.minecraft.entity.boss.dragon.EnderDragonEntity} for an example implementation.
     * @return true if this is a multipart entity.
     */
    default boolean isMultipartEntity()
    {
        return false;
    }

    /**
     * Gets the individual sub parts that make up this entity.
     *
     * The entities returned by this method are NOT saved to the world in nay way, they exist as an extension
     * of their host entity. The child entity does not track its server-side(or client-side) counterpart, and
     * the host entity is responsible for moving and managing these children.
     *
     * Only used if {@link #isMultipartEntity()} returns true.
     *
     * See {@link net.minecraft.entity.boss.dragon.EnderDragonEntity} for an example implementation.
     * @return The child parts of this entity. The value to be returned here should be cached.
     */
    @Nullable
    default PartEntity<?>[] getParts()
    {
        return null;
    }
}
