package io.users.doctor.domain;

import com.google.protobuf.Descriptors;
import com.google.protobuf.Empty;
import com.google.protobuf.EmptyProto;
import io.users.doctor.api.DoctorApi;
import kalix.javasdk.valueentity.ValueEntityContext;
import kalix.javasdk.valueentity.ValueEntityOptions;
import kalix.javasdk.valueentity.ValueEntityProvider;

import java.util.function.Function;

// This code is managed by Kalix tooling.
// It will be re-generated to reflect any changes to your protobuf definitions.
// DO NOT EDIT

/**
 * A value entity provider that defines how to register and create the entity for
 * the Protobuf service <code>DoctorService</code>.
 *
 * Should be used with the <code>register</code> method in {@link kalix.javasdk.Kalix}.
 */
public class DoctorProvider implements ValueEntityProvider<DoctorDomain.DoctorState, Doctor> {

  private final Function<ValueEntityContext, Doctor> entityFactory;
  private final ValueEntityOptions options;

  /** Factory method of DoctorProvider */
  public static DoctorProvider of(Function<ValueEntityContext, Doctor> entityFactory) {
    return new DoctorProvider(entityFactory, ValueEntityOptions.defaults());
  }

  private DoctorProvider(
      Function<ValueEntityContext, Doctor> entityFactory,
      ValueEntityOptions options) {
    this.entityFactory = entityFactory;
    this.options = options;
  }

  @Override
  public final ValueEntityOptions options() {
    return options;
  }

  public final DoctorProvider withOptions(ValueEntityOptions options) {
    return new DoctorProvider(entityFactory, options);
  }

  @Override
  public final Descriptors.ServiceDescriptor serviceDescriptor() {
    return DoctorApi.getDescriptor().findServiceByName("DoctorService");
  }

  @Override
  public final String entityType() {
    return "doctors";
  }

  @Override
  public final DoctorRouter newRouter(ValueEntityContext context) {
    return new DoctorRouter(entityFactory.apply(context));
  }

  @Override
  public final Descriptors.FileDescriptor[] additionalDescriptors() {
    return new Descriptors.FileDescriptor[] {
      DoctorApi.getDescriptor(),
      DoctorDomain.getDescriptor(),
      EmptyProto.getDescriptor()
    };
  }
}
