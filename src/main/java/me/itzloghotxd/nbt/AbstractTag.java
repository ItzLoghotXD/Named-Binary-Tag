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
import org.jetbrains.annotations.Nullable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Objects;

/**
 * An implementation of the {@link Tag} interface.
 * This class handles the common storage and retrieval of the Tag's name and value,
 * as well as standard {@code equals}, {@code hashCode}, and {@code toString} implementations.
 *
 * @param <T> The type of the value this tag holds
 * @author ItzLoghotXD
 * @since 1.0.0
 */
@SuppressWarnings("unused")
public abstract class AbstractTag<T> implements Tag<T> {

    @Nullable
    private String name;
    @SuppressWarnings("NotNullFieldNotInitialized")
    @NotNull
    private T value;

    /**
     * Constructs a new tag with the specified name and value.
     *
     * @param name The name of the tag, or null if unnamed
     * @param value The value of the tag, must not be null
     * @throws NullPointerException if value is null
     */
    public AbstractTag(@Nullable String name, @NotNull T value) {
        this.name = name;
        this.value = Objects.requireNonNull(value, "Tag value cannot be null");
    }

    /**
     * Constructs a new tag with the specified name.
     * Note: Subclasses using this constructor must ensure the value is initialized
     * before use.
     *
     * @param name The name of the tag
     */
    protected AbstractTag(@Nullable String name) {
        this.name = name;
    }

    /**
     * Constructs a new unnamed tag with the specified value.
     *
     * @param value The value of the tag, must not be null
     */
    public AbstractTag(@NotNull T value) {
        this(null, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract @NotNull TagType getType();

    /**
     * {@inheritDoc}
     */
    @Override
    public @Nullable String getName() {
        return name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setName(@Nullable String name) {
        this.name = name;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("ConstantConditions")
    @Override
    public @NotNull T getValue() {
        if (value == null) {
            throw new IllegalStateException(getClass().getSimpleName() + " value has not been initialized yet");
        }
        return value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setValue(@NotNull T value) {
        this.value = Objects.requireNonNull(value, "Tag value cannot be null");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract void serialize(@NotNull DataOutput output) throws IOException;

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract void deserialize(@NotNull DataInput input) throws IOException;

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract @NotNull Tag<T> clone();

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract @NotNull String toJson();

    /**
     * Returns a string representation of this tag.
     * Complex Tags like Compound, List, or Arrays Tag should override this to provide
     * recursive string formatting.
     *
     * @return String representation in format 'TAG_{TYPE}({NAME}):{VALUE}'.
     */
    @Override
    public @NotNull String toString() {
        String displayName = (name == null) ? "unnamed" : "\"" + name + "\"";
        return getValue() instanceof String ? "TAG_" + getType().getName() + "(" + displayName + "):" + "\"" + value + "\"" : "TAG_" + getType().getName() + "(" + displayName + "):" + value;
    }

    /**
     * Compares this tag to the specified object.
     * The result is true if and only if the argument is not null and is a Tag object
     * that has the same name, type, and value as this object.
     *
     * @param o The object to compare this {@code AbstractTag} against
     * @return {@code true} if the given object represents a Tag equivalent to this tag, {@code false} otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractTag<?> that = (AbstractTag<?>) o;
        return Objects.equals(name, that.name) && getType() == that.getType() && Objects.equals(value, that.value);
    }

    /**
     * Returns a hash code for this tag.
     * The hash code is computed based on the tag's name, type, and value.
     *
     * @return A hash code value for this object
     */
    @Override
    public int hashCode() {
        return Objects.hash(name, getType(), value);
    }
}
