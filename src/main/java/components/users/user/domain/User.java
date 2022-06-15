package components.users.user.domain;

import com.google.protobuf.Empty;
import components.users.user.api.UserApi;
import kalix.javasdk.valueentity.ValueEntityContext;
import java.util.Optional;

// This class was initially generated based on the .proto definition by Kalix tooling.
// This is the implementation for the Value Entity Service described in your components/users/user/api/user_api.proto file.
//
// As long as this file exists it will not be overwritten: you can maintain it yourself,
// or delete it so it is regenerated as needed.

public class User extends AbstractUser {
  @SuppressWarnings("unused")
  private final String entityId;

  public User(ValueEntityContext context) {
    this.entityId = context.entityId();
  }

  @Override
  public UserDomain.UserState emptyState() {
    return UserDomain.UserState.getDefaultInstance();
  }

  @Override
  public Effect<Empty> createUser(
      UserDomain.UserState currentState, UserApi.User command) {
    UserDomain.UserState state = convertToDomain(command);
    return reject(currentState, command).orElseGet(() -> handle(state, command));
  }

  private Optional<Effect<Empty>> reject(UserDomain.UserState currentState, UserApi.User command) {
    // if (currentState.getEmail().equals(command.getEmail())) {
    // return Optional.of(effects().error("Email is already exists!"));
    // }
    return Optional.empty();
  }

  private Effect<Empty> handle(UserDomain.UserState state, UserApi.User command) {
    return effects().updateState(state).thenReply(Empty.getDefaultInstance());
  }

  private UserDomain.UserState convertToDomain(UserApi.User user) {
    return UserDomain.UserState.newBuilder()
        .setId(user.getId())
        .setImg(user.getImg())
        .setEmail(user.getEmail())
        .setPassword(user.getPassword())
        .setFirstName(user.getFirstName())
        .setLastName(user.getLastName())
        .setRole(user.getRole())
        .build();
  }

  @Override
  public Effect<UserApi.User> getUser(UserDomain.UserState currentState, UserApi.GetUserRequest command) {
    if (currentState.getEmail().equals(command.getEmail())) {
      return effects().reply(convertToApi(currentState));
    } else {
      return effects().error("User " + command.getEmail() + " has not been created.");
    }
  }

  private UserApi.User convertToApi(UserDomain.UserState state) {
    return UserApi.User.newBuilder()
        .setId(state.getId())
        .setEmail(state.getEmail())
        .setImg(state.getImg())
        // .setPassword(state.getPassword())
        .setFirstName(state.getFirstName())
        .setLastName(state.getLastName())
        .setRole(state.getRole())
        .build();
  }
}
