package com.Chagui68.weaponsaddon.items.turrets;

import com.Chagui68.weaponsaddon.WeaponsAddon;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

public final class TurretStructureManager {
    private static final String[] ATTACK_STRUCTURES = {
            "level_1_attack_tower", "level_2_attack_tower", "level_3_attack_tower", "level_4_attack_tower"
    };
    private static final String[] RAPID_STRUCTURES = {
            "level_1_rapid_tower", "level_2_rapid_tower", "level_3_rapid_tower", "level_4_rapid_tower"
    };
    private static final Map<String, List<StructBlock>> STRUCTURE_CACHE = new HashMap<>();

    private TurretStructureManager() {
    }

    private static final class StructBlock {
        private int x;
        private int y;
        private int z;
        private String blockData;

        private String offsetKey() {
            return x + ":" + y + ":" + z;
        }
    }

    public static void initialize() {
        File structuresFolder = new File(WeaponsAddon.getInstance().getDataFolder(), "structures");
        if (!structuresFolder.exists() && !structuresFolder.mkdirs()) {
            WeaponsAddon.getInstance().getLogger().warning("Could not create turret structures folder: " + structuresFolder);
        }

        for (String name : ATTACK_STRUCTURES) {
            saveResource(name + ".nbt", structuresFolder);
        }
        for (String name : RAPID_STRUCTURES) {
            saveResource(name + ".nbt", structuresFolder);
        }

        STRUCTURE_CACHE.clear();
    }

    private static void saveResource(String fileName, File targetFolder) {
        File target = new File(targetFolder, fileName);
        if (target.exists()) {
            return;
        }

        try (InputStream is = WeaponsAddon.getInstance().getResource("structures/" + fileName)) {
            if (is == null) {
                WeaponsAddon.getInstance().getLogger().warning("Structure resource not found: structures/" + fileName);
                return;
            }

            try (FileOutputStream fos = new FileOutputStream(target)) {
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, bytesRead);
                }
            }
        } catch (IOException e) {
            WeaponsAddon.getInstance().getLogger().severe("Failed to save structure " + fileName + ": " + e.getMessage());
        }
    }

    public static boolean placeStructure(Location baseLoc, String structureName) {
        World world = baseLoc.getWorld();
        if (world == null) {
            return false;
        }

        List<StructBlock> blocks = loadStructure(structureName);
        if (blocks == null) {
            return false;
        }

        try {
            for (StructBlock sb : blocks) {
                Block block = world.getBlockAt(
                        baseLoc.getBlockX() + sb.x,
                        baseLoc.getBlockY() + sb.y,
                        baseLoc.getBlockZ() + sb.z
                );
                block.setBlockData(Bukkit.createBlockData(sb.blockData), false);
            }
            return true;
        } catch (IllegalArgumentException e) {
            WeaponsAddon.getInstance().getLogger().severe(
                    "Failed to place turret structure " + structureName + ": " + e.getMessage()
            );
            return false;
        }
    }

    /**
     * Repairs missing replaceable pieces of an existing turret without overwriting a
     * non-replaceable block placed by a player or another plugin.
     */
    public static boolean repairStructure(Location baseLoc, String structureName) {
        World world = baseLoc.getWorld();
        if (world == null) {
            return false;
        }

        List<StructBlock> blocks = loadStructure(structureName);
        if (blocks == null) {
            return false;
        }

        for (StructBlock sb : blocks) {
            Block target = world.getBlockAt(
                    baseLoc.getBlockX() + sb.x,
                    baseLoc.getBlockY() + sb.y,
                    baseLoc.getBlockZ() + sb.z
            );

            if (matchesStructureBlock(target, sb)) {
                continue;
            }
            if (!isReplaceable(target.getType())) {
                return false;
            }

            try {
                target.setBlockData(Bukkit.createBlockData(sb.blockData), false);
            } catch (IllegalArgumentException e) {
                WeaponsAddon.getInstance().getLogger().warning(
                        "Could not repair turret structure " + structureName + ": " + e.getMessage()
                );
                return false;
            }
        }
        return true;
    }

    public static boolean isStructureIntact(Location baseLoc, String structureName) {
        World world = baseLoc.getWorld();
        if (world == null) {
            return false;
        }

        List<StructBlock> blocks = loadStructure(structureName);
        if (blocks == null) {
            return false;
        }

        for (StructBlock sb : blocks) {
            Block target = world.getBlockAt(
                    baseLoc.getBlockX() + sb.x,
                    baseLoc.getBlockY() + sb.y,
                    baseLoc.getBlockZ() + sb.z
            );
            if (!matchesStructureBlock(target, sb)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks whether a structure can be placed without replacing unrelated world blocks.
     * Coordinates occupied by the current turret structure are reusable only when the
     * actual world block still matches the expected current NBT block.
     */
    public static boolean canPlaceStructure(Location baseLoc, String newStructureName, String currentStructureName) {
        World world = baseLoc.getWorld();
        if (world == null) {
            return false;
        }

        List<StructBlock> newBlocks = loadStructure(newStructureName);
        if (newBlocks == null) {
            return false;
        }

        Map<String, StructBlock> currentOffsets = new HashMap<>();
        if (currentStructureName != null) {
            List<StructBlock> currentBlocks = loadStructure(currentStructureName);
            if (currentBlocks == null) {
                return false;
            }
            for (StructBlock current : currentBlocks) {
                currentOffsets.put(current.offsetKey(), current);
            }
        }

        for (StructBlock next : newBlocks) {
            // During initial placement the Slimefun item itself occupies the anchor block.
            if (currentStructureName == null && next.x == 0 && next.y == 0 && next.z == 0) {
                continue;
            }

            Block target = world.getBlockAt(
                    baseLoc.getBlockX() + next.x,
                    baseLoc.getBlockY() + next.y,
                    baseLoc.getBlockZ() + next.z
            );

            if (isReplaceable(target.getType())) {
                continue;
            }

            StructBlock current = currentOffsets.get(next.offsetKey());
            if (current != null && matchesStructureBlock(target, current)) {
                continue;
            }

            return false;
        }

        return true;
    }

    private static boolean isReplaceable(Material material) {
        return material == Material.AIR
                || material == Material.CAVE_AIR
                || material == Material.VOID_AIR
                || material == Material.LIGHT
                || material == Material.STRUCTURE_BLOCK;
    }

    private static boolean matchesStructureBlock(Block block, StructBlock expectedBlock) {
        try {
            BlockData expected = Bukkit.createBlockData(expectedBlock.blockData);
            return block.getBlockData().matches(expected);
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Removes only blocks that still match the NBT structure. A block that has been
     * manually replaced or changed is intentionally left alone.
     */
    public static void removeStructure(Location baseLoc, String structureName) {
        World world = baseLoc.getWorld();
        if (world == null) {
            return;
        }

        List<StructBlock> blocks = loadStructure(structureName);
        if (blocks == null) {
            return;
        }

        for (StructBlock sb : blocks) {
            Block block = world.getBlockAt(
                    baseLoc.getBlockX() + sb.x,
                    baseLoc.getBlockY() + sb.y,
                    baseLoc.getBlockZ() + sb.z
            );
            if (matchesStructureBlock(block, sb)) {
                block.setType(Material.AIR, false);
            }
        }
    }

    public static int getStructureHeight(String structureName) {
        List<StructBlock> blocks = loadStructure(structureName);
        if (blocks == null || blocks.isEmpty()) {
            return 0;
        }

        int highest = 0;
        for (StructBlock block : blocks) {
            highest = Math.max(highest, block.y);
        }
        return highest;
    }

    public static int findHighestPoint(Location baseLoc, int maxHeight) {
        World world = baseLoc.getWorld();
        if (world == null) {
            return 0;
        }

        for (int y = maxHeight; y >= 0; y--) {
            Block block = world.getBlockAt(baseLoc.getBlockX(), baseLoc.getBlockY() + y, baseLoc.getBlockZ());
            if (!isReplaceable(block.getType())) {
                return y;
            }
        }
        return 0;
    }

    public static String getStructureName(String prefix, int level) {
        return "level_" + level + "_" + prefix;
    }

    public static int getMaxHeight(String prefix) {
        return switch (prefix) {
            case "attack_tower" -> 6;
            case "rapid_tower" -> 5;
            default -> 4;
        };
    }

    private static List<StructBlock> loadStructure(String structureName) {
        List<StructBlock> cached = STRUCTURE_CACHE.get(structureName);
        if (cached != null) {
            return cached;
        }

        File structureFile = new File(
                WeaponsAddon.getInstance().getDataFolder(),
                "structures/" + structureName + ".nbt"
        );
        if (!structureFile.exists()) {
            WeaponsAddon.getInstance().getLogger().warning("Structure file not found: " + structureName);
            return null;
        }

        try (DataInputStream dis = new DataInputStream(new GZIPInputStream(new FileInputStream(structureFile)))) {
            byte rootType = dis.readByte();
            if (rootType != 10) {
                WeaponsAddon.getInstance().getLogger().warning("Invalid NBT root for turret structure: " + structureName);
                return null;
            }

            skipString(dis);
            List<StructBlock> blocks = readStructureCompound(dis);
            if (blocks == null) {
                WeaponsAddon.getInstance().getLogger().warning("Could not read turret structure: " + structureName);
                return null;
            }

            List<StructBlock> immutable = List.copyOf(blocks);
            STRUCTURE_CACHE.put(structureName, immutable);
            return immutable;
        } catch (IOException | RuntimeException e) {
            WeaponsAddon.getInstance().getLogger().severe(
                    "Failed to parse turret structure " + structureName + ": " + e.getMessage()
            );
            return null;
        }
    }

    private static List<StructBlock> readStructureCompound(DataInputStream dis) throws IOException {
        List<String> palette = null;
        List<int[]> positions = null;
        List<Integer> stateIndices = null;
        boolean hasSize = false;

        while (true) {
            byte type = dis.readByte();
            if (type == 0) {
                break;
            }

            String name = readString(dis);
            switch (name) {
                case "size" -> {
                    readIntTag(dis, type);
                    hasSize = true;
                }
                case "palette" -> palette = readPaletteList(dis);
                case "blocks" -> {
                    dis.readByte();
                    int listLen = dis.readInt();
                    positions = new ArrayList<>();
                    stateIndices = new ArrayList<>();
                    for (int i = 0; i < listLen; i++) {
                        readBlockEntry(dis, positions, stateIndices);
                    }
                }
                default -> skipPayload(dis, type);
            }
        }

        if (palette == null || positions == null || stateIndices == null || !hasSize) {
            return null;
        }

        List<StructBlock> result = new ArrayList<>();
        for (int i = 0; i < positions.size(); i++) {
            int state = stateIndices.get(i);
            if (state < 0 || state >= palette.size()) {
                continue;
            }

            int[] pos = positions.get(i);
            StructBlock sb = new StructBlock();
            sb.x = pos[0];
            sb.y = pos[1];
            sb.z = pos[2];
            sb.blockData = palette.get(state);
            result.add(sb);
        }
        return result;
    }

    private static List<String> readPaletteList(DataInputStream dis) throws IOException {
        dis.readByte();
        int length = dis.readInt();
        List<String> result = new ArrayList<>(length);
        for (int i = 0; i < length; i++) {
            result.add(readBlockStateName(dis));
        }
        return result;
    }

    private static String readBlockStateName(DataInputStream dis) throws IOException {
        String name = null;
        StringBuilder props = null;

        while (true) {
            byte type = dis.readByte();
            if (type == 0) {
                break;
            }

            String key = readString(dis);
            switch (key) {
                case "Name" -> name = readStringPayload(dis);
                case "Properties" -> {
                    props = new StringBuilder("[");
                    boolean first = true;
                    while (true) {
                        byte propType = dis.readByte();
                        if (propType == 0) {
                            break;
                        }
                        String propKey = readString(dis);
                        String propVal = readStringPayload(dis);
                        if (!first) {
                            props.append(',');
                        }
                        props.append(propKey).append('=').append(propVal);
                        first = false;
                    }
                    props.append(']');
                }
                default -> skipPayload(dis, type);
            }
        }

        if (name == null) {
            return "minecraft:air";
        }
        return props == null ? name : name + props;
    }

    private static void readBlockEntry(
            DataInputStream dis,
            List<int[]> positions,
            List<Integer> stateIndices
    ) throws IOException {
        int[] pos = null;
        int state = 0;

        while (true) {
            byte type = dis.readByte();
            if (type == 0) {
                break;
            }

            String key = readString(dis);
            switch (key) {
                case "pos" -> pos = readIntTag(dis, type);
                case "state" -> state = readIntPayload(dis);
                case "nbt" -> skipPayload(dis, type);
                default -> skipPayload(dis, type);
            }
        }

        if (pos != null) {
            positions.add(pos);
            stateIndices.add(state);
        }
    }

    private static String readString(DataInputStream dis) throws IOException {
        int len = dis.readUnsignedShort();
        byte[] bytes = new byte[len];
        dis.readFully(bytes);
        return new String(bytes, StandardCharsets.UTF_8);
    }

    private static void skipString(DataInputStream dis) throws IOException {
        int len = dis.readUnsignedShort();
        dis.skipNBytes(len);
    }

    private static String readStringPayload(DataInputStream dis) throws IOException {
        return readString(dis);
    }

    private static int readIntPayload(DataInputStream dis) throws IOException {
        return dis.readInt();
    }

    private static int[] readIntTag(DataInputStream dis, byte type) throws IOException {
        if (type == 9) {
            dis.readByte();
            int len = dis.readInt();
            int[] arr = new int[len];
            for (int i = 0; i < len; i++) {
                arr[i] = dis.readInt();
            }
            return arr;
        }

        if (type == 11) {
            int len = dis.readInt();
            int[] arr = new int[len];
            for (int i = 0; i < len; i++) {
                arr[i] = dis.readInt();
            }
            return arr;
        }

        throw new IOException("Expected integer list/array tag, got type " + type);
    }

    private static void skipPayload(DataInputStream dis, byte type) throws IOException {
        switch (type) {
            case 1 -> dis.skipNBytes(1);
            case 2 -> dis.skipNBytes(2);
            case 3 -> dis.skipNBytes(4);
            case 4 -> dis.skipNBytes(8);
            case 5 -> dis.skipNBytes(4);
            case 6 -> dis.skipNBytes(8);
            case 7 -> dis.skipNBytes(dis.readInt());
            case 8 -> dis.skipNBytes(dis.readUnsignedShort());
            case 9 -> {
                byte elementType = dis.readByte();
                int length = dis.readInt();
                for (int i = 0; i < length; i++) {
                    skipPayload(dis, elementType);
                }
            }
            case 10 -> {
                while (true) {
                    byte nestedType = dis.readByte();
                    if (nestedType == 0) {
                        break;
                    }
                    skipString(dis);
                    skipPayload(dis, nestedType);
                }
            }
            case 11 -> dis.skipNBytes((long) dis.readInt() * 4L);
            case 12 -> dis.skipNBytes((long) dis.readInt() * 8L);
            default -> throw new IOException("Unsupported NBT tag type: " + type);
        }
    }
}
