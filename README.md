# "Cannot remove from team" workaround
[MC-270728](https://bugs.mojang.com/browse/MC-270728)
[Modrinth](https://modrinth.com/mod/cannot-remove-from-team-workaround)

On some buggy servers you may have noticeable lag spikes caused by `Error executing task on client`:
```text
net.minecraft.class_148: Main thread packet handler
	at net.minecraft.class_2600.method_11072(class_2600.java:33) ~[client-intermediary.jar:?]
	at net.minecraft.class_1255.method_18859(class_1255.java:156) ~[client-intermediary.jar:?]
	at net.minecraft.class_4093.method_18859(class_4093.java:23) ~[client-intermediary.jar:?]
	at net.minecraft.class_1255.method_16075(class_1255.java:130) ~[client-intermediary.jar:?]
	at net.minecraft.class_1255.method_5383(class_1255.java:115) ~[client-intermediary.jar:?]
	at net.minecraft.class_310.method_1523(class_310.java:1283) ~[client-intermediary.jar:?]
	at net.minecraft.class_310.method_1514(class_310.java:888) ~[client-intermediary.jar:?]
	at net.minecraft.client.main.Main.main(Main.java:265) ~[Fabric%201.20.4.jar:?]
	at net.fabricmc.loader.impl.game.minecraft.MinecraftGameProvider.launch(MinecraftGameProvider.java:470) ~[fabric-loader-0.15.7.jar:?]
	at net.fabricmc.loader.impl.launch.knot.Knot.launch(Knot.java:74) ~[fabric-loader-0.15.7.jar:?]
	at net.fabricmc.loader.impl.launch.knot.KnotClient.main(KnotClient.java:23) ~[fabric-loader-0.15.7.jar:?]
Caused by: java.lang.IllegalStateException: Player is either on another team or not on any team. Cannot remove from team 'Z/Sta/517631'.
	at net.minecraft.class_269.method_1157(class_269.java:307) ~[client-intermediary.jar:?]
	at net.minecraft.class_634.method_11099(class_634.java:2029) ~[client-intermediary.jar:?]
	at net.minecraft.class_5900.method_34173(class_5900.java:129) ~[client-intermediary.jar:?]
	at net.minecraft.class_5900.method_11054(class_5900.java:14) ~[client-intermediary.jar:?]
	at net.minecraft.class_2600.method_11072(class_2600.java:24) ~[client-intermediary.jar:?]
	... 10 more
```

One thing this mod does is it replaces `IllegalStateException` throwing with `LOGGER.warn()`.

Default `net.minecraft.scoreboard.Scoreboard.removeScoreHolderFromTeam()` (`class_269.method_1157()`) implementation:
```java
public void removeScoreHolderFromTeam(String scoreHolderName, Team team) {
    if (this.getScoreHolderTeam(scoreHolderName) != team) {
        throw new IllegalStateException("Player is either on another team or not on any team. Cannot remove from team '" + team.getName() + "'.");
    } else {
        this.teamsByScoreHolder.remove(scoreHolderName);
        team.getPlayerList().remove(scoreHolderName);
    }
}
```

Though I'm not a Java coding guru, I don't understand why the exception is thrown on render thread.
