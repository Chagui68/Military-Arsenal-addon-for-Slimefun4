package com.Chagui68.weaponsaddon.items.turrets;

import com.Chagui68.weaponsaddon.WeaponsAddon;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

public class TurretStructureManager {
    private static final String[] ATTACK_STRUCTURES = {"level_1_attack_tower", "level_2_attack_tower", "level_3_attack_tower", "level_4_attack_tower"};
    private static final String[] RAPID_STRUCTURES = {"level_1_rapid_tower", "level_2_rapid_tower", "level_3_rapid_tower", "level_4_rapid_tower"};

    private static class StructBlock {
        int x, y, z;
        String blockData;
    }

    public static void initialize() {
        File structuresFolder = new File(WeaponsAddon.getInstance().getDataFolder(), "structures");
        if (!structuresFolder.exists()) {
            structuresFolder.mkdirs();
        }
        for (String name : ATTACK_STRUCTURES) {
            saveResource(name + ".nbt", structuresFolder);
        }
        for (String name : RAPID_STRUCTURES) {
            saveResource(name + ".nbt", structuresFolder);
        }
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
            WeaponsAddon.getInstance().getLogger().severe("Failed to save structure: " + fileName + " - " + e.getMessage());
        }
    }

    public static boolean placeStructure(Location baseLoc, String structureName) {
        try {
            File structureFile = new File(WeaponsAddon.getInstance().getDataFolder(), "structures/" + structureName + ".nbt");
            if (!structureFile.exists()) {
                WeaponsAddon.getInstance().getLogger().warning("Structure file not found: " + structureName);
                return false;
            }
            return placeFromNbt(baseLoc, structureFile);
        } catch (Exception e) {
            WeaponsAddon.getInstance().getLogger().severe("Failed to place structure " + structureName + ": " + e.getMessage());
            return false;
        }
    }

    private static boolean placeFromNbt(Location baseLoc, File file) {
        try (DataInputStream dis = new DataInputStream(new GZIPInputStream(new FileInputStream(file)))) {
            byte rootType = dis.readByte();
            if (rootType != 10) return false;
            skipString(dis);
            List<StructBlock> blocks = readStructureCompound(dis);
            if (blocks == null) return false;

            World world = baseLoc.getWorld();
            if (world == null) return false;

            for (StructBlock sb : blocks) {
                world.setBlockData(baseLoc.getBlockX() + sb.x, baseLoc.getBlockY() + sb.y, baseLoc.getBlockZ() + sb.z,
                        org.bukkit.Bukkit.createBlockData(sb.blockData));
            }
            return true;
        } catch (Exception e) {
            WeaponsAddon.getInstance().getLogger().severe("Failed to parse NBT: " + e.getMessage());
            return false;
        }
    }

    private static List<StructBlock> readStructureCompound(DataInputStream dis) throws IOException {
        List<String> palette = null;
        List<int[]> positions = null;
        List<Integer> stateIndices = null;
        int[] size = null;

        while (true) {
            byte type = dis.readByte();
            if (type == 0) break;
            String name = readString(dis);
            switch (name) {
                case "size" -> size = readIntTag(dis, type);
                case "palette" -> palette = readPaletteList(dis);
                case "blocks" -> {
                    byte listType = dis.readByte();
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

        if (palette == null || positions == null || stateIndices == null || size == null) return null;

        List<StructBlock> result = new ArrayList<>();
        for (int i = 0; i < positions.size(); i++) {
            int[] pos = positions.get(i);
            int state = stateIndices.get(i);
            if (state < 0 || state >= palette.size()) continue;
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
        byte listType = dis.readByte();
        int length = dis.readInt();
        List<String> result = new ArrayList<>();
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
            if (type == 0) break;
            String key = readString(dis);
            switch (key) {
                case "Name" -> name = readStringPayload(dis);
                case "Properties" -> {
                    props = new StringBuilder();
                    props.append('[');
                    boolean first = true;
                    while (true) {
                        byte propType = dis.readByte();
                        if (propType == 0) break;
                        String propKey = readString(dis);
                        String propVal = readStringPayload(dis);
                        if (!first) props.append(',');
                        props.append(propKey).append('=').append(propVal);
                        first = false;
                    }
                    props.append(']');
                }
                default -> skipPayload(dis, type);
            }
        }

        if (name == null) return "minecraft:air";
        if (props != null) {
            return name + props;
        }
        return name;
    }

    private static void readBlockEntry(DataInputStream dis, List<int[]> positions, List<Integer> stateIndices) throws IOException {
        int[] pos = null;
        int state = 0;

        while (true) {
            byte type = dis.readByte();
            if (type == 0) break;
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
        short len = dis.readShort();
        byte[] bytes = new byte[len];
        dis.readFully(bytes);
        return new String(bytes, StandardCharsets.UTF_8);
    }

    private static void skipString(DataInputStream dis) throws IOException {
        short len = dis.readShort();
        dis.skipBytes(len);
    }

    private static String readStringPayload(DataInputStream dis) throws IOException {
        short len = dis.readShort();
        byte[] bytes = new byte[len];
        dis.readFully(bytes);
        return new String(bytes, StandardCharsets.UTF_8);
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
        int len = dis.readInt();
        int[] arr = new int[len];
        for (int i = 0; i < len; i++) {
            arr[i] = dis.readInt();
        }
        return arr;
    }

    private static void skipPayload(DataInputStream dis, byte type) throws IOException {
        switch (type) {
            case 1 -> dis.skipBytes(1);
            case 2 -> dis.skipBytes(2);
            case 3 -> dis.skipBytes(4);
            case 4 -> dis.skipBytes(8);
            case 5 -> dis.skipBytes(4);
            case 6 -> dis.skipBytes(8);
            case 7 -> dis.skipBytes(dis.readInt());
            case 8 -> dis.skipBytes(dis.readShort());
            case 9 -> {
                byte elementType = dis.readByte();
                int length = dis.readInt();
                for (int i = 0; i < length; i++) skipPayload(dis, elementType);
            }
            case 10 -> {
                while (true) {
                    byte t = dis.readByte();
                    if (t == 0) break;
                    skipString(dis);
                    skipPayload(dis, t);
                }
            }
            case 11 -> dis.skipBytes(dis.readInt() * 4);
            case 12 -> dis.skipBytes(dis.readInt() * 8);
        }
    }

    public static void removeStructure(Location baseLoc, int height) {
        World world = baseLoc.getWorld();
        if (world == null) return;
        for (int x = -2; x <= 2; x++) {
            for (int z = -2; z <= 2; z++) {
                for (int y = 0; y <= height; y++) {
                    Block block = baseLoc.clone().add(x, y, z).getBlock();
                    if (block.getType() != Material.AIR && block.getType() != Material.LIGHT) {
                        block.setType(Material.AIR);
                    }
                }
            }
        }
    }

    public static int findHighestPoint(Location baseLoc, int maxHeight) {
        World world = baseLoc.getWorld();
        if (world == null) return 0;
        int highest = 0;
        for (int y = maxHeight; y >= 0; y--) {
            Block block = baseLoc.clone().add(0, y, 0).getBlock();
            if (block.getType() != Material.AIR && block.getType() != Material.LIGHT) {
                highest = y;
                break;
            }
        }
        return highest;
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
}
