package user;

import kalix.javasdk.DeferredCall;

// This code is managed by Kalix tooling.
// It will be re-generated to reflect any changes to your protobuf definitions.
// DO NOT EDIT

/**
 * Not intended for user extension, provided through generated implementation
 */
public interface Components {
  UserCalls user();
  UserStateSubscriptionActionCalls userStateSubscriptionAction();
  AuthCalls auth();

  interface UserCalls {
    DeferredCall<user.api.UserApi.User, com.google.protobuf.Empty> createUser(user.api.UserApi.User user);

    DeferredCall<user.api.UserApi.GetUserRequest, user.api.UserApi.User> getUser(user.api.UserApi.GetUserRequest getUserRequest);
  }
  interface UserStateSubscriptionActionCalls {
    DeferredCall<user.domain.UserDomain.UserState, user.api.UserApi.User> onStateChange(user.domain.UserDomain.UserState userState);
  }
  interface AuthCalls {
    DeferredCall<user.api.AuthApi.Auth, com.google.protobuf.Empty> register(user.api.AuthApi.Auth auth);

    DeferredCall<user.api.AuthApi.GetLoginRequest, user.api.AuthApi.Auth> login(user.api.AuthApi.GetLoginRequest getLoginRequest);
  }
}
