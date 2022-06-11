package user.domain;

import com.google.protobuf.Empty;
import kalix.javasdk.impl.valueentity.ValueEntityRouter;
import kalix.javasdk.valueentity.CommandContext;
import kalix.javasdk.valueentity.ValueEntity;
import user.api.AuthApi;

// This code is managed by Kalix tooling.
// It will be re-generated to reflect any changes to your protobuf definitions.
// DO NOT EDIT

/**
 * A value entity handler that is the glue between the Protobuf service <code>AuthService</code>
 * and the command handler methods in the <code>Auth</code> class.
 */
public class AuthRouter extends ValueEntityRouter<AuthDomain.AuthState, Auth> {

  public AuthRouter(Auth entity) {
    super(entity);
  }

  @Override
  public ValueEntity.Effect<?> handleCommand(
      String commandName, AuthDomain.AuthState state, Object command, CommandContext context) {
    switch (commandName) {

      case "Register":
        return entity().register(state, (AuthApi.Auth) command);

      case "Login":
        return entity().login(state, (AuthApi.GetLoginRequest) command);

      default:
        throw new ValueEntityRouter.CommandHandlerNotFound(commandName);
    }
  }
}
