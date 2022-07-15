package io.users.admin.domain;

import com.google.protobuf.Descriptors;
import com.google.protobuf.Empty;
import com.google.protobuf.EmptyProto;
import io.users.admin.api.AdminApi;
import kalix.javasdk.valueentity.ValueEntityContext;
import kalix.javasdk.valueentity.ValueEntityOptions;
import kalix.javasdk.valueentity.ValueEntityProvider;

import java.util.function.Function;

// This code is managed by Kalix tooling.
// It will be re-generated to reflect any changes to your protobuf definitions.
// DO NOT EDIT

/**
 * A value entity provider that defines how to register and create the entity for
 * the Protobuf service <code>AdminService</code>.
 *
 * Should be used with the <code>register</code> method in {@link kalix.javasdk.Kalix}.
 */
public class AdminProvider implements ValueEntityProvider<AdminDomain.AdminState, Admin> {

  private final Function<ValueEntityContext, Admin> entityFactory;
  private final ValueEntityOptions options;

  /** Factory method of AdminProvider */
  public static AdminProvider of(Function<ValueEntityContext, Admin> entityFactory) {
    return new AdminProvider(entityFactory, ValueEntityOptions.defaults());
  }

  private AdminProvider(
      Function<ValueEntityContext, Admin> entityFactory,
      ValueEntityOptions options) {
    this.entityFactory = entityFactory;
    this.options = options;
  }

  @Override
  public final ValueEntityOptions options() {
    return options;
  }

  public final AdminProvider withOptions(ValueEntityOptions options) {
    return new AdminProvider(entityFactory, options);
  }

  @Override
  public final Descriptors.ServiceDescriptor serviceDescriptor() {
    return AdminApi.getDescriptor().findServiceByName("AdminService");
  }

  @Override
  public final String entityType() {
    return "admins";
  }

  @Override
  public final AdminRouter newRouter(ValueEntityContext context) {
    return new AdminRouter(entityFactory.apply(context));
  }

  @Override
  public final Descriptors.FileDescriptor[] additionalDescriptors() {
    return new Descriptors.FileDescriptor[] {
      AdminApi.getDescriptor(),
      AdminDomain.getDescriptor(),
      EmptyProto.getDescriptor()
    };
  }
}
