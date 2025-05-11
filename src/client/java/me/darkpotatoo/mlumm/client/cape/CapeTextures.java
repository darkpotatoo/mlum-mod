package me.darkpotatoo.mlumm.client.cape;

import me.darkpotatoo.mlumm.client.Configuration;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.util.Identifier;

public class CapeTextures {

    private static final Configuration config = AutoConfig.getConfigHolder(Configuration.class).getConfig();

    public static Identifier getCapeTexture() {
        return switch(config.cape_texture) {
            case MLUM_S16 -> Identifier.of("mlumm", "textures/cape_s16.png");
            case MLUM_S15 -> Identifier.of("mlumm", "textures/cape_s15.png");
            case MLUM_S14 -> Identifier.of("mlumm", "textures/cape_s14.png");
            case MLUM_S13 -> Identifier.of("mlumm", "textures/cape_s13.png");
            case MLUM_S12 -> Identifier.of("mlumm", "textures/cape_s12.png");
            case MLUM_S11 -> Identifier.of("mlumm", "textures/cape_s11.png");
            case MLUM_GENERIC -> Identifier.of("mlumm", "textures/cape_generic.png");
            case GUARD_MAIN -> Identifier.of("mlumm", "textures/cape_guard.png");
            case DET_MAIN -> Identifier.of("mlumm", "textures/cape_det.png");
            case PROUD_UTILITY_USER -> Identifier.of("mlumm", "textures/cape_proud_utility_user.png");
            case CHOCOLATE -> Identifier.of("mlumm", "textures/cape_chocolate.png");
            case S15GANG_MGM -> Identifier.of("mlumm", "textures/cape_s15_mgm.png");
            case S15GANG_STOICS -> Identifier.of("mlumm", "textures/cape_s15_stoics.png");
            case S15GANG_MOAI -> Identifier.of("mlumm", "textures/cape_s15_moai.png");
            case S15GANG_SLAVES -> Identifier.of("mlumm", "textures/cape_s15_slaves.png");
            case S15GANG_ANTS -> Identifier.of("mlumm", "textures/cape_s15_ants.png");
            case S15TOSS_CREEPER -> Identifier.of("mlumm", "textures/cape_toss_creeper.png");
        };
    }
}
