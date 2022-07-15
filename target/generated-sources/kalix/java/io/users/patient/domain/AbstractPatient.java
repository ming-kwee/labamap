package io.users.patient.domain;

import com.google.protobuf.Empty;
import io.users.Components;
import io.users.ComponentsImpl;
import io.users.patient.api.PatientApi;
import kalix.javasdk.valueentity.ValueEntity;

// This code is managed by Kalix tooling.
// It will be re-generated to reflect any changes to your protobuf definitions.
// DO NOT EDIT

public abstract class AbstractPatient extends ValueEntity<PatientDomain.PatientState> {

  protected final Components components() {
    return new ComponentsImpl(commandContext());
  }

  public abstract Effect<Empty> createPatient(PatientDomain.PatientState currentState, PatientApi.Patient patient);

  public abstract Effect<PatientApi.Patient> getPatient(PatientDomain.PatientState currentState, PatientApi.GetPatientRequest getPatientRequest);

}
