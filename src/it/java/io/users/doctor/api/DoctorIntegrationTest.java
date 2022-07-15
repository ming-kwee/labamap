package io.users.doctor.api;

import com.google.protobuf.Empty;
import io.users.Main;
import io.users.doctor.domain.DoctorDomain;
import kalix.javasdk.testkit.junit.KalixTestKitResource;
import org.junit.ClassRule;
import org.junit.Test;

import static java.util.concurrent.TimeUnit.*;

// This class was initially generated based on the .proto definition by Kalix tooling.
//
// As long as this file exists it will not be overwritten: you can maintain it yourself,
// or delete it so it is regenerated as needed.

// Example of an integration test calling our service via the Kalix proxy
// Run all test classes ending with "IntegrationTest" using `mvn verify -Pit`
public class DoctorIntegrationTest {

  /**
   * The test kit starts both the service container and the Kalix proxy.
   */
  @ClassRule
  public static final KalixTestKitResource testKit =
    new KalixTestKitResource(Main.createKalix());

  /**
   * Use the generated gRPC client to call the service through the Kalix proxy.
   */
  private final DoctorService client;

  public DoctorIntegrationTest() {
    client = testKit.getGrpcClient(DoctorService.class);
  }

  @Test
  public void createDoctorOnNonExistingEntity() throws Exception {
    // TODO: set fields in command, and provide assertions to match replies
    // client.createDoctor(DoctorApi.Doctor.newBuilder().build())
    //         .toCompletableFuture().get(5, SECONDS);
  }

  @Test
  public void getDoctorOnNonExistingEntity() throws Exception {
    // TODO: set fields in command, and provide assertions to match replies
    // client.getDoctor(DoctorApi.GetDoctorRequest.newBuilder().build())
    //         .toCompletableFuture().get(5, SECONDS);
  }
}
