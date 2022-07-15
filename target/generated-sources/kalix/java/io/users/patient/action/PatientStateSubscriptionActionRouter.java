package io.users.patient.action;

import akka.NotUsed;
import akka.stream.javadsl.Source;
import io.users.patient.api.PatientApi;
import io.users.patient.domain.PatientDomain;
import kalix.javasdk.action.Action.Effect;
import kalix.javasdk.action.MessageEnvelope;
import kalix.javasdk.impl.action.ActionRouter;

// This code is managed by Kalix tooling.
// It will be re-generated to reflect any changes to your protobuf definitions.
// DO NOT EDIT

public class PatientStateSubscriptionActionRouter extends ActionRouter<PatientStateSubscriptionAction> {

  public PatientStateSubscriptionActionRouter(PatientStateSubscriptionAction actionBehavior) {
    super(actionBehavior);
  }

  @Override
  public Effect<?> handleUnary(String commandName, MessageEnvelope<Object> message) {
    switch (commandName) {
      case "OnStateChange":
        return action()
                 .onStateChange((PatientDomain.PatientState) message.payload());
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
