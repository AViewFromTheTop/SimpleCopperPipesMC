package net.lunade.copper.block.entity;

import java.util.ArrayList;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.lunade.copper.block.CopperFittingBlock;
import net.lunade.copper.block.CopperPipeBlock;
import net.lunade.copper.block.entity.leaking.LeakingPipeManager;
import net.lunade.copper.block.entity.data.TransferablePipeDataHandler;
import net.lunade.copper.block.properties.PipeFluid;
import net.lunade.copper.config.SimpleCopperPipesConfig;
import net.lunade.copper.registry.CopperPipeDispenseBehaviors;
import net.lunade.copper.registry.PipeMovementRestrictions;
import net.lunade.copper.registry.SimpleCopperPipesBlockEntityTypes;
import net.lunade.copper.registry.SimpleCopperPipesBlockStateProperties;
import net.lunade.copper.registry.SimpleCopperPipesSoundEvents;
import net.lunade.copper.tag.SimpleCopperPipesBlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.SupportType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.gameevent.BlockPositionSource;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.GameEventListener;
import net.minecraft.world.level.gameevent.PositionSource;
import net.minecraft.world.level.gameevent.vibrations.VibrationSystem;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CopperPipeEntity extends AbstractSimpleCopperBlockEntity implements GameEventListener.Provider<VibrationSystem.Listener>, VibrationSystem {
	private static final int VIBRATION_RANGE = 8;
	private static final int MAX_TRANSFER_AMOUNT = 1;
	private final VibrationSystem.Listener vibrationListener;
	private final VibrationSystem.User vibrationUser;
	public int transferCooldown;
	public int dispenseCooldown;
	public int noteBlockCooldown;
	public boolean canDispense;
	public boolean shootsControlled;
	public boolean shootsSpecial;
	public boolean canAccept;
	public BlockPos inputGameEventPos;
	public Vec3 gameEventNbtVec3;
	private VibrationSystem.Data vibrationData;

	public CopperPipeEntity(BlockPos blockPos, BlockState blockState) {
		super(SimpleCopperPipesBlockEntityTypes.COPPER_PIPE, blockPos, blockState, MoveType.FROM_PIPE);
		this.noteBlockCooldown = 0;
		this.vibrationUser = this.createVibrationUser();
		this.vibrationData = new VibrationSystem.Data();
		this.vibrationListener = new VibrationSystem.Listener(this);
	}

	public static boolean canTransfer(Level level, BlockPos pos, boolean to, @NotNull CopperPipeEntity copperPipe, @Nullable Storage<ItemVariant> inventory, @Nullable Storage<ItemVariant> pipeInventory) {
		if (copperPipe.transferCooldown > 0) return false;

		boolean transferApiCheck = true;
		boolean usingTransferApi = false;
		if (inventory != null) {
			usingTransferApi = true;
			transferApiCheck = to
				? inventory.supportsInsertion() && (pipeInventory == null || pipeInventory.supportsExtraction())
				: inventory.supportsExtraction() && (pipeInventory == null || pipeInventory.supportsInsertion());
		}

		final BlockEntity blockEntity = level.getBlockEntity(pos);
		if (blockEntity != null) {
			if (blockEntity instanceof CopperPipeEntity pipe) return (to || pipe.transferCooldown <= 0) && transferApiCheck;
			if (blockEntity instanceof CopperFittingEntity) return false;

			if (to) {
				final PipeMovementRestrictions.CanTransferTo<BlockEntity> canTransfer = PipeMovementRestrictions.getCanTransferTo(blockEntity);
				if (canTransfer != null) return canTransfer.canTransfer((ServerLevel) level, pos, level.getBlockState(pos), copperPipe, blockEntity) && transferApiCheck;
			} else {
				final PipeMovementRestrictions.CanTakeFrom<BlockEntity> canTake = PipeMovementRestrictions.getCanTakeFrom(blockEntity);
				if (canTake != null) return canTake.canTake((ServerLevel) level, pos, level.getBlockState(pos), copperPipe, blockEntity) && transferApiCheck;
			}
		}
		return usingTransferApi && transferApiCheck;
	}

	public static long addItem(ItemVariant resource, @NotNull Storage<ItemVariant> inventory, Transaction transaction) {
		if (inventory.supportsInsertion()) return inventory.insert(resource, MAX_TRANSFER_AMOUNT, transaction);
		return 0L;
	}

	public static void spawnItem(Level level, ItemStack stack, int shotPower, @NotNull Direction direction, @NotNull Vec3 pos, @NotNull Direction facing) { //Simply Spawn An Item
		final double x = pos.x();
		final double y = pos.y() - (direction.getAxis() == Direction.Axis.Y ? 0.125D : 0.15625D);
		final double z = pos.z();

		final Direction.Axis axis = facing.getAxis();
		final double xd = axis == Direction.Axis.X ? (shotPower * facing.getStepX()) * 0.1 : 0D;
		final double yd = axis == Direction.Axis.Y ? (shotPower * facing.getStepY()) * 0.1 : 0D;
		final double zd = axis == Direction.Axis.Z ? (shotPower * facing.getStepZ()) * 0.1 : 0D;

		ItemEntity itemEntity = new ItemEntity(level, x, y, z, stack);
		itemEntity.setDeltaMovement(xd, yd, zd);
		level.addFreshEntity(itemEntity);
	}

	public static void setCooldown(@NotNull Level level, BlockPos blockPos) {
		if (level.getBlockEntity(blockPos) instanceof CopperPipeEntity pipe) pipe.setCooldown(level.getBlockState(blockPos));
	}

	public static Storage<ItemVariant> getStorageAt(Level level, BlockPos blockPos, Direction direction) {
		return ItemStorage.SIDED.find(level, blockPos, level.getBlockState(blockPos), level.getBlockEntity(blockPos), direction);
	}

	@Override
	public void setItem(int i, ItemStack itemStack) {
		this.unpackLootTable(null);
		if (itemStack != null) {
			this.getItems().set(i, itemStack);
			if (itemStack.getCount() > this.getMaxStackSize()) itemStack.setCount(this.getMaxStackSize());
		}
	}

	@Override
	public void serverTick(@NotNull Level level, BlockPos blockPos, BlockState blockState) {
		VibrationSystem.Ticker.tick(this.level, this.getVibrationData(), this.createVibrationUser());
		super.serverTick(level, blockPos, blockState);

		if (level.isClientSide()) return;

		if (this.noteBlockCooldown > 0) --this.noteBlockCooldown;
		if (this.dispenseCooldown > 0) {
			--this.dispenseCooldown;
		} else {
			this.dispense((ServerLevel) level, blockPos, blockState);
			int i = 0;
			if (level.getBlockState(blockPos.relative(blockState.getValue(BlockStateProperties.FACING).getOpposite())).getBlock() instanceof CopperFittingBlock fitting) {
				i = fitting.getCooldown();
			} else {
				if (blockState.getBlock() instanceof CopperPipeBlock pipe) i = Mth.floor(pipe.getCooldown() * 0.5);
			}
			this.dispenseCooldown = i;
		}

		if (this.transferCooldown > 0) {
			--this.transferCooldown;
		} else {
			this.pipeMove(level, blockPos, blockState);
		}

		if (blockState.getValue(SimpleCopperPipesBlockStateProperties.FLUID) == PipeFluid.WATER && blockState.getValue(BlockStateProperties.FACING) != Direction.UP) {
			LeakingPipeManager.addPos(level, blockPos);
		}
	}

	@Override
	public void updateBlockEntityValues(Level level, BlockPos pos, @NotNull BlockState state) {
		if (!(state.getBlock() instanceof CopperPipeBlock)) return;

		final Direction facing = state.getValue(BlockStateProperties.FACING);
		final Direction facingAway = facing.getOpposite();
		final BlockPos facingPos = pos.relative(facing);
		final BlockState facingState = level.getBlockState(facingPos);
		final BlockState oppositeState = level.getBlockState(pos.relative(facingAway));
		final Block oppositeBlock = oppositeState.getBlock();
		final SimpleCopperPipesConfig config = SimpleCopperPipesConfig.get();

		this.canDispense = !facingState.isFaceSturdy(level, facingPos, facing, SupportType.CENTER) && facingState.isFaceSturdy(level, facingPos, facing, SupportType.CENTER);
		this.shootsControlled = oppositeBlock == Blocks.DROPPER;
		this.shootsSpecial = oppositeBlock == Blocks.DISPENSER;
		this.canAccept = !(oppositeBlock instanceof CopperPipeBlock) && !(oppositeBlock instanceof CopperFittingBlock) && !oppositeState.isRedstoneConductor(level, pos);
		this.canWater = (oppositeState.getFluidState().is(FluidTags.WATER) || state.getValue(BlockStateProperties.WATERLOGGED) || oppositeState.getValueOrElse(BlockStateProperties.WATERLOGGED, false)) && config.carryWater;
		this.canLava = oppositeState.getFluidState().is(FluidTags.LAVA) && config.carryLava;
		final boolean canWaterAndLava = this.canWater && this.canLava;
		this.canSmoke = (oppositeBlock instanceof CampfireBlock && !this.canWater && !this.canLava ? oppositeState.getValue(BlockStateProperties.LIT) : canWaterAndLava) && config.carrySmoke;
		if (canWaterAndLava) {
			this.canWater = false;
			this.canLava = false;
		}
	}

	public void pipeMove(Level level, BlockPos blockPos, @NotNull BlockState blockState) {
		final Direction facing = blockState.getValue(BlockStateProperties.FACING);
		final boolean movedOut = this.moveOut(level, blockPos, facing);
		final int movedIn = this.moveIn(level, blockPos, blockState, facing);
		if (movedOut || movedIn >= 2) {
			setCooldown(blockState);
			setChanged(level, blockPos, blockState);
			if (movedIn == 3) {
				if (!SimpleCopperPipesConfig.get().suctionSounds) return;
				level.playSound(null, blockPos, SimpleCopperPipesSoundEvents.ITEM_IN, SoundSource.BLOCKS, 0.2F, (level.random.nextFloat() * 0.25F) + 0.8F);
			}
		}
	}

	private int moveIn(Level level, @NotNull BlockPos pos, BlockState state, @NotNull Direction facing) {
		final Direction facingAway = facing.getOpposite();
		final BlockPos facingAwayPos = pos.relative(facingAway);
		final Storage<ItemVariant> inventory = getStorageAt(level, facingAwayPos, facing);
		final Storage<ItemVariant> pipeInventory = getStorageAt(level, pos, facingAway);
		if (inventory == null || pipeInventory == null || !canTransfer(level, facingAwayPos, false, this, inventory, pipeInventory)) return 0;

		for (StorageView<ItemVariant> storageView : inventory) {
			if (storageView.isResourceBlank() || storageView.getAmount() <= 0) continue;

			final Transaction transaction = Transaction.openOuter();
			final var resource = storageView.getResource();
			final long extracted = inventory.extract(resource, MAX_TRANSFER_AMOUNT, transaction);
			if (extracted > 0) { // successfully extracted item
				final long inserted = addItem(resource, pipeInventory, transaction);
				if (inserted > 0) { // successfully inserted item
					transaction.commit(); // applies the changes
					if (state.is(SimpleCopperPipesBlockTags.SILENT_PIPES)) return 2;

					final Block block = level.getBlockState(facingAwayPos).getBlock();
					if (!(block instanceof CopperPipeBlock) && !(block instanceof CopperFittingBlock)) return 3;
					return 2;
				}
			}
			transaction.close(); // if it cant commit, close it.
		}

		return 0;
	}

	private boolean moveOut(Level level, @NotNull BlockPos pos, Direction facing) {
		final BlockPos facingPos = pos.relative(facing);
		final Storage<ItemVariant> inventory = getStorageAt(level, facingPos, facing.getOpposite());
		final Storage<ItemVariant> pipeInventory = getStorageAt(level, pos, facing);
		if (inventory == null || pipeInventory == null || !canTransfer(level, facingPos, true, this, inventory, pipeInventory)) return false;

		final BlockState facingState = level.getBlockState(facingPos);
		final boolean canMove = !(facingState.getBlock() instanceof CopperPipeBlock) || facingState.getValue(CopperPipeBlock.FACING) != facing;
		if (!canMove) return false;

		for (StorageView<ItemVariant> storageView : pipeInventory) {
			if (storageView.isResourceBlank() || storageView.getAmount() <= 0) continue;

			final Transaction transaction = Transaction.openOuter();
			final var resource = storageView.getResource();
			final long inserted = inventory.insert(resource, MAX_TRANSFER_AMOUNT, transaction);
			if (inserted > 0) { // successfully inserted item
				final long extracted = pipeInventory.extract(resource, MAX_TRANSFER_AMOUNT, transaction);
				if (extracted > 0) { // successfully extracted item
					transaction.commit(); // applies the changes
					return true;
				}
			}
			transaction.close(); // if it can't commit, close it.
		}

		return false;
	}

	private boolean dispense(ServerLevel level, BlockPos pos, @NotNull BlockState state) {
		if (!this.canDispense) return false;

		int slot = this.chooseNonEmptySlot(level.random);
		if (slot < 0) return false;

		final ItemStack stack = this.getItem(slot);
		if (stack.isEmpty()) return false;

		ItemStack shotItem;
		int shotPower = 4;
		if (this.shootsControlled) { //If Dropper
			shotPower = 10;
			if (SimpleCopperPipesConfig.get().dispenseSounds) {
				level.playSound(null, pos, SimpleCopperPipesSoundEvents.LAUNCH, SoundSource.BLOCKS, 0.2F, (level.random.nextFloat() * 0.25F) + 0.8F);
			}
		} else if (this.shootsSpecial) { //If Dispenser, Use Pipe-Specific Launch Length
			if (state.getBlock() instanceof CopperPipeBlock pipe) {
				shotPower = pipe.dispenseShotPower;
				if (SimpleCopperPipesConfig.get().dispenseSounds) {
					level.playSound(null, pos, SimpleCopperPipesSoundEvents.LAUNCH, SoundSource.BLOCKS, 0.2F, (level.random.nextFloat() * 0.25F) + 0.8F);
				}
			} else {
				shotPower = 12;
			}
		}

		final Direction facing = state.getValue(BlockStateProperties.FACING);
		final boolean silent = state.is(SimpleCopperPipesBlockTags.SILENT_PIPES);
		if (level.getBlockState(pos.relative(facing.getOpposite())).getBlock() instanceof CopperFittingBlock) {
			shotItem = canonShoot(level, pos, stack, state, facing, shotPower, true, silent);
		} else {
			shotItem = canonShoot(level, pos, stack, state, facing, shotPower, false, silent);
			level.levelEvent(LevelEvent.PARTICLES_SHOOT_WHITE_SMOKE, pos, facing.get3DDataValue());
		}
		this.setItem(slot, shotItem);
		return true;
	}

	private ItemStack canonShoot(
		ServerLevel level,
		@NotNull BlockPos pos,
		ItemStack stack,
		@NotNull BlockState state,
		Direction facing,
		int shotPower,
		boolean fitting,
		boolean silent
	) {
		final Vec3 output = pos.getCenter().relative(facing, 0.7D);;
		ItemStack usableStack = stack;
		final SimpleCopperPipesConfig config = SimpleCopperPipesConfig.get();

		if (state.getValue(CopperPipeBlock.POWERED)) { //Special Behavior When Powered
			CopperPipeDispenseBehaviors.PoweredDispense poweredDispense = CopperPipeDispenseBehaviors.getDispense(usableStack.getItem());
			if (poweredDispense != null) {
				usableStack = stack.split(1);
				poweredDispense.dispense(level, usableStack, shotPower, facing, output, state, pos, this);
				if (!fitting && !silent) {
					if (config.dispenseSounds) level.playSound(null, pos, SimpleCopperPipesSoundEvents.ITEM_OUT, SoundSource.BLOCKS, 0.2F, (level.random.nextFloat() * 0.25F) + 0.8F);
					level.gameEvent(null, GameEvent.ENTITY_PLACE, pos);
				}
				return stack;
			}
		}

		if (config.dispensing) {
			usableStack = stack.split(1);
			level.levelEvent(LevelEvent.PARTICLES_SHOOT_SMOKE, pos, facing.get3DDataValue());
			spawnItem(level, usableStack, shotPower, facing, output, facing);
			if (!silent) {
				level.gameEvent(null, GameEvent.ENTITY_PLACE, pos);
				if (config.dispenseSounds) {
					level.playSound(null, pos, SimpleCopperPipesSoundEvents.ITEM_OUT, SoundSource.BLOCKS, 0.2F, (level.random.nextFloat() * 0.25F) + 0.8F);
				}
			}
		}

		return stack;
	}

	public int chooseNonEmptySlot(RandomSource random) {
		this.unpackLootTable(null);
		int i = -1;
		int j = 1;
		for (int k = 0; k < this.inventory.size(); ++k) {
			if (!this.inventory.get(k).isEmpty() && random.nextInt(j++) == 0) i = k;
		}
		return i;
	}

	public void setCooldown(@NotNull BlockState state) {
		this.transferCooldown = state.getBlock() instanceof CopperPipeBlock pipe ? pipe.getCooldown() : 2;
	}

	@Override
	public void loadAdditional(ValueInput input) {
		super.loadAdditional(input);
		this.transferCooldown = input.getIntOr("transferCooldown", 0);
		this.dispenseCooldown = input.getIntOr("dispenseCooldown", 0);
		this.noteBlockCooldown = input.getIntOr("noteBlockCooldown", 0);
		this.canDispense = input.getBooleanOr("canDispense", false);
		this.shootsControlled = input.getBooleanOr("shootsControlled", false);
		this.shootsSpecial = input.getBooleanOr("shootsSpecial", false);
		this.canAccept = input.getBooleanOr("canAccept", false);
		this.vibrationData = input.read("listener", Data.CODEC).orElseGet(VibrationSystem.Data::new);
	}

	@Override
	protected void saveAdditional(ValueOutput output) {
		super.saveAdditional(output);
		output.putInt("transferCooldown", this.transferCooldown);
		output.putInt("dispenseCooldown", this.dispenseCooldown);
		output.putInt("noteBlockCooldown", this.noteBlockCooldown);
		output.putBoolean("canDispense", this.canDispense);
		output.putBoolean("shootsControlled", this.shootsControlled);
		output.putBoolean("shootsSpecial", this.shootsSpecial);
		output.putBoolean("canAccept", this.canAccept);
		output.store("listener", Data.CODEC, this.vibrationData);
	}

	public VibrationSystem.User createVibrationUser() {
		return new VibrationUser(this.getBlockPos());
	}

	@Override
	@NotNull
	public VibrationSystem.Data getVibrationData() {
		return this.vibrationData;
	}

	@Override
	@NotNull
	public VibrationSystem.User getVibrationUser() {
		return this.vibrationUser;
	}

	@Override
	@NotNull
	public VibrationSystem.Listener getListener() {
		return this.vibrationListener;
	}

	@Override
	public boolean canAcceptMoveableNbt(MoveType moveType, Direction moveDirection, BlockState fromState) {
		if (moveType == MoveType.FROM_FITTING) return this.getBlockState().getValue(BlockStateProperties.FACING) == moveDirection;
		return this.getBlockState().getValue(BlockStateProperties.FACING) == moveDirection || moveDirection == fromState.getValue(BlockStateProperties.FACING);
	}

	@Override
	public boolean canMoveNbtInDirection(Direction direction, @NotNull BlockState state) {
		return direction != state.getValue(BlockStateProperties.FACING).getOpposite();
	}

	@Override
	public void dispenseMoveableNbt(ServerLevel serverLevel, BlockPos blockPos, BlockState blockState) {
		if (!this.canDispense) return;

		final ArrayList<TransferablePipeDataHandler.SaveableTransferablePipeData> data = this.transferableDataHandler.getSavedDataList();
		if (data.isEmpty()) return;

		for (TransferablePipeDataHandler.SaveableTransferablePipeData nbt : data) {
			if (nbt.shouldMove()) nbt.dispense(serverLevel, blockPos, blockState, this);
		}
		this.moveMoveableNbt(serverLevel, blockPos, blockState);
	}

	public class VibrationUser implements VibrationSystem.User {
		protected final BlockPos blockPos;
		private final PositionSource positionSource;

		public VibrationUser(BlockPos blockPos) {
			this.blockPos = blockPos;
			this.positionSource = new BlockPositionSource(blockPos);
		}

		@Override
		public int getListenerRadius() {
			return VIBRATION_RANGE;
		}

		@Override
		@NotNull
		public PositionSource getPositionSource() {
			return this.positionSource;
		}

		@Override
		public boolean canReceiveVibration(@NotNull ServerLevel serverLevel, @NotNull BlockPos blockPos, @NotNull Holder<GameEvent> gameEvent, @Nullable GameEvent.Context context) {
			if (!SimpleCopperPipesConfig.get().senseGameEvents) return false;

			boolean placeDestroy = gameEvent == GameEvent.BLOCK_DESTROY || gameEvent == GameEvent.BLOCK_PLACE;
			if ((serverLevel.getBlockState(blockPos).getBlock() instanceof CopperPipeBlock) || (blockPos == CopperPipeEntity.this.getBlockPos() && placeDestroy)) return false;

			if (CopperPipeEntity.this.canAccept) {
				CopperPipeEntity.this.transferableDataHandler.addSaveableMoveablePipeNbt(new TransferablePipeDataHandler.SaveableTransferablePipeData(gameEvent.value(), Vec3.atCenterOf(blockPos), context, CopperPipeEntity.this.getBlockPos()).withShouldMove(true).withShouldSave(true));
				return true;
			}
			return false;
		}

		@Override
		public void onReceiveVibration(@NotNull ServerLevel serverLevel, @NotNull BlockPos blockPos, @NotNull Holder<GameEvent> gameEvent, @Nullable Entity entity, @Nullable Entity entity2, float f) {

		}

		@Override
		public void onDataChanged() {
			CopperPipeEntity.this.setChanged();
		}

		@Override
		public boolean requiresAdjacentChunksToBeTicking() {
			return true;
		}
	}

}
