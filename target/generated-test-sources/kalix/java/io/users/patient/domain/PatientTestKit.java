package io.users.patient.domain;

import com.google.protobuf.Empty;
import io.users.patient.api.PatientApi;
import kalix.javasdk.Metadata;
import kalix.javasdk.impl.effect.MessageReplyImpl;
import kalix.javasdk.impl.effect.SecondaryEffectImpl;
import kalix.javasdk.impl.valueentity.ValueEntityEffectImpl;
import kalix.javasdk.testkit.ValueEntityResult;
import kalix.javasdk.testkit.impl.TestKitValueEntityCommandContext;
import kalix.javasdk.testkit.impl.TestKitValueEntityContext;
import kalix.javasdk.testkit.impl.ValueEntityResultImpl;
import kalix.javasdk.valueentity.ValueEntity;
import kalix.javasdk.valueentity.ValueEntityContext;

import java.util.Optional;
import java.util.function.Function;

// This code is managed by Kalix tooling.
// It will be re-generated to reflect any changes to your protobuf definitions.
// DO NOT EDIT

/**
 * TestKit for unit testing Patient
 */
public final class PatientTestKit {

  private PatientDomain.PatientState state;
  private Patient entity;
  private String entityId;

  /**
   * Create a testkit instance of Patient
   * @param entityFactory A function that creates a Patient based on the given ValueEntityContext,
   *                      a default entity id is used.
   */
  public static PatientTestKit of(Function<ValueEntityContext, Patient> entityFactory) {
    return of("testkit-entity-id", entityFactory);
  }

  /**
   * Create a testkit instance of Patient with a specific entity id.
   */
  public static PatientTestKit of(String entityId, Function<ValueEntityContext, Patient> entityFactory) {
    return new PatientTestKit(entityFactory.apply(new TestKitValueEntityContext(entityId)), entityId);
  }

  /** Construction is done through the static PatientTestKit.of-methods */
  private PatientTestKit(Patient entity, String entityId) {
    this.entityId = entityId;
    this.state = entity.emptyState();
    this.entity = entity;
  }

  private PatientTestKit(Patient entity, String entityId, PatientDomain.PatientState state) {
    this.entityId = entityId;
    this.state = state;
    this.entity = entity;
  }

  /**
   * @return The current state of the Patient under test
   */
  public PatientDomain.PatientState getState() {
    return state;
  }

  private <Reply> ValueEntityResult<Reply> interpretEffects(ValueEntity.Effect<Reply> effect) {
    @SuppressWarnings("unchecked")
    ValueEntityResultImpl<Reply> result = new ValueEntityResultImpl<>(effect);
    if (result.stateWasUpdated()) {
      this.state = (PatientDomain.PatientState) result.getUpdatedState();
    }
    return result;
  }

  public ValueEntityResult<Empty> createPatient(PatientApi.Patient patient, Metadata metadata) {
    entity ._internalSetCommandContext(Optional.of(new TestKitValueEntityCommandContext(entityId, metadata)));
    ValueEntity.Effect<Empty> effect = entity.createPatient(state, patient);
    return interpretEffects(effect);
  }

  public ValueEntityResult<PatientApi.Patient> getPatient(PatientApi.GetPatientRequest getPatientRequest, Metadata metadata) {
    entity ._internalSetCommandContext(Optional.of(new TestKitValueEntityCommandContext(entityId, metadata)));
    ValueEntity.Effect<PatientApi.Patient> effect = entity.getPatient(state, getPatientRequest);
    return interpretEffects(effect);
  }

  public ValueEntityResult<Empty> updatePatient(PatientApi.Patient patient, Metadata metadata) {
    entity ._internalSetCommandContext(Optional.of(new TestKitValueEntityCommandContext(entityId, metadata)));
    ValueEntity.Effect<Empty> effect = entity.updatePatient(state, patient);
    return interpretEffects(effect);
  }

  public ValueEntityResult<Empty> createPatient(PatientApi.Patient patient) {
    entity ._internalSetCommandContext(Optional.of(new TestKitValueEntityCommandContext(entityId, Metadata.EMPTY)));
    ValueEntity.Effect<Empty> effect = entity.createPatient(state, patient);
    return interpretEffects(effect);
  }

  public ValueEntityResult<PatientApi.Patient> getPatient(PatientApi.GetPatientRequest getPatientRequest) {
    entity ._internalSetCommandContext(Optional.of(new TestKitValueEntityCommandContext(entityId, Metadata.EMPTY)));
    ValueEntity.Effect<PatientApi.Patient> effect = entity.getPatient(state, getPatientRequest);
    return interpretEffects(effect);
  }

  public ValueEntityResult<Empty> updatePatient(PatientApi.Patient patient) {
    entity ._internalSetCommandContext(Optional.of(new TestKitValueEntityCommandContext(entityId, Metadata.EMPTY)));
    ValueEntity.Effect<Empty> effect = entity.updatePatient(state, patient);
    return interpretEffects(effect);
  }
}
