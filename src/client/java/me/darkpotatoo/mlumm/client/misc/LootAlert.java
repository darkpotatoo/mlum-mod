package me.darkpotatoo.mlumm.client.misc;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.packet.s2c.play.SubtitleS2CPacket;
import net.minecraft.network.packet.s2c.play.TitleS2CPacket;
import net.minecraft.text.Text;

public class LootAlert {

    public static void lootAlert(String itemname) {
        MinecraftClient client = MinecraftClient.getInstance();
        ClientPlayerEntity player = client.player;
        TitleS2CPacket packet1 = null;
        if (itemname.contains("Diamond Chestplate")) {
            packet1 = new TitleS2CPacket(Text.of("§b&lTank Vest!"));
        }
        if (itemname.contains("Netherite")) {
            packet1 = new TitleS2CPacket(Text.of("§f&lRiot Piece!"));
        }
        player.networkHandler.sendPacket(packet1);
        player.networkHandler.sendPacket(new SubtitleS2CPacket(Text.of("§eRARE DROP!")));
    }

}
