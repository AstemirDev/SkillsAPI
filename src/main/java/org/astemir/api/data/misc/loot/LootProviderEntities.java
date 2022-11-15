package org.astemir.api.data.misc.loot;

import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.data.loot.EntityLoot;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class LootProviderEntities extends EntityLoot implements ILootProvider<EntityType<?>> {

    private Map<EntityType, MobLoot> mobsLoot = new HashMap<>();

    @Override
    protected void addTables() {
        addLoot();
    }

    @Override
    protected Iterable<EntityType<?>> getKnownEntities() {
        return getKnown();
    }


    public static class MobLoot{

        private List<ItemDrop> items = new ArrayList<>();


        public ItemDrop addDrop(Item item, int min, int max){
            ItemDrop drop = new ItemDrop(item);
            drop.count(min,max);
            this.items.add(drop);
            return drop;
        }

        public ItemDrop addDrop(Item item){
            ItemDrop drop = new ItemDrop(item);
            this.items.add(drop);
            return drop;
        }

        public List<ItemDrop> getItems() {
            return items;
        }


        public LootTable.Builder build(){
            LootTable.Builder builder = LootTable.lootTable();
            for (ItemDrop item : items) {
                builder = builder.withPool(item.toPool());
            }
            return builder;
        }
    }

    public static class ItemDrop {

        private ItemLike item;
        private UniformInt count;
        private DropParameters parameters = new DropParameters();
        private float chance = 1;


        public ItemDrop(Item item) {
            this.item = item;
            this.count = UniformInt.of(1,1);
        }

        public ItemDrop count(int min,int max) {
            this.count = UniformInt.of(min,max);
            return this;
        }

        public ItemDrop chance(float chance){
            this.chance = chance;
            return this;
        }

        public ItemDrop parameters(DropParameters parameters) {
            this.parameters = parameters;
            return this;
        }

        public ItemLike getItem() {
            return item;
        }

        public LootPool.Builder toPool(){
            LootPool.Builder itemPoolBuilder = LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F));
            LootPoolSingletonContainer.Builder itemContainerBuilder = LootItem.lootTableItem(item);
            itemContainerBuilder = itemContainerBuilder.apply(SetItemCountFunction.setCount(UniformGenerator.between(count.getMinValue(),count.getMaxValue())));
            if (parameters.isCanBeCooked()){
                itemContainerBuilder = itemContainerBuilder.apply(SmeltItemFunction.smelted().when(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, ENTITY_ON_FIRE)));
            }
            if (parameters.getLooting() != null){
                itemContainerBuilder = itemContainerBuilder.apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(parameters.getLooting().getMinValue(), parameters.getLooting().getMaxValue())).when(LootItemKilledByPlayerCondition.killedByPlayer()));
            }
            itemPoolBuilder = itemPoolBuilder.add(itemContainerBuilder);
            if (parameters.getNeedToBeKilledBy() != null){
                itemPoolBuilder = itemPoolBuilder.when(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.KILLER, EntityPredicate.Builder.entity().of(parameters.getNeedToBeKilledBy())));
            }
            if (chance != 1){
                itemPoolBuilder = itemPoolBuilder.when(LootItemRandomChanceCondition.randomChance(chance));
            }
            return itemPoolBuilder;
        }
    }


    public static class DropParameters{

        private UniformInt looting;
        private boolean canBeCooked = false;
        private EntityType needToBeKilledBy;


        public DropParameters canBeCooked(){
            this.canBeCooked = true;
            return this;
        }

        public DropParameters looting(int minBonus,int maxBonus){
            this.looting = UniformInt.of(minBonus,maxBonus);
            return this;
        }

        public DropParameters killerCondition(EntityType killer){
            this.needToBeKilledBy = killer;
            return this;
        }

        public boolean isCanBeCooked() {
            return canBeCooked;
        }

        public EntityType getNeedToBeKilledBy() {
            return needToBeKilledBy;
        }

        public UniformInt getLooting() {
            return looting;
        }
    }
}
