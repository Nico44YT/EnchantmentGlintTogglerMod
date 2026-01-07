package nico.enchantment_glint_toggler.mixin;

import net.minecraft.block.EnchantingTableBlock;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

//? >= 1.20.5 {
import net.minecraft.component.DataComponentTypes;
//?} else >=1.17 {
/*import net.minecraft.nbt.NbtCompound;
*///?} else {
/*import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.TranslatableText;
*///?}

import java.util.Optional;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {

    //? >=1.20.5 {
    @Inject(method = "useOnBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/Item;useOnBlock(Lnet/minecraft/item/ItemUsageContext;)Lnet/minecraft/util/ActionResult;"), cancellable = true)
    public void glintToggler$useOnBlock(ItemUsageContext context, CallbackInfoReturnable<ActionResult> cir) {
        ItemStack thisStack = (ItemStack) (Object) this;

        if(context.getWorld().getBlockState(context.getBlockPos()).getBlock() instanceof EnchantingTableBlock && (thisStack.getItem().hasGlint(thisStack) || thisStack.contains(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE)) && context.getPlayer() instanceof ServerPlayerEntity serverPlayer) {
            boolean comp = thisStack.getOrDefault(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE, glintTogglerHelper$hasGlint(thisStack.getItem(), thisStack));

            thisStack.set(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE, !comp);

            glintTogglerHelper$sendMessage(serverPlayer, !comp ? Text.translatable("enchantment_glint_toggler.info.on") : Text.translatable("enchantment_glint_toggler.info.off"));

            cir.setReturnValue(ActionResult.SUCCESS);
        }
    }
    //?} else if >=1.19 {
    /*@Inject(method = "useOnBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/Item;useOnBlock(Lnet/minecraft/item/ItemUsageContext;)Lnet/minecraft/util/ActionResult;"), cancellable = true)
    public void glintToggler$useOnBlock(ItemUsageContext context, CallbackInfoReturnable<ActionResult> cir) {
        ItemStack thisStack = (ItemStack) (Object) this;

        if(context.getWorld().getBlockState(context.getBlockPos()).getBlock() instanceof EnchantingTableBlock && glintTogglerHelper$hasGlint(thisStack.getItem(), thisStack) && context.getPlayer() instanceof ServerPlayerEntity serverPlayer) {
            NbtCompound nbtCompound = thisStack.getOrCreateSubNbt("glint_toggler");
            if(!nbtCompound.contains("show_glint")) {
                nbtCompound.putBoolean("show_glint", true);
            }

            nbtCompound.putBoolean("show_glint", !nbtCompound.getBoolean("show_glint"));

            glintTogglerHelper$sendMessage(serverPlayer, nbtCompound.getBoolean("show_glint") ? Text.translatable("enchantment_glint_toggler.info.on") : Text.translatable("enchantment_glint_toggler.info.off"));

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
    *///?} else {
    /*@Inject(method = "useOnBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/Item;useOnBlock(Lnet/minecraft/item/ItemUsageContext;)Lnet/minecraft/util/ActionResult;"), cancellable = true)
    public void glintToggler$useOnBlock(ItemUsageContext context, CallbackInfoReturnable<ActionResult> cir) {
        ItemStack thisStack = (ItemStack) (Object) this;

        if (context.getWorld().getBlockState(context.getBlockPos()).getBlock() instanceof EnchantingTableBlock && glintTogglerHelper$hasGlint(thisStack.getItem(), thisStack) && context.getPlayer() instanceof ServerPlayerEntity) {
            CompoundTag nbtCompound = thisStack.getOrCreateSubTag("glint_toggler");
            if (!nbtCompound.contains("show_glint")) {
                nbtCompound.putBoolean("show_glint", true);
            }

            nbtCompound.putBoolean("show_glint", !nbtCompound.getBoolean("show_glint"));

            assert context.getPlayer() != null;
            glintTogglerHelper$sendMessage((ServerPlayerEntity) context.getPlayer(), nbtCompound.getBoolean("show_glint") ? new TranslatableText("enchantment_glint_toggler.info.on") : new TranslatableText("enchantment_glint_toggler.info.off"));

            cir.setReturnValue(ActionResult.SUCCESS);
        }
    }

    //? >=1.16 {
    @Inject(method = "hasGlint", at = @At("HEAD"), cancellable = true)
     //?} else {
    /^@Inject(method = "hasEnchantmentGlint", at = @At("HEAD"), cancellable = true)
    ^///?}
    public void glintToggler$hasGlint(CallbackInfoReturnable<Boolean> cir) {
        ItemStack thisStack = (ItemStack) (Object) this;

        Optional.ofNullable(thisStack.getSubTag("glint_toggler")).ifPresent(nbt -> {
            if (nbt.contains("show_glint") && !nbt.getBoolean("show_glint")) cir.setReturnValue(false);
        });
    }

    *///?}

    private static boolean glintTogglerHelper$hasGlint(Item item, ItemStack itemStack) {
        //? >=1.20.5 {
        return item.hasGlint(itemStack) || itemStack.contains(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE);
        //?} else >=1.16 {
        //return item.hasGlint(itemStack);
         //?} else if >=1.15 {
        /*return item.hasEnchantmentGlint(itemStack);
        *///?}
    }

    private static void glintTogglerHelper$sendMessage(ServerPlayerEntity player, Text text) {
        //? >=1.16 {
        player.sendMessage(text, true);
         //?} else if >=1.15 {
        /*player.sendChatMessage(text, net.minecraft.network.MessageType.GAME_INFO);
        *///?}
    }
}
