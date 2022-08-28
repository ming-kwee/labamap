package io.users;

import kalix.javasdk.Context;
import kalix.javasdk.DeferredCall;
import kalix.javasdk.impl.DeferredCallImpl;
import kalix.javasdk.impl.InternalContext;
import kalix.javasdk.impl.MetadataImpl;

// This code is managed by Kalix tooling.
// It will be re-generated to reflect any changes to your protobuf definitions.
// DO NOT EDIT

/**
 * Not intended for direct instantiation, called by generated code, use Action.components() to access
 */
public final class ComponentsImpl implements Components {

  private final InternalContext context;

  public ComponentsImpl(Context context) {
    this.context = (InternalContext) context;
  }

  private <T> T getGrpcClient(Class<T> serviceClass) {
    return context.getComponentGrpcClient(serviceClass);
  }

  @Override
  public Components.AuthActionImplCalls authActionImpl() {
    return new AuthActionImplCallsImpl();
  }
  @Override
  public Components.DoctorCalls doctor() {
    return new DoctorCallsImpl();
  }
  @Override
  public Components.DoctorStateSubscriptionActionCalls doctorStateSubscriptionAction() {
    return new DoctorStateSubscriptionActionCallsImpl();
  }
  @Override
  public Components.DoctorByRoleViewCalls doctorByRoleView() {
    return new DoctorByRoleViewCallsImpl();
  }
  @Override
  public Components.PatientCalls patient() {
    return new PatientCallsImpl();
  }
  @Override
  public Components.PatientStateSubscriptionActionCalls patientStateSubscriptionAction() {
    return new PatientStateSubscriptionActionCallsImpl();
  }
  @Override
  public Components.AdminCalls admin() {
    return new AdminCallsImpl();
  }
  @Override
  public Components.AuthCalls auth() {
    return new AuthCallsImpl();
  }
  @Override
  public Components.PatientByRoleViewCalls patientByRoleView() {
    return new PatientByRoleViewCallsImpl();
  }

  private final class AuthActionImplCallsImpl implements Components.AuthActionImplCalls {
     @Override
    public DeferredCall<io.users.auth.action.AuthActionApi.Auth, io.users.auth.action.AuthActionApi.Auth> register(io.users.auth.action.AuthActionApi.Auth auth) {
      return new DeferredCallImpl<>(
        auth,
        MetadataImpl.Empty(),
        "io.users.auth.action.AuthAction",
        "Register",
        () -> getGrpcClient(io.users.auth.action.AuthAction.class).register(auth)
      );
    }
    @Override
    public DeferredCall<io.users.auth.action.AuthActionApi.Auth, io.users.auth.action.AuthActionApi.Auth> updateUser(io.users.auth.action.AuthActionApi.Auth auth) {
      return new DeferredCallImpl<>(
        auth,
        MetadataImpl.Empty(),
        "io.users.auth.action.AuthAction",
        "UpdateUser",
        () -> getGrpcClient(io.users.auth.action.AuthAction.class).updateUser(auth)
      );
    }
  }
  private final class DoctorCallsImpl implements Components.DoctorCalls {
     @Override
    public DeferredCall<io.users.doctor.api.DoctorApi.Doctor, com.google.protobuf.Empty> createDoctor(io.users.doctor.api.DoctorApi.Doctor doctor) {
      return new DeferredCallImpl<>(
        doctor,
        MetadataImpl.Empty(),
        "io.users.doctor.api.DoctorService",
        "CreateDoctor",
        () -> getGrpcClient(io.users.doctor.api.DoctorService.class).createDoctor(doctor)
      );
    }
    @Override
    public DeferredCall<io.users.doctor.api.DoctorApi.GetDoctorRequest, io.users.doctor.api.DoctorApi.Doctor> getDoctor(io.users.doctor.api.DoctorApi.GetDoctorRequest getDoctorRequest) {
      return new DeferredCallImpl<>(
        getDoctorRequest,
        MetadataImpl.Empty(),
        "io.users.doctor.api.DoctorService",
        "GetDoctor",
        () -> getGrpcClient(io.users.doctor.api.DoctorService.class).getDoctor(getDoctorRequest)
      );
    }
    @Override
    public DeferredCall<io.users.doctor.api.DoctorApi.Doctor, com.google.protobuf.Empty> updateDoctor(io.users.doctor.api.DoctorApi.Doctor doctor) {
      return new DeferredCallImpl<>(
        doctor,
        MetadataImpl.Empty(),
        "io.users.doctor.api.DoctorService",
        "UpdateDoctor",
        () -> getGrpcClient(io.users.doctor.api.DoctorService.class).updateDoctor(doctor)
      );
    }
  }
  private final class DoctorStateSubscriptionActionCallsImpl implements Components.DoctorStateSubscriptionActionCalls {
     @Override
    public DeferredCall<io.users.doctor.domain.DoctorDomain.DoctorState, io.users.doctor.api.DoctorApi.Doctor> onStateChange(io.users.doctor.domain.DoctorDomain.DoctorState doctorState) {
      return new DeferredCallImpl<>(
        doctorState,
        MetadataImpl.Empty(),
        "io.users.doctor.action.DoctorStateSubscription",
        "OnStateChange",
        () -> getGrpcClient(io.users.doctor.action.DoctorStateSubscription.class).onStateChange(doctorState)
      );
    }
  }
  private final class DoctorByRoleViewCallsImpl implements Components.DoctorByRoleViewCalls {
     @Override
    public DeferredCall<io.users.doctor.view.DoctorViewModel.ByRoleRequest, io.users.doctor.view.DoctorViewModel.ByRoleResponse> getDoctors(io.users.doctor.view.DoctorViewModel.ByRoleRequest byRoleRequest) {
      return new DeferredCallImpl<>(
        byRoleRequest,
        MetadataImpl.Empty(),
        "io.users.doctor.view.DoctorByRole",
        "GetDoctors",
        () -> getGrpcClient(io.users.doctor.view.DoctorByRole.class).getDoctors(byRoleRequest)
      );
    }
  }
  private final class PatientCallsImpl implements Components.PatientCalls {
     @Override
    public DeferredCall<io.users.patient.api.PatientApi.Patient, com.google.protobuf.Empty> createPatient(io.users.patient.api.PatientApi.Patient patient) {
      return new DeferredCallImpl<>(
        patient,
        MetadataImpl.Empty(),
        "io.users.patient.api.PatientService",
        "CreatePatient",
        () -> getGrpcClient(io.users.patient.api.PatientService.class).createPatient(patient)
      );
    }
    @Override
    public DeferredCall<io.users.patient.api.PatientApi.GetPatientRequest, io.users.patient.api.PatientApi.Patient> getPatient(io.users.patient.api.PatientApi.GetPatientRequest getPatientRequest) {
      return new DeferredCallImpl<>(
        getPatientRequest,
        MetadataImpl.Empty(),
        "io.users.patient.api.PatientService",
        "GetPatient",
        () -> getGrpcClient(io.users.patient.api.PatientService.class).getPatient(getPatientRequest)
      );
    }
    @Override
    public DeferredCall<io.users.patient.api.PatientApi.Patient, com.google.protobuf.Empty> updatePatient(io.users.patient.api.PatientApi.Patient patient) {
      return new DeferredCallImpl<>(
        patient,
        MetadataImpl.Empty(),
        "io.users.patient.api.PatientService",
        "UpdatePatient",
        () -> getGrpcClient(io.users.patient.api.PatientService.class).updatePatient(patient)
      );
    }
  }
  private final class PatientStateSubscriptionActionCallsImpl implements Components.PatientStateSubscriptionActionCalls {
     @Override
    public DeferredCall<io.users.patient.domain.PatientDomain.PatientState, io.users.patient.api.PatientApi.Patient> onStateChange(io.users.patient.domain.PatientDomain.PatientState patientState) {
      return new DeferredCallImpl<>(
        patientState,
        MetadataImpl.Empty(),
        "io.users.patient.action.PatientStateSubscription",
        "OnStateChange",
        () -> getGrpcClient(io.users.patient.action.PatientStateSubscription.class).onStateChange(patientState)
      );
    }
  }
  private final class AdminCallsImpl implements Components.AdminCalls {
     @Override
    public DeferredCall<io.users.admin.api.AdminApi.Admin, com.google.protobuf.Empty> createAdmin(io.users.admin.api.AdminApi.Admin admin) {
      return new DeferredCallImpl<>(
        admin,
        MetadataImpl.Empty(),
        "io.users.admin.api.AdminService",
        "CreateAdmin",
        () -> getGrpcClient(io.users.admin.api.AdminService.class).createAdmin(admin)
      );
    }
    @Override
    public DeferredCall<io.users.admin.api.AdminApi.GetAdminRequest, io.users.admin.api.AdminApi.Admin> getAdmin(io.users.admin.api.AdminApi.GetAdminRequest getAdminRequest) {
      return new DeferredCallImpl<>(
        getAdminRequest,
        MetadataImpl.Empty(),
        "io.users.admin.api.AdminService",
        "GetAdmin",
        () -> getGrpcClient(io.users.admin.api.AdminService.class).getAdmin(getAdminRequest)
      );
    }
  }
  private final class AuthCallsImpl implements Components.AuthCalls {
     @Override
    public DeferredCall<io.users.auth.action.AuthActionApi.Auth, com.google.protobuf.Empty> register(io.users.auth.action.AuthActionApi.Auth auth) {
      return new DeferredCallImpl<>(
        auth,
        MetadataImpl.Empty(),
        "io.users.auth.api.AuthService",
        "Register",
        () -> getGrpcClient(io.users.auth.api.AuthService.class).register(auth)
      );
    }
    @Override
    public DeferredCall<io.users.auth.api.AuthApi.GetLoginRequest, io.users.auth.action.AuthActionApi.Auth> login(io.users.auth.api.AuthApi.GetLoginRequest getLoginRequest) {
      return new DeferredCallImpl<>(
        getLoginRequest,
        MetadataImpl.Empty(),
        "io.users.auth.api.AuthService",
        "Login",
        () -> getGrpcClient(io.users.auth.api.AuthService.class).login(getLoginRequest)
      );
    }
    @Override
    public DeferredCall<io.users.auth.action.AuthActionApi.Auth, com.google.protobuf.Empty> updateUser(io.users.auth.action.AuthActionApi.Auth auth) {
      return new DeferredCallImpl<>(
        auth,
        MetadataImpl.Empty(),
        "io.users.auth.api.AuthService",
        "UpdateUser",
        () -> getGrpcClient(io.users.auth.api.AuthService.class).updateUser(auth)
      );
    }
  }
  private final class PatientByRoleViewCallsImpl implements Components.PatientByRoleViewCalls {
     @Override
    public DeferredCall<io.users.patient.view.PatientViewModel.ByRoleRequest, io.users.patient.view.PatientViewModel.ByRoleResponse> getPatients(io.users.patient.view.PatientViewModel.ByRoleRequest byRoleRequest) {
      return new DeferredCallImpl<>(
        byRoleRequest,
        MetadataImpl.Empty(),
        "io.users.patient.view.PatientByRole",
        "GetPatients",
        () -> getGrpcClient(io.users.patient.view.PatientByRole.class).getPatients(byRoleRequest)
      );
    }
  }
}
