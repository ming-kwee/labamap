package io.users;

import kalix.javasdk.DeferredCall;

// This code is managed by Kalix tooling.
// It will be re-generated to reflect any changes to your protobuf definitions.
// DO NOT EDIT

/**
 * Not intended for user extension, provided through generated implementation
 */
public interface Components {
  AuthActionImplCalls authActionImpl();
  AuthCalls auth();
  UserCalls user();
  UserStateSubscriptionActionCalls userStateSubscriptionAction();

  interface AuthActionImplCalls {
    DeferredCall<io.users.auth.action.AuthActionApi.Auth, io.users.auth.action.AuthActionApi.Auth> register(io.users.auth.action.AuthActionApi.Auth auth);
  }
  interface AuthCalls {
    DeferredCall<io.users.auth.action.AuthActionApi.Auth, com.google.protobuf.Empty> register(io.users.auth.action.AuthActionApi.Auth auth);

    DeferredCall<io.users.auth.api.AuthApi.GetLoginRequest, io.users.auth.action.AuthActionApi.Auth> login(io.users.auth.api.AuthApi.GetLoginRequest getLoginRequest);
  }
  interface UserCalls {
    DeferredCall<io.users.user.api.UserApi.User, com.google.protobuf.Empty> createUser(io.users.user.api.UserApi.User user);

    DeferredCall<io.users.user.api.UserApi.GetUserRequest, io.users.user.api.UserApi.User> getUser(io.users.user.api.UserApi.GetUserRequest getUserRequest);
  }
  interface UserStateSubscriptionActionCalls {
    DeferredCall<io.users.user.domain.UserDomain.UserState, io.users.user.api.UserApi.User> onStateChange(io.users.user.domain.UserDomain.UserState userState);
  }
}
