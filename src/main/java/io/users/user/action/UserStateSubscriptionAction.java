package io.users.user.action;

import io.users.user.api.UserApi;
import io.users.user.domain.UserDomain;
import kalix.javasdk.action.ActionCreationContext;

// This class was initially generated based on the .proto definition by Kalix tooling.
// This is the implementation for the Action Service described in your io/users/user/action/user_action.proto file.
//
// As long as this file exists it will not be overwritten: you can maintain it yourself,
// or delete it so it is regenerated as needed.

public class UserStateSubscriptionAction extends AbstractUserStateSubscriptionAction {

  public UserStateSubscriptionAction(ActionCreationContext creationContext) {
  }

  @Override
  public Effect<UserApi.User> onStateChange(UserDomain.UserState userState) {
    UserApi.User user = UserApi.User.newBuilder()
        .setId(userState.getId())
        .setEmail(userState.getEmail())
        .setImg(userState.getImg())
        // .setPassword(userState.getPassword())
        .setFirstName(userState.getFirstName())
        .setLastName(userState.getLastName())
        .setRole(userState.getRole())
        .build();

    return effects().reply(user);
  }
}
