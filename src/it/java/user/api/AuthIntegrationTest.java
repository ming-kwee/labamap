package user.api;

import com.google.protobuf.Empty;
import kalix.javasdk.testkit.junit.KalixTestKitResource;
import org.junit.ClassRule;
import org.junit.Test;
import user.Main;
import user.domain.AuthDomain;

import static java.util.concurrent.TimeUnit.*;

// This class was initially generated based on the .proto definition by Kalix tooling.
//
// As long as this file exists it will not be overwritten: you can maintain it yourself,
// or delete it so it is regenerated as needed.

// Example of an integration test calling our service via the Kalix proxy
// Run all test classes ending with "IntegrationTest" using `mvn verify -Pit`
public class AuthIntegrationTest {

  /**
   * The test kit starts both the service container and the Kalix proxy.
   */
  @ClassRule
  public static final KalixTestKitResource testKit =
    new KalixTestKitResource(Main.createKalix());

  /**
   * Use the generated gRPC client to call the service through the Kalix proxy.
   */
  private final AuthService client;

  public AuthIntegrationTest() {
    client = testKit.getGrpcClient(AuthService.class);
  }

  @Test
  public void registerOnNonExistingEntity() throws Exception {
    // TODO: set fields in command, and provide assertions to match replies
    // client.register(AuthApi.Auth.newBuilder().build())
    //         .toCompletableFuture().get(5, SECONDS);
  }

  @Test
  public void loginOnNonExistingEntity() throws Exception {
    // TODO: set fields in command, and provide assertions to match replies
    // client.login(AuthApi.GetLoginRequest.newBuilder().build())
    //         .toCompletableFuture().get(5, SECONDS);
  }
}
