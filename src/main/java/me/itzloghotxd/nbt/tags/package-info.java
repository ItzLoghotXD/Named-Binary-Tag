/**
 * Contains concrete implementations of {@link me.itzloghotxd.nbt.Tag}
 * representing all currently supported Named Binary Tag (NBT) types.
 *
 * <p>Each class in this package corresponds to a specific {@link me.itzloghotxd.nbt.TagType}
 * and is responsible for handling its own value storage, serialization,
 * deserialization, and cloning.</p>
 *
 * <p>Primitive tags store single values (e.g., {@code int}, {@code byte}),
 * while array tags store sequences of primitive values.</p>
 *
 * <p>More complex structures such as list and compound tags may be added in future versions.</p>
 *
 * @author ItzLoghotXD
 * @since 1.0.1
 */
package me.itzloghotxd.nbt.tags;