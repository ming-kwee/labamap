package user;

import kalix.javasdk.Kalix;
import kalix.javasdk.action.ActionCreationContext;
import kalix.javasdk.valueentity.ValueEntityContext;
import user.action.UserAction;
import user.action.UserStateSubscriptionAction;
import user.action.UserStateSubscriptionActionProvider;
import user.api.AuthApi;
import user.api.UserApi;
import user.domain.Auth;
import user.domain.AuthProvider;
import user.domain.User;
import user.domain.UserProvider;

import java.util.function.Function;

// This code is managed by Kalix tooling.
// It will be re-generated to reflect any changes to your protobuf definitions.
// DO NOT EDIT

public final class KalixFactory {

  public static Kalix withComponents(
      Function<ValueEntityContext, Auth> createAuth,
      Function<ValueEntityContext, User> createUser,
      Function<ActionCreationContext, UserStateSubscriptionAction> createUserStateSubscriptionAction) {
    Kalix kalix = new Kalix();
    return kalix
      .register(AuthProvider.of(createAuth))
      .register(UserProvider.of(createUser))
      .register(UserStateSubscriptionActionProvider.of(createUserStateSubscriptionAction));
  }
}
