package me.itzloghotxd.nbt.tags;

import me.itzloghotxd.nbt.Tag;
import me.itzloghotxd.nbt.TagType;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class TagTest {

    @Test
    void testPrimitiveTags() throws IOException {
        testTag(new ByteTag("byte", (byte) 5));
        testTag(new ShortTag("short", (short) 10));
        testTag(new IntegerTag("int", 100));
        testTag(new LongTag("long", 1000L));
        testTag(new FloatTag("float", 1.5F));
        testTag(new DoubleTag("double", 2.5D));
        testTag(new StringTag("string", "hello"));
    }

    @Test
    void testArrayTags() throws IOException {
        testTag(new ByteArrayTag("bytes", new byte[]{1, 2, 3}));
        testTag(new IntegerArrayTag("ints", new int[]{4, 5, 6}));
        testTag(new LongArrayTag("longs", new long[]{7L, 8L, 9L}));
    }

    @Test
    void testCompoundTag() throws IOException {
        CompoundTag original = new CompoundTag("box");
        original.add("slot_1", new StringTag("item_1"))
                .add("slot_2", new StringTag("item_2"))
                .add("slot_3", new StringTag("item_3"))
                .add("slot_4", new StringTag("item_4"));

        // Serialize (with type header like your main)
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(baos);

        out.writeByte(original.getType().getId());
        out.writeUTF(original.getName());
        original.serialize(out);

        // Deserialize
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        DataInputStream in = new DataInputStream(bais);

        TagType type = TagType.getTypeById(in.readUnsignedByte());
        CompoundTag copy = (CompoundTag) TagType.createByType(type);
        copy.setName(in.readUTF());
        copy.deserialize(in);

        // Assertions
        assertEquals(original.getName(), copy.getName());
        assertEquals(original.getValue().size(), copy.getValue().size());

        for (Map.Entry<String, Tag<?>> entry : original.getValue().entrySet()) {
            Tag<?> copied = copy.getValue().get(entry.getKey());
            assertNotNull(copied);
            assertEquals(entry.getValue().toString(), copied.toString());
        }

        // toString sanity
        assertNotNull(copy.toString());
        assertFalse(copy.toString().isEmpty());

        // toJson sanity
        String json = copy.toJson();
        assertNotNull(json);
    }

    @Test
    void testListTag() throws IOException {
        CompoundTag original = new CompoundTag();

        original.add("0", new ListTag<CompoundTag>().add(
                new CompoundTag().add("id", new StringTag("minecraft:wooden_axe"))
                        .add("tag", new CompoundTag().add("Damage", new IntegerTag(0)))
                        .add("Count", new ByteTag((byte) 1)),

                new CompoundTag().add("id", new StringTag("minecraft:light_blue_concrete"))
                        .add("Count", new ByteTag((byte) 1)),

                new CompoundTag().add("id", new StringTag("minecraft:redstone"))
                        .add("Count", new ByteTag((byte) 1)),

                new CompoundTag().add("id", new StringTag("minecraft:repeater"))
                        .add("Count", new ByteTag((byte) 1)),

                new CompoundTag().add("id", new StringTag("minecraft:redstone_torch"))
                        .add("Count", new ByteTag((byte) 1)),

                new CompoundTag().add("id", new StringTag("minecraft:comparator"))
                        .add("Count", new ByteTag((byte) 1)),

                new CompoundTag().add("id", new StringTag("minecraft:stone_button"))
                        .add("Count", new ByteTag((byte) 1)),

                new CompoundTag().add("id", new StringTag("minecraft:lever"))
                        .add("Count", new ByteTag((byte) 1)),

                new CompoundTag().add("id", new StringTag("minecraft:redstone_lamp"))
                        .add("Count", new ByteTag((byte) 1))
        )).add("DataVersion", new IntegerTag(2586));

        // Serialize
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(baos);

        out.writeByte(original.getType().getId());
        out.writeUTF(original.getName());
        original.serialize(out);

        // Deserialize
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        DataInputStream in = new DataInputStream(bais);

        TagType type = TagType.getTypeById(in.readUnsignedByte());
        CompoundTag copy = (CompoundTag) TagType.createByType(type);
        copy.setName(in.readUTF());
        copy.deserialize(in);

        // Basic assertions
        assertEquals(original.getValue().size(), copy.getValue().size());
        assertEquals(original.toJson(), copy.toJson());

        // Ensure list exists
        Tag<?> listTag = copy.getValue().get("0");
        assertNotNull(listTag);
        assertInstanceOf(ListTag.class, listTag);

        ListTag<?> list = (ListTag<?>) listTag;
        assertEquals(9, list.getValue().size());

        // Check one deep value
        CompoundTag firstItem = (CompoundTag) list.getValue().get(0);
        assertEquals("minecraft:wooden_axe",
                ((StringTag) firstItem.getValue().get("id")).getValue());

        // toString sanity
        assertNotNull(copy.toString());
        assertFalse(copy.toString().isEmpty());
    }

    // ---------- helper ----------

    private <T> void testTag(Tag<T> original) throws IOException {
        // Clone test
        Tag<T> clone = original.clone();
        assertEquals(original, clone);
        assertNotSame(original, clone);

        // Serialize -> Deserialize
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(baos);

        original.serialize(out);

        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        DataInputStream in = new DataInputStream(bais);

        Tag<?> copy = TagType.createByType(original.getType());
        copy.deserialize(in);

        assertValuesEqual(original.getValue(), copy.getValue());

        // toString sanity
        String str = original.toString();
        assertNotNull(str);
        assertFalse(str.isEmpty());

        // toJson sanity
        String json = original.toJson();
        assertNotNull(json);
        assertTrue(json.contains("\"value\""));
    }

    private void assertValuesEqual(Object a, Object b) {
        if (a instanceof byte[] a1 && b instanceof byte[] b1) {
            assertArrayEquals(a1, b1);
        } else if (a instanceof int[] a1 && b instanceof int[] b1) {
            assertArrayEquals(a1, b1);
        } else if (a instanceof long[] a1 && b instanceof long[] b1) {
            assertArrayEquals(a1, b1);
        } else {
            assertEquals(a, b);
        }
    }
}