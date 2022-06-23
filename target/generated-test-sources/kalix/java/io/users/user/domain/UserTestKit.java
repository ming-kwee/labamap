package io.users.user.domain;

import com.google.protobuf.Empty;
import io.users.user.api.UserApi;
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
 * TestKit for unit testing User
 */
public final class UserTestKit {

  private UserDomain.UserState state;
  private User entity;
  private String entityId;

  /**
   * Create a testkit instance of User
   * @param entityFactory A function that creates a User based on the given ValueEntityContext,
   *                      a default entity id is used.
   */
  public static UserTestKit of(Function<ValueEntityContext, User> entityFactory) {
    return of("testkit-entity-id", entityFactory);
  }

  /**
   * Create a testkit instance of User with a specific entity id.
   */
  public static UserTestKit of(String entityId, Function<ValueEntityContext, User> entityFactory) {
    return new UserTestKit(entityFactory.apply(new TestKitValueEntityContext(entityId)), entityId);
  }

  /** Construction is done through the static UserTestKit.of-methods */
  private UserTestKit(User entity, String entityId) {
    this.entityId = entityId;
    this.state = entity.emptyState();
    this.entity = entity;
  }

  private UserTestKit(User entity, String entityId, UserDomain.UserState state) {
    this.entityId = entityId;
    this.state = state;
    this.entity = entity;
  }

  /**
   * @return The current state of the User under test
   */
  public UserDomain.UserState getState() {
    return state;
  }

  private <Reply> ValueEntityResult<Reply> interpretEffects(ValueEntity.Effect<Reply> effect) {
    @SuppressWarnings("unchecked")
    ValueEntityResultImpl<Reply> result = new ValueEntityResultImpl<>(effect);
    if (result.stateWasUpdated()) {
      this.state = (UserDomain.UserState) result.getUpdatedState();
    }
    return result;
  }

  public ValueEntityResult<Empty> createUser(UserApi.User user, Metadata metadata) {
    entity ._internalSetCommandContext(Optional.of(new TestKitValueEntityCommandContext(entityId, metadata)));
    ValueEntity.Effect<Empty> effect = entity.createUser(state, user);
    return interpretEffects(effect);
  }

  public ValueEntityResult<UserApi.User> getUser(UserApi.GetUserRequest getUserRequest, Metadata metadata) {
    entity ._internalSetCommandContext(Optional.of(new TestKitValueEntityCommandContext(entityId, metadata)));
    ValueEntity.Effect<UserApi.User> effect = entity.getUser(state, getUserRequest);
    return interpretEffects(effect);
  }

  public ValueEntityResult<Empty> createUser(UserApi.User user) {
    entity ._internalSetCommandContext(Optional.of(new TestKitValueEntityCommandContext(entityId, Metadata.EMPTY)));
    ValueEntity.Effect<Empty> effect = entity.createUser(state, user);
    return interpretEffects(effect);
  }

  public ValueEntityResult<UserApi.User> getUser(UserApi.GetUserRequest getUserRequest) {
    entity ._internalSetCommandContext(Optional.of(new TestKitValueEntityCommandContext(entityId, Metadata.EMPTY)));
    ValueEntity.Effect<UserApi.User> effect = entity.getUser(state, getUserRequest);
    return interpretEffects(effect);
  }
}
