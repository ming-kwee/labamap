package io.users.doctor.domain;

import com.google.protobuf.Empty;
import io.users.doctor.api.DoctorApi;
import kalix.javasdk.impl.valueentity.ValueEntityRouter;
import kalix.javasdk.valueentity.CommandContext;
import kalix.javasdk.valueentity.ValueEntity;

// This code is managed by Kalix tooling.
// It will be re-generated to reflect any changes to your protobuf definitions.
// DO NOT EDIT

/**
 * A value entity handler that is the glue between the Protobuf service <code>DoctorService</code>
 * and the command handler methods in the <code>Doctor</code> class.
 */
public class DoctorRouter extends ValueEntityRouter<DoctorDomain.DoctorState, Doctor> {

  public DoctorRouter(Doctor entity) {
    super(entity);
  }

  @Override
  public ValueEntity.Effect<?> handleCommand(
      String commandName, DoctorDomain.DoctorState state, Object command, CommandContext context) {
    switch (commandName) {

      case "CreateDoctor":
        return entity().createDoctor(state, (DoctorApi.Doctor) command);

      case "GetDoctor":
        return entity().getDoctor(state, (DoctorApi.GetDoctorRequest) command);

      default:
        throw new ValueEntityRouter.CommandHandlerNotFound(commandName);
    }
  }
}
