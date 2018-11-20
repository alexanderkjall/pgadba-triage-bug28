package hello;

import java.util.List;
import java.util.stream.Collectors;
import jdk.incubator.sql2.AdbaType;
import jdk.incubator.sql2.DataSource;
import jdk.incubator.sql2.Result;
import jdk.incubator.sql2.Result.RowColumn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import reactor.core.publisher.Mono;

@RestController
public class GreetingHandler {

	private DataSource dataSource;

  public GreetingHandler(DataSource dataSource) {
	  this.dataSource = dataSource;
  }

  private User toUser(RowColumn rc) {
	  User u = new User();
	  u.setEmail(rc.at(1).get(String.class));
	  return u;
  }

  @GetMapping("/email")
  public Mono<User> findByEmail() {
    try (final var session = dataSource.getSession()) {
      final var future = session.<List<RowColumn>>rowOperation("select * from users where email = $1")
          .set("$1", "test", AdbaType.VARCHAR)
          .collect(Collectors.toList())
          .submit()
          .getCompletionStage()
          .thenApply(rc -> toUser(rc.get(0)))
          .toCompletableFuture();
      return Mono.fromFuture(future);
    }
  }
}
