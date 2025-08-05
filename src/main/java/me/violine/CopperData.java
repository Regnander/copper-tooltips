package me.violine;

import net.minecraft.world.item.Item;

import java.util.*;

import static me.violine.CopperData.CopperStage.*;

public record CopperData(String simpleId, CopperStage stage, boolean waxed) {

    private static final List<String> BLOCK_NAMES = List.of(
            "chiseled_copper",
            "copper",
            "copper_bars",
            "copper_bulb",
            "copper_chain",
            "copper_chest",
            "copper_door",
            "copper_golem_statue",
            "copper_grate",
            "copper_lantern",
            "copper_trapdoor",
            "cut_copper",
            "cut_copper_slab",
            "cut_copper_stairs",
            "lightning_rod"
    );
    private static final HashMap<String, CopperData> DATA
            = HashMap.newHashMap(BLOCK_NAMES.size() * values().length * 2);

    static {
        for (var stage : values()) {
            for (var blockName : BLOCK_NAMES) {
                var unwaxedData = new CopperData(blockName, stage, false);
                DATA.put(unwaxedData.getDescriptiveId(), unwaxedData);

                var waxedData = new CopperData(blockName, stage, true);
                DATA.put(waxedData.getDescriptiveId(), waxedData);
            }
        }
    }

    /// Get descriptive ID
    /// e.g.: "cut_copper" -> "block.minecraft.cut_copper"
    /// Also handle special case "Block of Copper" with non-standard ID
    /// e.g.: "waxed_copper" -> "block.minecraft.waxed_copper_block"
    public String getDescriptiveId() {
        final var BLOCK_PREFIX = "block.minecraft.";
        final var WAXED_PREFIX = "waxed_";

        var isNonStandardId = this.simpleId.equals("copper") && this.stage == UNOXIDIZED;
        var simpleId = isNonStandardId ? "copper_block" : this.simpleId;
        return BLOCK_PREFIX + (this.waxed ? WAXED_PREFIX : "") + this.stage.getPrefix() + simpleId;
    }

    public ArrayList<String> getTooltipTranslationKeys() {
        final var TRANSLATE_KEY_PREFIX = "copper-tooltips.";

        var keys = new ArrayList<String>();
        keys.add(TRANSLATE_KEY_PREFIX + "stage." + this.stage.toString());
        if (this.waxed)
            keys.add(TRANSLATE_KEY_PREFIX + "waxed");
        return keys;
    }

    public CopperData getMain() {
        return new CopperData(this.simpleId, UNOXIDIZED, false);
    }

    public static Optional<CopperData> get(Item item) {
        var id = item.getDescriptionId();
        return Optional.ofNullable(DATA.get(id));
    }

    public enum CopperStage {
        UNOXIDIZED,
        EXPOSED,
        WEATHERED,
        OXIDIZED;

        public String getPrefix() {
            return switch (this) {
                case UNOXIDIZED -> "";
                case EXPOSED -> "exposed_";
                case WEATHERED -> "weathered_";
                case OXIDIZED -> "oxidized_";
            };
        }

        public String toString() {
            return switch (this) {
                case UNOXIDIZED -> "unoxidized";
                case EXPOSED -> "exposed";
                case WEATHERED -> "weathered";
                case OXIDIZED -> "oxidized";
            };
        }
    }
}
