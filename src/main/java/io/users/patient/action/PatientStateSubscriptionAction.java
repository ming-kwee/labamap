package io.users.patient.action;

import io.users.patient.api.PatientApi;
import io.users.patient.domain.PatientDomain;
import kalix.javasdk.action.ActionCreationContext;

// This class was initially generated based on the .proto definition by Kalix tooling.
// This is the implementation for the Action Service described in your io/users/patient/action/patient_action.proto file.
//
// As long as this file exists it will not be overwritten: you can maintain it yourself,
// or delete it so it is regenerated as needed.

public class PatientStateSubscriptionAction extends AbstractPatientStateSubscriptionAction {

  public PatientStateSubscriptionAction(ActionCreationContext creationContext) {}

  @Override
  public Effect<PatientApi.Patient> onStateChange(PatientDomain.PatientState patientState) {
    PatientApi.Patient patient = PatientApi.Patient.newBuilder()
        .setId(patientState.getId())
        .setEmail(patientState.getEmail())
        .setImg(patientState.getImg())
        // .setPassword(patientState.getPassword())
        .setFirstName(patientState.getFirstName())
        .setLastName(patientState.getLastName())
        .setRole(patientState.getRole())
        // Patient
        .setGender(patientState.getGender())
        .setMobile(patientState.getMobile())
        .setDateOfBirth(patientState.getDateOfBirth())
        .setAge(patientState.getAge())
        .setMaritalStatus(patientState.getMaritalStatus())
        .setAddress(patientState.getAddress())
        .setBloodGroup(patientState.getBloodGroup())
        .setBloodPressure(patientState.getBloodPressure())
        .setSugger(patientState.getSugger())
        .setInjury(patientState.getInjury())
        .setImg(patientState.getImg())
        .build();

    return effects().reply(patient);
  }
}
