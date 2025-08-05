package me.violine.mixin;

import me.violine.CopperData;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
    @Shadow
    public abstract boolean is(Item item);

    @Shadow
    public abstract Item getItem();

    @Inject(
        method = "getHoverName",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;getItemName()Lnet/minecraft/network/chat/Component;"
        ),
        cancellable = true
    )
    private void changeCopperItemName(CallbackInfoReturnable<Component> cir) {
        var copperData = CopperData.get(this.getItem());
        copperData.ifPresent(data ->
            cir.setReturnValue(Component.translatable(data.getMain().getDescriptiveId()))
        );
    }
}
