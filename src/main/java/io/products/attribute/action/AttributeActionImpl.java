package io.products.attribute.action;

import java.util.UUID;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

import com.google.protobuf.Empty;

import io.grpc.Status;
import io.products.attribute.action.AttributeActionApi.AttributeWithoutId;
import io.products.attribute.api.AttributeApi.Attribute;
// import io.products.attribute.api.AttributeApi;
// import io.products.attribute.action.AttributeApi.Attribute;
import kalix.javasdk.action.Action.Effect;
import kalix.javasdk.action.ActionCreationContext;

// This class was initially generated based on the .proto definition by Kalix tooling.
// This is the implementation for the Action Service described in your io/products/attribute/action/attribute_action.proto file.
//
// As long as this file exists it will not be overwritten: you can maintain it yourself,
// or delete it so it is regenerated as needed.

public class AttributeActionImpl extends AbstractAttributeAction {

  public AttributeActionImpl(ActionCreationContext creationContext) {}

  @Override
  public Effect<Empty> createAttribute(AttributeActionApi.AttributeWithoutId attribute) {
    Attribute.Builder attributeBuilder = Attribute.newBuilder();
    attributeBuilder.setAttributeId(UUID.randomUUID().toString());
    attributeBuilder.setAttributeName(attribute.getAttributeName());
    attributeBuilder.setAttributeType(attribute.getAttributeType());
    attributeBuilder.setIsCommon(attribute.getIsCommon());

      /* _________________________ */
    /* ------------------------- */
    // Create Attribute
    /* _________________________ */
    /* ------------------------- */


    
    CompletionStage<Empty> create_attribute = components().attribute()
        .createAttribute(attributeBuilder.build()).execute();
    CompletionStage<Effect<Empty>> effect = create_attribute.thenApply(x -> {
      return effects().reply(Empty.getDefaultInstance());
    });
    // throw new RuntimeException("The command handler for `CreateAttribute` is not implemented, yet");
    /* ------------------------------------------------------------- */
    return effects().asyncEffect(effect.exceptionally(NotEmptyAuth()));
    /* ------------------------------------------------------------- */

    
  }

  private Function<Throwable, ? extends Effect<Empty>> NotEmptyAuth() {
    /* --- jika kesini artinya ada error saat mencreate product ---- */
    /* ------------------------------------------------------------- */
    return (e) -> effects().error(e.getMessage(), Status.Code.CANCELLED);
    /* ------------------------------------------------------------- */
  }
}
