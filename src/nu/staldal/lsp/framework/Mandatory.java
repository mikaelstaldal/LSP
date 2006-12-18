package nu.staldal.lsp.framework;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

/**
 * Indicates that a parameter is mandatory. Must be used together with
 * {@link Parameter}. 
 * 
 * @see Parameter
 *
 * @author Mikael Ståldal
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Mandatory
{
    // marker only
}
