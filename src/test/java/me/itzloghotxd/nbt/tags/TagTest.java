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

        original.serialize(out);

        // Deserialize
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        DataInputStream in = new DataInputStream(bais);

        TagType type = TagType.getTypeById(in.readByte());
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
    void testUnsupportedTags() {
        assertThrows(UnsupportedOperationException.class, () -> TagType.createByType(TagType.LIST));
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