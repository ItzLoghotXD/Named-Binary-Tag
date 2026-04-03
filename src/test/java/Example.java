import me.itzloghotxd.nbt.tags.*;
import me.itzloghotxd.nbt.TagIO;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class Example {
    public static void main(String[] args) {
        
        // Create a compound tag with children
        CompoundTag root = new CompoundTag();
        root
                .add("Health", new FloatTag(20F))
                .add("foodLevel", new IntegerTag(20))
                .add("name", new StringTag("LoghotGamerz"))
                .add("Pos", new ListTag<FloatTag>()
                        .add(new FloatTag(1.0f))
                        .add(new FloatTag(64.0f))
                        .add(new FloatTag(-32.5f)))
                .add("UUID", new IntegerArrayTag(uuidToIntArray(UUID.fromString("5ed562bc-6b6d-4e5b-9a13-b0cc341fe7c7"))))
                ;

        // Serialize to  (compressed)
        File file = new File("player.nbt");
        try {
            TagIO.serialize(file, root, true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Read it back
        CompoundTag readRoot;
        try {
            readRoot = TagIO.deserialize(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println(readRoot.toJson());  // {"Health":20.0,"foodLevel":20,"name":"LoghotGamerz","Pos":[1.0,64.0,-32.5],"UUID":[1591042748, 1802325595, -1709985588, 874506183]}
    }

    private static int[] uuidToIntArray(UUID uuid) {
        long most = uuid.getMostSignificantBits();
        long least = uuid.getLeastSignificantBits();

        return new int[] {
                (int) (most >> 32),
                (int) most,
                (int) (least >> 32),
                (int) least
        };
    }
}