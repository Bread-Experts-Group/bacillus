package com.ttttdoy.bacterium.block.entity;

import com.ttttdoy.bacterium.registry.ModBlockEntityTypes;
import com.ttttdoy.bacterium.registry.ModBlocks;
import com.ttttdoy.bacterium.util.NeighborLists;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashSet;
import java.util.Set;

public class BacteriaBlockEntity extends BlockEntity {
    private Set<Block> input;
    private Block output;
    private long nextTick = 0;
    private boolean jammed = false;

    public static final int MAXDELAY = 30;
    public static final int MINDELAY = 10;
    public static long jammedTick = 0;

    public BacteriaBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlockEntityTypes.BACTERIA_BLOCK_ENTITY.get(), blockPos, blockState);
    }

    public static void replace(Level level, BlockPos pos, Set<Block> input, Block output) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof Container) {
            Containers.dropContents(level, pos, (Container) blockEntity);
            level.updateNeighborsAt(pos, level.getBlockState(pos).getBlock());
        }

        BlockState state = ModBlocks.REPLACER.get().defaultBlockState();
        if (output == Blocks.AIR) state = ModBlocks.DESTROYER.get().defaultBlockState();

        level.setBlockAndUpdate(pos, state);
        if (level.getBlockEntity(pos) instanceof BacteriaBlockEntity bacteriaBlockEntity) {
            bacteriaBlockEntity.input = input;
            bacteriaBlockEntity.output = output;
        }

        level.playSound(null, pos, SoundEvents.CHORUS_FLOWER_GROW, SoundSource.BLOCKS, 0.8f, 1f);
    }

    public static void tick (Level level, BlockPos pos, BlockState state, BacteriaBlockEntity blockEntity) {
        if (level != null && !level.isClientSide) {
            // Update jammer time
            if (jammedTick != 0) blockEntity.jammed = true;
            if (level.getGameTime() >= jammedTick) jammedTick = 0;

            // Spread
            if (blockEntity.nextTick == 0) blockEntity.nextTick = blockEntity.getNextTick();
            else if (level.getGameTime() >= blockEntity.nextTick) {
                BlockPos next = NeighborLists.nextPlace(level, pos, blockEntity.input, level.random);
                if (!blockEntity.jammed && next != null) {
                    replace(level, next, blockEntity.input, blockEntity.output);
                    blockEntity.nextTick = blockEntity.getNextTick();
                } else {
                    level.setBlockAndUpdate(pos, blockEntity.output.defaultBlockState());
                    blockEntity.setRemoved();
                }
            }
        }
    }

    @Override
    protected void saveAdditional(CompoundTag compoundTag, HolderLookup.Provider provider) {
        StringBuilder string = new StringBuilder();
        if (input == null) return;
        for (Block b : input) string.append(BuiltInRegistries.BLOCK.getKey(b)).append("#");
        compoundTag.putString("inputId", string.toString());
        compoundTag.putString("outputId", BuiltInRegistries.BLOCK.getKey(output).toString());
        super.saveAdditional(compoundTag, provider);
    }

    @Override
    protected void loadAdditional(CompoundTag compoundTag, HolderLookup.Provider provider) {
        input = new HashSet<>();
        for (String string : compoundTag.getString("inputId").split("#"))
            input.add(BuiltInRegistries.BLOCK.get(ResourceLocation.tryParse(string)));

        output = BuiltInRegistries.BLOCK.get(ResourceLocation.tryParse(compoundTag.getString("outputId")));
        super.loadAdditional(compoundTag, provider);
    }

    private long getNextTick() {
        if (level == null) return 0;
        return level.getGameTime() + level.getRandom().nextInt(MAXDELAY) + MINDELAY;
    }

    public static void setJammed(Level level) { jammedTick = level.getGameTime() + MINDELAY; }
}
