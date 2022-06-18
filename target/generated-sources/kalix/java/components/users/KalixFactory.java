package components.users;

import components.users.auth.action.AuthActionApi;
import components.users.auth.action.AuthActionImpl;
import components.users.auth.action.AuthActionProvider;
import components.users.auth.api.AuthApi;
import components.users.auth.domain.Auth;
import components.users.auth.domain.AuthProvider;
import components.users.user.action.UserAction;
import components.users.user.action.UserStateSubscriptionAction;
import components.users.user.action.UserStateSubscriptionActionProvider;
import components.users.user.api.UserApi;
import components.users.user.domain.User;
import components.users.user.domain.UserProvider;
import kalix.javasdk.Kalix;
import kalix.javasdk.action.ActionCreationContext;
import kalix.javasdk.valueentity.ValueEntityContext;

import java.util.function.Function;

// This code is managed by Kalix tooling.
// It will be re-generated to reflect any changes to your protobuf definitions.
// DO NOT EDIT

public final class KalixFactory {

  public static Kalix withComponents(
      Function<ValueEntityContext, Auth> createAuth,
      Function<ValueEntityContext, User> createUser,
      Function<ActionCreationContext, AuthActionImpl> createAuthActionImpl,
      Function<ActionCreationContext, UserStateSubscriptionAction> createUserStateSubscriptionAction) {
    Kalix kalix = new Kalix();
    return kalix
      .register(AuthActionProvider.of(createAuthActionImpl))
      .register(AuthProvider.of(createAuth))
      .register(UserProvider.of(createUser))
      .register(UserStateSubscriptionActionProvider.of(createUserStateSubscriptionAction));
  }
}
