package com.kyu.kyuzones.zone;

import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ZoneBase {
    public final BlockPos pos1;
    public final BlockPos pos2;
    public boolean breakBlock = true;
    public boolean placeBlock = true;
    private final List<UUID> entities = new ArrayList<>();
    public final String id;
    public final String dimension;

    public ZoneBase(BlockPos pos1, BlockPos pos2, String id, String dimension){
        this.pos1 = pos1;
        this.pos2 = pos2;
        this.id = id;
        this.dimension = dimension;
    }

    public boolean isEntityInZone(Entity entity){
        return this.entities.contains(entity.getUuid());
    }

    public void addEntity(Entity entity){
        entity.addCommandTag(String.format("zone-in-%s", this.id));
        this.entities.add(entity.getUuid());
        System.out.println(entity.getName() + "Entrou");
    }

    public void removeEntity(Entity entity){
        entity.removeCommandTag(String.format("zone-in-%s", this.id));
        this.entities.remove(entity.getUuid());
        System.out.println(entity.getName() + "Saiu");
    }

    public boolean isInside(BlockPos point) {
        int minX = Math.min(pos1.getX(), pos2.getX());
        int maxX = Math.max(pos1.getX(), pos2.getX());

        int minY = Math.min(pos1.getY(), pos2.getY());
        int maxY = Math.max(pos1.getY(), pos2.getY());

        int minZ = Math.min(pos1.getZ(), pos2.getZ());
        int maxZ = Math.max(pos1.getZ(), pos2.getZ());

        return point.getX() >= minX && point.getX() <= maxX
                && point.getY() >= minY && point.getY() <= maxY
                && point.getZ() >= minZ && point.getZ() <= maxZ;
    }

    public boolean isInDimension(ServerWorld world){
        return world.getRegistryKey().getValue().toString().equals(this.dimension);
    }

    public boolean isInDimension(String dimension){
        return this.dimension.equals(dimension);
    }
}