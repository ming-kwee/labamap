package io.users.auth.domain;

import com.google.protobuf.Empty;
import io.users.auth.action.AuthActionApi;
import io.users.auth.api.AuthApi;
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
 * TestKit for unit testing Auth
 */
public final class AuthTestKit {

  private AuthDomain.AuthState state;
  private Auth entity;
  private String entityId;

  /**
   * Create a testkit instance of Auth
   * @param entityFactory A function that creates a Auth based on the given ValueEntityContext,
   *                      a default entity id is used.
   */
  public static AuthTestKit of(Function<ValueEntityContext, Auth> entityFactory) {
    return of("testkit-entity-id", entityFactory);
  }

  /**
   * Create a testkit instance of Auth with a specific entity id.
   */
  public static AuthTestKit of(String entityId, Function<ValueEntityContext, Auth> entityFactory) {
    return new AuthTestKit(entityFactory.apply(new TestKitValueEntityContext(entityId)), entityId);
  }

  /** Construction is done through the static AuthTestKit.of-methods */
  private AuthTestKit(Auth entity, String entityId) {
    this.entityId = entityId;
    this.state = entity.emptyState();
    this.entity = entity;
  }

  private AuthTestKit(Auth entity, String entityId, AuthDomain.AuthState state) {
    this.entityId = entityId;
    this.state = state;
    this.entity = entity;
  }

  /**
   * @return The current state of the Auth under test
   */
  public AuthDomain.AuthState getState() {
    return state;
  }

  private <Reply> ValueEntityResult<Reply> interpretEffects(ValueEntity.Effect<Reply> effect) {
    @SuppressWarnings("unchecked")
    ValueEntityResultImpl<Reply> result = new ValueEntityResultImpl<>(effect);
    if (result.stateWasUpdated()) {
      this.state = (AuthDomain.AuthState) result.getUpdatedState();
    }
    return result;
  }

  public ValueEntityResult<Empty> register(AuthActionApi.Auth auth, Metadata metadata) {
    entity ._internalSetCommandContext(Optional.of(new TestKitValueEntityCommandContext(entityId, metadata)));
    ValueEntity.Effect<Empty> effect = entity.register(state, auth);
    return interpretEffects(effect);
  }

  public ValueEntityResult<AuthActionApi.Auth> login(AuthApi.GetLoginRequest getLoginRequest, Metadata metadata) {
    entity ._internalSetCommandContext(Optional.of(new TestKitValueEntityCommandContext(entityId, metadata)));
    ValueEntity.Effect<AuthActionApi.Auth> effect = entity.login(state, getLoginRequest);
    return interpretEffects(effect);
  }

  public ValueEntityResult<Empty> register(AuthActionApi.Auth auth) {
    entity ._internalSetCommandContext(Optional.of(new TestKitValueEntityCommandContext(entityId, Metadata.EMPTY)));
    ValueEntity.Effect<Empty> effect = entity.register(state, auth);
    return interpretEffects(effect);
  }

  public ValueEntityResult<AuthActionApi.Auth> login(AuthApi.GetLoginRequest getLoginRequest) {
    entity ._internalSetCommandContext(Optional.of(new TestKitValueEntityCommandContext(entityId, Metadata.EMPTY)));
    ValueEntity.Effect<AuthActionApi.Auth> effect = entity.login(state, getLoginRequest);
    return interpretEffects(effect);
  }
}
