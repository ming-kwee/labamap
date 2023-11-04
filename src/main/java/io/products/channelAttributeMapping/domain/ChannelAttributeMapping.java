package io.products.channelAttributeMapping.domain;

import com.google.protobuf.Empty;
import io.grpc.Status;
import io.products.channelAttributeMapping.api.ChannelAttributeMappingApi;
import io.products.channelPlatform.api.ChannelPlatformApi;
import io.products.channelPlatform.domain.ChannelPlatformDomain;
import kalix.javasdk.valueentity.ValueEntityContext;

import java.util.Optional;

// This class was initially generated based on the .proto definition by Kalix tooling.
// This is the implementation for the Value Entity Service described in your io/products/channelAttributeMapping/api/channel_attribute_mapping_api.proto file.
//
// As long as this file exists it will not be overwritten: you can maintain it yourself,
// or delete it so it is regenerated as needed.

public class ChannelAttributeMapping extends AbstractChannelAttributeMapping {
  @SuppressWarnings("unused")
  private final String entityId;

  public ChannelAttributeMapping(ValueEntityContext context) {
    this.entityId = context.entityId();
  }

  @Override
  public ChannelAttributeMappingDomain.ChannelAttributeMappingState emptyState() {
    return ChannelAttributeMappingDomain.ChannelAttributeMappingState.getDefaultInstance();
  }

  @Override
  public Effect<Empty> createChannelAttributeMapping(ChannelAttributeMappingDomain.ChannelAttributeMappingState currentState, ChannelAttributeMappingApi.ChannelAttributeMapping command) {
    ChannelAttributeMappingDomain.ChannelAttributeMappingState state = convertToDomain(command);
    return reject(currentState, command).orElseGet(() -> handle(state, command));
  }

  private Optional<Effect<Empty>> reject(ChannelAttributeMappingDomain.ChannelAttributeMappingState currentState, ChannelAttributeMappingApi.ChannelAttributeMapping command) {
    if (currentState.getMappingId().equals(command.getMappingId())) {
      return Optional.of(effects().error("Channel Attribute Mapping is already exists!!", Status.Code.NOT_FOUND));
    } else {
      return Optional.empty();
    }
  }

  private ChannelAttributeMappingDomain.ChannelAttributeMappingState convertToDomain(ChannelAttributeMappingApi.ChannelAttributeMapping channelAttributeMapping) {
    return ChannelAttributeMappingDomain.ChannelAttributeMappingState.newBuilder()
            .setMappingId(channelAttributeMapping.getMappingId())
            .setAttributeId(channelAttributeMapping.getAttributeId())
            .setChannelId(channelAttributeMapping.getChannelId())
            .setDestinationField(channelAttributeMapping.getDestinationField())
            .setGroup(channelAttributeMapping.getGroup())
            .setIsCommon(channelAttributeMapping.getIsCommon())
            .setType(channelAttributeMapping.getType())
            .build();
  }

  private Effect<Empty> handle(ChannelAttributeMappingDomain.ChannelAttributeMappingState state, ChannelAttributeMappingApi.ChannelAttributeMapping command) {
    return effects().updateState(state).thenReply(Empty.getDefaultInstance());
  }

  @Override
  public Effect<ChannelAttributeMappingApi.ChannelAttributeMapping> getChannelAttributeMapping(ChannelAttributeMappingDomain.ChannelAttributeMappingState currentState, ChannelAttributeMappingApi.GetChannelAttributeMappingRequest getChannelAttributeMappingRequest) {
    return effects().error("The command handler for `GetChannelAttributeMapping` is not implemented, yet");
  }

  @Override
  public Effect<Empty> updateChannelAttributeMapping(ChannelAttributeMappingDomain.ChannelAttributeMappingState currentState, ChannelAttributeMappingApi.ChannelAttributeMapping channelAttributeMapping) {
    return effects().error("The command handler for `UpdateChannelAttributeMapping` is not implemented, yet");
  }

  @Override
  public Effect<Empty> deleteChannelAttributeMapping(ChannelAttributeMappingDomain.ChannelAttributeMappingState currentState, ChannelAttributeMappingApi.DeleteChannelAttributeMappingRequest deleteChannelAttributeMappingRequest) {
    return effects().error("The command handler for `DeleteChannelAttributeMapping` is not implemented, yet");
  }
}
