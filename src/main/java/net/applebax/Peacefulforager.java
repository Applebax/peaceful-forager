package net.applebax;

import net.applebax.blocks.*;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.condition.SurvivesExplosionLootCondition;
import net.minecraft.loot.condition.TableBonusLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.registry.*;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.feature.TreeConfiguredFeatures;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class Peacefulforager implements ModInitializer {
	public static final String MOD_ID = "peaceful-forager";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    private record FruitDrop(Block leaves, Item fruit, float[] fruitChances){}

    private final FruitDrop[] fruitDrops = {
        new FruitDrop(Blocks.BIRCH_LEAVES,PEAR, new float[]{0.005F, 0.0055555557F, 0.00625F, 0.008333334F, 0.025F}),
        new FruitDrop(Blocks.SPRUCE_LEAVES,PINE_CONE, new float[]{ 0.02F, 0.022222223F, 0.025F, 0.033333335F, 0.1F}),
        new FruitDrop(Blocks.DARK_OAK_LEAVES,BURR, new float[]{0.005F, 0.0055555557F, 0.00625F, 0.008333334F, 0.025F}),
        new FruitDrop(Blocks.CHERRY_LEAVES,CHERRIES, new float[]{0.005F, 0.0055555557F, 0.00625F, 0.008333334F, 0.025F}),
        new FruitDrop(Blocks.JUNGLE_LEAVES,BANANAS, new float[]{0.005F, 0.0055555557F, 0.00625F, 0.008333334F, 0.025F}),
    };

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		LootTableEvents.MODIFY.register((key, tableBuilder, source, registries) -> {
            for(FruitDrop fruitDrop : fruitDrops){
                if (source.isBuiltin() && fruitDrop.leaves.getLootTableKey().equals(key)) {
                    var enchantment = registries.getWrapperOrThrow(RegistryKeys.ENCHANTMENT).getOrThrow(Enchantments.FORTUNE);

                    LootPool.Builder poolBuilder = LootPool.builder()
                        .with(
                            ItemEntry.builder(
                                    fruitDrop.fruit
                                )
                                .conditionally(
                                    SurvivesExplosionLootCondition.builder()
                                )
                        )
                        .conditionally(
                            TableBonusLootCondition.builder(
                                enchantment, fruitDrop.fruitChances
                            )
                        );
                    tableBuilder.pool(poolBuilder);
                }
            }
		});
		Registry.register(Registries.ITEM_GROUP, id("peaceful_forager"), ITEMGROUP_PEACEFUL_FORAGER);
		LOGGER.info("Hello!");
	}

	public static Identifier id(String path){
		return Identifier.of(MOD_ID,path);
	}

	public static <T extends Item> T register(String name,T item){
		ITEMS.add(item);
		return Registry.register(Registries.ITEM,id(name),item);
	}

	public static Item register(String name){
		return register(name, new Item(new Item.Settings()));
	}

	public static Item register(String name, FoodComponent food){
		return register(name, new Item(new Item.Settings().food(food)));
	}

	public static <T extends Block> T register(String name, T block){
		Registry.register(Registries.BLOCK,id(name),block);
		BlockItem blockItem=new BlockItem(block, new Item.Settings());
		register(name,blockItem);
		return block;
	}

	private static ArrayList<Item> ITEMS = new ArrayList<>();

	public static final TagKey<Block> COOKABLE_MUSHROOM_TAG = TagKey.of(RegistryKeys.BLOCK, Identifier.of(MOD_ID, "cookable_mushroom"));

	public static final FoodComponent PEAR_FOOD = (new FoodComponent.Builder()).nutrition(4).saturationModifier(0.3F).build();
	public static final FoodComponent CHERRIES_FOOD = (new FoodComponent.Builder()).nutrition(2).saturationModifier(0.3F).build();
	public static final FoodComponent BANANAS_FOOD = (new FoodComponent.Builder()).nutrition(5).saturationModifier(0.3F).build();
	public static final FoodComponent CHESTNUT_FOOD = (new FoodComponent.Builder()).nutrition(1).saturationModifier(0.5F).snack().build();
	public static final FoodComponent PINE_BREAD_FOOD = (new FoodComponent.Builder()).nutrition(4).saturationModifier(0.6F).build();
	public static final FoodComponent PEAR_JAM_FOOD = (new FoodComponent.Builder()).nutrition(4).saturationModifier(0.9F).build();
	public static final FoodComponent PANCAKE_FOOD = (new FoodComponent.Builder()).nutrition(4).saturationModifier(0.7F).build();
	public static final FoodComponent BANANA_CHIPS_FOOD = (new FoodComponent.Builder()).nutrition(5).saturationModifier(0.6F).snack().build();
	public static final FoodComponent MUSHROOM_SKEWER_FOOD = (new FoodComponent.Builder()).nutrition(6).saturationModifier(0.8F).build();
	public static final FoodComponent ROSE_HIPS_FOOD = (new FoodComponent.Builder()).nutrition(1).saturationModifier(0.5F).snack().build();
	public static final FoodComponent COOKED_MUSHROOM_FOOD = (new FoodComponent.Builder()).nutrition(3).saturationModifier(0.8F).snack().build();


	public static final Item PEAR=register("pear", PEAR_FOOD);
	public static final Item PINE_CONE=register("pine_cone");
	public static final Item BURR=register("burr");
	public static final Item CHERRIES=register("cherries", CHERRIES_FOOD);
	public static final Item BANANAS=register("bananas", BANANAS_FOOD);
	public static final Item CHESTNUT=register("chestnut");
	public static final Item ROASTED_CHESTNUT=register("roasted_chestnut", CHESTNUT_FOOD);
	public static final Item PINE_FLOUR=register("pine_flour");
	public static final Item PINE_BREAD=register("pine_bread", PINE_BREAD_FOOD);
	public static final Item PEAR_JAM=register("pear_jam", PEAR_JAM_FOOD);
	public static final Item PANCAKE=register("pancake", PANCAKE_FOOD);
	public static final Item BANANA_CHIPS=register("banana_chips", BANANA_CHIPS_FOOD);
	public static final Item MUSHROOM_SKEWER=register("mushroom_skewer", MUSHROOM_SKEWER_FOOD);
	public static final Item ROSE_HIPS=register("rose_hips", ROSE_HIPS_FOOD);
	public static final Item COOKED_MUSHROOM=register("cooked_mushroom", COOKED_MUSHROOM_FOOD);

	public static final Block CHANTERELLE_MUSHROOM=register(
		"chanterelle_mushroom",
		new MushroomPlantBlock(TreeConfiguredFeatures.HUGE_BROWN_MUSHROOM,
			AbstractBlock.Settings.create()
				.mapColor(MapColor.PALE_YELLOW)
				.noCollision()
				.ticksRandomly()
				.breakInstantly()
				.sounds(BlockSoundGroup.GRASS)
				.luminance(state -> 1)
				.postProcess(Blocks::always)
				.pistonBehavior(PistonBehavior.DESTROY)
		)

	);
	public static final Block WAXCAP_MUSHROOM=register(
		"waxcap_mushroom",
		new MushroomPlantBlock(TreeConfiguredFeatures.HUGE_RED_MUSHROOM,
			AbstractBlock.Settings.create()
				.mapColor(MapColor.PINK)
				.noCollision()
				.ticksRandomly()
				.breakInstantly()
				.sounds(BlockSoundGroup.GRASS)
				.luminance(state -> 1)
				.postProcess(Blocks::always)
				.pistonBehavior(PistonBehavior.DESTROY)
		)

	);
public static final Block INKY_CAP_MUSHROOM=register(
		"inky_cap_mushroom",
		new MushroomPlantBlock(TreeConfiguredFeatures.HUGE_RED_MUSHROOM,
			AbstractBlock.Settings.create()
				.mapColor(MapColor.TERRACOTTA_BLACK)
				.noCollision()
				.ticksRandomly()
				.breakInstantly()
				.sounds(BlockSoundGroup.GRASS)
				.luminance(state -> 1)
				.postProcess(Blocks::always)
				.pistonBehavior(PistonBehavior.DESTROY)
		)

	);
public static final Block RUSSULA_MUSHROOM=register(
		"russula_mushroom",
		new MushroomPlantBlock(TreeConfiguredFeatures.HUGE_BROWN_MUSHROOM,
			AbstractBlock.Settings.create()
				.mapColor(MapColor.LICHEN_GREEN)
				.noCollision()
				.ticksRandomly()
				.breakInstantly()
				.sounds(BlockSoundGroup.GRASS)
				.luminance(state -> 1)
				.postProcess(Blocks::always)
				.pistonBehavior(PistonBehavior.DESTROY)
		)

	);
public static final Block GREEN_MUSHROOM=register(
		"green_mushroom",
		new MushroomPlantBlock(TreeConfiguredFeatures.HUGE_RED_MUSHROOM,
			AbstractBlock.Settings.create()
				.mapColor(MapColor.EMERALD_GREEN)
				.noCollision()
				.ticksRandomly()
				.breakInstantly()
				.sounds(BlockSoundGroup.GRASS)
				.luminance(state -> 1)
				.postProcess(Blocks::always)
				.pistonBehavior(PistonBehavior.DESTROY)
		)

	);
public static final Block STINGING_NETTLE=register(
		"stinging_nettle",
		new FlowerBlock(StatusEffects.SATURATION, 0.35F,
			AbstractBlock.Settings.create()
				.mapColor(MapColor.DARK_GREEN)
				.noCollision()
				.ticksRandomly()
				.breakInstantly()
				.sounds(BlockSoundGroup.GRASS)
				.offset(AbstractBlock.OffsetType.XZ)
				.postProcess(Blocks::always)
				.pistonBehavior(PistonBehavior.DESTROY)
		)

	);
public static final Block ROCKS=register(
		"rocks",
		new RocksBlock(
		FabricBlockSettings.create()
				.strength(0.5f)
		)
	);

public static final Block FLAT_STONE=register(
		"flat_stone",
		new FlatStoneBlock(
		FabricBlockSettings.create()
				.strength(0.5f)
		)
	);

public static final Block BROWN_ROCK=register(
		"brown_rock",
		new BrownRockBlock(
		FabricBlockSettings.create()
				.strength(0.5f)
		)
	);

public static final Block LIGHT_GRAY_ROCK=register(
		"light_gray_rock",
		new LightGrayRockBlock(
		FabricBlockSettings.create()
				.strength(0.5f)
		)
	);

public static final Block YELLOW_ROCK=register(
		"yellow_rock",
		new YellowRockBlock(
		FabricBlockSettings.create()
				.strength(0.5f)
		)
	);

// Creative Mode Tab
	public static final ItemGroup ITEMGROUP_PEACEFUL_FORAGER = FabricItemGroup.builder()
		.icon(() -> new ItemStack(Peacefulforager.CHANTERELLE_MUSHROOM))
		.displayName(Text.literal("Peaceful Forager"))
		.entries((displayContext, entries) -> {
			for (Item item : ITEMS) {
				entries.add(item);
			}
		})
		.build();

}