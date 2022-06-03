package user.domain;

import com.google.protobuf.Descriptors;
import com.google.protobuf.Empty;
import com.google.protobuf.EmptyProto;
import kalix.javasdk.valueentity.ValueEntityContext;
import kalix.javasdk.valueentity.ValueEntityOptions;
import kalix.javasdk.valueentity.ValueEntityProvider;
import user.api.UserApi;

import java.util.function.Function;

// This code is managed by Kalix tooling.
// It will be re-generated to reflect any changes to your protobuf definitions.
// DO NOT EDIT

/**
 * A value entity provider that defines how to register and create the entity for
 * the Protobuf service <code>UserService</code>.
 *
 * Should be used with the <code>register</code> method in {@link kalix.javasdk.Kalix}.
 */
public class UserProvider implements ValueEntityProvider<UserDomain.UserState, User> {

  private final Function<ValueEntityContext, User> entityFactory;
  private final ValueEntityOptions options;

  /** Factory method of UserProvider */
  public static UserProvider of(Function<ValueEntityContext, User> entityFactory) {
    return new UserProvider(entityFactory, ValueEntityOptions.defaults());
  }

  private UserProvider(
      Function<ValueEntityContext, User> entityFactory,
      ValueEntityOptions options) {
    this.entityFactory = entityFactory;
    this.options = options;
  }

  @Override
  public final ValueEntityOptions options() {
    return options;
  }

  public final UserProvider withOptions(ValueEntityOptions options) {
    return new UserProvider(entityFactory, options);
  }

  @Override
  public final Descriptors.ServiceDescriptor serviceDescriptor() {
    return UserApi.getDescriptor().findServiceByName("UserService");
  }

  @Override
  public final String entityType() {
    return "users";
  }

  @Override
  public final UserRouter newRouter(ValueEntityContext context) {
    return new UserRouter(entityFactory.apply(context));
  }

  @Override
  public final Descriptors.FileDescriptor[] additionalDescriptors() {
    return new Descriptors.FileDescriptor[] {
      EmptyProto.getDescriptor(),
      UserApi.getDescriptor(),
      UserDomain.getDescriptor()
    };
  }
}
