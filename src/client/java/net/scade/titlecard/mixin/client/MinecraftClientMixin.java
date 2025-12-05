package net.scade.titlecard.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.scade.titlecard.TitleCardClient;
import net.scade.titlecard.config.TitleConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @Inject(method = "updateWindowTitle", at = @At("HEAD"), cancellable = true)
    private void onUpdateWindowTitle(CallbackInfo ci) {
        if (!TitleConfig.config.enabled) {return;}
        MinecraftClient client = (MinecraftClient) (Object) this;
        TitleCardClient.updateWindowTitle(client);
        ci.cancel();
    }
}