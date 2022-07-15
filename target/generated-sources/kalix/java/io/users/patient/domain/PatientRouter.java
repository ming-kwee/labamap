package io.users.patient.domain;

import com.google.protobuf.Empty;
import io.users.patient.api.PatientApi;
import kalix.javasdk.impl.valueentity.ValueEntityRouter;
import kalix.javasdk.valueentity.CommandContext;
import kalix.javasdk.valueentity.ValueEntity;

// This code is managed by Kalix tooling.
// It will be re-generated to reflect any changes to your protobuf definitions.
// DO NOT EDIT

/**
 * A value entity handler that is the glue between the Protobuf service <code>PatientService</code>
 * and the command handler methods in the <code>Patient</code> class.
 */
public class PatientRouter extends ValueEntityRouter<PatientDomain.PatientState, Patient> {

  public PatientRouter(Patient entity) {
    super(entity);
  }

  @Override
  public ValueEntity.Effect<?> handleCommand(
      String commandName, PatientDomain.PatientState state, Object command, CommandContext context) {
    switch (commandName) {

      case "CreatePatient":
        return entity().createPatient(state, (PatientApi.Patient) command);

      case "GetPatient":
        return entity().getPatient(state, (PatientApi.GetPatientRequest) command);

      default:
        throw new ValueEntityRouter.CommandHandlerNotFound(commandName);
    }
  }
}
