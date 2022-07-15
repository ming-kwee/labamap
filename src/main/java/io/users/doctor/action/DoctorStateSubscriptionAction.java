package io.users.doctor.action;

import io.users.doctor.api.DoctorApi;
import io.users.doctor.domain.DoctorDomain;
import kalix.javasdk.action.ActionCreationContext;

// This class was initially generated based on the .proto definition by Kalix tooling.
// This is the implementation for the Action Service described in your io/users/doctor/action/doctor_action.proto file.
//
// As long as this file exists it will not be overwritten: you can maintain it yourself,
// or delete it so it is regenerated as needed.

public class DoctorStateSubscriptionAction extends AbstractDoctorStateSubscriptionAction {

  public DoctorStateSubscriptionAction(ActionCreationContext creationContext) {
  }

  @Override
  public Effect<DoctorApi.Doctor> onStateChange(DoctorDomain.DoctorState doctorState) {
    DoctorApi.Doctor doctor = DoctorApi.Doctor.newBuilder()
        .setId(doctorState.getId())
        .setEmail(doctorState.getEmail())
        .setImg(doctorState.getImg())
        // .setPassword(doctorState.getPassword())
        .setFirstName(doctorState.getFirstName())
        .setLastName(doctorState.getLastName())
        .setRole(doctorState.getRole())
        // Doctor
        .setGender(doctorState.getGender())
        .setMobile(doctorState.getMobile())
        .setDesignation(doctorState.getDesignation())
        .setDepartment(doctorState.getDepartment())
        .setAddress(doctorState.getAddress())
        .setDateOfBirth(doctorState.getDateOfBirth())
        .setEducation(doctorState.getEducation())
        .setImg(doctorState.getImg())
        .setSpecialization(doctorState.getSpecialization())
        .setDegree(doctorState.getDegree())
        .setJoiningDate(doctorState.getJoiningDate())
        .build();

    return effects().reply(doctor);
  }
}
