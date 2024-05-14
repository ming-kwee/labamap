package io.products.channelMetadata.domain;

import com.google.protobuf.Empty;
import io.grpc.Status;
import io.products.channelPlatform.api.ChannelPlatformApi;
import io.products.channelPlatform.domain.ChannelPlatformDomain;
import io.products.channelMetadata.api.ChannelMetadataApi;
import kalix.javasdk.valueentity.ValueEntityContext;

import java.util.Optional;
import java.util.UUID;

// This class was initially generated based on the .proto definition by Kalix tooling.
// This is the implementation for the Value Entity Service described in your io/products/channelMetadata/api/channel_metadata_api.proto file.
//
// As long as this file exists it will not be overwritten: you can maintain it yourself,
// or delete it so it is regenerated as needed.

public class ChannelMetadata extends AbstractChannelMetadata {
  @SuppressWarnings("unused")
  private final String entityId;

  public ChannelMetadata(ValueEntityContext context) {
    this.entityId = context.entityId();
  }

  @Override
  public ChannelMetadataDomain.ChannelMetadataState emptyState() {
    return ChannelMetadataDomain.ChannelMetadataState.getDefaultInstance();
  }

  @Override
  public Effect<Empty> createChannelMetadata(ChannelMetadataDomain.ChannelMetadataState currentState, ChannelMetadataApi.ChannelMetadata command) {
    ChannelMetadataDomain.ChannelMetadataState state = convertToDomain(command);
    return reject(currentState, command).orElseGet(() -> handle(state, command));
  }

  private Optional<Effect<Empty>> reject(ChannelMetadataDomain.ChannelMetadataState  currentState, ChannelMetadataApi.ChannelMetadata  command) {
    if (currentState.getChannelId().equals(command.getChannelId()) && currentState.getKey().equals(command.getKey())) {
      return Optional.of(effects().error("Metadata is already exists!!", Status.Code.ALREADY_EXISTS));
    } else {
      return Optional.empty();
    }
  }

  private ChannelMetadataDomain.ChannelMetadataState  convertToDomain(ChannelMetadataApi.ChannelMetadata metadata) {
    return ChannelMetadataDomain.ChannelMetadataState.newBuilder()
            .setChannelId(metadata.getChannelId())
            .setKey(metadata.getKey())
            .setValue(metadata.getValue())
            .setTarget(metadata.getTarget())
            .setGrouping(metadata.getGrouping())
            .setSubGrouping(metadata.getSubGrouping())
            .build();
  }

  private Effect<Empty> handle(ChannelMetadataDomain.ChannelMetadataState state, ChannelMetadataApi.ChannelMetadata command) {
    return effects().updateState(state).thenReply(Empty.getDefaultInstance());
  }

  @Override
  public Effect<ChannelMetadataApi.ChannelMetadata> getChannelMetadata(ChannelMetadataDomain.ChannelMetadataState currentState, ChannelMetadataApi.GetChannelMetadataRequest getChannelMetadataRequest) {
    return effects().error("The command handler for `GetChannelMetadata` is not implemented, yet");
  }

  @Override
  public Effect<Empty> updateChannelMetadata(ChannelMetadataDomain.ChannelMetadataState currentState, ChannelMetadataApi.ChannelMetadata channelMetadata) {
    return effects().error("The command handler for `UpdateChannelMetadata` is not implemented, yet");
  }

  @Override
  public Effect<Empty> deleteChannelMetadata(ChannelMetadataDomain.ChannelMetadataState currentState, ChannelMetadataApi.DeleteChannelMetadataRequest deleteChannelMetadataRequest) {
    return effects().error("The command handler for `DeleteChannelMetadata` is not implemented, yet");
  }
}
