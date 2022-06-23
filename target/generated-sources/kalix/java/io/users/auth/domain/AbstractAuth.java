package io.users.auth.domain;

import com.google.protobuf.Empty;
import io.users.Components;
import io.users.ComponentsImpl;
import io.users.auth.action.AuthActionApi;
import io.users.auth.api.AuthApi;
import kalix.javasdk.valueentity.ValueEntity;

// This code is managed by Kalix tooling.
// It will be re-generated to reflect any changes to your protobuf definitions.
// DO NOT EDIT

public abstract class AbstractAuth extends ValueEntity<AuthDomain.AuthState> {

  protected final Components components() {
    return new ComponentsImpl(commandContext());
  }

  public abstract Effect<Empty> register(AuthDomain.AuthState currentState, AuthActionApi.Auth auth);

  public abstract Effect<AuthActionApi.Auth> login(AuthDomain.AuthState currentState, AuthApi.GetLoginRequest getLoginRequest);

}
