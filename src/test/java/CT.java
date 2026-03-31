import me.itzloghotxd.nbt.TagType;
import me.itzloghotxd.nbt.tags.CompoundTag;
import me.itzloghotxd.nbt.tags.StringTag;

import java.io.*;

public class CT {
    private static final ByteArrayOutputStream baos = new ByteArrayOutputStream();
    private static final DataOutput out = new DataOutputStream(baos);
    private static DataInput in;

    public static void main(String[] args) {
        w();
        t();
    }

    static void w() {
        CompoundTag tag = new CompoundTag("box");
        tag.add("slot_1", new StringTag("item_1"))
                .add("slot_2", new StringTag("item_2"))
                .add("slot_3", new StringTag("item_3"))
                .add("slot_4", new StringTag("item_4"))
                .add("b", new CompoundTag().add("?", new StringTag("huh?")));

        try {
            tag.serialize(out);
            in = new DataInputStream(new ByteArrayInputStream(baos.toByteArray()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static void t() {
        try {
            CompoundTag cTag = (CompoundTag) TagType.createByType(TagType.getTypeById(in.readByte()));
            cTag.setName(in.readUTF());
            cTag.deserialize(in);

            System.out.println("\n"+cTag);
            System.out.println(cTag.toJson()+"\n");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}