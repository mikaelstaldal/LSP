package nu.staldal.lsp.framework;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

/**
 * Mark a field in a {@link ThrowawayService} with this annotation to 
 * set have it automatically populated with the value of a HTTP request 
 * parameter. The parameter name is the <code>value</code> argument, or 
 * the field name if the <code>value</code> argument is not set.
 *<p> 
 * The field may be of any primitive type, any primitive wrapper type,
 * {@link java.lang.String}, or an {@link java.lang.Enum} type.
 *<p>
 * If the parameter is not set ({@link javax.servlet.http.HttpServletRequest#getParameter(String)} 
 * return <code>null</code>), the field is not set and retains its initial value. Unless the
 * field has an {@link Mandatory} annotation, then a HTTP 400 BAD REQUEST error is generated.
 *<p>
 * If a parameter cannot be parsed to the type of the field, a HTTP 400 BAD REQUEST error is 
 * generated.
 *
 * @see javax.servlet.http.HttpServletRequest#getParameter(String)
 *
 * @author Mikael St�ldal
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Parameter
{
    /**
     * Parameter name. Use field name if not set.
     * 
     * @return parameter name
     */
    String value() default "";
}
