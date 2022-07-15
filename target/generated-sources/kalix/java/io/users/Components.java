package io.users;

import kalix.javasdk.DeferredCall;

// This code is managed by Kalix tooling.
// It will be re-generated to reflect any changes to your protobuf definitions.
// DO NOT EDIT

/**
 * Not intended for user extension, provided through generated implementation
 */
public interface Components {
  AuthActionImplCalls authActionImpl();
  DoctorCalls doctor();
  DoctorStateSubscriptionActionCalls doctorStateSubscriptionAction();
  PatientCalls patient();
  PatientStateSubscriptionActionCalls patientStateSubscriptionAction();
  AdminCalls admin();
  AuthCalls auth();

  interface AuthActionImplCalls {
    DeferredCall<io.users.auth.action.AuthActionApi.Auth, io.users.auth.action.AuthActionApi.Auth> register(io.users.auth.action.AuthActionApi.Auth auth);
  }
  interface DoctorCalls {
    DeferredCall<io.users.doctor.api.DoctorApi.Doctor, com.google.protobuf.Empty> createDoctor(io.users.doctor.api.DoctorApi.Doctor doctor);

    DeferredCall<io.users.doctor.api.DoctorApi.GetDoctorRequest, io.users.doctor.api.DoctorApi.Doctor> getDoctor(io.users.doctor.api.DoctorApi.GetDoctorRequest getDoctorRequest);
  }
  interface DoctorStateSubscriptionActionCalls {
    DeferredCall<io.users.doctor.domain.DoctorDomain.DoctorState, io.users.doctor.api.DoctorApi.Doctor> onStateChange(io.users.doctor.domain.DoctorDomain.DoctorState doctorState);
  }
  interface PatientCalls {
    DeferredCall<io.users.patient.api.PatientApi.Patient, com.google.protobuf.Empty> createPatient(io.users.patient.api.PatientApi.Patient patient);

    DeferredCall<io.users.patient.api.PatientApi.GetPatientRequest, io.users.patient.api.PatientApi.Patient> getPatient(io.users.patient.api.PatientApi.GetPatientRequest getPatientRequest);
  }
  interface PatientStateSubscriptionActionCalls {
    DeferredCall<io.users.patient.domain.PatientDomain.PatientState, io.users.patient.api.PatientApi.Patient> onStateChange(io.users.patient.domain.PatientDomain.PatientState patientState);
  }
  interface AdminCalls {
    DeferredCall<io.users.admin.api.AdminApi.Admin, com.google.protobuf.Empty> createAdmin(io.users.admin.api.AdminApi.Admin admin);

    DeferredCall<io.users.admin.api.AdminApi.GetAdminRequest, io.users.admin.api.AdminApi.Admin> getAdmin(io.users.admin.api.AdminApi.GetAdminRequest getAdminRequest);
  }
  interface AuthCalls {
    DeferredCall<io.users.auth.action.AuthActionApi.Auth, com.google.protobuf.Empty> register(io.users.auth.action.AuthActionApi.Auth auth);

    DeferredCall<io.users.auth.api.AuthApi.GetLoginRequest, io.users.auth.action.AuthActionApi.Auth> login(io.users.auth.api.AuthApi.GetLoginRequest getLoginRequest);
  }
}
