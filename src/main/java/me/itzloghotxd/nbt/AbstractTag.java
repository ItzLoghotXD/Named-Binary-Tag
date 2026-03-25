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
import java.util.Arrays;
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
//@SuppressWarnings("unused")
public abstract class AbstractTag<T> implements Tag<T> {

    /**
     */
    @NotNull
    protected String name;
    /**
     */
    @NotNull
    protected T value;

    /**
     * Constructs a new tag with the specified name and value.
     *
     * @param name The name of the tag, or use empty string {@code ""} if unnamed
     * @param value The value of the tag, must not be null
     * @throws NullPointerException if value is null
     */
    public AbstractTag(@NotNull String name, @NotNull T value) {
        this.name = name;
        this.value = value;
    }

    /**
     * Constructs a new unnamed tag with the specified value.
     *
     * @param value The value of the tag, must not be null
     */
    public AbstractTag(@NotNull T value) {
        this("", value);
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
    public @NotNull String getName() {
        return name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setName(@NotNull String name) {
        this.name = name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull T getValue() {
        return value;
    }

    /**
     * Sets the value of this tag.
     *
     * @param value The new value, must not be {@code null}
     */
    @Override
    public void setValue(@NotNull T value) {
        this.value = value;
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
     * Converts this tag into its JSON string representation.
     * <p>
     * The resulting JSON includes the tag's type, name, and value in the following format:
     * </p>
     *
     * <pre>
     * {
     *   "type": "TYPE_NAME",
     *   "name": "TAG_NAME",
     *   "value": VALUE
     * }
     * </pre>
     *
     * <p>
     * The value is serialized using {@link #valueToJson()}, which may be overridden
     * by subclasses to support complex or nested data structures such as lists or compounds.
     * String values are properly escaped using {@link #escape(String)} to ensure valid JSON output.
     * </p>
     *
     * <p>
     * Subclasses generally do not need to override this method unless they require
     * a completely custom JSON structure.
     * </p>
     *
     * @return A valid JSON string representation of this tag (never {@code null})
     */
    @Override
    public @NotNull String toJson() {
        return "{\"type\":\"" + getType().getName() + "\"," +
                "\"name\":\"" + escape(name) + "\"," +
                "\"value\":" + valueToJson() + "}";
    }

    /**
     * Escapes special characters in a string to make it safe for inclusion in JSON.
     * <p>
     * This method currently escapes:
     * <ul>
     *     <li>Backslashes ({@code \}) → {@code \\}</li>
     *     <li>Double quotes ({@code "}) → {@code \"}</li>
     * </ul>
     * <p>
     * This ensures that the resulting string does not break JSON syntax.
     * Additional escaping rules (e.g., control characters) may be added if needed.
     *
     * @param s The input string to escape, must not be {@code null}
     * @return The escaped string safe for JSON output
     */
    protected @NotNull String escape(@NotNull String s) {
        return s.replace("\\", "\\\\")
                .replace("\"", "\\\"");
    }

    /**
     * Converts the underlying value of this tag into a valid JSON representation.
     * <p>
     * This method is used internally by {@link #toJson()} to serialize the tag's value.
     * Primitive types are converted using {@link String#valueOf(Object)}, while
     * special handling is applied for certain types:
     * <ul>
     *     <li>{@link String} values are wrapped in double quotes and escaped</li>
     *     <li>Primitive arrays (e.g., {@code byte[]}, {@code int[]}, {@code long[]}) are
     *     converted using {@link java.util.Arrays#toString(Object[])}</li>
     * </ul>
     * <p>
     * Complex tag types (e.g., List or Compound tags) should override this method
     * to provide proper recursive JSON serialization.
     *
     * @return A JSON-formatted string representing the value of this tag
     */
    protected @NotNull String valueToJson() {
        if (value instanceof String s) {
            return "\"" + escape(s) + "\"";
        } else if (value instanceof byte[] b) {
            return Arrays.toString(b);
        } else if (value instanceof int[] i) {
            return Arrays.toString(i);
        } else if (value instanceof long[] loghot) {
            return Arrays.toString(loghot);
        }
        return String.valueOf(value);
    }

    /**
     * Returns a string representation of this tag.
     * Complex Tags like Compound, List Tag should override this to provide
     * recursive string formatting.
     *
     * @return String representation in format 'TAG_{TYPE}({NAME}):{VALUE}'.
     */
    @Override
    public @NotNull String toString() {
        String displayName = name.isEmpty() ? "unnamed" : "\"" + name + "\"";
        String payLoad = String.valueOf(value);
        if (getValue() instanceof String s) {
            payLoad = "\"" + s + "\"";
        } else if (getValue() instanceof byte[] b) {
            payLoad = Arrays.toString(b);
        } else if (getValue() instanceof int[] i) {
            payLoad = Arrays.toString(i);
        } else if (getValue() instanceof long[] loghot) {
            payLoad = Arrays.toString(loghot);
        }
        return "TAG_" + getType().getName() + "(" + displayName + "):" + payLoad;
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

        return Objects.equals(name, that.name)
                && getType() == that.getType()
                && valuesEqual(this.value, that.value);
    }

    private static boolean valuesEqual(Object a, Object b) {
        if (a instanceof byte[] a1 && b instanceof byte[] b1) return Arrays.equals(a1, b1);
        if (a instanceof int[] a1 && b instanceof int[] b1) return Arrays.equals(a1, b1);
        if (a instanceof long[] a1 && b instanceof long[] b1) return Arrays.equals(a1, b1);

        return Objects.equals(a, b);
    }

    /**
     * Returns a hash code for this tag.
     * The hash code is computed based on the tag's name, type, and value.
     *
     * @return A hash code value for this object
     */
    @Override
    public int hashCode() {
        return Objects.hash(name, getType(), valueHash(value));
    }

    private static int valueHash(Object v) {
        if (v instanceof byte[] b) return Arrays.hashCode(b);
        if (v instanceof int[] i) return Arrays.hashCode(i);
        if (v instanceof long[] l) return Arrays.hashCode(l);

        return Objects.hashCode(v);
    }
}
