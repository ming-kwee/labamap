package io.users.doctor.domain;

import com.google.protobuf.Empty;
import io.users.doctor.api.DoctorApi;
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
 * TestKit for unit testing Doctor
 */
public final class DoctorTestKit {

  private DoctorDomain.DoctorState state;
  private Doctor entity;
  private String entityId;

  /**
   * Create a testkit instance of Doctor
   * @param entityFactory A function that creates a Doctor based on the given ValueEntityContext,
   *                      a default entity id is used.
   */
  public static DoctorTestKit of(Function<ValueEntityContext, Doctor> entityFactory) {
    return of("testkit-entity-id", entityFactory);
  }

  /**
   * Create a testkit instance of Doctor with a specific entity id.
   */
  public static DoctorTestKit of(String entityId, Function<ValueEntityContext, Doctor> entityFactory) {
    return new DoctorTestKit(entityFactory.apply(new TestKitValueEntityContext(entityId)), entityId);
  }

  /** Construction is done through the static DoctorTestKit.of-methods */
  private DoctorTestKit(Doctor entity, String entityId) {
    this.entityId = entityId;
    this.state = entity.emptyState();
    this.entity = entity;
  }

  private DoctorTestKit(Doctor entity, String entityId, DoctorDomain.DoctorState state) {
    this.entityId = entityId;
    this.state = state;
    this.entity = entity;
  }

  /**
   * @return The current state of the Doctor under test
   */
  public DoctorDomain.DoctorState getState() {
    return state;
  }

  private <Reply> ValueEntityResult<Reply> interpretEffects(ValueEntity.Effect<Reply> effect) {
    @SuppressWarnings("unchecked")
    ValueEntityResultImpl<Reply> result = new ValueEntityResultImpl<>(effect);
    if (result.stateWasUpdated()) {
      this.state = (DoctorDomain.DoctorState) result.getUpdatedState();
    }
    return result;
  }

  public ValueEntityResult<Empty> createDoctor(DoctorApi.Doctor doctor, Metadata metadata) {
    entity ._internalSetCommandContext(Optional.of(new TestKitValueEntityCommandContext(entityId, metadata)));
    ValueEntity.Effect<Empty> effect = entity.createDoctor(state, doctor);
    return interpretEffects(effect);
  }

  public ValueEntityResult<DoctorApi.Doctor> getDoctor(DoctorApi.GetDoctorRequest getDoctorRequest, Metadata metadata) {
    entity ._internalSetCommandContext(Optional.of(new TestKitValueEntityCommandContext(entityId, metadata)));
    ValueEntity.Effect<DoctorApi.Doctor> effect = entity.getDoctor(state, getDoctorRequest);
    return interpretEffects(effect);
  }

  public ValueEntityResult<Empty> createDoctor(DoctorApi.Doctor doctor) {
    entity ._internalSetCommandContext(Optional.of(new TestKitValueEntityCommandContext(entityId, Metadata.EMPTY)));
    ValueEntity.Effect<Empty> effect = entity.createDoctor(state, doctor);
    return interpretEffects(effect);
  }

  public ValueEntityResult<DoctorApi.Doctor> getDoctor(DoctorApi.GetDoctorRequest getDoctorRequest) {
    entity ._internalSetCommandContext(Optional.of(new TestKitValueEntityCommandContext(entityId, Metadata.EMPTY)));
    ValueEntity.Effect<DoctorApi.Doctor> effect = entity.getDoctor(state, getDoctorRequest);
    return interpretEffects(effect);
  }
}
