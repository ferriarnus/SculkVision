package com.ferri.arnus.sculkvision.mixin;

import com.ferri.arnus.sculkvision.enchantment.EnchantmentRegistry;
import com.ferri.arnus.sculkvision.network.GlowPacket;
import com.ferri.arnus.sculkvision.network.SculkVisionNetwork;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.GameEventTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.DynamicGameEventListener;
import net.minecraft.world.level.gameevent.EntityPositionSource;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.PositionSource;
import net.minecraft.world.level.gameevent.vibrations.VibrationSystem;
import net.minecraftforge.network.PacketDistributor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;
import java.util.function.BiConsumer;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity implements VibrationSystem {

    @Unique
    private final VibrationSystem.User user = new VibrationSystem.User() {

        private final PositionSource positionSource = new EntityPositionSource(PlayerMixin.this, 0.2f);

        @Override
        public int getListenerRadius() {
            return 20;
        }

        @Override
        public PositionSource getPositionSource() {
            return positionSource;
        }

        @Override
        public TagKey<GameEvent> getListenableEvents() {
            return GameEventTags.WARDEN_CAN_LISTEN;
        }

        @Override
        public boolean canReceiveVibration(ServerLevel p_282960_, BlockPos p_282488_, GameEvent p_282865_, GameEvent.Context p_283577_) {
            return !isDeadOrDying() && p_283577_.sourceEntity() instanceof LivingEntity living && living != PlayerMixin.this && EnchantmentHelper.getEnchantmentLevel(EnchantmentRegistry.SCULKVISION.get(),  PlayerMixin.this) > 0;
        }

        @Override
        public void onReceiveVibration(ServerLevel pLevel, BlockPos pPos, GameEvent pGameEvent, @Nullable Entity pEntity, @Nullable Entity pPlayerEntity, float pDistance) {
            if (pEntity instanceof LivingEntity living) {
                SculkVisionNetwork.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) (Object) PlayerMixin.this), new GlowPacket(living.getId()));
            }
        }
    };

    @Unique
    private final VibrationSystem.Data vibrationData = new VibrationSystem.Data();

    @Unique
    private final DynamicGameEventListener<Listener> dynamicGameEventListener = new DynamicGameEventListener<>(new VibrationSystem.Listener(this));;

    protected PlayerMixin(EntityType<? extends LivingEntity> p_20966_, Level p_20967_) {
        super(p_20966_, p_20967_);
    }

    @Inject(method = "tick", at = @At("HEAD"))
    public void appendTick(CallbackInfo ci) {
        if (this.level() instanceof ServerLevel serverlevel && EnchantmentHelper.getEnchantmentLevel(EnchantmentRegistry.SCULKVISION.get(), this) > 0) {
            VibrationSystem.Ticker.tick(serverlevel, this.vibrationData, this.user);
        }
    }

    @Override
    public void updateDynamicGameEventListener(BiConsumer<DynamicGameEventListener<?>, ServerLevel> p_219413_) {
        Level level = this.level();
        if (level instanceof ServerLevel serverlevel) {
            p_219413_.accept(this.dynamicGameEventListener, serverlevel);
        }
    }

    @Override
    public Data getVibrationData() {
        return vibrationData;
    }

    @Override
    public User getVibrationUser() {
        return user;
    }
}
