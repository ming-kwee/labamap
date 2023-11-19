package io.products.variant.domain;

import com.google.protobuf.Empty;
import io.grpc.Status;
import io.products.variant.api.VariantApi;
import kalix.javasdk.action.Action.Effect;
import kalix.javasdk.valueentity.ValueEntityContext;

import java.util.Optional;
import java.util.UUID;

// This class was initially generated based on the .proto definition by Kalix tooling.
// This is the implementation for the Value Entity Service described in your io/products/variant/api/variant_api.proto file.
//
// As long as this file exists it will not be overwritten: you can maintain it yourself,
// or delete it so it is regenerated as needed.

public class Variant extends AbstractVariant {
  @SuppressWarnings("unused")
  private final String entityId;

  public Variant(ValueEntityContext context) {
    this.entityId = context.entityId();
  }

  @Override
  public VariantDomain.VariantState emptyState() {
    return VariantDomain.VariantState.getDefaultInstance();
  }

  @Override
  public Effect<Empty> createVariant(VariantDomain.VariantState currentState, VariantApi.Variant command) {
    VariantDomain.VariantState state = convertToDomain(command);
    return reject(currentState, command).orElseGet(() -> handle(state, command));
  }

  private Optional<Effect<Empty>> reject(VariantDomain.VariantState currentState, VariantApi.Variant command) {
    if (currentState.getVariantId().equals(command.getVariantId())) {
      return Optional.of(effects().error("Variant is already exists!!", Status.Code.NOT_FOUND));
    } else {
      return Optional.empty();
    }
  }

  private VariantDomain.VariantState convertToDomain(VariantApi.Variant variant) {
    return VariantDomain.VariantState.newBuilder()
            .setVariantId(UUID.randomUUID().toString())
            .setProductId(variant.getProductId())
            .setVariantTitle(variant.getVariantTitle())
            .setPrice(variant.getPrice())
            .setWeight(variant.getWeight())
            .build();
  }

  private Effect<Empty> handle(VariantDomain.VariantState state, VariantApi.Variant command) {
    return effects().updateState(state).thenReply(Empty.getDefaultInstance());
  }

  @Override
  public Effect<VariantApi.Variant> getVariant(VariantDomain.VariantState currentState, VariantApi.GetVariantRequest getVariantRequest) {
    return effects().error("The command handler for `GetVariant` is not implemented, yet");
  }

  @Override
  public Effect<Empty> updateVariant(VariantDomain.VariantState currentState, VariantApi.Variant variant) {
    return effects().error("The command handler for `UpdateVariant` is not implemented, yet");
  }

  @Override
  public Effect<Empty> deleteVariant(VariantDomain.VariantState currentState, VariantApi.DeleteVariantRequest deleteVariantRequest) {
    return effects().error("The command handler for `DeleteVariant` is not implemented, yet");
  }
}
