package io.users;

import kalix.javasdk.Context;
import kalix.javasdk.DeferredCall;
import kalix.javasdk.impl.DeferredCallImpl;
import kalix.javasdk.impl.InternalContext;
import kalix.javasdk.impl.MetadataImpl;

// This code is managed by Kalix tooling.
// It will be re-generated to reflect any changes to your protobuf definitions.
// DO NOT EDIT

/**
 * Not intended for direct instantiation, called by generated code, use Action.components() to access
 */
public final class ComponentsImpl implements Components {

  private final InternalContext context;

  public ComponentsImpl(Context context) {
    this.context = (InternalContext) context;
  }

  private <T> T getGrpcClient(Class<T> serviceClass) {
    return context.getComponentGrpcClient(serviceClass);
  }

  @Override
  public Components.AuthActionImplCalls authActionImpl() {
    return new AuthActionImplCallsImpl();
  }
  @Override
  public Components.AuthCalls auth() {
    return new AuthCallsImpl();
  }
  @Override
  public Components.UserCalls user() {
    return new UserCallsImpl();
  }
  @Override
  public Components.UserStateSubscriptionActionCalls userStateSubscriptionAction() {
    return new UserStateSubscriptionActionCallsImpl();
  }

  private final class AuthActionImplCallsImpl implements Components.AuthActionImplCalls {
     @Override
    public DeferredCall<io.users.auth.action.AuthActionApi.Auth, io.users.auth.action.AuthActionApi.Auth> register(io.users.auth.action.AuthActionApi.Auth auth) {
      return new DeferredCallImpl<>(
        auth,
        MetadataImpl.Empty(),
        "io.users.auth.action.AuthAction",
        "Register",
        () -> getGrpcClient(io.users.auth.action.AuthAction.class).register(auth)
      );
    }
  }
  private final class AuthCallsImpl implements Components.AuthCalls {
     @Override
    public DeferredCall<io.users.auth.action.AuthActionApi.Auth, com.google.protobuf.Empty> register(io.users.auth.action.AuthActionApi.Auth auth) {
      return new DeferredCallImpl<>(
        auth,
        MetadataImpl.Empty(),
        "io.users.auth.api.AuthService",
        "Register",
        () -> getGrpcClient(io.users.auth.api.AuthService.class).register(auth)
      );
    }
    @Override
    public DeferredCall<io.users.auth.api.AuthApi.GetLoginRequest, io.users.auth.action.AuthActionApi.Auth> login(io.users.auth.api.AuthApi.GetLoginRequest getLoginRequest) {
      return new DeferredCallImpl<>(
        getLoginRequest,
        MetadataImpl.Empty(),
        "io.users.auth.api.AuthService",
        "Login",
        () -> getGrpcClient(io.users.auth.api.AuthService.class).login(getLoginRequest)
      );
    }
  }
  private final class UserCallsImpl implements Components.UserCalls {
     @Override
    public DeferredCall<io.users.user.api.UserApi.User, com.google.protobuf.Empty> createUser(io.users.user.api.UserApi.User user) {
      return new DeferredCallImpl<>(
        user,
        MetadataImpl.Empty(),
        "io.users.user.api.UserService",
        "CreateUser",
        () -> getGrpcClient(io.users.user.api.UserService.class).createUser(user)
      );
    }
    @Override
    public DeferredCall<io.users.user.api.UserApi.GetUserRequest, io.users.user.api.UserApi.User> getUser(io.users.user.api.UserApi.GetUserRequest getUserRequest) {
      return new DeferredCallImpl<>(
        getUserRequest,
        MetadataImpl.Empty(),
        "io.users.user.api.UserService",
        "GetUser",
        () -> getGrpcClient(io.users.user.api.UserService.class).getUser(getUserRequest)
      );
    }
  }
  private final class UserStateSubscriptionActionCallsImpl implements Components.UserStateSubscriptionActionCalls {
     @Override
    public DeferredCall<io.users.user.domain.UserDomain.UserState, io.users.user.api.UserApi.User> onStateChange(io.users.user.domain.UserDomain.UserState userState) {
      return new DeferredCallImpl<>(
        userState,
        MetadataImpl.Empty(),
        "io.users.user.action.UserStateSubscription",
        "OnStateChange",
        () -> getGrpcClient(io.users.user.action.UserStateSubscription.class).onStateChange(userState)
      );
    }
  }
}
