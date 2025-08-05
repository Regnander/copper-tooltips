package me.violine.mixin;

import me.violine.CopperData;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(Item.class)
public class ItemMixin {
    @Inject(method = "appendHoverText", at = @At(value = "HEAD"))
    private void addCopperHoverText(
        ItemStack itemStack,
        Item.TooltipContext tooltipContext,
        TooltipDisplay tooltipDisplay,
        Consumer<Component> consumer,
        TooltipFlag tooltipFlag,
        CallbackInfo ci
    ) {
        CopperData.get(itemStack.getItem()).ifPresent(copperData -> {
            for (var key : copperData.getTooltipTranslationKeys()) {
                consumer.accept(Component.translatable(key).withStyle(ChatFormatting.GRAY));
            }
        });
    }
}
