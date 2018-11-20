package hello;

import jdk.incubator.sql2.DataSource;
import jdk.incubator.sql2.DataSourceFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class GreetingRouter {

	@Bean
	public DataSource dataSource() {
		return DataSourceFactory.newFactory("org.postgresql.adba.PgDataSourceFactory")
				.builder()
				.url("jdbc:postgresql://localhost:5432/test")
				.username("test")
				.password("test")
				.build();
	}

	@Bean
	public GreetingHandler greetingHandler(DataSource dataSource) {
		return new GreetingHandler(dataSource);
	}
}
