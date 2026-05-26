package com.kyu.kyuzones.neoforge;

import net.neoforged.fml.common.Mod;

import com.kyu.kyuzones.KyuZones;

@Mod(KyuZones.MOD_ID)
public final class ExampleModNeoForge {
    public ExampleModNeoForge() {
        // Run our common setup.
        KyuZones.init();
    }
}
