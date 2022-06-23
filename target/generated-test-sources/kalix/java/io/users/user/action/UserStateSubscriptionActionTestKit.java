package io.users.user.action;

import io.users.user.action.UserStateSubscriptionAction;
import io.users.user.api.UserApi;
import io.users.user.domain.UserDomain;
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

public final class UserStateSubscriptionActionTestKit {

  private Function<ActionCreationContext, UserStateSubscriptionAction> actionFactory;

  private UserStateSubscriptionAction createAction(TestKitActionContext context) {
    UserStateSubscriptionAction action = actionFactory.apply(context);
    action._internalSetActionContext(Optional.of(context));
    return action;
  }

  public static UserStateSubscriptionActionTestKit of(Function<ActionCreationContext, UserStateSubscriptionAction> actionFactory) {
    return new UserStateSubscriptionActionTestKit(actionFactory);
  }

  private UserStateSubscriptionActionTestKit(Function<ActionCreationContext, UserStateSubscriptionAction> actionFactory) {
    this.actionFactory = actionFactory;
  }

  private <E> ActionResult<E> interpretEffects(Effect<E> effect) {
    return new ActionResultImpl(effect);
  }

  public ActionResult<UserApi.User> onStateChange(UserDomain.UserState userState, Metadata metadata) {
    TestKitActionContext context = new TestKitActionContext(metadata);
    Effect<UserApi.User> effect = createAction(context).onStateChange(userState);
    return interpretEffects(effect);
  }

  public ActionResult<UserApi.User> onStateChange(UserDomain.UserState userState) {
    return onStateChange(userState, Metadata.EMPTY);
  }

}
