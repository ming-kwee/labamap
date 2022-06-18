package components.users.auth.domain;

import com.google.protobuf.Empty;
import components.users.auth.api.AuthApi;
import kalix.javasdk.testkit.ValueEntityResult;
import kalix.javasdk.valueentity.ValueEntity;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

// This class was initially generated based on the .proto definition by Kalix tooling.
//
// As long as this file exists it will not be overwritten: you can maintain it yourself,
// or delete it so it is regenerated as needed.

public class AuthTest {

  @Test
  @Ignore("to be implemented")
  public void exampleTest() {
    AuthTestKit service = AuthTestKit.of(Auth::new);
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
  public void loginTest() {
    AuthTestKit service = AuthTestKit.of(Auth::new);
    // GetLoginRequest command = GetLoginRequest.newBuilder()...build();
    // ValueEntityResult<Auth> result = service.login(command);
  }

}
