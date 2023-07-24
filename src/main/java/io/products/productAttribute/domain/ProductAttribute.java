package io.products.productAttribute.domain;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.grpc.Status;
import com.google.protobuf.Empty;
import io.products.productAttribute.api.ProductAttributeApi;
import io.products.productAttribute.domain.ProductAttributeDomain;
import kalix.javasdk.valueentity.ValueEntityContext;

// This class was initially generated based on the .proto definition by Kalix tooling.
// This is the implementation for the Value Entity Service described in your io/products/productAttribute/api/product_attribute_api.proto file.
//
// As long as this file exists it will not be overwritten: you can maintain it yourself,
// or delete it so it is regenerated as needed.

public class ProductAttribute extends AbstractProductAttribute {
  @SuppressWarnings("unused")
  private final String entityId;

  public ProductAttribute(ValueEntityContext context) {
    this.entityId = context.entityId();
  }

  @Override
  public ProductAttributeDomain.ProductAttributeState emptyState() {
    return ProductAttributeDomain.ProductAttributeState.getDefaultInstance();
  }




  /**************************************************
   * CREATE
   **************************************************/
    @Override
  public Effect<Empty> createProductAttribute(ProductAttributeDomain.ProductAttributeState currentState, ProductAttributeApi.ProductAttribute command) {
    ProductAttributeDomain.ProductAttributeState state = convertToDomain(command);
    return reject(currentState, command).orElseGet(() -> handle(state, command));   
  }



  private Optional<Effect<Empty>> reject(ProductAttributeDomain.ProductAttributeState currentState, io.products.productAttribute.api.ProductAttributeApi.ProductAttribute command) {

    if (currentState.getAttributeId().equals(command.getAttributeId())) {
      return Optional.of(effects().error("Product Attribute is already exists!!", Status.Code.NOT_FOUND));

    } else {
      return Optional.empty();
    }
  }


  private ProductAttributeDomain.ProductAttributeState convertToDomain(io.products.productAttribute.api.ProductAttributeApi.ProductAttribute command) {
    return ProductAttributeDomain.ProductAttributeState.newBuilder()
    .setAttributeId(command.getAttributeId())
    .setAttributeName(command.getAttributeName())
    .setAttributeType(command.getAttributeType())
    .build();
  }

  private Effect<Empty> handle(ProductAttributeDomain.ProductAttributeState state, io.products.productAttribute.api.ProductAttributeApi.ProductAttribute  command) {
    return effects().updateState(state).thenReply(Empty.getDefaultInstance());
  }



  @Override
  public Effect<ProductAttributeApi.ProductAttribute> getProductAttribute(ProductAttributeDomain.ProductAttributeState currentState, ProductAttributeApi.GetProductAttributeRequest command) {
    if (currentState.getAttributeId().equals(command.getAttributeId())) {
      return effects().reply(convertToApi(currentState));
    } else {
      return effects().error("Attribute " + command.getAttributeId() + " has not been created.");
    }

  }

  private ProductAttributeApi.ProductAttribute convertToApi(ProductAttributeDomain.ProductAttributeState state) {
    return ProductAttributeApi.ProductAttribute.newBuilder()
    .setAttributeId(state.getAttributeId())    
    .setAttributeName(state.getAttributeName())
    .setAttributeType(state.getAttributeType())
    .build();
  }

  @Override
  public Effect<Empty> updateProductAttribute(ProductAttributeDomain.ProductAttributeState currentState, ProductAttributeApi.ProductAttribute productAttribute) {
    return effects().error("The command handler for `UpdateProductAttribute` is not implemented, yet");
  }

  @Override
  public Effect<Empty> deleteProductAttribute(ProductAttributeDomain.ProductAttributeState currentState, ProductAttributeApi.DeleteProductAttributeRequest deleteProductAttributeRequest) {
    return effects().error("The command handler for `DeleteProductAttribute` is not implemented, yet");
  }
}
