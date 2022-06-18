package components.users.auth.action;

import com.google.protobuf.Descriptors;
import kalix.javasdk.action.ActionCreationContext;
import kalix.javasdk.action.ActionOptions;
import kalix.javasdk.action.ActionProvider;
import kalix.javasdk.impl.action.ActionRouter;

import java.util.function.Function;

// This code is managed by Kalix tooling.
// It will be re-generated to reflect any changes to your protobuf definitions.
// DO NOT EDIT

/**
 * AuthActionProvider that defines how to register and create the action for
 * the Protobuf service <code>AuthAction</code>.
 *
 * Should be used with the <code>register</code> method in {@link kalix.javasdk.Kalix}.
 */
public class AuthActionProvider implements ActionProvider<AuthActionImpl> {

  private final Function<ActionCreationContext, AuthActionImpl> actionFactory;
  private final ActionOptions options;

  /** Factory method of AuthActionProvider */
  public static AuthActionProvider of(Function<ActionCreationContext, AuthActionImpl> actionFactory) {
    return new AuthActionProvider(actionFactory, ActionOptions.defaults());
  }

  private AuthActionProvider(Function<ActionCreationContext, AuthActionImpl> actionFactory, ActionOptions options) {
    this.actionFactory = actionFactory;
    this.options = options;
  }

  @Override
  public final ActionOptions options() {
    return options;
  }

  public final AuthActionProvider withOptions(ActionOptions options) {
    return new AuthActionProvider(actionFactory, options);
  }

  @Override
  public final Descriptors.ServiceDescriptor serviceDescriptor() {
    return AuthActionApi.getDescriptor().findServiceByName("AuthAction");
  }

  @Override
  public final AuthActionRouter newRouter(ActionCreationContext context) {
    return new AuthActionRouter(actionFactory.apply(context));
  }

  @Override
  public final Descriptors.FileDescriptor[] additionalDescriptors() {
    return new Descriptors.FileDescriptor[] {
      AuthActionApi.getDescriptor()
    };
  }

}
