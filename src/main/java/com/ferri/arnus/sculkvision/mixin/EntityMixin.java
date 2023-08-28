package com.ferri.arnus.sculkvision.mixin;

import com.ferri.arnus.sculkvision.EntityExtension;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixin implements EntityExtension {

    @Shadow public abstract void tick();

    @Unique
    private boolean glowing;

    @Unique
    private int timer;

    @Inject(method = "isCurrentlyGlowing", at = @At("RETURN"), cancellable = true)
    public void edditGlow(CallbackInfoReturnable<Boolean> cir) {
        if (glowing) {
            cir.setReturnValue(glowing);
        }
    }

    @Inject(method = "tick", at = @At("RETURN"))
    public void appendTick(CallbackInfo ci) {
        if (timer <= 0) {
            glowing = false;
        } else {
            timer--;
        }
    }

    @Override
    public void setGlowing(boolean glowing, int timer) {
        this.glowing = glowing;
        this.timer = timer;
    }
}
