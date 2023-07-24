package io.products.productAttributeValue.domain;

import java.util.Optional;

import com.google.protobuf.Empty;

import io.grpc.Status;
import io.products.productAttribute.api.ProductAttributeApi;
import io.products.productAttribute.domain.ProductAttributeDomain;
import io.products.productAttributeValue.api.ProductAttributeValueApi;
import kalix.javasdk.valueentity.ValueEntityContext;

// This class was initially generated based on the .proto definition by Kalix tooling.
// This is the implementation for the Value Entity Service described in your io/products/productAttributeValue/api/product_attribute_value_api.proto file.
//
// As long as this file exists it will not be overwritten: you can maintain it yourself,
// or delete it so it is regenerated as needed.

public class ProductAttributeValue extends AbstractProductAttributeValue {
  @SuppressWarnings("unused")
  private final String entityId;

  public ProductAttributeValue(ValueEntityContext context) {
    this.entityId = context.entityId();
  }

  @Override
  public ProductAttributeValueDomain.ProductAttributeValueState emptyState() {
    return ProductAttributeValueDomain.ProductAttributeValueState.getDefaultInstance();
  }


  /**************************************************
   * CREATE
   **************************************************/

  @Override
  public Effect<Empty> createProductAttributeValue(ProductAttributeValueDomain.ProductAttributeValueState currentState, ProductAttributeValueApi.ProductAttributeValue command) {
    ProductAttributeValueDomain.ProductAttributeValueState state = convertToDomain(command);
    return reject(currentState, command).orElseGet(() -> handle(state, command));   
  }

  private Optional<Effect<Empty>> reject(ProductAttributeValueDomain.ProductAttributeValueState currentState, io.products.productAttributeValue.api.ProductAttributeValueApi.ProductAttributeValue command) {

    if (currentState.getValueId().equals(command.getValueId())) {
      return Optional.of(effects().error("Product Attribute Value is already exists!!", Status.Code.NOT_FOUND));

    } else {
      return Optional.empty();
    }
  }






  private ProductAttributeValueDomain.ProductAttributeValueState convertToDomain(io.products.productAttributeValue.api.ProductAttributeValueApi.ProductAttributeValue command) {
    return ProductAttributeValueDomain.ProductAttributeValueState.newBuilder()
    .setValueId(command.getValueId())
    .setAttributeId(command.getAttributeId())
    .setChannelId(command.getChannelId())
    .setValue(command.getValue())
    .build();
  }

  private Effect<Empty> handle(ProductAttributeValueDomain.ProductAttributeValueState state, io.products.productAttributeValue.api.ProductAttributeValueApi.ProductAttributeValue  command) {
    return effects().updateState(state).thenReply(Empty.getDefaultInstance());
  }


  @Override
  public Effect<ProductAttributeValueApi.ProductAttributeValue> getProductAttributeValue(ProductAttributeValueDomain.ProductAttributeValueState currentState, ProductAttributeValueApi.GetProductAttributeValueRequest getProductAttributeValueRequest) {
    return effects().error("The command handler for `GetProductAttributeValue` is not implemented, yet");
  }

  @Override
  public Effect<Empty> updateProductAttributeValue(ProductAttributeValueDomain.ProductAttributeValueState currentState, ProductAttributeValueApi.ProductAttributeValue productAttributeValue) {
    return effects().error("The command handler for `UpdateProductAttributeValue` is not implemented, yet");
  }

  @Override
  public Effect<Empty> deleteProductAttributeValue(ProductAttributeValueDomain.ProductAttributeValueState currentState, ProductAttributeValueApi.DeleteProductAttributeValueRequest deleteProductAttributeValueRequest) {
    return effects().error("The command handler for `DeleteProductAttributeValue` is not implemented, yet");
  }
}
