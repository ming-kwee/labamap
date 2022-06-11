package user.domain;

import com.google.protobuf.Empty;
import kalix.javasdk.impl.valueentity.ValueEntityRouter;
import kalix.javasdk.valueentity.CommandContext;
import kalix.javasdk.valueentity.ValueEntity;
import user.api.UserApi;

// This code is managed by Kalix tooling.
// It will be re-generated to reflect any changes to your protobuf definitions.
// DO NOT EDIT

/**
 * A value entity handler that is the glue between the Protobuf service <code>UserService</code>
 * and the command handler methods in the <code>User</code> class.
 */
public class UserRouter extends ValueEntityRouter<UserDomain.UserState, User> {

  public UserRouter(User entity) {
    super(entity);
  }

  @Override
  public ValueEntity.Effect<?> handleCommand(
      String commandName, UserDomain.UserState state, Object command, CommandContext context) {
    switch (commandName) {

      case "CreateUser":
        return entity().createUser(state, (UserApi.User) command);

      case "GetUser":
        return entity().getUser(state, (UserApi.GetUserRequest) command);

      default:
        throw new ValueEntityRouter.CommandHandlerNotFound(commandName);
    }
  }
}
