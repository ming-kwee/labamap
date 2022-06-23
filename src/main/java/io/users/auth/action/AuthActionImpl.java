package io.users.auth.action;

import java.util.concurrent.CompletionStage;
// import io.users.auth.domain.Auth;
// This class was initially generated based on the .proto definition by Kalix tooling.
// This is the implementation for the Action Service described in your io/users/auth/action/auth_action.proto file.
//
// As long as this file exists it will not be overwritten: you can maintain it yourself,
// or delete it so it is regenerated as needed.

import com.google.protobuf.Empty;
import io.users.user.api.UserApi;
import kalix.javasdk.action.ActionCreationContext;
import java.util.UUID;

// This class was initially generated based on the .proto definition by Kalix tooling.
// This is the implementation for the Action Service described in your io/users/auth/action/auth_action.proto file.
//
// As long as this file exists it will not be overwritten: you can maintain it yourself,
// or delete it so it is regenerated as needed.

public class AuthActionImpl extends AbstractAuthAction {

  public AuthActionImpl(ActionCreationContext creationContext) {
  }

  @Override
  public Effect<AuthActionApi.Auth> register(AuthActionApi.Auth auth) {
    String id = UUID.randomUUID().toString();

    CompletionStage<Empty> authCreated = components().auth().register(AuthActionApi.Auth.newBuilder()
        .setId(id)
        .setEmail(auth.getEmail())
        .setFirstName(auth.getFirstName())
        .setLastName(auth.getLastName())
        .setRole(auth.getRole())
        .setPassword(auth.getPassword()).build())
        .execute();

    CompletionStage<Empty> userCreated = authCreated.thenCompose(empty -> {
      return components().user().createUser(UserApi.User.newBuilder()
          .setId(id)
          .setEmail(auth.getEmail())
          .setFirstName(auth.getFirstName())
          .setLastName(auth.getLastName())
          .setRole(auth.getRole())
          .setPassword(auth.getPassword()).build())
          .execute();
    });
    // DeferredCall<AuthActionApi.Auth, Empty> call =
    // components().auth().register(auth);
    // return effects().forward(call);

    CompletionStage<AuthActionApi.Auth> reply = userCreated
        .thenApply(empty -> AuthActionApi.Auth.newBuilder()
            .setEmail(auth.getEmail()).build());

    return effects().asyncReply(reply);

  }
}
