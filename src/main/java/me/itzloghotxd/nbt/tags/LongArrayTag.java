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
 * Represents an NBT tag holding an array of {@code long} values.
 *
 * <p>This tag corresponds to {@link me.itzloghotxd.nbt.TagType#LONG_ARRAY}
 * and is used to store sequences of 64-bit integers.</p>
 *
 * <p>The array is serialized with its length followed by its elements.</p>
 *
 * @author ItzLoghotXD
 * @since 1.0.1
 */
public class LongArrayTag extends AbstractTag<long[]> {
    /**
     * Constructs a new tag with the specified name and long array value.
     *
     * @param name  The name of the tag, must not be null
     * @param value The long array value, must not be null
     */
    public LongArrayTag(@NotNull String name, long @NotNull [] value) {
        super(name, value);
    }

    /**
     * Constructs a new unnamed tag with the specified long array value.
     *
     * @param value The long array value, must not be null
     */
    public LongArrayTag(long @NotNull [] value) {
        super(value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull TagType getType() {
        return TagType.LONG_ARRAY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void serialize(@NotNull DataOutput output) throws IOException {
        output.writeInt(getValue().length);
        for (long i : getValue()) {
            output.writeLong(i);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deserialize(@NotNull DataInput input) throws IOException {
        int length = input.readInt();
        long[] data = new long[length];
        for (int i = 0; i < length; i++) {
            data[i] = input.readLong();
        }
        setValue(data);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull Tag<long[]> clone() {
        return new LongArrayTag(getName(), getValue().clone());
    }
}
