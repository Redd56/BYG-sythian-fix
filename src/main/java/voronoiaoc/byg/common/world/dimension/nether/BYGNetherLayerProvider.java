package voronoiaoc.byg.common.world.dimension.nether;


import net.minecraft.world.gen.IExtendedNoiseRandom;
import net.minecraft.world.gen.LazyAreaLayerContext;
import net.minecraft.world.gen.area.IAreaFactory;
import net.minecraft.world.gen.area.LazyArea;
import net.minecraft.world.gen.layer.Layer;
import net.minecraft.world.gen.layer.ZoomLayer;
import voronoiaoc.byg.config.BYGWorldConfig;

import java.util.function.LongFunction;

public class BYGNetherLayerProvider {
    public static Layer stackLayers(long seed) {
        LongFunction<IExtendedNoiseRandom<LazyArea>> randomProvider = salt -> new LazyAreaLayerContext(1, seed, salt);


        IAreaFactory<LazyArea> netherFactory = BYGNetherMasterLayer.INSTANCE.apply(randomProvider.apply(1000L));

        for (int netherBiomeSize = 0; netherBiomeSize <= BYGWorldConfig.biomeSizeNETHER.get(); netherBiomeSize++) {
            netherFactory = ZoomLayer.NORMAL.apply(randomProvider.apply(1000L + netherBiomeSize), netherFactory);
        }
        netherFactory = ZoomLayer.FUZZY.apply(randomProvider.apply(1000L), netherFactory);
        netherFactory = ZoomLayer.NORMAL.apply(randomProvider.apply(1000L), netherFactory);

        return new Layer(netherFactory);
    }
}