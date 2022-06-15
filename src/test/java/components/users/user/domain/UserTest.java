package components.users.user.domain;

import com.google.protobuf.Empty;
import components.users.user.api.UserApi;
import kalix.javasdk.testkit.ValueEntityResult;
import kalix.javasdk.valueentity.ValueEntity;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

// This class was initially generated based on the .proto definition by Kalix tooling.
//
// As long as this file exists it will not be overwritten: you can maintain it yourself,
// or delete it so it is regenerated as needed.

public class UserTest {

  @Test
  @Ignore("to be implemented")
  public void exampleTest() {
    UserTestKit service = UserTestKit.of(User::new);
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
  public void createUserTest() {
    UserTestKit service = UserTestKit.of(User::new);
    // User command = User.newBuilder()...build();
    // ValueEntityResult<Empty> result = service.createUser(command);
  }


  @Test
  @Ignore("to be implemented")
  public void getUserTest() {
    UserTestKit service = UserTestKit.of(User::new);
    // GetUserRequest command = GetUserRequest.newBuilder()...build();
    // ValueEntityResult<User> result = service.getUser(command);
  }

}
