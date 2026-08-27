package com.example.addon.hud;

import com.example.addon.AddonTemplate;
import meteordevelopment.meteorclient.systems.hud.HudElement;
import meteordevelopment.meteorclient.systems.hud.HudElementInfo;
import meteordevelopment.meteorclient.systems.hud.HudRenderer;
import meteordevelopment.meteorclient.utils.Utils;
import meteordevelopment.meteorclient.utils.render.color.Color;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.block.enums.ChestType;

import static meteordevelopment.meteorclient.MeteorClient.mc;

public class ChestCount extends HudElement {
    // Átírva a te saját AddonTemplate.HUD_GROUP-odra és regisztrációs struktúrádra
    public static final HudElementInfo<ChestCount> INFO = new HudElementInfo<>(
        AddonTemplate.HUD_GROUP, 
        "dubs-count", 
        "Displays how many dubs are in render distance", 
        ChestCount::new
    );

    public ChestCount() {
        super(INFO);
    }

    @Override
    public void render(HudRenderer renderer) {
        if (mc.player == null || mc.world == null) return;

        int count = 0;
        
        // Végigmegyünk a világban betöltött blokk-entitásokon
        for (BlockEntity blockEntity : Utils.blockEntities()) {
            if (!(blockEntity instanceof ChestBlockEntity chestBlock)) continue;

            BlockState blockState = chestBlock.getCachedState();
            
            // Biztonsági ellenőrzés, hogy a blokk valóban rendelkezik-e CHEST_TYPE tulajdonsággal
            if (!blockState.contains(ChestBlock.CHEST_TYPE)) continue;
            
            ChestType chestType = blockState.get(ChestBlock.CHEST_TYPE);

            // Csak a bal oldali ládákat számoljuk, hogy elkerüljük a dupla számolást (Single és Right átugrása)
            if (chestType.equals(ChestType.SINGLE) || chestType.equals(ChestType.RIGHT)) continue;

            count++;
        }

        // Szöveg kirajzolása a képernyőre
        renderer.text("Dubs: ", x, y, Color.WHITE, true);
        renderer.text("" + count, x + renderer.textWidth("Dubs: ", true), y, Color.LIGHT_GRAY, true);

        // HUD elem méretének frissítése, hogy a Meteor GUI-ban megfelelően lehessen mozgatni
        setSize(renderer.textWidth("Dubs: " + count, true), renderer.textHeight(true) + 1);
    }
}
