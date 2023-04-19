package org.astemir.api.common.world.schematic;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import org.astemir.api.math.MathUtils;
import org.astemir.api.math.collection.Couple;
import org.astemir.api.math.components.Rect2;
import org.astemir.api.math.components.Vector3;

import java.awt.*;
import java.util.*;
import java.util.List;

public interface ISchematicBuilder {


    default boolean buildSchematicCheckFreeSpace(WESchematic schematic, WorldGenLevel level, BlockPos pos, Vector3 rotation, boolean centered, boolean skipAir){
        if (schematic.isEmptyForPlace(level,pos,rotation,centered,skipAir)){
            buildSchematic(schematic,level,pos,rotation,centered,skipAir);
            return true;
        }
        return false;
    }

    default void buildSchematic(WESchematic schematic, WorldGenLevel level,BlockPos pos, Vector3 rotation,boolean centered, boolean skipAir){
        for (Map.Entry<Vector3, BlockState> entry : schematic.blocks(skipAir).entrySet()) {
            Vector3 point = entry.getKey();
            if (centered) {
                point = point.add(-schematic.getWidth() / 2, 0, -schematic.getLength() / 2);
            }
            point = point.rotateAroundZ(rotation.z).rotateAroundY(rotation.y).rotateAroundX(rotation.x);
            level.setBlock(pos.offset(point.x,point.y,point.z), entry.getValue(), 19);
        }
    }

    static Set<SchematicSafePlacement> schematicToPieces(WESchematic schematic,boolean skipAir){
        Map<Vector3,BlockState> points = schematic.blocks(skipAir);
        Rect2 original = new Rect2(0,0,schematic.getWidth(),schematic.getLength());
        List<Rect2> areas = MathUtils.splitRectangleToSmaller(original,new Rect2(0,0,16,16));
        Set<SchematicSafePlacement> areasPoints = new HashSet<>();
        for (Rect2 area : areas) {
            List<Couple<Vector3,BlockState>> list = new ArrayList<>();
            for (Map.Entry<Vector3, BlockState> point : points.entrySet()) {
                if (area.contains(point.getKey().x,point.getKey().z)){
                    list.add(new Couple<>(point.getKey(),point.getValue()));
                }
            }
            areasPoints.add(new SchematicSafePlacement(list,schematic,area));
        }
        return areasPoints;
    }

    public class SchematicSafePlacement{

        private List<Couple<Vector3,BlockState>> points = new ArrayList<>();
        private Rect2 rectangle;
        private WESchematic schematic;

        public SchematicSafePlacement(List<Couple<Vector3, BlockState>> points, WESchematic schematic, Rect2 rectangle) {
            this.points = points;
            this.schematic = schematic;
            this.rectangle = rectangle;
        }

        public void place(WorldGenLevel level,BlockPos pos, Vector3 rotation,boolean centered){
            for (Couple<Vector3, BlockState> entry : points) {
                Vector3 point = entry.getKey();
                if (centered) {
                    point = point.add(-schematic.getWidth() / 2, 0, -schematic.getLength() / 2);
                }
                point = point.rotateAroundZ(rotation.z).rotateAroundY(rotation.y).rotateAroundX(rotation.x);
                level.setBlock(pos.offset(point.x,point.y,point.z), entry.getValue(), 19);
            }
        }

        public List<Couple<Vector3, BlockState>> getPoints() {
            return points;
        }

        public Rect2 getRectangle() {
            return rectangle;
        }

        public WESchematic getSchematic() {
            return schematic;
        }
    }
}
