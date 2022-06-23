package io.users.auth.action;

import io.users.auth.action.AuthActionApi;
import io.users.auth.action.AuthActionImpl;
import kalix.javasdk.Metadata;
import kalix.javasdk.action.Action.Effect;
import kalix.javasdk.action.ActionCreationContext;
import kalix.javasdk.impl.action.ActionEffectImpl;
import kalix.javasdk.testkit.ActionResult;
import kalix.javasdk.testkit.impl.ActionResultImpl;
import kalix.javasdk.testkit.impl.TestKitActionContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

// This code is managed by Kalix tooling.
// It will be re-generated to reflect any changes to your protobuf definitions.
// DO NOT EDIT

public final class AuthActionImplTestKit {

  private Function<ActionCreationContext, AuthActionImpl> actionFactory;

  private AuthActionImpl createAction(TestKitActionContext context) {
    AuthActionImpl action = actionFactory.apply(context);
    action._internalSetActionContext(Optional.of(context));
    return action;
  }

  public static AuthActionImplTestKit of(Function<ActionCreationContext, AuthActionImpl> actionFactory) {
    return new AuthActionImplTestKit(actionFactory);
  }

  private AuthActionImplTestKit(Function<ActionCreationContext, AuthActionImpl> actionFactory) {
    this.actionFactory = actionFactory;
  }

  private <E> ActionResult<E> interpretEffects(Effect<E> effect) {
    return new ActionResultImpl(effect);
  }

  public ActionResult<AuthActionApi.Auth> register(AuthActionApi.Auth auth, Metadata metadata) {
    TestKitActionContext context = new TestKitActionContext(metadata);
    Effect<AuthActionApi.Auth> effect = createAction(context).register(auth);
    return interpretEffects(effect);
  }

  public ActionResult<AuthActionApi.Auth> register(AuthActionApi.Auth auth) {
    return register(auth, Metadata.EMPTY);
  }

}
