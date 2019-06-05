package moxy;

import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;

/**
 * Register MoxyReflector packages from other modules
 *
 * @deprecated MoxyReflector won't be generated, you can safely remove this annotation
 */
@Deprecated
@Target(value = TYPE)
public @interface RegisterMoxyReflectorPackages {
	String[] value();
}
