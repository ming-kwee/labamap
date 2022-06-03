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

  interface UserCalls {
    DeferredCall<user.api.UserApi.User, com.google.protobuf.Empty> register(user.api.UserApi.User user);

    DeferredCall<user.api.UserApi.GetLoginRequest, user.api.UserApi.User> login(user.api.UserApi.GetLoginRequest getLoginRequest);
  }
}
