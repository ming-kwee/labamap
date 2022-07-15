package io.users.doctor.action;

import io.users.Components;
import io.users.ComponentsImpl;
import io.users.doctor.api.DoctorApi;
import io.users.doctor.domain.DoctorDomain;

// This code is managed by Kalix tooling.
// It will be re-generated to reflect any changes to your protobuf definitions.
// DO NOT EDIT

public abstract class AbstractDoctorStateSubscriptionAction extends kalix.javasdk.action.Action {

  protected final Components components() {
    return new ComponentsImpl(contextForComponents());
  }

  public abstract Effect<DoctorApi.Doctor> onStateChange(DoctorDomain.DoctorState doctorState);
}