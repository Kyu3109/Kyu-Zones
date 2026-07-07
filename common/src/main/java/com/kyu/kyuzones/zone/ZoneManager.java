package com.kyu.kyuzones.zone;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kyu.kyuzones.KyuZones;
import dev.architectury.event.EventResult;
import dev.architectury.utils.value.IntValue;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MovementType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ZoneManager {
    public static final List<ZoneBase> zoneList = new ArrayList<>();
    public static JsonObject jsonZones = new JsonObject();
    private static final Gson gson = new Gson();

    public static EventResult breakBlock(World world, BlockPos pos, BlockState state, ServerPlayerEntity player, IntValue xp){
        for(ZoneBase zone : zoneList){
            if(!zone.isInside(pos)){ return EventResult.pass(); }
            else if(zone.breakBlock){ return EventResult.pass(); }

            if(player != null){
                if(player.isCreative()){
                    return EventResult.pass();
                }
            }

            return EventResult.interruptFalse();
        }

        return EventResult.pass();
    }

    public static void OnMove(MovementType moveType, Vec3d move, CallbackInfo ci, Entity entity){
        for(ZoneBase zone : zoneList){
            if(!zone.isInDimension((ServerWorld) entity.getWorld())){
                return;
            }

            else if(zone.isEntityInZone(entity)){
                if(!zone.isInside(entity.getBlockPos())){
                    zone.removeEntity(entity);
                }
            }

            else if(zone.isInside(entity.getBlockPos()) && !zone.isEntityInZone(entity)){
                zone.addEntity(entity);
            }
        }
    }

    public static EventResult placeBlock(World world, BlockPos pos, BlockState blockState, @Nullable Entity entity) {
        for(ZoneBase zone : zoneList){
            if(!zone.isInside(pos)){ return EventResult.pass(); }
            else if(zone.placeBlock){ return EventResult.pass(); }

            if(entity instanceof ServerPlayerEntity){
                if(((ServerPlayerEntity) entity).isCreative()){
                    return EventResult.pass();
                }
            }

            return EventResult.interruptFalse();
        }

        return EventResult.pass();
    }

    public static void serverStarted(MinecraftServer server){
        KyuZones.test();
        KyuZones.DATA.readFile("zones", jsonObject -> {
            System.out.println(jsonObject);
            if(jsonObject != null){
                jsonZones = jsonObject;
            }

            load();
        });
    }

    public static void addZone(ZoneBase newZone){
        zoneList.add(newZone);
        JsonArray jsonArray = new JsonArray();
        jsonArray.add(newZone.pos1.getX());
        jsonArray.add(newZone.pos1.getY());
        jsonArray.add(newZone.pos1.getZ());
        jsonArray.add(newZone.pos2.getX());
        jsonArray.add(newZone.pos2.getY());
        jsonArray.add(newZone.pos2.getZ());
        jsonArray.add(newZone.id);
        jsonArray.add(newZone.dimension);
        jsonZones.add(newZone.getClass().getName(), jsonArray);
        save();
    }

    private static void save(){
        KyuZones.DATA.writeFile("zones", jsonZones);
    }

    private static void load(){
        Set<Map.Entry<String, JsonElement>> set = jsonZones.entrySet();
        set.forEach((entry) -> {
            try{
                Class<?> zoneClass = Class.forName(entry.getKey());
                Constructor<?> zoneCons = zoneClass.getConstructor(BlockPos.class, BlockPos.class, String.class, String.class);
                JsonArray arr = entry.getValue().getAsJsonArray();
                Object zoneOBJ = zoneCons.newInstance(new BlockPos(arr.get(0).getAsInt(), arr.get(1).getAsInt(), arr.get(2).getAsInt()), new BlockPos(arr.get(3).getAsInt(), arr.get(4).getAsInt(), arr.get(5).getAsInt()), arr.get(6).getAsString(), arr.get(7).getAsString());
                zoneList.add((ZoneBase) zoneOBJ);
            }
            catch (Exception e){
                e.printStackTrace();
                System.out.println("Failed to load the zone.");
            }
        });
    }
}