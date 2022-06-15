package components.users.user.action;

import com.google.protobuf.Descriptors;
import components.users.user.api.UserApi;
import components.users.user.domain.UserDomain;
import kalix.javasdk.action.ActionCreationContext;
import kalix.javasdk.action.ActionOptions;
import kalix.javasdk.action.ActionProvider;
import kalix.javasdk.impl.action.ActionRouter;

import java.util.function.Function;

// This code is managed by Kalix tooling.
// It will be re-generated to reflect any changes to your protobuf definitions.
// DO NOT EDIT

/**
 * UserStateSubscriptionActionProvider that defines how to register and create the action for
 * the Protobuf service <code>UserStateSubscription</code>.
 *
 * Should be used with the <code>register</code> method in {@link kalix.javasdk.Kalix}.
 */
public class UserStateSubscriptionActionProvider implements ActionProvider<UserStateSubscriptionAction> {

  private final Function<ActionCreationContext, UserStateSubscriptionAction> actionFactory;
  private final ActionOptions options;

  /** Factory method of UserStateSubscriptionActionProvider */
  public static UserStateSubscriptionActionProvider of(Function<ActionCreationContext, UserStateSubscriptionAction> actionFactory) {
    return new UserStateSubscriptionActionProvider(actionFactory, ActionOptions.defaults());
  }

  private UserStateSubscriptionActionProvider(Function<ActionCreationContext, UserStateSubscriptionAction> actionFactory, ActionOptions options) {
    this.actionFactory = actionFactory;
    this.options = options;
  }

  @Override
  public final ActionOptions options() {
    return options;
  }

  public final UserStateSubscriptionActionProvider withOptions(ActionOptions options) {
    return new UserStateSubscriptionActionProvider(actionFactory, options);
  }

  @Override
  public final Descriptors.ServiceDescriptor serviceDescriptor() {
    return UserAction.getDescriptor().findServiceByName("UserStateSubscription");
  }

  @Override
  public final UserStateSubscriptionActionRouter newRouter(ActionCreationContext context) {
    return new UserStateSubscriptionActionRouter(actionFactory.apply(context));
  }

  @Override
  public final Descriptors.FileDescriptor[] additionalDescriptors() {
    return new Descriptors.FileDescriptor[] {
      UserAction.getDescriptor(),
      UserApi.getDescriptor(),
      UserDomain.getDescriptor()
    };
  }

}
