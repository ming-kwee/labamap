package io.users.doctor.domain;

import com.google.protobuf.Empty;
import io.users.doctor.api.DoctorApi;
import kalix.javasdk.valueentity.ValueEntityContext;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// This class was initially generated based on the .proto definition by Kalix tooling.
// This is the implementation for the Value Entity Service described in your io/users/doctor/api/doctor_api.proto file.
//
// As long as this file exists it will not be overwritten: you can maintain it yourself,
// or delete it so it is regenerated as needed.

public class Doctor extends AbstractDoctor {
  static final Logger log = LoggerFactory.getLogger(Doctor.class);
  @SuppressWarnings("unused")
  private final String entityId;

  public Doctor(ValueEntityContext context) {
    this.entityId = context.entityId();
  }

  @Override
  public DoctorDomain.DoctorState emptyState() {
    return DoctorDomain.DoctorState.getDefaultInstance();
  }

  @Override
  public Effect<Empty> createDoctor(DoctorDomain.DoctorState currentState, DoctorApi.Doctor command) {
    DoctorDomain.DoctorState state = convertToDomain(command);
    return reject(currentState, command).orElseGet(() -> handle(state, command));
  }
  private Optional<Effect<Empty>> reject(DoctorDomain.DoctorState currentState, DoctorApi.Doctor command) {
    if (currentState.getEmail().equals(command.getEmail())) {
      return Optional.of(effects().error("Email is already exists!"));
    }
    return Optional.empty();
  }
  private Effect<Empty> handle(DoctorDomain.DoctorState state, DoctorApi.Doctor command) {
    return effects().updateState(state).thenReply(Empty.getDefaultInstance());
  }
  private DoctorDomain.DoctorState convertToDomain(DoctorApi.Doctor doctor) {
    return DoctorDomain.DoctorState.newBuilder()
        .setId(doctor.getId())
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

  @Override
  public Effect<DoctorApi.Doctor> getDoctor(DoctorDomain.DoctorState currentState,
      DoctorApi.GetDoctorRequest command) {
    if (currentState.getEmail().equals(command.getEmail())) {
      return effects().reply(convertToApi(currentState));
    } else {
      return effects().error("Doctor " + command.getEmail() + " has not been created.");
    }
  }
  private DoctorApi.Doctor convertToApi(DoctorDomain.DoctorState state) {
    return DoctorApi.Doctor.newBuilder()
        .setId(state.getId())
        .setEmail(state.getEmail())
        // .setPassword(state.getPassword())
        .setFirstName(state.getFirstName())
        .setLastName(state.getLastName())
        .setRole(state.getRole())
        .setGender(state.getGender())
        .setMobile(state.getMobile())
        .setDesignation(state.getDesignation())
        .setDepartment(state.getDepartment())
        .setAddress(state.getAddress())
        .setDateOfBirth(state.getDateOfBirth())
        .setEducation(state.getEducation())
        .setImg(state.getImg())
        .setSpecialization(state.getSpecialization())
        .setDegree(state.getDegree())
        .setJoiningDate(state.getJoiningDate())
        .build();
  }

  @Override
  public Effect<Empty> updateDoctor(DoctorDomain.DoctorState currentState, DoctorApi.Doctor command) {
    // if (currentState.getId().equals(command.getId())) {
      DoctorDomain.DoctorState updatedDoctor = convertToDomain(command);
      return effects().updateState(updatedDoctor).thenReply(Empty.getDefaultInstance());
    // } else {
    //   return effects().error("User " + command.getId() + " not found.");
    // }
  }

}
