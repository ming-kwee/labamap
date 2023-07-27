package io.products.channelProductVariant.domain;

import com.google.protobuf.Empty;
import io.grpc.Status;
import io.products.channelProductAttribute.domain.ChannelProductAttributeDomain;
import io.products.channelProductVariant.api.ChannelProductVariantApi;
import kalix.javasdk.valueentity.ValueEntityContext;

import java.util.Optional;

// This class was initially generated based on the .proto definition by Kalix tooling.
// This is the implementation for the Value Entity Service described in your io/products/channelProductVariant/api/channel_product_variant_api.proto file.
//
// As long as this file exists it will not be overwritten: you can maintain it yourself,
// or delete it so it is regenerated as needed.

public class ChannelProductVariant extends AbstractChannelProductVariant {
  @SuppressWarnings("unused")
  private final String entityId;

  public ChannelProductVariant(ValueEntityContext context) {
    this.entityId = context.entityId();
  }

  @Override
  public ChannelProductVariantDomain.ChannelProductVariantState emptyState() {
    return ChannelProductVariantDomain.ChannelProductVariantState.getDefaultInstance();
  }

  @Override
  public Effect<Empty> createChannelProductVariant(ChannelProductVariantDomain.ChannelProductVariantState currentState, ChannelProductVariantApi.ChannelProductVariant command) {
    ChannelProductVariantDomain.ChannelProductVariantState state = convertToDomain(command);
    return reject(currentState, command).orElseGet(() -> handle(state, command));
  }

  private Optional<Effect<Empty>> reject(ChannelProductVariantDomain.ChannelProductVariantState currentState, ChannelProductVariantApi.ChannelProductVariant command) {
    if (currentState.getChannelProductVariantId().equals(command.getChannelProductVariantId())) {
      return Optional.of(effects().error("Channel Product Variant is already exists!!", Status.Code.NOT_FOUND));
    } else {
      return Optional.empty();
    }
  }

  private ChannelProductVariantDomain.ChannelProductVariantState convertToDomain(ChannelProductVariantApi.ChannelProductVariant channelProductVariant) {
    return ChannelProductVariantDomain.ChannelProductVariantState.newBuilder()
            .setChannelProductVariantId(channelProductVariant.getChannelProductVariantId())
            .setProductId(channelProductVariant.getProductId())
            .setChannelId(channelProductVariant.getChannelId())
            .setVariantId(channelProductVariant.getVariantId())
            .build();
  }

  private Effect<Empty> handle(ChannelProductVariantDomain.ChannelProductVariantState state, ChannelProductVariantApi.ChannelProductVariant command) {
    return effects().updateState(state).thenReply(Empty.getDefaultInstance());
  }

  @Override
  public Effect<ChannelProductVariantApi.ChannelProductVariant> getChannelProductVariant(ChannelProductVariantDomain.ChannelProductVariantState currentState, ChannelProductVariantApi.GetChannelProductVariantRequest getChannelProductVariantRequest) {
    return effects().error("The command handler for `GetChannelProductVariant` is not implemented, yet");
  }

  @Override
  public Effect<Empty> updateChannelProductVariant(ChannelProductVariantDomain.ChannelProductVariantState currentState, ChannelProductVariantApi.ChannelProductVariant channelProductVariant) {
    return effects().error("The command handler for `UpdateChannelProductVariant` is not implemented, yet");
  }

  @Override
  public Effect<Empty> deleteChannelProductVariant(ChannelProductVariantDomain.ChannelProductVariantState currentState, ChannelProductVariantApi.DeleteChannelProductVariantRequest deleteChannelProductVariantRequest) {
    return effects().error("The command handler for `DeleteChannelProductVariant` is not implemented, yet");
  }
}
