package io.users.admin.domain;

import com.google.protobuf.Empty;
import io.users.admin.api.AdminApi;
import kalix.javasdk.testkit.ValueEntityResult;
import kalix.javasdk.valueentity.ValueEntity;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

// This class was initially generated based on the .proto definition by Kalix tooling.
//
// As long as this file exists it will not be overwritten: you can maintain it yourself,
// or delete it so it is regenerated as needed.

public class AdminTest {

  @Test
  @Ignore("to be implemented")
  public void exampleTest() {
    AdminTestKit service = AdminTestKit.of(Admin::new);
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
  public void createAdminTest() {
    AdminTestKit service = AdminTestKit.of(Admin::new);
    // Admin command = Admin.newBuilder()...build();
    // ValueEntityResult<Empty> result = service.createAdmin(command);
  }


  @Test
  @Ignore("to be implemented")
  public void getAdminTest() {
    AdminTestKit service = AdminTestKit.of(Admin::new);
    // GetAdminRequest command = GetAdminRequest.newBuilder()...build();
    // ValueEntityResult<Admin> result = service.getAdmin(command);
  }

}
