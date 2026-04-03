/*
 * Copyright (c) 2026 ItzLoghotXD
 *
 * This file is part of "Named Binary Tags" Library.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation, version 3 of the License.
 */

package me.itzloghotxd.nbt;

import org.jetbrains.annotations.NotNull;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * The {@code Tag} interface represents a generic Named Binary Tag (NBT) element.
 * NBT is a binary format designed to store data in a compact and structured way,
 * commonly used for saving structured data.
 *
 * @param <T> The type of value this tag holds
 * @author ItzLoghotXD
 * @since 1.0.0
 */
@SuppressWarnings("unused")
public interface Tag<T> {

    /**
     * Gets the type of this tag.
     *
     * @return The {@link TagType} of this tag
     */
    @NotNull
    TagType getType();

    /**
     * Gets the name of this tag.
     *
     * @return The name of the tag, or empty string {@code ""} if unnamed
     */
    @NotNull
    String getName();

    /**
     * Sets the name of this tag.
     *
     * @param name The new name for this tag
     */
    void setName(@NotNull String name);

    /**
     * Gets the value stored in this tag.
     *
     * @return The non-null value
     */
    @NotNull
    T getValue();

    /**
     * Sets the value of this tag.
     *
     * @param value The new value, must not be {@code null}
     */
    void setValue(@NotNull T value);

    /**
     * Serializes this tag's payload to the specified output.
     *
     * @param output The data output to write to
     * @throws IOException If an I/O error occurs during writing
     */
    void serialize(@NotNull DataOutput output) throws IOException;

    /**
     * Deserializes data from the specified input into this tag.
     *
     * @param input The data input to read from
     * @throws IOException If an I/O error occurs during reading
     */
    void deserialize(@NotNull DataInput input) throws IOException;

    /**
     * Creates a deep copy of this tag.
     *
     * @return A new, independent tag instance containing the same data
     */
    @NotNull
    Tag<T> clone();

    /**
     * Converts the tag and its contents into a JSON string representation.
     *
     * @return A non-null JSON string
     */
    @NotNull
    String toJson();
}
