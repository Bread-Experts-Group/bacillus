package com.ttttdoy.bacterium.block;

import com.ttttdoy.bacterium.block.entity.BacteriaBlockEntity;
import com.ttttdoy.bacterium.registry.ModBlockTags;
import com.ttttdoy.bacterium.registry.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashSet;
import java.util.Set;

public class DestroyerStarter extends Block implements IStarter {
    public DestroyerStarter() { super(Properties.ofFullCopy(Blocks.SPONGE)); }

    @Override
    protected void neighborChanged(BlockState blockState, Level level, BlockPos blockPos, Block block, BlockPos neighborPos, boolean moved) {
        if (level.hasNeighborSignal(blockPos)) {
            activate(level, blockPos);
        }
    }

    @Override
    public boolean activate(Level level, BlockPos pos) {
        final Set<Block> set = new HashSet<>();
        BlockState input = level.getBlockState(pos.above());
        BlockPos next = pos.above();
        while (!input.isAir() && !input.is(ModBlockTags.unbreakable) &&
        input.getBlock() != ModBlocks.REPLACER.get() && input.getBlock() != ModBlocks.DESTROYER.get()
        ) {
            set.add(level.getBlockState(next).getBlock());
            level.setBlockAndUpdate(next, Blocks.AIR.defaultBlockState());
            next = next.above();
            input = level.getBlockState(next);
        }

        if (!set.isEmpty()) {
            BacteriaBlockEntity.replace(level, pos, set, Blocks.AIR);
            return true;
        }
        return false;
    }
}
