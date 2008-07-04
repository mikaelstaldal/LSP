package nu.staldal.lsp.framework;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

/**
 * A field in a {@link EasyService} with this annotation  
 * set will be automatically passed as a page parameter to the template.
 * The page parameter name is the <code>value</code> argument, or 
 * the field name if the <code>value</code> argument is not set. 
 * The field must be <code>public</code>. 
 *
 * @author Mikael Ståldal
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface PageParameter
{
    /**
     * Page parameter name. Use field name if not set.
     * 
     * @return parameter name
     */
    String value() default "";
}
