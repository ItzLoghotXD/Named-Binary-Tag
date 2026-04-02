import me.itzloghotxd.nbt.TagType;
import me.itzloghotxd.nbt.tags.*;

import java.io.*;

public class LT {

    private static final ByteArrayOutputStream memoryBuffer = new ByteArrayOutputStream();
    private static final DataOutput out = new DataOutputStream(memoryBuffer);

    public static void main(String[] args) {
        s();
        d();
    }

    static void s() {
        CompoundTag tag = new CompoundTag();

        tag.add("0", new ListTag<CompoundTag>().add(
                new CompoundTag().add("id", new StringTag("minecraft:wooden_axe")).add("tag", new CompoundTag().add("Damage", new IntegerTag(0))).add("Count", new ByteTag((byte) 1)),
                new CompoundTag().add("id", new StringTag("minecraft:light_blue_concrete")).add("Count", new ByteTag((byte) 1)),
                new CompoundTag().add("id", new StringTag("minecraft:redstone")).add("Count", new ByteTag((byte) 1)),
                new CompoundTag().add("id", new StringTag("minecraft:repeater")).add("Count", new ByteTag((byte) 1)),
                new CompoundTag().add("id", new StringTag("minecraft:redstone_torch")).add("Count", new ByteTag((byte) 1)),
                new CompoundTag().add("id", new StringTag("minecraft:comparator")).add("Count", new ByteTag((byte) 1)),
                new CompoundTag().add("id", new StringTag("minecraft:stone_button")).add("Count", new ByteTag((byte) 1)),
                new CompoundTag().add("id", new StringTag("minecraft:lever")).add("Count", new ByteTag((byte) 1)),
                new CompoundTag().add("id", new StringTag("minecraft:redstone_lamp")).add("Count", new ByteTag((byte) 1)))
        ).add("DataVersion", new IntegerTag(2586));

        try {
            out.writeByte(tag.getType().getId());
            out.writeUTF(tag.getName());
            tag.serialize(out);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static void d() {
        DataInput in = new DataInputStream(new ByteArrayInputStream(memoryBuffer.toByteArray()));

        try {
            CompoundTag tag = (CompoundTag) TagType.createByType(TagType.getTypeById(in.readByte()));
            tag.setName(in.readUTF());
            tag.deserialize(in);

            System.out.println("\n"+tag);
            System.out.println(tag.toJson()+"\n");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
