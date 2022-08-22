package io.users.auth.action;

import kalix.javasdk.action.ActionCreationContext;
import java.util.concurrent.CompletionStage;
// import io.users.auth.domain.Auth;
// This class was initially generated based on the .proto definition by Kalix tooling.
// This is the implementation for the Action Service described in your io/users/auth/action/auth_action.proto file.
//
// As long as this file exists it will not be overwritten: you can maintain it yourself,
// or delete it so it is regenerated as needed.

import com.google.protobuf.Empty;
// import io.users.auth.api.AuthApi;
import io.users.admin.api.AdminApi;
import io.users.doctor.api.DoctorApi;
import io.users.patient.api.PatientApi;
// import io.users.doctor.domain.DoctorDomain;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// This class was initially generated based on the .proto definition by Kalix tooling.
// This is the implementation for the Action Service described in your io/users/auth/action/auth_action.proto file.
//
// As long as this file exists it will not be overwritten: you can maintain it yourself,
// or delete it so it is regenerated as needed.

public class AuthActionImpl extends AbstractAuthAction {
  private static final Logger LOG = LoggerFactory.getLogger(AuthActionImpl.class);

  public AuthActionImpl(ActionCreationContext creationContext) {
  }

  @Override
  public Effect<AuthActionApi.Auth> register(AuthActionApi.Auth auth) {
    String id = UUID.randomUUID().toString();

    CompletionStage<Empty> authCreated = components().auth().register(AuthActionApi.Auth.newBuilder()
        .setId(id)
        .setEmail(auth.getEmail())
        .setFirstName(auth.getFirstName())
        .setLastName(auth.getLastName())
        .setRole(auth.getRole())
        .setPassword(auth.getPassword()).build())
        .execute();

    if (auth.getRole().equalsIgnoreCase("admin")) {
      // Create Admin
      CompletionStage<Empty> userCreated = authCreated.thenCompose(empty -> {
        return components().admin().createAdmin(convertToAdminApi(auth, id))
            .execute();
      });

      CompletionStage<AuthActionApi.Auth> reply = userCreated
          .thenApply(empty -> AuthActionApi.Auth.newBuilder()
              .setEmail(auth.getEmail()).build());

      return effects().asyncReply(reply);
    } else if (auth.getRole().equalsIgnoreCase("doctor")) {
      // Create Doctor
      CompletionStage<Empty> userCreated = authCreated.thenCompose(empty -> {
        return components().doctor().createDoctor(convertToDoctorApi(auth, id))
            .execute();
      });

      CompletionStage<AuthActionApi.Auth> reply = userCreated
          .thenApply(empty -> AuthActionApi.Auth.newBuilder()
              .setEmail(auth.getEmail()).build());

      return effects().asyncReply(reply);
    } else if (auth.getRole().equalsIgnoreCase("patient")) {
      // Create Patient
      CompletionStage<Empty> userCreated = authCreated.thenCompose(empty -> {
        return components().patient().createPatient(convertToPatientApi(auth, id))
            .execute();
      });

      CompletionStage<AuthActionApi.Auth> reply = userCreated
          .thenApply(empty -> AuthActionApi.Auth.newBuilder()
              .setEmail(auth.getEmail()).build());

      return effects().asyncReply(reply);
    } else {
      // Create User without role
      CompletionStage<AuthActionApi.Auth> reply = authCreated
          .thenApply(empty -> AuthActionApi.Auth.newBuilder()
              .setEmail(auth.getEmail()).build());

      return effects().asyncReply(reply);
    }

    // DeferredCall<AuthActionApi.Auth, Empty> call =
    // components().auth().register(auth);
    // return effects().forward(call);
  }

  @Override
  public Effect<AuthActionApi.Auth> updateUser(AuthActionApi.Auth auth) {
    CompletionStage<Empty> authCreated = components().auth().updateUser(auth).execute();

    if (auth.getRole().equalsIgnoreCase("doctor")) {
      // Update Doctor
      CompletionStage<Empty> doctorUpdated = authCreated.thenCompose(empty -> {
        return components().doctor().updateDoctor(convertToDoctorApi(auth, null))
            .execute();
      });

      CompletionStage<AuthActionApi.Auth> reply = doctorUpdated.thenApply(empty -> auth);
      return effects().asyncReply(reply);

    } else if (auth.getRole().equalsIgnoreCase("patient")) {
      // Update Patient
      CompletionStage<Empty> patientUpdated = authCreated.thenCompose(empty -> {
        return components().patient().updatePatient(convertToPatientApi(auth, null))
            .execute();
      });

      CompletionStage<AuthActionApi.Auth> reply = patientUpdated.thenApply(empty -> auth);
      return effects().asyncReply(reply);
    } else {
      // None

      // CompletionStage<AuthActionApi.Auth> reply = authCreated
      // .thenApply(empty -> AuthActionApi.Auth.newBuilder()
      // .setEmail(auth.getEmail()).build());

      return effects().reply(auth);
    }
  }

  private AdminApi.Admin convertToAdminApi(AuthActionApi.Auth admin, String id) {
    return AdminApi.Admin.newBuilder()
        .setId(id)
        .setEmail(admin.getEmail())
        .setFirstName(admin.getFirstName())
        .setLastName(admin.getLastName())
        .setRole(admin.getRole())
        .setPassword(admin.getPassword())
        .build();
  }

  private DoctorApi.Doctor convertToDoctorApi(AuthActionApi.Auth doctor, String id) {
    return DoctorApi.Doctor.newBuilder()
        .setId(id != null ? id : doctor.getId())
        .setEmail(doctor.getEmail())
        .setPassword(doctor.getPassword())
        .setFirstName(doctor.getFirstName())
        .setLastName(doctor.getLastName())
        .setRole(doctor.getRole())
        // Doctor
        .setGender(doctor.getGender())
        .setMobile(doctor.getMobile())
        .setDesignation(doctor.getDesignation())
        .setDepartment(doctor.getDepartment())
        .setAddress(doctor.getAddress())
        .setDateOfBirth(doctor.getDateOfBirth())
        .setEducation(doctor.getEducation())
        .setImg(doctor.getImg())
        .setSpecialization(doctor.getSpecialization())
        .setDegree(doctor.getDegree())
        .setJoiningDate(doctor.getJoiningDate())
        .build();
  }

  private PatientApi.Patient convertToPatientApi(AuthActionApi.Auth patient, String id) {
    return PatientApi.Patient.newBuilder()
        .setId(id != null ? id : patient.getId())
        .setEmail(patient.getEmail())
        .setFirstName(patient.getFirstName())
        .setLastName(patient.getLastName())
        .setRole(patient.getRole())
        .setPassword(patient.getPassword())
        // Patient
        .setGender(patient.getGender())
        .setMobile(patient.getMobile())
        .setDateOfBirth(patient.getDateOfBirth())
        .setAge(patient.getAge())
        .setMaritalStatus(patient.getMaritalStatus())
        .setAddress(patient.getAddress())
        .setBloodGroup(patient.getBloodGroup())
        .setBloodPressure(patient.getBloodPressure())
        .setSugger(patient.getSugger())
        .setInjury(patient.getInjury())
        .setImg(patient.getImg())
        .build();
  }

}
