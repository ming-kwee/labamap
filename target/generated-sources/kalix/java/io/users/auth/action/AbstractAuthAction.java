package io.users.auth.action;

import io.users.Components;
import io.users.ComponentsImpl;

// This code is managed by Kalix tooling.
// It will be re-generated to reflect any changes to your protobuf definitions.
// DO NOT EDIT

public abstract class AbstractAuthAction extends kalix.javasdk.action.Action {

  protected final Components components() {
    return new ComponentsImpl(contextForComponents());
  }

  public abstract Effect<AuthActionApi.Auth> register(AuthActionApi.Auth auth);
  public abstract Effect<AuthActionApi.Auth> updateUser(AuthActionApi.Auth auth);
}