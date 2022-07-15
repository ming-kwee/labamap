package io.users.patient.action;

import io.users.patient.action.PatientStateSubscriptionAction;
import io.users.patient.api.PatientApi;
import io.users.patient.domain.PatientDomain;
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

public final class PatientStateSubscriptionActionTestKit {

  private Function<ActionCreationContext, PatientStateSubscriptionAction> actionFactory;

  private PatientStateSubscriptionAction createAction(TestKitActionContext context) {
    PatientStateSubscriptionAction action = actionFactory.apply(context);
    action._internalSetActionContext(Optional.of(context));
    return action;
  }

  public static PatientStateSubscriptionActionTestKit of(Function<ActionCreationContext, PatientStateSubscriptionAction> actionFactory) {
    return new PatientStateSubscriptionActionTestKit(actionFactory);
  }

  private PatientStateSubscriptionActionTestKit(Function<ActionCreationContext, PatientStateSubscriptionAction> actionFactory) {
    this.actionFactory = actionFactory;
  }

  private <E> ActionResult<E> interpretEffects(Effect<E> effect) {
    return new ActionResultImpl(effect);
  }

  public ActionResult<PatientApi.Patient> onStateChange(PatientDomain.PatientState patientState, Metadata metadata) {
    TestKitActionContext context = new TestKitActionContext(metadata);
    Effect<PatientApi.Patient> effect = createAction(context).onStateChange(patientState);
    return interpretEffects(effect);
  }

  public ActionResult<PatientApi.Patient> onStateChange(PatientDomain.PatientState patientState) {
    return onStateChange(patientState, Metadata.EMPTY);
  }

}
