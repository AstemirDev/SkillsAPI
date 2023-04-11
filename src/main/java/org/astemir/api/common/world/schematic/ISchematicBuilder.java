package org.astemir.api.common.world.schematic;

import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.structures.DesertPyramidStructure;
import net.minecraft.world.level.levelgen.structure.structures.IglooStructure;
import net.minecraft.world.level.levelgen.structure.structures.WoodlandMansionStructure;
import org.astemir.api.math.MathUtils;
import org.astemir.api.math.collection.Couple;
import org.astemir.api.math.vector.Vector2;
import org.astemir.api.math.vector.Vector3;
import org.w3c.dom.css.Rect;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

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

    default Set<SchematicSafePlacement> schematicToPieces(WESchematic schematic,boolean skipAir){
        Map<Vector3,BlockState> points = schematic.blocks(skipAir);
        Rectangle original = new Rectangle(schematic.getWidth(),schematic.getLength());
        List<Rectangle> areas = MathUtils.splitRectangleToSmaller(original,new Rectangle(16,16));
        Set<SchematicSafePlacement> areasPoints = new HashSet<>();
        for (Rectangle area : areas) {
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
        private Rectangle rectangle;
        private WESchematic schematic;

        public SchematicSafePlacement(List<Couple<Vector3, BlockState>> points, WESchematic schematic, Rectangle rectangle) {
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

        public Rectangle getRectangle() {
            return rectangle;
        }

        public WESchematic getSchematic() {
            return schematic;
        }
    }
}
