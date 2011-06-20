package nu.staldal.lsp.spring.webmvc.webapp;

import nu.staldal.lsp.spring.webmvc.LspViewResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;

@Configuration
public class AppConfig {

	@Bean
	ViewResolver viewResolver() {
		final LspViewResolver viewResolver = new LspViewResolver();
		viewResolver.setViewsPath("/WEB-INF/views");
		viewResolver.setParentView("parent");
		return viewResolver;
	}

}
