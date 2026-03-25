package me.itzloghotxd.nbt.tags;

import me.itzloghotxd.nbt.Tag;
import me.itzloghotxd.nbt.TagType;
import org.junit.jupiter.api.Test;

import java.io.*;

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
    void testUnsupportedTags() {
        assertThrows(UnsupportedOperationException.class, () -> TagType.createByType(TagType.END));
        assertThrows(UnsupportedOperationException.class, () -> TagType.createByType(TagType.LIST));
        assertThrows(UnsupportedOperationException.class, () -> TagType.createByType(TagType.COMPOUND));
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

        assertValuesEqual(original.getName(), copy.getName());
        assertValuesEqual(original.getValue(), copy.getValue());

        // toString sanity
        String str = original.toString();
        assertNotNull(str);
        assertFalse(str.isEmpty());

        // toJson sanity
        String json = original.toJson();
        assertNotNull(json);
        assertTrue(json.contains("\"type\""));
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