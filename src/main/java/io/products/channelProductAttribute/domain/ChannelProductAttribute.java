package io.products.channelProductAttribute.domain;

import com.google.protobuf.Empty;
import io.grpc.Status;
import kalix.javasdk.valueentity.ValueEntityContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.products.channelProductAttribute.api.ChannelProductAttributeApi;

import java.util.Optional;

// This class was initially generated based on the .proto definition by Kalix tooling.
// This is the implementation for the Value Entity Service described in your io/products/channelProductAttribute/api/channel_product_attribute_api.proto file.
//
// As long as this file exists it will not be overwritten: you can maintain it yourself,
// or delete it so it is regenerated as needed.

public class ChannelProductAttribute extends AbstractChannelProductAttribute {
  @SuppressWarnings("unused")
  private final String entityId;

  private static final Logger LOG = LoggerFactory.getLogger(ChannelProductAttribute.class);

  public ChannelProductAttribute(ValueEntityContext context) {
    this.entityId = context.entityId();
  }

  @Override
  public ChannelProductAttributeDomain.ChannelProductAttributeState emptyState() {
    return ChannelProductAttributeDomain.ChannelProductAttributeState.getDefaultInstance();
  }

  @Override
  public Effect<Empty> createChannelProductAttribute(ChannelProductAttributeDomain.ChannelProductAttributeState currentState, io.products.channelProductAttribute.api.ChannelProductAttributeApi.ChannelProductAttribute command) {
    ChannelProductAttributeDomain.ChannelProductAttributeState state = convertToDomain(command);
    return reject(currentState, command).orElseGet(() -> handle(state, command));
  }

  private Optional<Effect<Empty>> reject(ChannelProductAttributeDomain.ChannelProductAttributeState currentState, io.products.channelProductAttribute.api.ChannelProductAttributeApi.ChannelProductAttribute command) {
    if (currentState.getChannelProductAttributeId().equals(command.getChannelProductAttributeId())) {
      return Optional.of(effects().error("Channel Product Attribute is already exists!!", Status.Code.NOT_FOUND));
    } else {
      return Optional.empty();
    }
  }

  private ChannelProductAttributeDomain.ChannelProductAttributeState convertToDomain(io.products.channelProductAttribute.api.ChannelProductAttributeApi.ChannelProductAttribute channelProductAttribute) {
    return ChannelProductAttributeDomain.ChannelProductAttributeState.newBuilder()
            .setChannelProductAttributeId(channelProductAttribute.getChannelProductAttributeId())
            .setAttributeId(channelProductAttribute.getAttributeId())
            .setValueId(channelProductAttribute.getValueId())
            .setVariantId(channelProductAttribute.getVariantId())
            .build();
  }

  private Effect<Empty> handle(ChannelProductAttributeDomain.ChannelProductAttributeState state, io.products.channelProductAttribute.api.ChannelProductAttributeApi.ChannelProductAttribute command) {
    return effects().updateState(state).thenReply(Empty.getDefaultInstance());
  }

  @Override
  public Effect<ChannelProductAttributeApi.ChannelProductAttribute> getChannelProductAttribute(ChannelProductAttributeDomain.ChannelProductAttributeState currentState, ChannelProductAttributeApi.GetChannelProductAttributeRequest getChannelProductAttributeRequest) {
    return effects().error("The command handler for `GetChannelProductAttribute` is not implemented, yet");
  }

  @Override
  public Effect<Empty> updateChannelProductAttribute(ChannelProductAttributeDomain.ChannelProductAttributeState currentState, ChannelProductAttributeApi.ChannelProductAttribute channelProductAttribute) {
    return effects().error("The command handler for `UpdateChannelProductAttribute` is not implemented, yet");
  }

  @Override
  public Effect<Empty> deleteChannelProductAttribute(ChannelProductAttributeDomain.ChannelProductAttributeState currentState, ChannelProductAttributeApi.DeleteChannelProductAttributeRequest deleteChannelProductAttributeRequest) {
    return effects().error("The command handler for `DeleteChannelProductAttribute` is not implemented, yet");
  }
}
