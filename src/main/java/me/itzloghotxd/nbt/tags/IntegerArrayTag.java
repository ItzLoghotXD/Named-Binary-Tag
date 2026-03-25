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
 * Represents an NBT tag holding an array of {@code int} values.
 *
 * <p>This tag corresponds to {@link me.itzloghotxd.nbt.TagType#INT_ARRAY}
 * and is used to store sequences of 32-bit integers.</p>
 *
 * <p>The array is serialized with its length followed by its elements.</p>
 *
 * @author ItzLoghotXD
 * @since 1.0.1
 */
public class IntegerArrayTag extends AbstractTag<int[]> {
    /**
     * Constructs a new tag with the specified name and int array value.
     *
     * @param name  The name of the tag, must not be null
     * @param value The int array value, must not be null
     */
    public IntegerArrayTag(@NotNull String name, int @NotNull [] value) {
        super(name, value);
    }

    /**
     * Constructs a new unnamed tag with the specified int array value.
     *
     * @param value The int array value, must not be null
     */
    public IntegerArrayTag(int @NotNull [] value) {
        super(value);
    }

    @Override
    public @NotNull TagType getType() {
        return TagType.INT_ARRAY;
    }

    @Override
    public void serialize(@NotNull DataOutput output) throws IOException {
        output.writeUTF(getName());
        output.writeInt(getValue().length);
        for (int i : getValue()) {
            output.writeInt(i);
        }
    }

    @Override
    public void deserialize(@NotNull DataInput input) throws IOException {
        setName(input.readUTF());
        int length = input.readInt();
        int[] data = new int[length];
        for (int i = 0; i < length; i++) {
            data[i] = input.readInt();
        }
        setValue(data);
    }

    @Override
    public @NotNull Tag<int[]> clone() {
        return new IntegerArrayTag(getName(), getValue().clone());
    }
}
