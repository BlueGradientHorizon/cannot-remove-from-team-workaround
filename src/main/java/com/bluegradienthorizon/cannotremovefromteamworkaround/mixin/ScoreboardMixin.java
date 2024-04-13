package com.bluegradienthorizon.cannotremovefromteamworkaround.mixin;

import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.Team;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Scoreboard.class)
public class ScoreboardMixin {
	@Final
	@Shadow
	private static Logger LOGGER;

	@Inject(method = "removeScoreHolderFromTeam", at = @At(
		value = "INVOKE",
		target = "java/lang/IllegalStateException.<init> (Ljava/lang/String;)V"
	), cancellable = true)
	private void removeScoreHolderFromTeam(String scoreHolderName, Team team, CallbackInfo ci) {
        LOGGER.warn("Player is either on another team or not on any team. Cannot remove from team '{}'. "+
			"IllegalStateException was not threw due to currently applied \"Cannot remove from team\" workaround.", team.getName());
		ci.cancel();
	}
}