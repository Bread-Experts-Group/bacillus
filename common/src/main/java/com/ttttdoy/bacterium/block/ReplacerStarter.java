package com.ttttdoy.bacterium.block;

import com.ttttdoy.bacterium.block.entity.BacteriaBlockEntity;
import com.ttttdoy.bacterium.registry.ModBlockTags;
import com.ttttdoy.bacterium.registry.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.Shapes;

import java.util.Collections;
import java.util.HashSet;

public class ReplacerStarter extends Block implements IStarter {
    public ReplacerStarter() {
        super(Properties.ofFullCopy(Blocks.SPONGE));
    }

    @Override
    protected void neighborChanged(BlockState blockState, Level level, BlockPos blockPos, Block block, BlockPos neighborPos, boolean moved) {
        if (level.hasNeighborSignal(blockPos)) {
            activate(level, blockPos);
        }
    }

    @Override
    public boolean activate(Level level, BlockPos pos) {
        BlockState input = level.getBlockState(pos.below());
        BlockState output = level.getBlockState(pos.above());
        if ((output.getInteractionShape(level, pos.above()) == Shapes.block() ||
                output.getBlock() == Blocks.WATER || output.getBlock() == Blocks.LAVA) &&

                output.getBlock() != input.getBlock() && !output.is(ModBlockTags.unplaceable) &&
                output.getBlock() != ModBlocks.REPLACER.get() && output.getBlock() != ModBlocks.DESTROYER.get() &&

                !input.is(ModBlockTags.unbreakable) && input.getBlock() != ModBlocks.REPLACER.get() &&
                input.getBlock() != ModBlocks.DESTROYER.get()
        ) {
            BacteriaBlockEntity.replace(level, pos, new HashSet<>(Collections.singletonList(input.getBlock())), output.getBlock());
            level.setBlockAndUpdate(pos.above(), Blocks.AIR.defaultBlockState());
            return true;
        }
        return false;
    }
}
