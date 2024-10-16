package com.ttttdoy.bacterium.block;

import com.mojang.serialization.MapCodec;
import com.ttttdoy.bacterium.block.entity.BacteriaBlockEntity;
import com.ttttdoy.bacterium.registry.ModBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BacteriaBlock extends BaseEntityBlock {
    public static final MapCodec<BacteriaBlock> CODEC = simpleCodec(BacteriaBlock::new);

    private BacteriaBlock(BlockBehaviour.Properties properties) { super(properties); }

    public BacteriaBlock() {
        this(BlockBehaviour.Properties.ofFullCopy(Blocks.SPONGE).instabreak());
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new BacteriaBlockEntity(blockPos, blockState);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        return createTickerHelper(blockEntityType, ModBlockEntityTypes.BACTERIA_BLOCK_ENTITY.get(), BacteriaBlockEntity::tick);
    }

    @Override
    protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    protected @NotNull RenderShape getRenderShape(BlockState blockState) { return RenderShape.MODEL; }
}
