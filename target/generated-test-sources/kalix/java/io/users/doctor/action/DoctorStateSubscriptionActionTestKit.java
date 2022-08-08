package io.users.doctor.action;

import io.users.doctor.action.DoctorStateSubscriptionAction;
import io.users.doctor.api.DoctorApi;
import io.users.doctor.domain.DoctorDomain;
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

public final class DoctorStateSubscriptionActionTestKit {

  private final Function<ActionCreationContext, DoctorStateSubscriptionAction> actionFactory;

  private final MockRegistry mockRegistry;

  private DoctorStateSubscriptionAction createAction(TestKitActionContext context) {
    DoctorStateSubscriptionAction action = actionFactory.apply(context);
    action._internalSetActionContext(Optional.of(context));
    return action;
  }

  public static DoctorStateSubscriptionActionTestKit of(Function<ActionCreationContext, DoctorStateSubscriptionAction> actionFactory) {
    return new DoctorStateSubscriptionActionTestKit(actionFactory, MockRegistry.EMPTY);
  }

  public static DoctorStateSubscriptionActionTestKit of(Function<ActionCreationContext, DoctorStateSubscriptionAction> actionFactory, MockRegistry mockRegistry) {
    return new DoctorStateSubscriptionActionTestKit(actionFactory, mockRegistry);
  }

  private DoctorStateSubscriptionActionTestKit(Function<ActionCreationContext, DoctorStateSubscriptionAction> actionFactory, MockRegistry mockRegistry) {
    this.actionFactory = actionFactory;
    this.mockRegistry = mockRegistry;
  }

  private <E> ActionResult<E> interpretEffects(Effect<E> effect) {
    return new ActionResultImpl(effect);
  }

  public ActionResult<DoctorApi.Doctor> onStateChange(DoctorDomain.DoctorState doctorState, Metadata metadata) {
    TestKitActionContext context = new TestKitActionContext(metadata, mockRegistry);
    Effect<DoctorApi.Doctor> effect = createAction(context).onStateChange(doctorState);
    return interpretEffects(effect);
  }

  public ActionResult<DoctorApi.Doctor> onStateChange(DoctorDomain.DoctorState doctorState) {
    return onStateChange(doctorState, Metadata.EMPTY);
  }

}
