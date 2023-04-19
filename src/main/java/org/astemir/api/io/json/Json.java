package org.astemir.api.io.json;


import org.spongepowered.asm.mixin.Mixin;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Json {

    Deserialize[] deserialize() default {};

    Serialize[] serialize() default {};

}
