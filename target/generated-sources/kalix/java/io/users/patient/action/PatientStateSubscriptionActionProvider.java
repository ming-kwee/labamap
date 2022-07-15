package io.users.patient.action;

import com.google.protobuf.Descriptors;
import io.users.patient.api.PatientApi;
import io.users.patient.domain.PatientDomain;
import kalix.javasdk.action.ActionCreationContext;
import kalix.javasdk.action.ActionOptions;
import kalix.javasdk.action.ActionProvider;
import kalix.javasdk.impl.action.ActionRouter;

import java.util.function.Function;

// This code is managed by Kalix tooling.
// It will be re-generated to reflect any changes to your protobuf definitions.
// DO NOT EDIT

/**
 * PatientStateSubscriptionActionProvider that defines how to register and create the action for
 * the Protobuf service <code>PatientStateSubscription</code>.
 *
 * Should be used with the <code>register</code> method in {@link kalix.javasdk.Kalix}.
 */
public class PatientStateSubscriptionActionProvider implements ActionProvider<PatientStateSubscriptionAction> {

  private final Function<ActionCreationContext, PatientStateSubscriptionAction> actionFactory;
  private final ActionOptions options;

  /** Factory method of PatientStateSubscriptionActionProvider */
  public static PatientStateSubscriptionActionProvider of(Function<ActionCreationContext, PatientStateSubscriptionAction> actionFactory) {
    return new PatientStateSubscriptionActionProvider(actionFactory, ActionOptions.defaults());
  }

  private PatientStateSubscriptionActionProvider(Function<ActionCreationContext, PatientStateSubscriptionAction> actionFactory, ActionOptions options) {
    this.actionFactory = actionFactory;
    this.options = options;
  }

  @Override
  public final ActionOptions options() {
    return options;
  }

  public final PatientStateSubscriptionActionProvider withOptions(ActionOptions options) {
    return new PatientStateSubscriptionActionProvider(actionFactory, options);
  }

  @Override
  public final Descriptors.ServiceDescriptor serviceDescriptor() {
    return PatientAction.getDescriptor().findServiceByName("PatientStateSubscription");
  }

  @Override
  public final PatientStateSubscriptionActionRouter newRouter(ActionCreationContext context) {
    return new PatientStateSubscriptionActionRouter(actionFactory.apply(context));
  }

  @Override
  public final Descriptors.FileDescriptor[] additionalDescriptors() {
    return new Descriptors.FileDescriptor[] {
      PatientAction.getDescriptor(),
      PatientApi.getDescriptor(),
      PatientDomain.getDescriptor()
    };
  }

}
