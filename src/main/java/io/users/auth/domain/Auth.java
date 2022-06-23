package io.users.auth.domain;

import com.google.protobuf.Empty;
import io.users.auth.action.AuthActionApi;
import io.users.auth.api.AuthApi;
import kalix.javasdk.valueentity.ValueEntityContext;
import java.util.Optional;
// This class was initially generated based on the .proto definition by Kalix tooling.
// This is the implementation for the Value Entity Service described in your io/users/auth/api/auth_api.proto file.
//
// As long as this file exists it will not be overwritten: you can maintain it yourself,
// or delete it so it is regenerated as needed.

public class Auth extends AbstractAuth {
  @SuppressWarnings("unused")
  private final String entityId;

  public Auth(ValueEntityContext context) {
    this.entityId = context.entityId();
  }


  @Override
  public AuthDomain.AuthState emptyState() {
    return AuthDomain.AuthState.getDefaultInstance();
  }

  @Override
  public Effect<Empty> register(
      AuthDomain.AuthState currentState, AuthActionApi.Auth command) {
    AuthDomain.AuthState state = convertToDomain(command);
    return reject(currentState, command).orElseGet(() -> handle(state, command));
  }


  private Optional<Effect<Empty>> reject(AuthDomain.AuthState currentState, AuthActionApi.Auth command) {
    if (currentState.getEmail().equals(command.getEmail())) {
      return Optional.of(effects().error("Email is already exists!"));
    }
    return Optional.empty();
  }
  
  private Effect<Empty> handle(AuthDomain.AuthState state, AuthActionApi.Auth command) {
    return effects().updateState(state).thenReply(Empty.getDefaultInstance());
  }

  private AuthDomain.AuthState convertToDomain(AuthActionApi.Auth user) {
    return AuthDomain.AuthState.newBuilder()
        .setId(user.getId())
        .setEmail(user.getEmail())
        .setPassword(user.getPassword())
        .setFirstName(user.getFirstName())
        .setLastName(user.getLastName())
        .setRole(user.getRole())
        .build();
  }


  @Override
  public Effect<AuthActionApi.Auth> login(
      AuthDomain.AuthState currentState,
      AuthApi.GetLoginRequest command) {
    if (currentState.getEmail().equals(command.getEmail())
        && currentState.getPassword().equals(command.getPassword())) {
      return effects().reply(convertToApi(currentState));
    } else {
      return effects().error("User " + command.getEmail() + " has not been created.");
    }
  }


  private AuthActionApi.Auth convertToApi(AuthDomain.AuthState state) {
    return AuthActionApi.Auth.newBuilder()
        .setId(state.getId())
        .setEmail(state.getEmail())
        // .setPassword(state.getPassword())
        .setFirstName(state.getFirstName())
        .setLastName(state.getLastName())
        .setRole(state.getRole())
        .build();
  }

}
