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
import java.util.UUID;

// This class was initially generated based on the .proto definition by Kalix tooling.
// This is the implementation for the Action Service described in your io/users/auth/action/auth_action.proto file.
//
// As long as this file exists it will not be overwritten: you can maintain it yourself,
// or delete it so it is regenerated as needed.

public class AuthActionImpl extends AbstractAuthAction {

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
      CompletionStage<Empty> userCreated = authCreated.thenCompose(empty -> {
        return components().admin().createAdmin(AdminApi.Admin.newBuilder()
            .setId(id)
            .setEmail(auth.getEmail())
            .setFirstName(auth.getFirstName())
            .setLastName(auth.getLastName())
            .setRole(auth.getRole())
            .setPassword(auth.getPassword())
            .build())
            .execute();
      });

      CompletionStage<AuthActionApi.Auth> reply = userCreated
          .thenApply(empty -> AuthActionApi.Auth.newBuilder()
              .setEmail(auth.getEmail()).build());

      return effects().asyncReply(reply);
    } else if (auth.getRole().equalsIgnoreCase("doctor")) {
      CompletionStage<Empty> userCreated = authCreated.thenCompose(empty -> {
        return components().doctor().createDoctor(DoctorApi.Doctor.newBuilder()
            .setId(id)
            .setEmail(auth.getEmail())
            .setFirstName(auth.getFirstName())
            .setLastName(auth.getLastName())
            .setRole(auth.getRole())
            .setPassword(auth.getPassword())
            // Doctor
            .setGender(auth.getGender())
            .setMobile(auth.getMobile())
            .setDesignation(auth.getDesignation())
            .setDepartment(auth.getDepartment())
            .setAddress(auth.getAddress())
            .setDateOfBirth(auth.getDateOfBirth())
            .setEducation(auth.getEducation())
            .setImg(auth.getImg())
            .setSpecialization(auth.getSpecialization())
            .setDegree(auth.getDegree())
            .setJoiningDate(auth.getJoiningDate())
            .build())
            .execute();
      });

      CompletionStage<AuthActionApi.Auth> reply = userCreated
          .thenApply(empty -> AuthActionApi.Auth.newBuilder()
              .setEmail(auth.getEmail()).build());

      return effects().asyncReply(reply);
    } else {
      CompletionStage<Empty> userCreated = authCreated.thenCompose(empty -> {
        return components().patient().createPatient(PatientApi.Patient.newBuilder()
            .setId(id)
            .setEmail(auth.getEmail())
            .setFirstName(auth.getFirstName())
            .setLastName(auth.getLastName())
            .setRole(auth.getRole())
            .setPassword(auth.getPassword())
            // Patient
            .setGender(auth.getGender())
            .setMobile(auth.getMobile())
            .setDateOfBirth(auth.getDateOfBirth())
            .setAge(auth.getAge())
            .setMaritalStatus(auth.getMaritalStatus())
            .setAddress(auth.getAddress())
            .setBloodGroup(auth.getBloodGroup())
            .setBloodPressure(auth.getBloodPressure())
            .setSugger(auth.getSugger())
            .setInjury(auth.getInjury())
            .setImg(auth.getImg())
            .build())
            .execute();
      });

      CompletionStage<AuthActionApi.Auth> reply = userCreated
          .thenApply(empty -> AuthActionApi.Auth.newBuilder()
              .setEmail(auth.getEmail()).build());

      return effects().asyncReply(reply);
    }

    // DeferredCall<AuthActionApi.Auth, Empty> call =
    // components().auth().register(auth);
    // return effects().forward(call);
  }
}