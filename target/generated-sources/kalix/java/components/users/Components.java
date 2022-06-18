package components.users;

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
    DeferredCall<components.users.auth.action.AuthActionApi.Auth, components.users.auth.action.AuthActionApi.Auth> register(components.users.auth.action.AuthActionApi.Auth auth);
  }
  interface AuthCalls {
    DeferredCall<components.users.auth.action.AuthActionApi.Auth, com.google.protobuf.Empty> register(components.users.auth.action.AuthActionApi.Auth auth);

    DeferredCall<components.users.auth.api.AuthApi.GetLoginRequest, components.users.auth.action.AuthActionApi.Auth> login(components.users.auth.api.AuthApi.GetLoginRequest getLoginRequest);
  }
  interface UserCalls {
    DeferredCall<components.users.user.api.UserApi.User, com.google.protobuf.Empty> createUser(components.users.user.api.UserApi.User user);

    DeferredCall<components.users.user.api.UserApi.GetUserRequest, components.users.user.api.UserApi.User> getUser(components.users.user.api.UserApi.GetUserRequest getUserRequest);
  }
  interface UserStateSubscriptionActionCalls {
    DeferredCall<components.users.user.domain.UserDomain.UserState, components.users.user.api.UserApi.User> onStateChange(components.users.user.domain.UserDomain.UserState userState);
  }
}
