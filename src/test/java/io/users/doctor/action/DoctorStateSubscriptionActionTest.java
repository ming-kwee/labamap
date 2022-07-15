package io.users.doctor.action;

import akka.stream.javadsl.Source;
import io.users.doctor.action.DoctorStateSubscriptionAction;
import io.users.doctor.action.DoctorStateSubscriptionActionTestKit;
import io.users.doctor.api.DoctorApi;
import io.users.doctor.domain.DoctorDomain;
import kalix.javasdk.testkit.ActionResult;
import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.*;

// This class was initially generated based on the .proto definition by Kalix tooling.
//
// As long as this file exists it will not be overwritten: you can maintain it yourself,
// or delete it so it is regenerated as needed.

public class DoctorStateSubscriptionActionTest {

  @Test
  @Ignore("to be implemented")
  public void exampleTest() {
    DoctorStateSubscriptionActionTestKit service = DoctorStateSubscriptionActionTestKit.of(DoctorStateSubscriptionAction::new);
    // // use the testkit to execute a command
    // SomeCommand command = SomeCommand.newBuilder()...build();
    // ActionResult<SomeResponse> result = service.someOperation(command);
    // // verify the reply
    // SomeReply reply = result.getReply();
    // assertEquals(expectedReply, reply);
  }

  @Test
  @Ignore("to be implemented")
  public void onStateChangeTest() {
    DoctorStateSubscriptionActionTestKit testKit = DoctorStateSubscriptionActionTestKit.of(DoctorStateSubscriptionAction::new);
    // ActionResult<DoctorApi.Doctor> result = testKit.onStateChange(DoctorDomain.DoctorState.newBuilder()...build());
  }

}
