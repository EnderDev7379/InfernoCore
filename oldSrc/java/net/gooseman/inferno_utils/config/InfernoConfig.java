package net.gooseman.inferno_utils.config;


public class InfernoConfig {
    public static String provider(String namespace) {
        return """
                # General
                
                # Whether to show debug messages
                debug=true
                
                
                # Semi-Hardcore / Combat Logging Detection
                
                # How long being in combat lasts in ticks
                combat_length=400
                
                # How long the death ban should last
                death_ban_time=2d
                # Reason for the death ban
                death_ban_reason=You have died! If this death was not caused by a player, or you think it was otherwise unfair, please contact the moderators through the #tickets discord channel.
                
                # How long the combat logging ban should last
                combat_ban_time=2d
                # Reason for the combat logging ban
                combat_ban_reason=Combat logging isn't permitted! If you weren't in combat, or you think this ban is otherwise unfair, please contact the moderators through the #tickets discord channel.
                
                end_portal_disabled=true
                """;
    }
    public static SimpleConfig.ConfigRequest configRequest = SimpleConfig.of("inferno_utils-config").provider(InfernoConfig::provider);
    public static SimpleConfig config = configRequest.request();

    public static void reloadConfig() {
        config = configRequest.request();
        config.getOrDefault("debug", true);
    }

    public static String[] getStringArray(String key) {
        String arrayString = config.getOrDefault(key, "[]");
        arrayString = arrayString.substring(1, arrayString.length() - 1);
        return arrayString.split(",");
    }
}
