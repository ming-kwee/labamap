package user.action;

import akka.NotUsed;
import akka.stream.javadsl.Source;
import kalix.javasdk.action.Action.Effect;
import kalix.javasdk.action.MessageEnvelope;
import kalix.javasdk.impl.action.ActionRouter;
import user.api.UserApi;
import user.domain.UserDomain;

// This code is managed by Kalix tooling.
// It will be re-generated to reflect any changes to your protobuf definitions.
// DO NOT EDIT

public class UserStateSubscriptionActionRouter extends ActionRouter<UserStateSubscriptionAction> {

  public UserStateSubscriptionActionRouter(UserStateSubscriptionAction actionBehavior) {
    super(actionBehavior);
  }

  @Override
  public Effect<?> handleUnary(String commandName, MessageEnvelope<Object> message) {
    switch (commandName) {
      case "OnStateChange":
        return action()
                 .onStateChange((UserDomain.UserState) message.payload());
      default:
        throw new ActionRouter.HandlerNotFound(commandName);
    }
  }

  @Override
  @SuppressWarnings("unchecked")
  public Source<Effect<?>, NotUsed> handleStreamedOut(String commandName, MessageEnvelope<Object> message) {
    switch (commandName) {
      
      default:
        throw new ActionRouter.HandlerNotFound(commandName);
    }
  }

  @Override
  public Effect<?> handleStreamedIn(String commandName, Source<MessageEnvelope<Object>, NotUsed> stream) {
    switch (commandName) {
      
      default:
        throw new ActionRouter.HandlerNotFound(commandName);
    }
  }

  @Override
  @SuppressWarnings("unchecked")
  public Source<Effect<?>, NotUsed> handleStreamed(String commandName, Source<MessageEnvelope<Object>, NotUsed> stream) {
    switch (commandName) {
      
      default:
        throw new ActionRouter.HandlerNotFound(commandName);
    }
  }
}
