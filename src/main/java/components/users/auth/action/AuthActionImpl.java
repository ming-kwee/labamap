package components.users.auth.action;

import java.util.concurrent.CompletionStage;
// import components.users.auth.domain.Auth;
// This class was initially generated based on the .proto definition by Kalix tooling.
// This is the implementation for the Action Service described in your components/users/auth/action/auth_action.proto file.
//
// As long as this file exists it will not be overwritten: you can maintain it yourself,
// or delete it so it is regenerated as needed.

import com.google.protobuf.Empty;
import components.users.user.api.UserApi;
import kalix.javasdk.action.ActionCreationContext;

// This class was initially generated based on the .proto definition by Kalix tooling.
// This is the implementation for the Action Service described in your components/users/auth/action/auth_action.proto file.
//
// As long as this file exists it will not be overwritten: you can maintain it yourself,
// or delete it so it is regenerated as needed.

public class AuthActionImpl extends AbstractAuthAction {

  public AuthActionImpl(ActionCreationContext creationContext) {}

  @Override
  public Effect<AuthActionApi.Auth> register(AuthActionApi.Auth auth) {


    CompletionStage<Empty> userCreated = components().user().createUser(UserApi.User.newBuilder()
        .setId(auth.getId())
        .setEmail(auth.getEmail())
        .setFirstName(auth.getFirstName())
        .setLastName(auth.getLastName())
        .setRole(auth.getRole())
        .setImg("")
        .setPassword("").build())
        .execute();

    CompletionStage<Empty> authCreated = userCreated.thenCompose(empty -> {
      return components().auth().register(auth).execute();
    });
    // DeferredCall<AuthActionApi.Auth, Empty> call =
    // components().auth().register(auth);
    // return effects().forward(call);

    CompletionStage<AuthActionApi.Auth> reply = authCreated
        .thenApply(empty -> AuthActionApi.Auth.newBuilder()
        .setEmail(auth.getEmail()).build());

    return effects().asyncReply(reply);

  }
}
