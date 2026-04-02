package me.itzloghotxd.nbt;

import me.itzloghotxd.nbt.tags.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class TagIOTest {

    private final File testFile = new File("test_nbt.dat");

    @AfterEach
    void cleanup() {
        if (testFile.exists()) {
            assertTrue(testFile.delete());
        }
    }

    // ---------- basic helpers ----------

    private CompoundTag createSampleTag() {
        CompoundTag root = new CompoundTag("root");

        root.add("name", new StringTag("loghot"));

        ListTag<StringTag> list = new ListTag<>();
        list.add(new StringTag("one"));
        list.add(new StringTag("two"));

        root.add("list", list);

        return root;
    }

    private CompoundTag createDeepStructure() {
        CompoundTag root = new CompoundTag("root");

        CompoundTag level1 = new CompoundTag();
        CompoundTag level2 = new CompoundTag();
        CompoundTag level3 = new CompoundTag();

        level3.add("value", new StringTag("deep"));
        level2.add("level3", level3);
        level1.add("level2", level2);
        root.add("level1", level1);

        ListTag<CompoundTag> list = new ListTag<>();
        for (int i = 0; i < 5; i++) {
            list.add(new CompoundTag().add("id", new StringTag("item_" + i)));
        }
        root.add("list", list);

        return root;
    }

    // ---------- serialization ----------

    @Test
    void testSerializeAndDeserialize_Uncompressed() throws IOException {
        CompoundTag original = createSampleTag();

        TagIO.serialize(testFile, original, false);
        assertFalse(TagIO.isCompressed(testFile));

        CompoundTag read = TagIO.deserialize(testFile);

        assertNotNull(read);
        assertEquals("root", read.getName());
        assertEquals("loghot", ((StringTag) read.get("name")).getValue());
    }

    @Test
    void testSerializeAndDeserialize_Compressed() throws IOException {
        CompoundTag original = createSampleTag();

        TagIO.serialize(testFile, original, true);
        assertTrue(TagIO.isCompressed(testFile));

        CompoundTag read = TagIO.deserialize(testFile);

        assertNotNull(read);
        assertEquals("root", read.getName());
        assertEquals("loghot", ((StringTag) read.get("name")).getValue());
    }

    @Test
    void testIsCompressed_EmptyFile() throws IOException {
        assertTrue(testFile.createNewFile());
        assertThrows(IOException.class, () -> TagIO.isCompressed(testFile));
    }

    // ---------- getByPath basic ----------

    @Test
    void testGetByPath_Simple() {
        CompoundTag root = createSampleTag();

        Tag<?> tag = TagIO.getByPath(root, "name");

        assertNotNull(tag);
        assertEquals("loghot", ((StringTag) tag).getValue());
    }

    @Test
    void testGetByPath_ListIndex() {
        CompoundTag root = createSampleTag();

        Tag<?> tag = TagIO.getByPath(root, "list[1]");

        assertNotNull(tag);
        assertEquals("two", ((StringTag) tag).getValue());
    }

    @Test
    void testGetByPath_Nested() {
        CompoundTag root = new CompoundTag("root");

        CompoundTag inner = new CompoundTag();
        inner.add("value", new StringTag("hello"));

        root.add("inner", inner);

        Tag<?> tag = TagIO.getByPath(root, "inner.value");

        assertNotNull(tag);
        assertEquals("hello", ((StringTag) tag).getValue());
    }

    // ---------- getByPath stress ----------

    @Test
    void testWeirdPaths() {
        CompoundTag root = createDeepStructure();

        // weird dots
        assertNull(TagIO.getByPath(root, ".value"));
        assertNull(TagIO.getByPath(root, "level1."));
        assertNull(TagIO.getByPath(root, "level1..level2"));

        // invalid brackets
        assertNull(TagIO.getByPath(root, "list[]"));
        assertNull(TagIO.getByPath(root, "list["));
        assertNull(TagIO.getByPath(root, "list]"));
        assertNull(TagIO.getByPath(root, "list[abc]"));

        // out of bounds
        assertNull(TagIO.getByPath(root, "list[-1]"));
        assertNull(TagIO.getByPath(root, "list[999]"));

        // wrong type usage
        assertNull(TagIO.getByPath(root, "level1[0]"));
        assertNull(TagIO.getByPath(root, "list.id"));

        // unsupported chaining
        assertNull(TagIO.getByPath(root, "list[0][1]"));
    }

    @Test
    void testDeepValidPath() {
        CompoundTag root = createDeepStructure();

        Tag<?> tag = TagIO.getByPath(root, "level1.level2.level3.value");

        assertNotNull(tag);
        assertEquals("deep", ((StringTag) tag).getValue());
    }

    @Test
    void testListDeepAccess() {
        CompoundTag root = createDeepStructure();

        Tag<?> tag = TagIO.getByPath(root, "list[3].id");

        assertNotNull(tag);
        assertEquals("item_3", ((StringTag) tag).getValue());
    }

    @RepeatedTest(100)
    void fuzzPaths_shouldNeverCrash() {
        CompoundTag root = createDeepStructure();
        Random random = new Random();

        String chars = "abcdefghijklmnopqrstuvwxyz[].0123456789";
        int len = random.nextInt(30);

        StringBuilder path = new StringBuilder();
        for (int i = 0; i < len; i++) {
            path.append(chars.charAt(random.nextInt(chars.length())));
        }

        try {
            TagIO.getByPath(root, path.toString());
        } catch (Exception e) {
            fail("Crash on path: " + path + " -> " + e);
        }
    }

    @Test
    void testNullAndEmptyPaths() {
        CompoundTag root = createDeepStructure();

        assertNull(TagIO.getByPath(null, "anything"));
        assertNull(TagIO.getByPath(root, null));
        assertNull(TagIO.getByPath(root, ""));
    }
}