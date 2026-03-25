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
 * Represents an NBT tag holding an array of {@code byte} values.
 *
 * <p>This tag corresponds to {@link me.itzloghotxd.nbt.TagType#BYTE_ARRAY}
 * and is used to store raw binary data.</p>
 *
 * <p>The array is serialized with its length followed by its elements.</p>
 *
 * @author ItzLoghotXD
 * @since 1.0.1
 */
public class ByteArrayTag extends AbstractTag<byte[]> {

    /**
     * Constructs a new tag with the specified name and byte array value.
     *
     * @param name  The name of the tag, must not be null
     * @param value The byte array value, must not be null
     */
    public ByteArrayTag(@NotNull String name, byte @NotNull [] value) {
        super(name, value);
    }

    /**
     * Constructs a new unnamed tag with the specified byte array value.
     *
     * @param value The byte array value, must not be null
     */
    public ByteArrayTag(byte @NotNull [] value) {
        super(value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull TagType getType() {
        return TagType.BYTE_ARRAY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void serialize(@NotNull DataOutput output) throws IOException {
        output.writeUTF(getName());
        output.writeInt(getValue().length);
        output.write(getValue());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deserialize(@NotNull DataInput input) throws IOException {
        setName(input.readUTF());
        int length = input.readInt();
        byte[] data = new byte[length];
        input.readFully(data);
        setValue(data);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull Tag<byte[]> clone() {
        return new ByteArrayTag(getName(), getValue().clone());
    }
}
