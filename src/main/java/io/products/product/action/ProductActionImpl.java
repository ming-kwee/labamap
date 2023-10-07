package io.products.product.action;

import com.google.protobuf.Empty;
import com.google.protobuf.Descriptors.Descriptor;
import com.google.protobuf.Descriptors.FieldDescriptor;
import io.grpc.Status;
import io.products.attribute.api.AttributeApi;
import io.products.channelProduct.api.ChannelProductApi;
import io.products.product.action.ProductActionApi.Attribute;
import io.products.product.api.ProductApi;
import io.products.product.api.ProductApi.Product;
import io.products.product.api.ProductApi.ProductAttribute;
import io.products.product.api.ProductApi.Product.Builder;
import io.products.product.domain.ProductDomain;
// import io.products.productAttribute.api.ProductAttributeApi;

import java.util.function.Function;
import java.util.stream.Collectors;

import kalix.javasdk.DeferredCall;
import kalix.javasdk.action.ActionCreationContext;
import kalix.javasdk.action.Action.Effect;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionStage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.lang.reflect.Field;
import java.util.UUID;
// This class was initially generated based on the .proto definition by Kalix tooling.
// This is the implementation for the Action Service described in your io/products/product/action/product_action.proto file.
//
// As long as this file exists it will not be overwritten: you can maintain it yourself,
// or delete it so it is regenerated as needed.

public class ProductActionImpl extends AbstractProductAction {
  private static final Logger LOG = LoggerFactory.getLogger(ProductActionImpl.class);

  public ProductActionImpl(ActionCreationContext creationContext) {
  }

  private static String replaceAfterUnderscore(String input) {
    StringBuilder result = new StringBuilder();
    boolean convertNext = false;
    for (char c : input.toCharArray()) {
      if (c == '_') {
        convertNext = true;
      } else if (convertNext) {
        result.append(Character.toUpperCase(c));
        convertNext = false;
      } else {
        result.append(c);
      }
    }
    return result.toString() + "_";
  }

  /**************************************************
   * CREATE
   **************************************************/
  @Override
  public Effect<Empty> createProduct(ProductActionApi.Product product) {

    // Create a builder for the Person message
    Product.Builder productBuilder = Product.newBuilder();
    FieldDescriptor[] fields = productBuilder.getDescriptorForType().getFields().toArray(new FieldDescriptor[0]);

    List<Attribute> allItems = product.getAttributesList().stream().collect(Collectors.toList());
    List<ProductAttribute> productattributeList = new ArrayList<>();

    for (int i = 0; i < allItems.size(); i++) {
      if (allItems.get(i).getIsCommon() == true) {
        // Print the names of all fields
        for (FieldDescriptor field : fields) {
          if (field.getName().equals(allItems.get(i).getAttrName())) {
            LOG.info("Field Name: " + field.getName());
            String propName = replaceAfterUnderscore(allItems.get(i).getAttrName());
            LOG.info("propName ->" + propName);

            if ("id".equals(field.getName())) {
              productBuilder.setId(allItems.get(i).getAttrValue());
            } else if ("nama_produk".equals(field.getName())) {
              productBuilder.setNamaProduk(allItems.get(i).getAttrValue());
            } else if ("deskripsi_pendek".equals(field.getName())) {
              productBuilder.setDeskripsiPendek(allItems.get(i).getAttrValue());
            }
          }
        }
      }
      LOG.info("HAIYA ->" + allItems.get(i).getAttrId());
      ProductAttribute productAttribute = ProductAttribute.newBuilder()
          // .setId(productBuilder.getId().concat("@").concat(allItems.get(i).getAttrId()))
          // .setProdId(productBuilder.getId())
          .setAttrId(allItems.get(i).getAttrId())
          .setAttrName(allItems.get(i).getAttrName())
          .setValue(allItems.get(i).getAttrValue())
          .setIsCommon(allItems.get(i).getIsCommon())
          .build();
      productattributeList.add(productAttribute);
    }
    productBuilder.clearProductAttribute().addAllProductAttribute(productattributeList);

    // ===================================================================
    // tidak jadi create product attribute dgn table tersendiri
    // CREATE PRODUCT
    // CompletionStage<Empty> create_product = components().product()
    // .createProduct(productBuilder.build()).execute();
    // CompletionStage<Effect<Empty>> effect = create_product.thenApply(x -> {
    /* ---- jika melewati ini artinya product sudah dicreate ------- */
    /* ------------------------------------------------------------- */
    // for (int i = 0; i < allItems.size(); i++) {
    /* --- jika melewati ini artinya product_attribute ditemukan --- */
    /* ------------------------------------------------------------- */
    // CompletionStage<Empty> create_product_attribute = components()
    // .productAttribute()
    // .createProductAttribute(convertToProductAttributeApi(allItems.get(i),
    // productBuilder.getId())).execute();
    // }
    // return effects().reply(Empty.getDefaultInstance());
    // });
    /* ------------------------------------------------------------- */
    // return effects().asyncEffect(effect.exceptionally(NotEmptyAuth()));
    /* ------------------------------------------------------------- */
    // ===================================================================

    DeferredCall<ProductApi.Product, Empty> call = components().product()
        .createProduct(productBuilder.build());

    return effects().forward(call);

  }

  // private ProductAttributeApi.ProductAttribute convertToProductAttributeApi(
  // ProductActionApi.Attribute productattribute, String id) {
  // return ProductAttributeApi.ProductAttribute.newBuilder()
  // .setProdAttributeId(id.concat("@").concat(productattribute.getAttributeId()))
  // .setAttributeId(productattribute.getAttributeId())
  // .setProductId(id)
  // .setProdAttributeValue(productattribute.getAttributeValue())
  // .build();
  // }

  // private Function<Throwable, ? extends Effect<Empty>> NotEmptyAuth() {
  // /* --- jika kesini artinya ada error saat mencreate product ---- */
  // /* ------------------------------------------------------------- */
  // return (e) ->
  // effects().error(
  // e.getMessage(),
  // Status.Code.CANCELLED);
  // }

}