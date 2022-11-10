package org.astemir.api.common;

import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.astemir.api.math.Vector3;
import org.astemir.api.utils.RandomUtils;

public class ParticleEmitter {


    private ParticleOptions particle;

    private int count = 1;

    private int ticks = 0;

    private Vector3 size = new Vector3(0,0,0);


    public ParticleEmitter(ParticleOptions particle) {
        this.particle = particle;
    }

    public void update(){
        ticks++;
    }


    public void emit(Level level,Vector3 position,Vector3 speed){
        for (int i = 0;i<count;i++) {
            level.addParticle(particle, position.getX()+getXOffset(), position.getY()+getYOffset(), position.getZ()+getZOffset(), speed.getX(), speed.getY(), speed.getZ());
        }
    }

    public ParticleEmitter size(Vector3 size){
        this.size = size;
        return this;
    }


    public ParticleEmitter count(int count){
        this.count = count;
        return this;
    }



    private float getXOffset(){
        if (size.x > 0) {
            return RandomUtils.randomFloat(-size.x, size.x);
        }
        return 0;
    }

    private float getYOffset(){
        if (size.y > 0) {
            return RandomUtils.randomFloat(-size.y, size.y);
        }
        return 0;
    }

    private float getZOffset(){
        if (size.z > 0) {
            return RandomUtils.randomFloat(-size.z, size.z);
        }
        return 0;
    }

    public int getTicks() {
        return ticks;
    }

    public static ParticleOptions item(ItemStack item){
        return new ItemParticleOption(ParticleTypes.ITEM,item);
    }

    public static ParticleOptions block(BlockState state){
        return new BlockParticleOption(ParticleTypes.BLOCK,state);
    }

    public static ParticleOptions blockMarker(BlockState state){
        return new BlockParticleOption(ParticleTypes.BLOCK_MARKER,state);
    }

    public static ParticleOptions blockDust(BlockState state){
        return new BlockParticleOption(ParticleTypes.FALLING_DUST,state);
    }
}
