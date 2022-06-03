package user;

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
  public Components.UserCalls user() {
    return new UserCallsImpl();
  }

  private final class UserCallsImpl implements Components.UserCalls {
     @Override
    public DeferredCall<user.api.UserApi.User, com.google.protobuf.Empty> register(user.api.UserApi.User user) {
      return new DeferredCallImpl<>(
        user,
        MetadataImpl.Empty(),
        "user.api.UserService",
        "Register",
        () -> getGrpcClient(user.api.UserService.class).register(user)
      );
    }
    @Override
    public DeferredCall<user.api.UserApi.GetLoginRequest, user.api.UserApi.User> login(user.api.UserApi.GetLoginRequest getLoginRequest) {
      return new DeferredCallImpl<>(
        getLoginRequest,
        MetadataImpl.Empty(),
        "user.api.UserService",
        "Login",
        () -> getGrpcClient(user.api.UserService.class).login(getLoginRequest)
      );
    }
  }
}
