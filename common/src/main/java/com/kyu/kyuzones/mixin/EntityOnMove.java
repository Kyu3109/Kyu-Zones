package com.kyu.kyuzones.mixin;

import com.kyu.kyuzones.zone.ZoneManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MovementType;
import net.minecraft.util.math.Vec3d;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class EntityOnMove {

    @Inject(
            method = "move",
            at = @At("TAIL")
    )
    private void move(
            MovementType moveType,
            Vec3d move,
            CallbackInfo ci
    ) {
        Entity entity = (Entity)(Object)this;

        if (entity.getWorld().isClient) {
            return;
        }

        ZoneManager.OnMove(moveType, move, ci, entity);
    }
}
