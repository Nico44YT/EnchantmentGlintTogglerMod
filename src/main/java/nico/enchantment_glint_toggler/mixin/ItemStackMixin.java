package nico.enchantment_glint_toggler.mixin;

import net.minecraft.block.EnchantingTableBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

//? >= 1.20.5 {
/*import net.minecraft.component.DataComponentTypes;
*///?} else {
import net.minecraft.nbt.NbtCompound;
import java.util.Optional;
//?}

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {

    //? >=1.20.5 {
    /*@Inject(method = "useOnBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/Item;useOnBlock(Lnet/minecraft/item/ItemUsageContext;)Lnet/minecraft/util/ActionResult;"), cancellable = true)
    public void glintToggler$useOnBlock(ItemUsageContext context, CallbackInfoReturnable<ActionResult> cir) {
        ItemStack thisStack = (ItemStack) (Object) this;

        if(context.getWorld().getBlockState(context.getBlockPos()).getBlock() instanceof EnchantingTableBlock && (thisStack.getItem().hasGlint(thisStack) || thisStack.contains(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE))) {
            boolean comp = thisStack.getOrDefault(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE, thisStack.getItem().hasGlint(thisStack) || thisStack.contains(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE));

            thisStack.set(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE, !comp);

            assert context.getPlayer() != null;
            context.getPlayer().sendMessage(!comp ? Text.translatable("enchantment_glint_toggler.info.on") : Text.translatable("enchantment_glint_toggler.info.off"), true);

            cir.setReturnValue(ActionResult.SUCCESS);
        }
    }
    *///?} else {
    @Inject(method = "useOnBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/Item;useOnBlock(Lnet/minecraft/item/ItemUsageContext;)Lnet/minecraft/util/ActionResult;"), cancellable = true)
    public void glintToggler$useOnBlock(ItemUsageContext context, CallbackInfoReturnable<ActionResult> cir) {
        ItemStack thisStack = (ItemStack) (Object) this;

        if(context.getWorld().getBlockState(context.getBlockPos()).getBlock() instanceof EnchantingTableBlock && thisStack.getItem().hasGlint(thisStack)) {
            NbtCompound nbtCompound = thisStack.getOrCreateSubNbt("glint_toggler");
            if(!nbtCompound.contains("show_glint")) {
                nbtCompound.putBoolean("show_glint", true);
            }

            nbtCompound.putBoolean("show_glint", !nbtCompound.getBoolean("show_glint"));

            assert context.getPlayer() != null;
            context.getPlayer().sendMessage(nbtCompound.getBoolean("show_glint") ? Text.translatable("enchantment_glint_toggler.info.on") : Text.translatable("enchantment_glint_toggler.info.off"), true);

            cir.setReturnValue(ActionResult.SUCCESS);
        }
    }

    @Inject(method = "hasGlint", at = @At("HEAD"), cancellable = true)
    public void glintToggler$hasGlint(CallbackInfoReturnable<Boolean> cir) {
        ItemStack thisStack = (ItemStack) (Object) this;

        Optional.ofNullable(thisStack.getSubNbt("glint_toggler")).ifPresent(nbt -> {
            if(nbt.contains("show_glint") && !nbt.getBoolean("show_glint")) cir.setReturnValue(false);
        });
    }
    //?}
}
