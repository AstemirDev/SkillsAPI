package org.astemir.api.data.loot.entity;

import net.minecraft.advancements.critereon.EntityFlagsPredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.functions.LootingEnchantFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.functions.SmeltItemFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemKilledByPlayerCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import org.astemir.api.data.loot.ItemDrop;

import java.util.ArrayList;
import java.util.List;

public class MobDrop {

    protected static final EntityPredicate.Builder ENTITY_ON_FIRE = EntityPredicate.Builder.entity().flags(EntityFlagsPredicate.Builder.flags().setOnFire(true).build());

    private List<ItemDrop<MobDropParameters>> items = new ArrayList<>();

    private EntityType<?> type;

    public MobDrop(EntityType<?> type) {
        this.type = type;
    }

    public MobDrop addDrop(ItemDrop drop){
        this.items.add(drop);
        return this;
    }


    public LootTable.Builder build(LootProviderEntities lootProviderEntities){
        LootTable.Builder builder = LootTable.lootTable();
        for (ItemDrop item : items) {
            builder = builder.withPool(buildDrop(item));
        }
        return builder;
    }

    public static ItemDrop itemDrop(Item item){
        ItemDrop drop = new ItemDrop(item);
        return drop;
    }

    public static ItemDrop itemDrop(Item item, int min, int max){
        ItemDrop drop = new ItemDrop(item);
        drop.count(min,max);
        return drop;
    }

    public LootPool.Builder buildDrop(ItemDrop<MobDropParameters> drop){
        LootPool.Builder itemPoolBuilder = LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F));
        LootPoolSingletonContainer.Builder itemContainerBuilder = LootItem.lootTableItem(drop.getItem());
        itemContainerBuilder = itemContainerBuilder.apply(SetItemCountFunction.setCount(UniformGenerator.between(drop.getCount().getMinValue(),drop.getCount().getMaxValue())));
        if (drop.getParameters() != null) {
            if (drop.getParameters().isCanBeCooked()) {
                itemContainerBuilder = itemContainerBuilder.apply(SmeltItemFunction.smelted().when(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, ENTITY_ON_FIRE)));
            }
            if (drop.getParameters().getLooting() != null) {
                itemContainerBuilder = itemContainerBuilder.apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(drop.getParameters().getLooting().getMinValue(), drop.getParameters().getLooting().getMaxValue())).when(LootItemKilledByPlayerCondition.killedByPlayer()));
            }
        }
        itemPoolBuilder = itemPoolBuilder.add(itemContainerBuilder);
        if (drop.getParameters() != null) {
            if (drop.getParameters().getNeedToBeKilledBy() != null) {
                itemPoolBuilder = itemPoolBuilder.when(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.KILLER, EntityPredicate.Builder.entity().of(drop.getParameters().getNeedToBeKilledBy())));
            }
        }
        if (drop.getChance() != 1){
            itemPoolBuilder = itemPoolBuilder.when(LootItemRandomChanceCondition.randomChance(drop.getChance()));
        }
        return itemPoolBuilder;
    }

    public EntityType<?> getType() {
        return type;
    }

    public List<ItemDrop<MobDropParameters>> getItems() {
        return items;
    }
}
