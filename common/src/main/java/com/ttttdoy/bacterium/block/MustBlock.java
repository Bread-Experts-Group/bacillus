package com.ttttdoy.bacterium.block;

import com.ttttdoy.bacterium.util.NeighborLists;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class MustBlock extends Block implements BonemealableBlock {
    public static final IntegerProperty AGE;
    private static final Set<Block> filter = new HashSet<>(Collections.singletonList(Blocks.WATER));

    public MustBlock() {
        super(Properties.ofFullCopy(Blocks.SPONGE));
        this.registerDefaultState(this.getStateDefinition().any().setValue(AGE, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AGE);
    }

//    @Override
//    public @Nullable BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
//        return this.defaultBlockState().setValue(AGE, 0);
//    }

    @Override
    public boolean isValidBonemealTarget(LevelReader levelReader, BlockPos blockPos, BlockState blockState) {
        return true;
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource randomSource, BlockPos blockPos, BlockState blockState) {
        return NeighborLists.nextPlace(level, blockPos, filter, 0) != null;
    }

    @Override
    public void performBonemeal(ServerLevel serverLevel, RandomSource randomSource, BlockPos blockPos, BlockState blockState) {
        if (serverLevel != null) {
            int i = blockState.getValue(AGE) + randomSource.nextInt(3) + 1;
            if (i < 16) serverLevel.setBlockAndUpdate(blockPos, blockState.setValue(AGE, i));
            if (randomSource.nextInt(8) + 8 < i) {
                BlockPos next = NeighborLists.nextPlace(serverLevel, blockPos, filter, randomSource);
                if (next != null) {
                    serverLevel.setBlockAndUpdate(next, this.defaultBlockState());
                    serverLevel.setBlockAndUpdate(blockPos, blockState.setValue(AGE, randomSource.nextInt(10)));
                }
            }
        }
    }

    @Override
    protected boolean isRandomlyTicking(BlockState blockState) { return true; }

    @Override
    protected void randomTick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, RandomSource randomSource) {
        super.randomTick(blockState, serverLevel, blockPos, randomSource);
    }

    static {
        AGE = BlockStateProperties.AGE_15;
    }
}
