package io.users.patient.action;

import akka.stream.javadsl.Source;
import io.users.patient.action.PatientStateSubscriptionAction;
import io.users.patient.action.PatientStateSubscriptionActionTestKit;
import io.users.patient.api.PatientApi;
import io.users.patient.domain.PatientDomain;
import kalix.javasdk.testkit.ActionResult;
import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.*;

// This class was initially generated based on the .proto definition by Kalix tooling.
//
// As long as this file exists it will not be overwritten: you can maintain it yourself,
// or delete it so it is regenerated as needed.

public class PatientStateSubscriptionActionTest {

  @Test
  @Ignore("to be implemented")
  public void exampleTest() {
    PatientStateSubscriptionActionTestKit service = PatientStateSubscriptionActionTestKit.of(PatientStateSubscriptionAction::new);
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
    PatientStateSubscriptionActionTestKit testKit = PatientStateSubscriptionActionTestKit.of(PatientStateSubscriptionAction::new);
    // ActionResult<PatientApi.Patient> result = testKit.onStateChange(PatientDomain.PatientState.newBuilder()...build());
  }

}
