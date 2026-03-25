/*
 * Copyright (c) 2026 ItzLoghotXD
 *
 * This file is part of "Named Binary Tags" Library.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation, version 3 of the License.
 */

package me.itzloghotxd.nbt.tags;

import me.itzloghotxd.nbt.AbstractTag;
import me.itzloghotxd.nbt.Tag;
import me.itzloghotxd.nbt.TagType;
import org.jetbrains.annotations.NotNull;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * Represents an NBT tag holding a single {@code byte} value.
 *
 * <p>This tag corresponds to {@link me.itzloghotxd.nbt.TagType#BYTE}
 * and is used to store small numeric values.</p>
 *
 * @author ItzLoghotXD
 * @since 1.0.1
 */
public class ByteTag extends AbstractTag<Byte> {
    /**
     * Constructs a new tag with the specified name and byte value.
     *
     * @param name  The name of the tag, must not be null
     * @param value The value, must not be null
     */
    public ByteTag(@NotNull String name, @NotNull Byte value) {
        super(name, value);
    }

    /**
     * Constructs a new unnamed tag with the specified byte value.
     *
     * @param value The value, must not be null
     */
    public ByteTag(@NotNull Byte value) {
        super(value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull TagType getType() {
        return TagType.BYTE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void serialize(@NotNull DataOutput output) throws IOException {
        output.writeUTF(getName());
        output.writeByte(getValue());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deserialize(@NotNull DataInput input) throws IOException {
        setName(input.readUTF());
        setValue(input.readByte());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull Tag<Byte> clone() {
        return new ByteTag(getName(), getValue());
    }
}
