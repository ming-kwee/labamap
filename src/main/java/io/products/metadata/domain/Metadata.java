package io.products.metadata.domain;

import com.google.protobuf.Empty;
import io.grpc.Status;
import io.products.channelPlatform.api.ChannelPlatformApi;
import io.products.channelPlatform.domain.ChannelPlatformDomain;
import io.products.metadata.api.MetadataApi;
import kalix.javasdk.valueentity.ValueEntityContext;

import java.util.Optional;
import java.util.UUID;

// This class was initially generated based on the .proto definition by Kalix tooling.
// This is the implementation for the Value Entity Service described in your io/products/metadata/api/metadata_api.proto file.
//
// As long as this file exists it will not be overwritten: you can maintain it yourself,
// or delete it so it is regenerated as needed.

public class Metadata extends AbstractMetadata {
  @SuppressWarnings("unused")
  private final String entityId;

  public Metadata(ValueEntityContext context) {
    this.entityId = context.entityId();
  }

  @Override
  public MetadataDomain.MetadataState emptyState() {
    return MetadataDomain.MetadataState.getDefaultInstance();
  }

  @Override
  public Effect<Empty> createMetadata(MetadataDomain.MetadataState currentState, MetadataApi.Metadata command) {
    MetadataDomain.MetadataState state = convertToDomain(command);
    return reject(currentState, command).orElseGet(() -> handle(state, command));
  }

  private Optional<Effect<Empty>> reject(MetadataDomain.MetadataState  currentState, MetadataApi.Metadata  command) {
    if (currentState.getChannelId().equals(command.getChannelId()) && currentState.getKey().equals(command.getKey())) {
     return Optional.of(effects().error("Metadata is already exists!!", Status.Code.ALREADY_EXISTS));
    } else {
     return Optional.empty();
    }
  }

  private MetadataDomain.MetadataState  convertToDomain(MetadataApi.Metadata metadata) {
    return MetadataDomain.MetadataState.newBuilder()
            .setChannelId(metadata.getChannelId())
            .setKey(metadata.getKey())
            .setValue(metadata.getValue())
            .setTarget(metadata.getTarget())
            .setGrouping(metadata.getGrouping())
            .setSubGrouping(metadata.getSubGrouping())
            .build();
  }

  private Effect<Empty> handle(MetadataDomain.MetadataState state, MetadataApi.Metadata command) {
    return effects().updateState(state).thenReply(Empty.getDefaultInstance());
  }

  @Override
  public Effect<MetadataApi.Metadata> getMetadata(MetadataDomain.MetadataState currentState, MetadataApi.GetMetadataRequest getMetadataRequest) {
    return effects().error("The command handler for `GetMetadata` is not implemented, yet");
  }

  @Override
  public Effect<Empty> updateMetadata(MetadataDomain.MetadataState currentState, MetadataApi.Metadata metadata) {
    return effects().error("The command handler for `UpdateMetadata` is not implemented, yet");
  }

  @Override
  public Effect<Empty> deleteMetadata(MetadataDomain.MetadataState currentState, MetadataApi.DeleteMetadataRequest deleteMetadataRequest) {
    return effects().error("The command handler for `DeleteMetadata` is not implemented, yet");
  }
}
