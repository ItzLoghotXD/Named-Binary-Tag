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

import me.itzloghotxd.nbt.tags.CompoundTag;
import me.itzloghotxd.nbt.tags.ListTag;

import java.io.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * This class provides methods for reading/writing {@link CompoundTag} to specified file
 *
 * @author ItzLoghotXD
 * @since 1.0.4
 */
public class TagIO {

    /**
     * write a {@link CompoundTag} to the specified file.
     * if the file doesn't exist then it will create it.
     *
     * @param file the file to write
     * @param tag the tag to store in the file
     * @throws IOException If an I/O error occurs
     */
    public static void serialize(File file, CompoundTag tag, boolean compress) throws IOException {
        if (compress) {
            serializeCompressed(file, tag);
        } else {
            serializeUncompressed(file, tag);
        }
    }

    /**
     * write a {@link CompoundTag} to the specified file (uncompressed).
     * if the file doesn't exist then it will create it.
     *
     * @param file the file to write
     * @param tag the tag to store in the file
     * @throws IOException If an I/O error occurs
     */
    private static void serializeUncompressed(File file, CompoundTag tag) throws IOException {
        try (DataOutputStream output = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(file)))) {
            output.writeByte(tag.getType().getId());
            output.writeUTF(tag.getName());
            tag.serialize(output);
        }
    }

    /**
     * write a {@link CompoundTag} to the specified file and compress it using gzip.
     * if the file doesn't exist then it will create it.
     *
     * @param file the file to write
     * @param tag the tag to store in the file
     * @throws IOException If an I/O error occurs
     */
    private static void serializeCompressed(File file, CompoundTag tag) throws IOException {
        try (DataOutputStream output = new DataOutputStream(new GZIPOutputStream(new BufferedOutputStream(new FileOutputStream(file))))) {
            output.writeByte(tag.getType().getId());
            output.writeUTF(tag.getName());
            tag.serialize(output);
        }
    }

    /**
     * reads data from the specified file.
     *
     * @param file the file to read
     * @return a {@link CompoundTag}
     * @throws IOException If an I/O error occurs
     */
    public static CompoundTag deserialize(File file) throws IOException {
        if (isCompressed(file)) {
            return deserializeCompressed(file);
        } else {
            return deserializeUncompressed(file);
        }
    }

    /**
     * reads data from the specified uncompressed file.
     *
     * @param file the file to read
     * @return a {@link CompoundTag}
     * @throws IOException If an I/O error occurs
     */
    private static CompoundTag deserializeUncompressed(File file) throws IOException {
        try (DataInputStream input = new DataInputStream(new BufferedInputStream(new FileInputStream(file)))) {
            TagType type = TagType.getTypeById(input.readUnsignedByte());

            if (type != TagType.COMPOUND) {
                throw new IOException("Root tag must be CompoundTag, found: " + type);
            }

            CompoundTag tag = (CompoundTag) TagType.createByType(type);
            tag.setName(input.readUTF());
            tag.deserialize(input);
            return tag;
        }
    }

    /**
     * reads data from the specified compressed file.
     *
     * @param file the file to read
     * @return a {@link CompoundTag}
     * @throws IOException If an I/O error occurs
     */
    private static CompoundTag deserializeCompressed(File file) throws IOException {
        try (DataInputStream input = new DataInputStream(new GZIPInputStream(new BufferedInputStream(new FileInputStream(file))))) {
            TagType type = TagType.getTypeById(input.readUnsignedByte());

            if (type != TagType.COMPOUND) {
                throw new IOException("Root tag must be CompoundTag, found: " + type);
            }

            CompoundTag tag = (CompoundTag) TagType.createByType(type);
            tag.setName(input.readUTF());
            tag.deserialize(input);
            return tag;
        }
    }

    public static boolean isCompressed(File file) throws IOException {
        try (InputStream input = new FileInputStream(file);
             BufferedInputStream buffered = new BufferedInputStream(input)) {

            buffered.mark(2);

            int b1 = buffered.read();
            int b2 = buffered.read();

            buffered.reset();

            if (b1 == -1 || b2 == -1) {
                throw new EOFException("File too short to determine compression");
            }

            return (b1 == 0x1F && b2 == 0x8B);
        }
    }

    public static Tag<?> getByPath(CompoundTag root, String path) {
        if (root == null || path == null || path.isEmpty()) return null;

        if (path.startsWith(".") || path.endsWith(".") || path.contains("..")) {
            return null;
        }

        Tag<?> current = root;
        int i = 0;

        while (i < path.length()) {
            int dot = path.indexOf('.', i);
            String part;

            if (dot == -1) {
                part = path.substring(i);
                i = path.length();
            } else {
                part = path.substring(i, dot);
                i = dot + 1;
            }

            if (part.isEmpty()) return null;

            int bracketStart = part.indexOf('[');
            if (bracketStart != -1) {
                int bracketEnd = part.indexOf(']', bracketStart);

                if (bracketEnd == -1 || bracketEnd != part.length() - 1) return null;

                String key = part.substring(0, bracketStart);
                String indexStr = part.substring(bracketStart + 1, bracketEnd);

                if (key.isEmpty() || indexStr.isEmpty()) return null;

                int index;

                try {
                    index = Integer.parseInt(indexStr);
                } catch (NumberFormatException e) {
                    return null;
                }

                if (!(current instanceof CompoundTag compound)) return null;
                Tag<?> listTag = compound.get(key);
                if (!(listTag instanceof ListTag<?> list)) return null;
                if (index < 0 || index >= list.getValue().size()) return null;
                current = list.get(index);

            } else {
                if (!(current instanceof CompoundTag compound)) return null;
                current = compound.get(part);
            }

            if (current == null) return null;
        }

        return current;
    }
}
