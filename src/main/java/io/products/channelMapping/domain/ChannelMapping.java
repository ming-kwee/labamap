package io.products.channelMapping.domain;

import com.google.protobuf.Empty;
import io.grpc.Status;
import io.products.channelMapping.api.ChannelMappingApi;
import io.products.channelPlatform.api.ChannelPlatformApi;
import io.products.channelPlatform.domain.ChannelPlatformDomain;
import kalix.javasdk.valueentity.ValueEntityContext;

import java.util.Optional;

// This class was initially generated based on the .proto definition by Kalix tooling.
// This is the implementation for the Value Entity Service described in your io/products/channelMapping/api/channel_mapping_api.proto file.
//
// As long as this file exists it will not be overwritten: you can maintain it yourself,
// or delete it so it is regenerated as needed.

public class ChannelMapping extends AbstractChannelMapping {
  @SuppressWarnings("unused")
  private final String entityId;

  public ChannelMapping(ValueEntityContext context) {
    this.entityId = context.entityId();
  }

  @Override
  public ChannelMappingDomain.ChannelMappingState emptyState() {
    return ChannelMappingDomain.ChannelMappingState.getDefaultInstance();
  }

  @Override
  public Effect<Empty> createChannelMapping(ChannelMappingDomain.ChannelMappingState currentState, ChannelMappingApi.ChannelMapping command) {
    ChannelMappingDomain.ChannelMappingState state = convertToDomain(command);
    return reject(currentState, command).orElseGet(() -> handle(state, command));
  }

  private Optional<Effect<Empty>> reject(ChannelMappingDomain.ChannelMappingState currentState, ChannelMappingApi.ChannelMapping command) {
    if (currentState.getChannelMapId().equals(command.getChannelMapId())) {
      return Optional.of(effects().error("Channel Mapping is already exists!!", Status.Code.NOT_FOUND));
    } else {
      return Optional.empty();
    }
  }

  private ChannelMappingDomain.ChannelMappingState convertToDomain(ChannelMappingApi.ChannelMapping channelMapping) {
    return ChannelMappingDomain.ChannelMappingState .newBuilder()
            .setChannelMapId(channelMapping.getChannelId())
            .setProductId(channelMapping.getProductId())
            .setChannelId(channelMapping.getChannelId())
            .build();
  }

  private Effect<Empty> handle(ChannelMappingDomain.ChannelMappingState state, ChannelMappingApi.ChannelMapping command) {
    return effects().updateState(state).thenReply(Empty.getDefaultInstance());
  }

  @Override
  public Effect<ChannelMappingApi.ChannelMapping> getChannelMapping(ChannelMappingDomain.ChannelMappingState currentState, ChannelMappingApi.GetChannelMappingRequest getChannelMappingRequest) {
    return effects().error("The command handler for `GetChannelMapping` is not implemented, yet");
  }

  @Override
  public Effect<Empty> updateChannelMapping(ChannelMappingDomain.ChannelMappingState currentState, ChannelMappingApi.ChannelMapping channelMapping) {
    return effects().error("The command handler for `UpdateChannelMapping` is not implemented, yet");
  }

  @Override
  public Effect<Empty> deleteChannelMapping(ChannelMappingDomain.ChannelMappingState currentState, ChannelMappingApi.DeleteChannelMappingRequest deleteChannelMappingRequest) {
    return effects().error("The command handler for `DeleteChannelMapping` is not implemented, yet");
  }
}
