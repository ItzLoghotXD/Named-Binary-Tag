package me.itzloghotxd.nbt.tags;

import me.itzloghotxd.nbt.Tag;
import me.itzloghotxd.nbt.TagType;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class CompoundTagTest {

    @Test
    void testCompoundSerialization() throws IOException {
        // Create compound
        CompoundTag original = new CompoundTag("box");
        original.add("slot_1", new StringTag("item_1"))
                .add("slot_2", new StringTag("item_2"))
                .add("slot_3", new StringTag("item_3"))
                .add("slot_4", new StringTag("item_4"));

        // Serialize
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(baos);
        original.serialize(out);

        // Deserialize
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        DataInputStream in = new DataInputStream(bais);

        int typeId = in.readByte();
        assertEquals(TagType.COMPOUND.getId(), typeId);

        CompoundTag copy = (CompoundTag) TagType.createByType(TagType.getTypeById(typeId));

        String name = in.readUTF();
        copy.setName(name);

        copy.deserialize(in);

        // Basic checks
        assertEquals(original.getName(), copy.getName());
        assertEquals(original.getValue().size(), copy.getValue().size());

        // Deep content check
        for (Map.Entry<String, Tag<?>> entry : original.getValue().entrySet()) {
            Tag<?> copyTag = copy.getValue().get(entry.getKey());

            assertNotNull(copyTag);
            assertEquals(entry.getValue().getType(), copyTag.getType());
            assertEquals(entry.getValue().getValue(), copyTag.getValue());
        }

        // toString sanity
        assertNotNull(copy.toString());
        assertFalse(copy.toString().isEmpty());

        // toJson sanity
        String json = copy.toJson();
        assertNotNull(json);
    }
}