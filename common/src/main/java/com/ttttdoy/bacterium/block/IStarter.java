package com.ttttdoy.bacterium.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public interface IStarter {
    boolean activate(Level level, BlockPos pos);
}
