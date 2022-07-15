package io.users.doctor.action;

import com.google.protobuf.Descriptors;
import io.users.doctor.api.DoctorApi;
import io.users.doctor.domain.DoctorDomain;
import kalix.javasdk.action.ActionCreationContext;
import kalix.javasdk.action.ActionOptions;
import kalix.javasdk.action.ActionProvider;
import kalix.javasdk.impl.action.ActionRouter;

import java.util.function.Function;

// This code is managed by Kalix tooling.
// It will be re-generated to reflect any changes to your protobuf definitions.
// DO NOT EDIT

/**
 * DoctorStateSubscriptionActionProvider that defines how to register and create the action for
 * the Protobuf service <code>DoctorStateSubscription</code>.
 *
 * Should be used with the <code>register</code> method in {@link kalix.javasdk.Kalix}.
 */
public class DoctorStateSubscriptionActionProvider implements ActionProvider<DoctorStateSubscriptionAction> {

  private final Function<ActionCreationContext, DoctorStateSubscriptionAction> actionFactory;
  private final ActionOptions options;

  /** Factory method of DoctorStateSubscriptionActionProvider */
  public static DoctorStateSubscriptionActionProvider of(Function<ActionCreationContext, DoctorStateSubscriptionAction> actionFactory) {
    return new DoctorStateSubscriptionActionProvider(actionFactory, ActionOptions.defaults());
  }

  private DoctorStateSubscriptionActionProvider(Function<ActionCreationContext, DoctorStateSubscriptionAction> actionFactory, ActionOptions options) {
    this.actionFactory = actionFactory;
    this.options = options;
  }

  @Override
  public final ActionOptions options() {
    return options;
  }

  public final DoctorStateSubscriptionActionProvider withOptions(ActionOptions options) {
    return new DoctorStateSubscriptionActionProvider(actionFactory, options);
  }

  @Override
  public final Descriptors.ServiceDescriptor serviceDescriptor() {
    return DoctorAction.getDescriptor().findServiceByName("DoctorStateSubscription");
  }

  @Override
  public final DoctorStateSubscriptionActionRouter newRouter(ActionCreationContext context) {
    return new DoctorStateSubscriptionActionRouter(actionFactory.apply(context));
  }

  @Override
  public final Descriptors.FileDescriptor[] additionalDescriptors() {
    return new Descriptors.FileDescriptor[] {
      DoctorAction.getDescriptor(),
      DoctorApi.getDescriptor(),
      DoctorDomain.getDescriptor()
    };
  }

}
