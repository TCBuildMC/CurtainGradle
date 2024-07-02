package xyz.tcbuildmc.minecraft.curtaingradle.api

import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

@Retention(RetentionPolicy.SOURCE)
@interface Property {
    String value() default "";
}