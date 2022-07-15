package io.users.patient.domain;

import com.google.protobuf.Empty;
import io.users.patient.api.PatientApi;
import kalix.javasdk.valueentity.ValueEntityContext;
import java.util.Optional;

// This class was initially generated based on the .proto definition by Kalix tooling.
// This is the implementation for the Value Entity Service described in your io/users/patient/api/patient_api.proto file.
//
// As long as this file exists it will not be overwritten: you can maintain it yourself,
// or delete it so it is regenerated as needed.

public class Patient extends AbstractPatient {
  @SuppressWarnings("unused")
  private final String entityId;

  public Patient(ValueEntityContext context) {
    this.entityId = context.entityId();
  }

  @Override
  public PatientDomain.PatientState emptyState() {
    return PatientDomain.PatientState.getDefaultInstance();
  }

  @Override
  public Effect<Empty> createPatient(PatientDomain.PatientState currentState, PatientApi.Patient command) {
    PatientDomain.PatientState state = convertToDomain(command);
    return reject(currentState, command).orElseGet(() -> handle(state, command));
  }

  private Optional<Effect<Empty>> reject(PatientDomain.PatientState currentState, PatientApi.Patient command) {
    if (currentState.getEmail().equals(command.getEmail())) {
      return Optional.of(effects().error("Email is already exists!"));
    }
    return Optional.empty();
  }

  private Effect<Empty> handle(PatientDomain.PatientState state, PatientApi.Patient command) {
    return effects().updateState(state).thenReply(Empty.getDefaultInstance());
  }

  private PatientDomain.PatientState convertToDomain(PatientApi.Patient patient) {
    return PatientDomain.PatientState.newBuilder()
        .setId(patient.getId())
        .setEmail(patient.getEmail())
        .setPassword(patient.getPassword())
        .setFirstName(patient.getFirstName())
        .setLastName(patient.getLastName())
        .setRole(patient.getRole())
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

  @Override
  public Effect<PatientApi.Patient> getPatient(PatientDomain.PatientState currentState,
      PatientApi.GetPatientRequest command) {
    if (currentState.getEmail().equals(command.getEmail())) {
      return effects().reply(convertToApi(currentState));
    } else {
      return effects().error("Patient " + command.getEmail() + " has not been created.");
    }
  }

  private PatientApi.Patient convertToApi(PatientDomain.PatientState state) {
    return PatientApi.Patient.newBuilder()
        .setId(state.getId())
        .setEmail(state.getEmail())
        // .setPassword(state.getPassword())
        .setFirstName(state.getFirstName())
        .setLastName(state.getLastName())
        .setRole(state.getRole())
        .setGender(state.getGender())
        .setMobile(state.getMobile())
        .setDateOfBirth(state.getDateOfBirth())
        .setAge(state.getAge())
        .setMaritalStatus(state.getMaritalStatus())
        .setAddress(state.getAddress())
        .setBloodGroup(state.getBloodGroup())
        .setBloodPressure(state.getBloodPressure())
        .setSugger(state.getSugger())
        .setInjury(state.getInjury())
        .setImg(state.getImg())
        .build();
  }
}
