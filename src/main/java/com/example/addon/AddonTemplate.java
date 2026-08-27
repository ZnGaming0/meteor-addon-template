package com.example.addon;

import com.mojang.logging.LogUtils;
import meteordevelopment.meteorclient.MeteorClient;
import meteordevelopment.meteorclient.addons.GithubRepo;
import meteordevelopment.meteorclient.addons.MeteorAddon;
import meteordevelopment.meteorclient.commands.Commands;
import meteordevelopment.meteorclient.pathing.BaritoneUtils;
import meteordevelopment.meteorclient.systems.hud.Hud;
import meteordevelopment.meteorclient.systems.hud.HudGroup;
import meteordevelopment.meteorclient.systems.modules.Category;
import meteordevelopment.meteorclient.systems.modules.Modules;
import meteordevelopment.meteorclient.utils.Utils;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.metadata.ModMetadata;
import org.slf4j.Logger;

// Itt már a te saját eléréseid szerepelnek:
import com.example.addon.commands.LinkCommand;
import com.example.addon.commands.ShulkerQueueCommand;
import com.example.addon.hud.ChestCount;
import com.example.addon.hud.OnlineTSRMembersHUD;
import com.example.addon.modules.*;

import java.io.File;

public class OmegawareAddons extends MeteorAddon {
    public static final String MOD_ID = "omegaware-addons";
    public static ModMetadata MOD_META;
    public static final Logger LOG = LogUtils.getLogger();
    
    // Maradhat a lap neve OmegaWare, de a kód mögötte már a te példád
    public static final Category CATEGORY = new Category("OmegaWare");
    public static final HudGroup HUD_GROUP = new HudGroup("OmegaWare");

    public static BetterBaritoneBuild BETTER_BARITONE_BUILD = null;

    public static File GetConfigFile(String key, String filename) {
        return new File(new File(new File(new File(MeteorClient.FOLDER, "omegaware"), key), Utils.getFileWorldName()), filename);
    }

    @Override
    public void onInitialize() {
        LOG.info("Initializing OmegaWare Addons in com.example.addon package");

        FabricLoader.getInstance().getModContainer(MOD_ID).ifPresent(modContainer -> {
            MOD_META = modContainer.getMetadata();
        });

        // Modulok regisztrálása
        Modules.get().add(new TPAAutomationModule());
        Modules.get().add(new BeaconRangeModule());
        Modules.get().add(new ChatFilterModule());
        Modules.get().add(new ItemFrameDupeModule());
        Modules.get().add(new BetterStashFinderModule());

        if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
            Modules.get().add(new TSRKitBotModule());
        }

        if (BaritoneUtils.IS_AVAILABLE) {
            BETTER_BARITONE_BUILD = new BetterBaritoneBuild();
            Modules.get().add(BETTER_BARITONE_BUILD);
        }

        // Parancsok
        Commands.add(new LinkCommand());
        Commands.add(new ShulkerQueueCommand());

        // HUD elemek
        Hud.get().register(OnlineTSRMembersHUD.INFO);
        Hud.get().register(ChestCount.INFO);
    }

    @Override
    public void onRegisterCategories() {
        Modules.registerCategory(CATEGORY);
    }

    @Override
    public String getPackage() {
        // Átírva az új package-re
        return "com.example.addon";
    }

    @Override
    public GithubRepo getRepo() {
        return new GithubRepo("Omega172", "OmegaWare-Addons");
    }
}
