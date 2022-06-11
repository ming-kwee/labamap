package user.domain;

import com.google.protobuf.Descriptors;
import com.google.protobuf.Empty;
import com.google.protobuf.EmptyProto;
import kalix.javasdk.valueentity.ValueEntityContext;
import kalix.javasdk.valueentity.ValueEntityOptions;
import kalix.javasdk.valueentity.ValueEntityProvider;
import user.api.AuthApi;

import java.util.function.Function;

// This code is managed by Kalix tooling.
// It will be re-generated to reflect any changes to your protobuf definitions.
// DO NOT EDIT

/**
 * A value entity provider that defines how to register and create the entity for
 * the Protobuf service <code>AuthService</code>.
 *
 * Should be used with the <code>register</code> method in {@link kalix.javasdk.Kalix}.
 */
public class AuthProvider implements ValueEntityProvider<AuthDomain.AuthState, Auth> {

  private final Function<ValueEntityContext, Auth> entityFactory;
  private final ValueEntityOptions options;

  /** Factory method of AuthProvider */
  public static AuthProvider of(Function<ValueEntityContext, Auth> entityFactory) {
    return new AuthProvider(entityFactory, ValueEntityOptions.defaults());
  }

  private AuthProvider(
      Function<ValueEntityContext, Auth> entityFactory,
      ValueEntityOptions options) {
    this.entityFactory = entityFactory;
    this.options = options;
  }

  @Override
  public final ValueEntityOptions options() {
    return options;
  }

  public final AuthProvider withOptions(ValueEntityOptions options) {
    return new AuthProvider(entityFactory, options);
  }

  @Override
  public final Descriptors.ServiceDescriptor serviceDescriptor() {
    return AuthApi.getDescriptor().findServiceByName("AuthService");
  }

  @Override
  public final String entityType() {
    return "authorization";
  }

  @Override
  public final AuthRouter newRouter(ValueEntityContext context) {
    return new AuthRouter(entityFactory.apply(context));
  }

  @Override
  public final Descriptors.FileDescriptor[] additionalDescriptors() {
    return new Descriptors.FileDescriptor[] {
      AuthApi.getDescriptor(),
      AuthDomain.getDescriptor(),
      EmptyProto.getDescriptor()
    };
  }
}
