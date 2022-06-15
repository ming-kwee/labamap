package components.users.user.domain;

import com.google.protobuf.Empty;
import components.users.Components;
import components.users.ComponentsImpl;
import components.users.user.api.UserApi;
import kalix.javasdk.valueentity.ValueEntity;

// This code is managed by Kalix tooling.
// It will be re-generated to reflect any changes to your protobuf definitions.
// DO NOT EDIT

public abstract class AbstractUser extends ValueEntity<UserDomain.UserState> {

  protected final Components components() {
    return new ComponentsImpl(commandContext());
  }

  public abstract Effect<Empty> createUser(UserDomain.UserState currentState, UserApi.User user);

  public abstract Effect<UserApi.User> getUser(UserDomain.UserState currentState, UserApi.GetUserRequest getUserRequest);

}
