package io.products.productAttributeValue.action;

import java.util.concurrent.CompletionStage;
import java.util.function.Function;

import com.google.protobuf.Empty;

import io.grpc.Status;
import io.products.productAttribute.api.ProductAttributeApi;
import io.products.productAttributeValue.api.ProductAttributeValueApi;
import io.products.channelProduct.api.ChannelProductApi;
import kalix.javasdk.DeferredCall;
import kalix.javasdk.action.Action.Effect;
import kalix.javasdk.action.ActionCreationContext;

// This class was initially generated based on the .proto definition by Kalix tooling.
// This is the implementation for the Action Service described in your io/products/product_attribute_value/action/product_attribute_value_action.proto file.
//
// As long as this file exists it will not be overwritten: you can maintain it yourself,
// or delete it so it is regenerated as needed.

public class ProductAttributeValueActionImpl extends AbstractProductAttributeValueAction {

  public ProductAttributeValueActionImpl(ActionCreationContext creationContext) {
  }

  @Override
  public Effect<Empty> createProductAttributeValue(
      ProductAttributeValueActionApi.ProductAttributeValue command) {

    CompletionStage<ProductAttributeApi.ProductAttribute> product_attribute = components()
        .productAttribute()
        .getProductAttribute(
            io.products.productAttribute.api.ProductAttributeApi.GetProductAttributeRequest
                .newBuilder()
                .setAttributeId(command.getAttributeId())
                .build())
        .execute();

    CompletionStage<Effect<Empty>> effect = product_attribute.thenApply(attribute -> {
      /* ------- jika melewati ini artinya attribute ketemu ---------- */
      /* ------------------------------------------------------------- */

      /* ------------------------------------------------------------- */
      /* --- #2 memanggil service createDoctor utk mencreate doctor--- */
      /* ------------------------------------------------------------- */
      DeferredCall<ProductAttributeValueApi.ProductAttributeValue, Empty> call = 
      components()
          .productAttributeValue()
          .createProductAttributeValue(convertProductAttributeValueApi(command, command.getValueId()));

      return effects().forward(call);
    });

    /* ------------------------------------------------------------- */
    return effects().asyncEffect(effect.exceptionally(AttributeNotExist()));

    // throw new RuntimeException("The command handler for `CreateProductAttributeValue` is not implemented, yet");

  }

  private ProductAttributeValueApi.ProductAttributeValue convertProductAttributeValueApi(
      ProductAttributeValueActionApi.ProductAttributeValue productAttributeValue, String id) {
    return ProductAttributeValueApi.ProductAttributeValue.newBuilder()
        .setValueId(id)
        .setAttributeId(productAttributeValue.getAttributeId())
        .setChannelId(productAttributeValue.getChannelId())
        .setValue(productAttributeValue.getValue())
        .build();
  }


    private Function<Throwable, ? extends Effect<Empty>> AttributeNotExist() {
    /* --- jika kesini artinya ada error saat mencreate product ---- */
    /* ------------------------------------------------------------- */
    
    return (e) -> effects().error(      
      e.getMessage().split(":")[2],
        Status.Code.valueOf(e.getMessage().split(":")[1].trim().toUpperCase())); 
        
  }
}
