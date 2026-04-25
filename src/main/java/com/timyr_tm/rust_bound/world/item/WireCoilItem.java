package com.timyr_tm.rust_bound.world.item;

import com.timyr_tm.rust_bound.core.component.DataComponents;
import com.timyr_tm.rust_bound.world.block.entity.ConnectableBlockEntity;
import com.timyr_tm.rust_bound.world.block.entity.ConnectionInfo;
import com.timyr_tm.rust_bound.world.block.entity.ConnectionPointInfo;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.NonNull;

import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

public class WireCoilItem extends Item {
    public WireCoilItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NonNull InteractionResult useOn(@NonNull UseOnContext context) {
        if (context.getLevel().getBlockEntity(context.getClickedPos()) instanceof ConnectableBlockEntity blockEntity) {
            final Vec3 pos = context.getClickLocation().subtract(new Vec3(blockEntity.getBlockPos()));
            final Optional<Map.Entry<String, ConnectionPointInfo>> point = blockEntity.getConnectionPoints().entrySet().stream()
                .filter(
                    info -> info.getValue().shape().toAabbs().stream()
                        .anyMatch(
                            aabb -> aabb.inflate(.001).contains(pos)
                        )
                )
                .findFirst();
            if (point.isEmpty())
                return InteractionResult.FAIL;

            final ItemStack stack = context.getItemInHand();

            if (stack.has(DataComponents.WIRE_COIL_CONNECTION)) {
                final ConnectionInfo firstConnection = stack.remove(DataComponents.WIRE_COIL_CONNECTION);
                final ConnectionInfo lastConnection = new ConnectionInfo(point.orElseThrow().getKey(), point.orElseThrow().getValue().pos());

                final ConnectableBlockEntity lastBlockEntity = firstConnection.getBlockEntity(context.getLevel());

                if (lastBlockEntity == null)
                    return InteractionResult.FAIL;

                blockEntity.addConnection(lastConnection.name(), firstConnection);
                lastBlockEntity.addConnection(firstConnection.name(), lastConnection);
                blockEntity.setChanged();
                lastBlockEntity.setChanged();

                if (context.getLevel().isClientSide() && context.getPlayer() != null)
                    context.getPlayer().displayClientMessage(
                        Component.literal(
                            "%s (%s) - %s (%s)".formatted(
                                firstConnection.name(), firstConnection.pos(),
                                lastConnection.name(), lastConnection.pos()
                            )
                        ),
                        true
                    );
            }
            else {
                stack.set(DataComponents.WIRE_COIL_CONNECTION, new ConnectionInfo(point.orElseThrow().getKey(), point.orElseThrow().getValue().pos()));
                if (context.getLevel().isClientSide() && context.getPlayer() != null)
                    context.getPlayer().displayClientMessage(Component.literal("+ %s".formatted(point.orElseThrow().getKey())), true);
            }
            return InteractionResult.SUCCESS;
        }
        return super.useOn(context);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void appendHoverText(
        @NonNull ItemStack stack, @NonNull TooltipContext context, @NonNull TooltipDisplay tooltipDisplay,
        @NonNull Consumer<Component> tooltipAdder, @NonNull TooltipFlag flag
    ) {

    }
}
