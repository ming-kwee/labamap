package io.users;

import io.users.admin.api.AdminApi;
import io.users.admin.domain.Admin;
import io.users.admin.domain.AdminProvider;
import io.users.auth.action.AuthActionApi;
import io.users.auth.action.AuthActionImpl;
import io.users.auth.action.AuthActionProvider;
import io.users.auth.api.AuthApi;
import io.users.auth.domain.Auth;
import io.users.auth.domain.AuthProvider;
import io.users.doctor.action.DoctorAction;
import io.users.doctor.action.DoctorStateSubscriptionAction;
import io.users.doctor.action.DoctorStateSubscriptionActionProvider;
import io.users.doctor.api.DoctorApi;
import io.users.doctor.domain.Doctor;
import io.users.doctor.domain.DoctorProvider;
import io.users.patient.action.PatientAction;
import io.users.patient.action.PatientStateSubscriptionAction;
import io.users.patient.action.PatientStateSubscriptionActionProvider;
import io.users.patient.api.PatientApi;
import io.users.patient.domain.Patient;
import io.users.patient.domain.PatientProvider;
import kalix.javasdk.Kalix;
import kalix.javasdk.action.ActionCreationContext;
import kalix.javasdk.valueentity.ValueEntityContext;

import java.util.function.Function;

// This code is managed by Kalix tooling.
// It will be re-generated to reflect any changes to your protobuf definitions.
// DO NOT EDIT

public final class KalixFactory {

  public static Kalix withComponents(
      Function<ValueEntityContext, Admin> createAdmin,
      Function<ValueEntityContext, Auth> createAuth,
      Function<ValueEntityContext, Doctor> createDoctor,
      Function<ValueEntityContext, Patient> createPatient,
      Function<ActionCreationContext, AuthActionImpl> createAuthActionImpl,
      Function<ActionCreationContext, DoctorStateSubscriptionAction> createDoctorStateSubscriptionAction,
      Function<ActionCreationContext, PatientStateSubscriptionAction> createPatientStateSubscriptionAction) {
    Kalix kalix = new Kalix();
    return kalix
      .register(AdminProvider.of(createAdmin))
      .register(AuthActionProvider.of(createAuthActionImpl))
      .register(AuthProvider.of(createAuth))
      .register(DoctorProvider.of(createDoctor))
      .register(DoctorStateSubscriptionActionProvider.of(createDoctorStateSubscriptionAction))
      .register(PatientProvider.of(createPatient))
      .register(PatientStateSubscriptionActionProvider.of(createPatientStateSubscriptionAction));
  }
}
