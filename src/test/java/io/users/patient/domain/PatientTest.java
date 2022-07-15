package io.users.patient.domain;

import com.google.protobuf.Empty;
import io.users.patient.api.PatientApi;
import kalix.javasdk.testkit.ValueEntityResult;
import kalix.javasdk.valueentity.ValueEntity;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

// This class was initially generated based on the .proto definition by Kalix tooling.
//
// As long as this file exists it will not be overwritten: you can maintain it yourself,
// or delete it so it is regenerated as needed.

public class PatientTest {

  @Test
  @Ignore("to be implemented")
  public void exampleTest() {
    PatientTestKit service = PatientTestKit.of(Patient::new);
    // // use the testkit to execute a command
    // // of events emitted, or a final updated state:
    // SomeCommand command = SomeCommand.newBuilder()...build();
    // ValueEntityResult<SomeResponse> result = service.someOperation(command);
    // // verify the reply
    // SomeReply reply = result.getReply();
    // assertEquals(expectedReply, reply);
    // // verify the final state after the command
    // assertEquals(expectedState, service.getState());
  }

  @Test
  @Ignore("to be implemented")
  public void createPatientTest() {
    PatientTestKit service = PatientTestKit.of(Patient::new);
    // Patient command = Patient.newBuilder()...build();
    // ValueEntityResult<Empty> result = service.createPatient(command);
  }


  @Test
  @Ignore("to be implemented")
  public void getPatientTest() {
    PatientTestKit service = PatientTestKit.of(Patient::new);
    // GetPatientRequest command = GetPatientRequest.newBuilder()...build();
    // ValueEntityResult<Patient> result = service.getPatient(command);
  }

}
