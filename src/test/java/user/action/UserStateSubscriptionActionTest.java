package user.action;

import akka.stream.javadsl.Source;
import kalix.javasdk.testkit.ActionResult;
import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.*;
import user.action.UserStateSubscriptionAction;
import user.action.UserStateSubscriptionActionTestKit;
import user.api.UserApi;
import user.domain.UserDomain;

// This class was initially generated based on the .proto definition by Kalix tooling.
//
// As long as this file exists it will not be overwritten: you can maintain it yourself,
// or delete it so it is regenerated as needed.

public class UserStateSubscriptionActionTest {

  @Test
  @Ignore("to be implemented")
  public void exampleTest() {
    UserStateSubscriptionActionTestKit service = UserStateSubscriptionActionTestKit.of(UserStateSubscriptionAction::new);
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
    UserStateSubscriptionActionTestKit testKit = UserStateSubscriptionActionTestKit.of(UserStateSubscriptionAction::new);
    // ActionResult<UserApi.User> result = testKit.onStateChange(UserDomain.UserState.newBuilder()...build());
  }

}
