package io.users.doctor.domain;

import com.google.protobuf.Empty;
import io.users.Components;
import io.users.ComponentsImpl;
import io.users.doctor.api.DoctorApi;
import kalix.javasdk.valueentity.ValueEntity;

// This code is managed by Kalix tooling.
// It will be re-generated to reflect any changes to your protobuf definitions.
// DO NOT EDIT

public abstract class AbstractDoctor extends ValueEntity<DoctorDomain.DoctorState> {

  protected final Components components() {
    return new ComponentsImpl(commandContext());
  }

  public abstract Effect<Empty> createDoctor(DoctorDomain.DoctorState currentState, DoctorApi.Doctor doctor);

  public abstract Effect<DoctorApi.Doctor> getDoctor(DoctorDomain.DoctorState currentState, DoctorApi.GetDoctorRequest getDoctorRequest);

  public abstract Effect<Empty> updateDoctor(DoctorDomain.DoctorState currentState, DoctorApi.Doctor doctor);

}
