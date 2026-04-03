# Named Binary Tag (NBT) Library

<div align="center">
  <img src="https://img.shields.io/github/actions/workflow/status/ItzLoghotXD/ItzLoghotXD/java-ci.yml?style=for-the-badge" height="25" alt="GitHub Actions Workflow Status">
</div>

**Named Binary Tag (NBT)** is a tag-based binary data format, created by Notch. This Java library provides a convenient API to create, serialize, and deserialize NBT data structures in your applications.

## Features

- Support for all NBT tag types: Byte, Short, Int, Long, Float, Double, ByteArray, String, List, Compound, IntArray, LongArray.
- Read and write NBT files (compressed or uncompressed) via `TagIO`.
- JSON and string representations of tags.
- Cloning and deep-copy of tags.
- JUnit tests covering core functionality.

## Getting Started

### Installation
- Clone this repository:
  ```bash
  git clone https://github.com/ItzLoghotXD/Named-Binary-Tag
  ```
- After cloning, add this to your local maven cache via `maven wrapper`. (no need to install maven. you can see maven wrapper guide [here](https://maven-wrapper-docs.vercel.app/)):
  ```bash
  mvnw clean install -DskipTests
  ```

- Add this library to your project via Maven:
  ```xml
  <dependency>
    <groupId>me.itzloghotxd.nbt</groupId>
    <artifactId>named-binary-tag</artifactId>
    <version>version</version>   <!-- replace with latest version -->
  </dependency>
  ```
  [NOTE: Ensure you have Java 17 or higher.]

### Basic Usage

Create tags and nested structures:

```java
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
```

### Tag Path Access

You can query nested tags by path using `TagIO.getByPath`:

```java
Tag<?> ageTag = TagIO.getByPath(root, "attributes.health");
Tag<?> listItem = TagIO.getByPath(root, "inventory.items[0].id");
```

[NOTE: Returns `null` if the path is invalid or missing.]

## License

This library is licensed under **GNU LGPL v3.0**. See [LICENSE](https://raw.githubusercontent.com/ItzLoghotXD/Named-Binary-Tag/refs/heads/main/LICENSE) for details. You may use it in open-source or proprietary projects under LGPL terms.

## Contributing

Contributions welcome! Please see [CONTRIBUTING.md](https://raw.githubusercontent.com/ItzLoghotXD/Named-Binary-Tag/refs/heads/main/CONTRIBUTING.md) for guidelines on code style, testing, and submitting PRs.