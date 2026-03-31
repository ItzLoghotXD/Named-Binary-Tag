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
 * Represents an NBT tag holding a single {@code double} value.
 *
 * <p>This tag corresponds to {@link me.itzloghotxd.nbt.TagType#DOUBLE}
 * and is used to store 64-bit floating point numbers.</p>
 *
 * @author ItzLoghotXD
 * @since 1.0.1
 */
public class DoubleTag extends AbstractTag<Double> {
    /**
     * Constructs a new tag with the specified name and double value.
     *
     * @param name  The name of the tag, must not be null
     * @param value The value, must not be null
     */
    public DoubleTag(@NotNull String name, @NotNull Double value) {
        super(name, value);
    }

    /**
     * Constructs a new unnamed tag with the specified double value.
     *
     * @param value The value, must not be null
     */
    public DoubleTag(@NotNull Double value) {
        super(value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull TagType getType() {
        return TagType.DOUBLE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void serialize(@NotNull DataOutput output) throws IOException {
        output.writeDouble(getValue());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deserialize(@NotNull DataInput input) throws IOException {
        setValue(input.readDouble());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull Tag<Double> clone() {
        return new DoubleTag(getName(), getValue());
    }
}
