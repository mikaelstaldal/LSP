package nu.staldal.lsp.spring.webmvc.test;

import org.springframework.web.context.WebApplicationContext;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Configures a mock {@link WebApplicationContext}.  Each test class (or parent class) using
 * {@link MockWebApplicationContextLoader} must be annotated with this.
 *
 * @see http://code.google.com/p/ted-young/source/browse/trunk/blog.spring-mvc-integration-testing/src/main/java/me/tedyoung/blog/spring_mvc_integration_testing/
 * @see http://tedyoung.me/2011/02/14/spring-mvc-integration-testing-controllers/
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface MockWebApplication {

	/**
	 * The location of the webapp directory relative to your project.
	 * For maven users, this is generally src/main/webapp (default).
	 */
	String webapp() default "src/main/webapp";

	/**
	 * The servlet name as defined in the web.xml.
	 */
	String name();
}
