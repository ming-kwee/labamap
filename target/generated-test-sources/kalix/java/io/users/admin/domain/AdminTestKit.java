package io.users.admin.domain;

import com.google.protobuf.Empty;
import io.users.admin.api.AdminApi;
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
 * TestKit for unit testing Admin
 */
public final class AdminTestKit {

  private AdminDomain.AdminState state;
  private Admin entity;
  private String entityId;

  /**
   * Create a testkit instance of Admin
   * @param entityFactory A function that creates a Admin based on the given ValueEntityContext,
   *                      a default entity id is used.
   */
  public static AdminTestKit of(Function<ValueEntityContext, Admin> entityFactory) {
    return of("testkit-entity-id", entityFactory);
  }

  /**
   * Create a testkit instance of Admin with a specific entity id.
   */
  public static AdminTestKit of(String entityId, Function<ValueEntityContext, Admin> entityFactory) {
    return new AdminTestKit(entityFactory.apply(new TestKitValueEntityContext(entityId)), entityId);
  }

  /** Construction is done through the static AdminTestKit.of-methods */
  private AdminTestKit(Admin entity, String entityId) {
    this.entityId = entityId;
    this.state = entity.emptyState();
    this.entity = entity;
  }

  private AdminTestKit(Admin entity, String entityId, AdminDomain.AdminState state) {
    this.entityId = entityId;
    this.state = state;
    this.entity = entity;
  }

  /**
   * @return The current state of the Admin under test
   */
  public AdminDomain.AdminState getState() {
    return state;
  }

  private <Reply> ValueEntityResult<Reply> interpretEffects(ValueEntity.Effect<Reply> effect) {
    @SuppressWarnings("unchecked")
    ValueEntityResultImpl<Reply> result = new ValueEntityResultImpl<>(effect);
    if (result.stateWasUpdated()) {
      this.state = (AdminDomain.AdminState) result.getUpdatedState();
    }
    return result;
  }

  public ValueEntityResult<Empty> createAdmin(AdminApi.Admin admin, Metadata metadata) {
    entity ._internalSetCommandContext(Optional.of(new TestKitValueEntityCommandContext(entityId, metadata)));
    ValueEntity.Effect<Empty> effect = entity.createAdmin(state, admin);
    return interpretEffects(effect);
  }

  public ValueEntityResult<AdminApi.Admin> getAdmin(AdminApi.GetAdminRequest getAdminRequest, Metadata metadata) {
    entity ._internalSetCommandContext(Optional.of(new TestKitValueEntityCommandContext(entityId, metadata)));
    ValueEntity.Effect<AdminApi.Admin> effect = entity.getAdmin(state, getAdminRequest);
    return interpretEffects(effect);
  }

  public ValueEntityResult<Empty> updateAdmin(AdminApi.Admin admin, Metadata metadata) {
    entity ._internalSetCommandContext(Optional.of(new TestKitValueEntityCommandContext(entityId, metadata)));
    ValueEntity.Effect<Empty> effect = entity.updateAdmin(state, admin);
    return interpretEffects(effect);
  }

  public ValueEntityResult<Empty> createAdmin(AdminApi.Admin admin) {
    entity ._internalSetCommandContext(Optional.of(new TestKitValueEntityCommandContext(entityId, Metadata.EMPTY)));
    ValueEntity.Effect<Empty> effect = entity.createAdmin(state, admin);
    return interpretEffects(effect);
  }

  public ValueEntityResult<AdminApi.Admin> getAdmin(AdminApi.GetAdminRequest getAdminRequest) {
    entity ._internalSetCommandContext(Optional.of(new TestKitValueEntityCommandContext(entityId, Metadata.EMPTY)));
    ValueEntity.Effect<AdminApi.Admin> effect = entity.getAdmin(state, getAdminRequest);
    return interpretEffects(effect);
  }

  public ValueEntityResult<Empty> updateAdmin(AdminApi.Admin admin) {
    entity ._internalSetCommandContext(Optional.of(new TestKitValueEntityCommandContext(entityId, Metadata.EMPTY)));
    ValueEntity.Effect<Empty> effect = entity.updateAdmin(state, admin);
    return interpretEffects(effect);
  }
}
