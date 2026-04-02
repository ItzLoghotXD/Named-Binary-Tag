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
import java.util.Arrays;
import java.util.Dictionary;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Represents an NBT compound tag that contains a collection of {@code Tags}.
 * This tag type acts as a container for other {@code Tags}, similar to a {@link Dictionary} or {@link Map} structure.
 *
 * @author ItzLoghotXD
 * @since 1.0.2
 */
@SuppressWarnings("unused")
public class CompoundTag extends AbstractTag<Map<String, Tag<?>>> {
    /**
     * Constructs a new tag with the specified name and value.
     *
     * @param name  The name of the tag, or use empty string {@code ""} if unnamed
     */
    public CompoundTag(@NotNull String name) {
        super(name, new LinkedHashMap<>());
    }

    /**
     * Constructs a new unnamed tag with the specified value.
     */
    public CompoundTag() {
        super(new LinkedHashMap<>());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull TagType getType() {
        return TagType.COMPOUND;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void serialize(@NotNull DataOutput output) throws IOException {
        for (Map.Entry<String, Tag<?>> entry : getValue().entrySet()) {
            Tag<?> tag = entry.getValue();
            output.writeByte(tag.getType().getId());
            output.writeUTF(entry.getKey());
            tag.serialize(output);
        }
        output.writeByte(TagType.END.getId());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deserialize(@NotNull DataInput input) throws IOException {
        while (true) {
            byte id = input.readByte();
            if (id == TagType.END.getId()) break;

            Tag<?> tag = TagType.createByType(TagType.getTypeById(id));
            String k = input.readUTF();
            tag.deserialize(input);
            tag.setName(k);

            add(k, tag);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull Tag<Map<String, Tag<?>>> clone() {
        CompoundTag copy = new CompoundTag();
        for (Map.Entry<String, Tag<?>> entry : getValue().entrySet()) {
            copy.add(entry.getKey(), entry.getValue().clone());
        }
        return copy;
    }

    /**
     * Puts the key and {@link Tag} to this Tag.
     *
     * @param key The name of the key
     * @param tag The {@link Tag} to store
     * @return a reference to this tag
     */
    @SuppressWarnings("UnusedReturnValue")
    public CompoundTag add(String key, Tag<?> tag) {
        tag.setName(key);
        getValue().put(key, tag);
        return this;
    }

    /**
     * Gets the {@link Tag} from the specified key.
     *
     * @param key The key to get value of
     * @return A {@link Tag}
     */
    public Tag<?> get(String key) {
        return getValue().get(key);
    }

    /**
     * Removes the {@link Tag} from the specified key.
     *
     * @param key The key to remove value of
     * @return a reference to this tag
     */
    public CompoundTag remove(String key) {
        getValue().remove(key);
        return this;
    }

    /**
     * Check whether the key exists in the {@link Map} or not.
     *
     * @param key the key to check
     * @return {@link Boolean}
     */
    public boolean contains(String key) {
        return getValue().containsKey(key);
    }

    /**
     * {@inheritDoc}
     *
     * @return A string in the format "name:[{value}]"
     */
    @Override
    public @NotNull String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("TAG_COMPOUND(\"").append(getName()).append("\"):{");
        boolean first = true;
        for (Map.Entry<String, Tag<?>> entry : getValue().entrySet()) {
            if (!first) builder.append(", ");
            first = false;
            builder.append("\"").append(entry.getKey()).append("\"=").append(entry.getValue().toString());
        }
        builder.append("}");
        return builder.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull String toJson() {
        StringBuilder json = new StringBuilder();
        json.append("{");

        boolean first = true;
        for (Map.Entry<String, Tag<?>> entry : getValue().entrySet()) {
            if (!first) json.append(",");
            first = false;

            Tag<?> tag = entry.getValue();

            json.append("\"").append(tag.getName()).append("\":");

            if (tag instanceof CompoundTag || tag.getValue() instanceof Map) {
                json.append(tag.toJson());
            } else if (tag instanceof ListTag<?>) {
                json.append(tag.toJson());
            } else if (tag.getValue() instanceof String) {
                json.append("\"").append(escape((String) tag.getValue())).append("\"");
            } else if (tag instanceof ByteArrayTag byteArrayTag) {
                json.append(Arrays.toString(byteArrayTag.getValue()));
            } else if (tag instanceof IntegerArrayTag intArrayTag) {
                json.append(Arrays.toString(intArrayTag.getValue()));
            } else if (tag instanceof LongArrayTag longArrayTag) {
                json.append(Arrays.toString(longArrayTag.getValue()));
            } else {
                json.append(tag.getValue());
            }
        }

        json.append("}");
        return json.toString();
    }
}
