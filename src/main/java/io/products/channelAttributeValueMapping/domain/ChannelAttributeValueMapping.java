package io.products.channelAttributeValueMapping.domain;

import com.google.protobuf.Empty;
import io.grpc.Status;
import io.products.channelAttributeValueMapping.api.ChannelAttributeValueMappingApi;
import io.products.channelMapping.api.ChannelMappingApi;
import io.products.channelMapping.domain.ChannelMappingDomain;
import kalix.javasdk.valueentity.ValueEntityContext;

import java.util.Optional;

// This class was initially generated based on the .proto definition by Kalix tooling.
// This is the implementation for the Value Entity Service described in your io/products/channelAttributeValueMapping/api/channel_attribute_value_mapping_api.proto file.
//
// As long as this file exists it will not be overwritten: you can maintain it yourself,
// or delete it so it is regenerated as needed.

public class ChannelAttributeValueMapping extends AbstractChannelAttributeValueMapping {
  @SuppressWarnings("unused")
  private final String entityId;

  public ChannelAttributeValueMapping(ValueEntityContext context) {
    this.entityId = context.entityId();
  }

  @Override
  public ChannelAttributeValueMappingDomain.ChannelAttributeValueMappingState emptyState() {
    return ChannelAttributeValueMappingDomain.ChannelAttributeValueMappingState.getDefaultInstance();
  }

  @Override
  public Effect<Empty> createChannelAttributeValueMapping(ChannelAttributeValueMappingDomain.ChannelAttributeValueMappingState currentState, ChannelAttributeValueMappingApi.ChannelAttributeValueMapping command) {
    ChannelAttributeValueMappingDomain.ChannelAttributeValueMappingState state = convertToDomain(command);
    return reject(currentState, command).orElseGet(() -> handle(state, command));
  }

  private Optional<Effect<Empty>> reject(ChannelAttributeValueMappingDomain.ChannelAttributeValueMappingState currentState, ChannelAttributeValueMappingApi.ChannelAttributeValueMapping command) {
    if (currentState.getChannelAttributeValueMapId().equals(command.getChannelAttributeValueMapId())) {
      return Optional.of(effects().error("Channel Attribute Value Mapping is already exists!!", Status.Code.NOT_FOUND));
    } else {
      return Optional.empty();
    }
  }

  private ChannelAttributeValueMappingDomain.ChannelAttributeValueMappingState convertToDomain(ChannelAttributeValueMappingApi.ChannelAttributeValueMapping channelAttributeValueMapping) {
    return ChannelAttributeValueMappingDomain.ChannelAttributeValueMappingState .newBuilder()
            .setChannelAttributeValueMapId(channelAttributeValueMapping.getChannelAttributeValueMapId())
            .setValueId(channelAttributeValueMapping.getValueId())
            .setChannelId(channelAttributeValueMapping.getChannelId())
            .setValue(channelAttributeValueMapping.getValue())
            .build();
  }

  private Effect<Empty> handle(ChannelAttributeValueMappingDomain.ChannelAttributeValueMappingState state, ChannelAttributeValueMappingApi.ChannelAttributeValueMapping command) {
    return effects().updateState(state).thenReply(Empty.getDefaultInstance());
  }

  @Override
  public Effect<ChannelAttributeValueMappingApi.ChannelAttributeValueMapping> getChannelAttributeValueMapping(ChannelAttributeValueMappingDomain.ChannelAttributeValueMappingState currentState, ChannelAttributeValueMappingApi.GetChannelAttributeValueMappingRequest getChannelAttributeValueMappingRequest) {
    return effects().error("The command handler for `GetChannelAttributeValueMapping` is not implemented, yet");
  }

  @Override
  public Effect<Empty> updateChannelAttributeValueMapping(ChannelAttributeValueMappingDomain.ChannelAttributeValueMappingState currentState, ChannelAttributeValueMappingApi.ChannelAttributeValueMapping channelAttributeValueMapping) {
    return effects().error("The command handler for `UpdateChannelAttributeValueMapping` is not implemented, yet");
  }

  @Override
  public Effect<Empty> deleteChannelAttributeValueMapping(ChannelAttributeValueMappingDomain.ChannelAttributeValueMappingState currentState, ChannelAttributeValueMappingApi.DeleteChannelAttributeValueMappingRequest deleteChannelAttributeValueMappingRequest) {
    return effects().error("The command handler for `DeleteChannelAttributeValueMapping` is not implemented, yet");
  }
}
