package potionstudios.byg.mixin.common.entity;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import potionstudios.byg.server.level.BYGPlayerTrackedData;
import potionstudios.byg.util.BYGAdditionalData;

@Mixin(Player.class)
public class MixinPlayer implements BYGAdditionalData, BYGPlayerTrackedData.Access {

    private BYGPlayerTrackedData bygPlayerTrackedData = new BYGPlayerTrackedData(new ObjectOpenHashSet<>());

    @Inject(method = "addAdditionalSaveData", at = @At("RETURN"))
    private void writeBYGData(CompoundTag tag, CallbackInfo ci) {
        this.writeBYG(tag);
    }

    @Inject(method = "readAdditionalSaveData", at = @At("RETURN"))
    private void readBYGData(CompoundTag tag, CallbackInfo ci) {
        this.readBYG(tag);
    }


    @Override
    public Tag write() {
        return BYGPlayerTrackedData.CODEC.encodeStart(NbtOps.INSTANCE, this.bygPlayerTrackedData).result().orElseThrow();
    }

    @Override
    public void read(CompoundTag tag) {
        this.bygPlayerTrackedData = BYGPlayerTrackedData.CODEC.decode(NbtOps.INSTANCE, tag).result().orElseThrow().getFirst();
    }

    @Override
    public BYGPlayerTrackedData getServerPlayerTrackedData() {
        return this.bygPlayerTrackedData;
    }
}