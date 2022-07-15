package io.users.patient.domain;

import com.google.protobuf.Descriptors;
import com.google.protobuf.Empty;
import com.google.protobuf.EmptyProto;
import io.users.patient.api.PatientApi;
import kalix.javasdk.valueentity.ValueEntityContext;
import kalix.javasdk.valueentity.ValueEntityOptions;
import kalix.javasdk.valueentity.ValueEntityProvider;

import java.util.function.Function;

// This code is managed by Kalix tooling.
// It will be re-generated to reflect any changes to your protobuf definitions.
// DO NOT EDIT

/**
 * A value entity provider that defines how to register and create the entity for
 * the Protobuf service <code>PatientService</code>.
 *
 * Should be used with the <code>register</code> method in {@link kalix.javasdk.Kalix}.
 */
public class PatientProvider implements ValueEntityProvider<PatientDomain.PatientState, Patient> {

  private final Function<ValueEntityContext, Patient> entityFactory;
  private final ValueEntityOptions options;

  /** Factory method of PatientProvider */
  public static PatientProvider of(Function<ValueEntityContext, Patient> entityFactory) {
    return new PatientProvider(entityFactory, ValueEntityOptions.defaults());
  }

  private PatientProvider(
      Function<ValueEntityContext, Patient> entityFactory,
      ValueEntityOptions options) {
    this.entityFactory = entityFactory;
    this.options = options;
  }

  @Override
  public final ValueEntityOptions options() {
    return options;
  }

  public final PatientProvider withOptions(ValueEntityOptions options) {
    return new PatientProvider(entityFactory, options);
  }

  @Override
  public final Descriptors.ServiceDescriptor serviceDescriptor() {
    return PatientApi.getDescriptor().findServiceByName("PatientService");
  }

  @Override
  public final String entityType() {
    return "patients";
  }

  @Override
  public final PatientRouter newRouter(ValueEntityContext context) {
    return new PatientRouter(entityFactory.apply(context));
  }

  @Override
  public final Descriptors.FileDescriptor[] additionalDescriptors() {
    return new Descriptors.FileDescriptor[] {
      EmptyProto.getDescriptor(),
      PatientApi.getDescriptor(),
      PatientDomain.getDescriptor()
    };
  }
}
