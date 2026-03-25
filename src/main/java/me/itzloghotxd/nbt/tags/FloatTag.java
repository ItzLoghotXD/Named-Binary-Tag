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
 * Represents an NBT tag holding a single {@code float} value.
 *
 * <p>This tag corresponds to {@link me.itzloghotxd.nbt.TagType#FLOAT}
 * and is used to store 32-bit floating point numbers.</p>
 *
 * @author ItzLoghotXD
 * @since 1.0.1
 */
public class FloatTag extends AbstractTag<Float> {
    /**
     * Constructs a new tag with the specified name and float value.
     *
     * @param name  The name of the tag, must not be null
     * @param value The value, must not be null
     */
    public FloatTag(@NotNull String name, @NotNull Float value) {
        super(name, value);
    }

    /**
     * Constructs a new unnamed tag with the specified float value.
     *
     * @param value The value, must not be null
     */
    public FloatTag(@NotNull Float value) {
        super(value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull TagType getType() {
        return TagType.FLOAT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void serialize(@NotNull DataOutput output) throws IOException {
        output.writeUTF(getName());
        output.writeFloat(getValue());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deserialize(@NotNull DataInput input) throws IOException {
        setName(input.readUTF());
        setValue(input.readFloat());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull Tag<Float> clone() {
        return new FloatTag(getName(), getValue());
    }
}
