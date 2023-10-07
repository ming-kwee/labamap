package io.products.product.domain;

import com.google.protobuf.Empty;
import com.google.rpc.context.AttributeContext.Auth;

import io.products.product.action.ProductActionApi;
import io.products.product.domain.ProductDomain.ProductAttribute;
import io.products.product.domain.ProductDomain.ProductState;
import kalix.javasdk.action.Action.Effect;
import kalix.javasdk.valueentity.ValueEntityContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.grpc.Status;
import io.products.product.api.ProductApi;

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
  public Effect<Empty> createProduct(ProductDomain.ProductState currentState,
      io.products.product.api.ProductApi.Product command) {
    ProductDomain.ProductState state = convertToDomain(currentState, command);
    return reject(currentState, command).orElseGet(() -> handle(state, command));
  }

  private Optional<Effect<Empty>> reject(ProductDomain.ProductState currentState,
      io.products.product.api.ProductApi.Product command) {

    if (currentState.getId().equals(command.getId())) {
      return Optional.of(effects().error("Product is already exists!!!", Status.Code.NOT_FOUND));

    } else {
      return Optional.empty();
    }
  }

  private ProductDomain.ProductState convertToDomain(ProductDomain.ProductState currentState,
      io.products.product.api.ProductApi.Product product) {

    ProductDomain.ProductState.Builder stateBuilder = currentState.toBuilder();
    List<io.products.product.api.ProductApi.ProductAttribute> prodattrlist = product.getProductAttributeList();

    // prodattrlist
    LOG.info("JML ATTR " + prodattrlist);

    // Create a list of Item objects dynamically
    List<ProductAttribute> itemList = new ArrayList<>();
    for (int i = 0; i < prodattrlist.size(); i++) {
      ProductAttribute item = ProductAttribute.newBuilder()
          // .setId(prodattrlist.get(i).getId())
          // .setProdId(prodattrlist.get(i).getProdId())
          .setAttrId(prodattrlist.get(i).getAttrId())
          .setAttrName(prodattrlist.get(i).getAttrName())
          .setValue(prodattrlist.get(i).getValue())
          .setIsCommon(prodattrlist.get(i).getIsCommon())
          .build();
      itemList.add(item);
    }

    stateBuilder
        .setId(product.getId())
        .setDeskripsiPendek(product.getDeskripsiPendek())
        .setNamaProduk(product.getNamaProduk())
        .clearProductAttribute().addAllProductAttribute(itemList);

    return stateBuilder.build();
  }

  private Effect<Empty> handle(ProductDomain.ProductState state, io.products.product.api.ProductApi.Product command) {
    return effects().updateState(state).thenReply(Empty.getDefaultInstance());
  }

}
