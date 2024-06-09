package io.products.attribute.domain;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.grpc.Status;
import com.google.protobuf.Empty;
import io.products.attribute.api.AttributeApi;
import kalix.javasdk.action.Action.Effect;
import kalix.javasdk.valueentity.ValueEntityContext;

// This class was initially generated based on the .proto definition by Kalix tooling.
// This is the implementation for the Value Entity Service described in your io/products/attribute/api/attribute_api.proto file.
//
// As long as this file exists it will not be overwritten: you can maintain it yourself,
// or delete it so it is regenerated as needed.

public class Attribute extends AbstractAttribute {
  private static final Logger LOG = LoggerFactory.getLogger(Attribute.class);

  @SuppressWarnings("unused")
  private final String entityId;

  public Attribute(ValueEntityContext context) {
    this.entityId = context.entityId();
  }

  @Override
  public AttributeDomain.AttributeState emptyState() {
        return AttributeDomain.AttributeState.getDefaultInstance();
  }


  /**************************************************
   * CREATE
   **************************************************/
  @Override
  public Effect<Empty> createAttribute(AttributeDomain.AttributeState currentState, AttributeApi.Attribute command) {
    AttributeDomain.AttributeState state = convertToDomain(command);
    return reject(currentState, command).orElseGet(() -> handle(state, command));   
  }



  private Optional<Effect<Empty>> reject(AttributeDomain.AttributeState currentState, io.products.attribute.api.AttributeApi.Attribute command) {


    LOG.info("CURRENT_STATE " + currentState);
    LOG.info("COMMAND " + command);


    if (currentState.getAttributeId().equals(command.getAttributeId())) {
      return Optional.of(effects().error("Attribute is already exists!!", Status.Code.NOT_FOUND));
    } else if (currentState.getAttributeName().equals(command.getAttributeName())) {
      return Optional.of(effects().error("Attribute Name is already exists!!", Status.Code.NOT_FOUND));
    } else {
      return Optional.empty();
    }
  }


  private AttributeDomain.AttributeState convertToDomain(io.products.attribute.api.AttributeApi.Attribute command) {
    return AttributeDomain.AttributeState.newBuilder()
    .setAttributeId(command.getAttributeId())
    .setAttributeName(command.getAttributeName())
    .setAttributeType(command.getAttributeType())
    .setIsCommonField(command.getIsCommonField())
    .setGroup(command.getGroup())
    .build();
  }

  private Effect<Empty> handle(AttributeDomain.AttributeState state, io.products.attribute.api.AttributeApi.Attribute  command) {
    return effects().updateState(state).thenReply(Empty.getDefaultInstance());
  }




  @Override
  public Effect<AttributeApi.Attribute> getAttribute(AttributeDomain.AttributeState currentState, AttributeApi.GetAttributeRequest command) {

    if (currentState.getAttributeId().equals(command.getAttributeId())) {
      return effects().reply(convertToApi(currentState));
    } else {
      return effects().error("Attribute " + command.getAttributeId() + " has not been created.");
    }

  }


  private AttributeApi.Attribute convertToApi(AttributeDomain.AttributeState state) {
    return AttributeApi.Attribute.newBuilder()
    .setAttributeId(state.getAttributeId())
    .setAttributeName(state.getAttributeName())
    .setAttributeType(state.getAttributeType())
    .setIsCommonField(state.getIsCommonField())
    .setGroup(state.getGroup())
    .build();
  }



  @Override
  public Effect<Empty> updateAttribute(AttributeDomain.AttributeState currentState, AttributeApi.Attribute attribute) {
    return effects().error("The command handler for `UpdateAttribute` is not implemented, yet");
  }

  @Override
  public Effect<Empty> deleteAttribute(AttributeDomain.AttributeState currentState, AttributeApi.DeleteAttributeRequest deleteAttributeRequest) {
    return effects().error("The command handler for `DeleteAttribute` is not implemented, yet");
  }
}
