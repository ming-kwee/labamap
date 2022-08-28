package io.users.auth.action;

import akka.stream.javadsl.Source;
import io.users.auth.action.AuthActionApi;
import io.users.auth.action.AuthActionImpl;
import io.users.auth.action.AuthActionImplTestKit;
import kalix.javasdk.testkit.ActionResult;
import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.*;

// This class was initially generated based on the .proto definition by Kalix tooling.
//
// As long as this file exists it will not be overwritten: you can maintain it yourself,
// or delete it so it is regenerated as needed.

public class AuthActionImplTest {

  @Test
  @Ignore("to be implemented")
  public void exampleTest() {
    AuthActionImplTestKit service = AuthActionImplTestKit.of(AuthActionImpl::new);
    // // use the testkit to execute a command
    // SomeCommand command = SomeCommand.newBuilder()...build();
    // ActionResult<SomeResponse> result = service.someOperation(command);
    // // verify the reply
    // SomeReply reply = result.getReply();
    // assertEquals(expectedReply, reply);
  }

  @Test
  @Ignore("to be implemented")
  public void registerTest() {
    AuthActionImplTestKit testKit = AuthActionImplTestKit.of(AuthActionImpl::new);
    // ActionResult<AuthActionApi.Auth> result = testKit.register(AuthActionApi.Auth.newBuilder()...build());
  }

  @Test
  @Ignore("to be implemented")
  public void updateUserTest() {
    AuthActionImplTestKit testKit = AuthActionImplTestKit.of(AuthActionImpl::new);
    // ActionResult<AuthActionApi.Auth> result = testKit.updateUser(AuthActionApi.Auth.newBuilder()...build());
  }

}
