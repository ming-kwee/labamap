package io.users.doctor.domain;

import com.google.protobuf.Empty;
import io.users.doctor.api.DoctorApi;
import kalix.javasdk.testkit.ValueEntityResult;
import kalix.javasdk.valueentity.ValueEntity;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

// This class was initially generated based on the .proto definition by Kalix tooling.
//
// As long as this file exists it will not be overwritten: you can maintain it yourself,
// or delete it so it is regenerated as needed.

public class DoctorTest {

  @Test
  @Ignore("to be implemented")
  public void exampleTest() {
    DoctorTestKit service = DoctorTestKit.of(Doctor::new);
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
  public void createDoctorTest() {
    DoctorTestKit service = DoctorTestKit.of(Doctor::new);
    // Doctor command = Doctor.newBuilder()...build();
    // ValueEntityResult<Empty> result = service.createDoctor(command);
  }


  @Test
  @Ignore("to be implemented")
  public void getDoctorTest() {
    DoctorTestKit service = DoctorTestKit.of(Doctor::new);
    // GetDoctorRequest command = GetDoctorRequest.newBuilder()...build();
    // ValueEntityResult<Doctor> result = service.getDoctor(command);
  }

}
