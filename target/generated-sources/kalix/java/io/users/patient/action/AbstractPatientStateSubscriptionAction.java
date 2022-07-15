package io.users.patient.action;

import io.users.Components;
import io.users.ComponentsImpl;
import io.users.patient.api.PatientApi;
import io.users.patient.domain.PatientDomain;

// This code is managed by Kalix tooling.
// It will be re-generated to reflect any changes to your protobuf definitions.
// DO NOT EDIT

public abstract class AbstractPatientStateSubscriptionAction extends kalix.javasdk.action.Action {

  protected final Components components() {
    return new ComponentsImpl(contextForComponents());
  }

  public abstract Effect<PatientApi.Patient> onStateChange(PatientDomain.PatientState patientState);
}