package com.kyu.kyuzones;

import com.kyu.kyucore.config.Config;
import com.kyu.kyucore.data.Data;
import com.kyu.kyucore.data.DataManager;
import com.kyu.kyuzones.zone.ZoneBase;
import com.kyu.kyuzones.zone.ZoneManager;
import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.BlockEvent;
import dev.architectury.event.events.common.LifecycleEvent;
import dev.architectury.event.events.common.PlayerEvent;
import dev.architectury.event.events.common.TickEvent;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;

public final class KyuZones {
    public static final String MOD_ID = "kyuzones";
    public static KyuZoneConfig CONFIG = new KyuZoneConfig();
    public static Data DATA = DataManager.newData(MOD_ID);

    public static void init() {
        LifecycleEvent.SERVER_STARTED.register(ZoneManager::serverStarted);
        BlockEvent.BREAK.register(ZoneManager::breakBlock);
        BlockEvent.PLACE.register(ZoneManager::placeBlock);
    }

    public static class KyuZoneConfig{
        public final Config config = new Config(KyuZones.MOD_ID);
        public final int ticksPosPlayer = config.set("ticks_pos_player", 10);
    }

    public static void test(){
        ZoneBase zoneBase = new ZoneBase(new BlockPos(0, -60, 0), new BlockPos(10, 10, 10), "teste", "minecraft:overworld");
        ZoneManager.addZone(zoneBase);
    }
}