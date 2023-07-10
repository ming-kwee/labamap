package io.products.product.domain;
import com.google.protobuf.Empty;
import com.google.rpc.context.AttributeContext.Auth;

import io.products.product.action.ProductActionApi;
import io.products.product.domain.ProductDomain.ProductState;
import kalix.javasdk.valueentity.ValueEntityContext;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.grpc.Status;
import io.products.product.api.ProductApi;
// This class was initially generated based on the .proto definition by Kalix tooling.
// This is the implementation for the Value Entity Service described in your io/products/product/api/product_api.proto file.
//
// As long as this file exists it will not be overwritten: you can maintain it yourself,
// or delete it so it is regenerated as needed.

public class Product extends AbstractProduct {
  @SuppressWarnings("unused")
  private final String entityId;
  private static final Logger LOG = LoggerFactory.getLogger(Auth.class);

  public Product(ValueEntityContext context) {
    this.entityId = context.entityId();
  }

  @Override
  public ProductDomain.ProductState emptyState() {
    return ProductDomain.ProductState.getDefaultInstance();
  }


  /**************************************************
   * CREATE
   **************************************************/
  @Override
  public Effect<Empty> createProduct(ProductDomain.ProductState currentState, io.products.product.api.ProductApi.Product command) {
    ProductDomain.ProductState state = convertToDomain(command);
    return reject(currentState, command).orElseGet(() -> handle(state, command));
  }

  private Optional<Effect<Empty>> reject(ProductDomain.ProductState currentState, io.products.product.api.ProductApi.Product command) {

    if (currentState.getItemName().equals(command.getItemName())) {
      return Optional.of(effects().error("Product is already exists!!", Status.Code.NOT_FOUND));

    } else {
      return Optional.empty();
    }
  }


  private ProductDomain.ProductState convertToDomain(io.products.product.api.ProductApi.Product product) {
    return ProductDomain.ProductState.newBuilder()
        .setId(product.getId())
        .setCategoryId(product.getCategoryId())
        .setItemName(product.getItemName())
        .build();
  }

  private Effect<Empty> handle(ProductDomain.ProductState state, io.products.product.api.ProductApi.Product command) {
    return effects().updateState(state).thenReply(Empty.getDefaultInstance());
  }




}
