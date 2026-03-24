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

//import me.itzloghotxd.nbt.tags.ByteTag;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Enumeration of all supported NBT tag types.
 * Each type has a unique ID and name that corresponds to the NBT specification.
 *
 * @author ItzLoghotXD
 * @since 1.0.0
 */
@SuppressWarnings("unused")
public enum TagType {
    /** Represents the end of a {@code CompoundTag} structure. */
    END(0, "END"),
    /** Represents a single signed 8-bit {@link Byte} value. */
    BYTE(1, "BYTE"),
    /** Represents a signed 16-bit {@link Short} integer value. */
    SHORT(2, "SHORT"),
    /** Represents a signed 32-bit {@link Integer} value. */
    INT(3, "INT"),
    /** Represents a signed 64-bit {@link Long} integer value. */
    LONG(4, "LONG"),
    /** Represents a 32-bit {@link Float}ing point value. */
    FLOAT(5, "FLOAT"),
    /** Represents a 64-bit {@link Double} precision floating point value. */
    DOUBLE(6, "DOUBLE"),
    /** Represents an array of {@link Byte} values. */
    BYTE_ARRAY(7, "BYTE_ARRAY"),
    /** Represents a UTF-8 {@link String} value. */
    STRING(8, "STRING"),
    /** Represents a {@link List} of nameless {@link Tag}s of the same type. */
    LIST(9, "LIST"),
    /** Represents a map-like collection of named {@link Tag}s. */
    COMPOUND(10, "COMPOUND"),
    /** Represents an array of {@link Integer} values. */
    INT_ARRAY(11, "INT_ARRAY"),
    /** Represents an array of {@link Long} values. */
    LONG_ARRAY(12, "LONG_ARRAY");
//    /** */
//    BOOLEAN(13, "BOOLEAN"),
//    /** */
//    SHORT_ARRAY(14, "SHORT ARRAY"),
//    /** */
//    FLOAT_ARRAY(15, "FLOAT ARRAY"),
//    /** */
//    DOUBLE_ARRAY(16, "DOUBLE ARRAY"),
//    /** */
//    STRING_ARRAY(17, "STRING ARRAY");

    private final int id;
    @NotNull
    private final String name;

    TagType(int id, @NotNull String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * Gets the unique numeric ID of this tag type.
     *
     * @return The tag type ID
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the string name of this tag type.
     *
     * @return The tag type name
     */
    @NotNull
    public String getName() {
        return name;
    }

    /**
     * Returns the TagType associated with the given ID.
     *
     * @param id The numerical ID of the tag type
     * @return The corresponding {@code TagType} constant
     * @throws IllegalArgumentException If no tag type exists for the given ID
     */
    @NotNull
    public static TagType getTypeById(int id) {
        for (TagType type : values()) {
            if (type.getId() == id) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown tag type ID: " + id);
    }


//    /**
//     * Creates a new, instance of a {@link Tag} corresponding to the specified {@code TagType}
//     * with its corresponding value and null name.
//     *
//     * @param type The {@code  TagType} of the {@link Tag} to create
//     * @return A new instance of the corresponding {@link Tag} implementation
//     */
//    @SuppressWarnings("DuplicateBranchesInSwitch")
//    public static Tag<?> createByType(TagType type) {
//        return switch (type) {
//            case END -> throw new UnsupportedOperationException("Not Supported YET!");
//            case BYTE -> new ByteTag((byte) 0);
//            case SHORT -> throw new UnsupportedOperationException("Not Supported YET!");
//            case INT -> throw new UnsupportedOperationException("Not Supported YET!");
//            case LONG -> throw new UnsupportedOperationException("Not Supported YET!");
//            case FLOAT -> throw new UnsupportedOperationException("Not Supported YET!");
//            case DOUBLE -> throw new UnsupportedOperationException("Not Supported YET!");
//            case BYTE_ARRAY -> throw new UnsupportedOperationException("Not Supported YET!");
//            case STRING -> throw new UnsupportedOperationException("Not Supported YET!");
//            case LIST -> throw new UnsupportedOperationException("Not Supported YET!");
//            case COMPOUND -> throw new UnsupportedOperationException("Not Supported YET!");
//            case INT_ARRAY -> throw new UnsupportedOperationException("Not Supported YET!");
//            case LONG_ARRAY -> throw new UnsupportedOperationException("Not Supported YET!");
//        };
//    }
}
