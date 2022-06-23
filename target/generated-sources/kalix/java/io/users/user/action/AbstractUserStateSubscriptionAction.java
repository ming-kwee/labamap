package io.users.user.action;

import io.users.Components;
import io.users.ComponentsImpl;
import io.users.user.api.UserApi;
import io.users.user.domain.UserDomain;

// This code is managed by Kalix tooling.
// It will be re-generated to reflect any changes to your protobuf definitions.
// DO NOT EDIT

public abstract class AbstractUserStateSubscriptionAction extends kalix.javasdk.action.Action {

  protected final Components components() {
    return new ComponentsImpl(contextForComponents());
  }

  public abstract Effect<UserApi.User> onStateChange(UserDomain.UserState userState);
}