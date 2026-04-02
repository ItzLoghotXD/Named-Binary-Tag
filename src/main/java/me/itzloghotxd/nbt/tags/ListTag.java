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
import java.util.ArrayList;
import java.util.List;


@SuppressWarnings("unused")
public class ListTag<E extends Tag<?>> extends AbstractTag<List<E>> {
    private TagType elementType = null;

    /**
     * Constructs a new tag with the specified name and value.
     *
     * @param name  The name of the tag, or use empty string {@code ""} if unnamed
     */
    public ListTag(@NotNull String name) {
        super(name, new ArrayList<>());
    }

    /**
     * Constructs a new unnamed tag with the specified value.
     */
    public ListTag() {
        super(new ArrayList<>());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull TagType getType() {
        return TagType.LIST;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void serialize(@NotNull DataOutput output) throws IOException {
        if (!isEmpty()) {
            TagType type = this.elementType != null ? this.elementType : getValue().get(0).getType();

            output.writeByte(type.getId());
            output.writeInt(size());

            for (E tag : getValue()) {
                tag.serialize(output);
            }
        } else {
            output.writeByte(TagType.END.getId());
            output.writeInt(0);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deserialize(@NotNull DataInput input) throws IOException {
        getValue().clear();

        byte id = (byte) input.readUnsignedByte();
        int length = input.readInt();

        this.elementType = id != 0 ? TagType.getTypeById(id) : null;

        for (int i = 0; i < length; i++) {
            //noinspection DataFlowIssue
            Tag<?> tag = TagType.createByType(elementType);
            tag.deserialize(input);

            if (tag.getType() != elementType) {
                throw new IOException("Invalid tag type inside ListTag");
            }

            @SuppressWarnings("unchecked")
            E casted = (E) tag;

            getValue().add(casted);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull Tag<List<E>> clone() {
        ListTag<E> copy = new ListTag<>(getName());

        for (E tag : getValue()) {
            @SuppressWarnings("unchecked")
            E cloned = (E) tag.clone();
            copy.add(cloned);
        }

        return copy;
    }

    @SafeVarargs
    @SuppressWarnings("UnusedReturnValue")
    public final ListTag<E> add(E... values) {
        for (E value : values) {
            if (elementType == null) {
                elementType = value.getType();
            } else if (value.getType() != elementType) {
                throw new IllegalArgumentException("All elements must be of same TagType");
            }
            getValue().add(value);
        }
        return this;
    }

    public ListTag<E> add(int index, E value) {
        if (elementType == null) {
            elementType = value.getType();
        } else if (value.getType() != elementType) {
            throw new IllegalArgumentException("All elements must be of same TagType");
        }

        getValue().add(index, value);
        return this;
    }

    public E get(int i) {
        return getValue().get(i);
    }

    public ListTag<E> remove(int i) {
        getValue().remove(i);

        if (getValue().isEmpty()) {
            elementType = null;
        }

        return this;
    }

    public int size() {
        return getValue().size();
    }

    public boolean isEmpty() {
        return getValue().isEmpty();
    }

    public ListTag<E> clear() {
        getValue().clear();
        elementType = null;
        return this;
    }

    public TagType getElementType() {
        return elementType;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull String toJson() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");

        for (int i = 0; i < getValue().size(); i++) {
            if (get(i) instanceof StringTag) {
                sb.append("\"").append(escape((String) get(i).getValue())).append("\"");
            } else if (get(i) instanceof CompoundTag || get(i) instanceof ListTag<?>) {
                sb.append(get(i).toJson());
            } else {
                sb.append(get(i).getValue());
            }
            if (i < getValue().size() - 1) sb.append(",");
        }

        sb.append("]");
        return sb.toString();
    }
}
