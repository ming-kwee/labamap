package user;

import kalix.javasdk.Kalix;
import kalix.javasdk.valueentity.ValueEntityContext;
import user.api.UserApi;
import user.domain.User;
import user.domain.UserProvider;

import java.util.function.Function;

// This code is managed by Kalix tooling.
// It will be re-generated to reflect any changes to your protobuf definitions.
// DO NOT EDIT

public final class KalixFactory {

  public static Kalix withComponents(
      Function<ValueEntityContext, User> createUser) {
    Kalix kalix = new Kalix();
    return kalix
      .register(UserProvider.of(createUser));
  }
}
