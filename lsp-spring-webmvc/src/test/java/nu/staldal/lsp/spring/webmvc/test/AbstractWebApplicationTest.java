package nu.staldal.lsp.spring.webmvc.test;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
		loader = MockWebApplicationContextLoader.class,
		locations = "classpath:/META-INF/spring/context.xml")
@MockWebApplication(name = "appServlet", webapp = "src/test/webapp")
public abstract class AbstractWebApplicationTest {

	@Autowired
	protected WebApplicationContext applicationContext;

}
