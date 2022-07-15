package io.users.doctor.action;

import akka.NotUsed;
import akka.stream.javadsl.Source;
import io.users.doctor.api.DoctorApi;
import io.users.doctor.domain.DoctorDomain;
import kalix.javasdk.action.Action.Effect;
import kalix.javasdk.action.MessageEnvelope;
import kalix.javasdk.impl.action.ActionRouter;

// This code is managed by Kalix tooling.
// It will be re-generated to reflect any changes to your protobuf definitions.
// DO NOT EDIT

public class DoctorStateSubscriptionActionRouter extends ActionRouter<DoctorStateSubscriptionAction> {

  public DoctorStateSubscriptionActionRouter(DoctorStateSubscriptionAction actionBehavior) {
    super(actionBehavior);
  }

  @Override
  public Effect<?> handleUnary(String commandName, MessageEnvelope<Object> message) {
    switch (commandName) {
      case "OnStateChange":
        return action()
                 .onStateChange((DoctorDomain.DoctorState) message.payload());
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
