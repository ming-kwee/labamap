package io.users.auth.action;

import io.users.auth.action.AuthActionApi;
import io.users.auth.action.AuthActionImpl;
import kalix.javasdk.Metadata;
import kalix.javasdk.action.Action.Effect;
import kalix.javasdk.action.ActionCreationContext;
import kalix.javasdk.testkit.ActionResult;
import kalix.javasdk.testkit.MockRegistry;
import kalix.javasdk.testkit.impl.ActionResultImpl;
import kalix.javasdk.testkit.impl.TestKitActionContext;

import java.util.Optional;
import java.util.function.Function;

// This code is managed by Kalix tooling.
// It will be re-generated to reflect any changes to your protobuf definitions.
// DO NOT EDIT

public final class AuthActionImplTestKit {

  private final Function<ActionCreationContext, AuthActionImpl> actionFactory;

  private final MockRegistry mockRegistry;

  private AuthActionImpl createAction(TestKitActionContext context) {
    AuthActionImpl action = actionFactory.apply(context);
    action._internalSetActionContext(Optional.of(context));
    return action;
  }

  public static AuthActionImplTestKit of(Function<ActionCreationContext, AuthActionImpl> actionFactory) {
    return new AuthActionImplTestKit(actionFactory, MockRegistry.EMPTY);
  }

  public static AuthActionImplTestKit of(Function<ActionCreationContext, AuthActionImpl> actionFactory, MockRegistry mockRegistry) {
    return new AuthActionImplTestKit(actionFactory, mockRegistry);
  }

  private AuthActionImplTestKit(Function<ActionCreationContext, AuthActionImpl> actionFactory, MockRegistry mockRegistry) {
    this.actionFactory = actionFactory;
    this.mockRegistry = mockRegistry;
  }

  private <E> ActionResult<E> interpretEffects(Effect<E> effect) {
    return new ActionResultImpl(effect);
  }

  public ActionResult<AuthActionApi.Auth> register(AuthActionApi.Auth auth, Metadata metadata) {
    TestKitActionContext context = new TestKitActionContext(metadata, mockRegistry);
    Effect<AuthActionApi.Auth> effect = createAction(context).register(auth);
    return interpretEffects(effect);
  }

  public ActionResult<AuthActionApi.Auth> updateUser(AuthActionApi.Auth auth, Metadata metadata) {
    TestKitActionContext context = new TestKitActionContext(metadata, mockRegistry);
    Effect<AuthActionApi.Auth> effect = createAction(context).updateUser(auth);
    return interpretEffects(effect);
  }

  public ActionResult<AuthActionApi.Auth> register(AuthActionApi.Auth auth) {
    return register(auth, Metadata.EMPTY);
  }

  public ActionResult<AuthActionApi.Auth> updateUser(AuthActionApi.Auth auth) {
    return updateUser(auth, Metadata.EMPTY);
  }

}
