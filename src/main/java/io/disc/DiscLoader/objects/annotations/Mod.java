package io.disc.DiscLoader.objects.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Mod {
	String modid();
	String version();
	String desc();
}