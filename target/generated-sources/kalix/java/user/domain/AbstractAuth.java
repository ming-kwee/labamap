package user.domain;

import com.google.protobuf.Empty;
import kalix.javasdk.valueentity.ValueEntity;
import user.Components;
import user.ComponentsImpl;
import user.api.AuthApi;

// This code is managed by Kalix tooling.
// It will be re-generated to reflect any changes to your protobuf definitions.
// DO NOT EDIT

public abstract class AbstractAuth extends ValueEntity<AuthDomain.AuthState> {

  protected final Components components() {
    return new ComponentsImpl(commandContext());
  }

  public abstract Effect<Empty> register(AuthDomain.AuthState currentState, AuthApi.Auth auth);

  public abstract Effect<AuthApi.Auth> login(AuthDomain.AuthState currentState, AuthApi.GetLoginRequest getLoginRequest);

}
