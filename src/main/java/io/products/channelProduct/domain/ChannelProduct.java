package io.products.channelProduct.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.Empty;
import kalix.javasdk.action.Action.Effect;

import akka.actor.ActorSystem;
import io.grpc.Status;
import io.products.channelProduct.action.ChannelProductActionApi.ChannelAttribute;
import io.products.channelProduct.action.ChannelProductActionApi.ChannelVariant;
import io.products.channelProduct.api.ChannelProductApi;
// import io.products.channelProduct.api.ChannelProductApi.ChannelProductVariant;
import io.products.channelProduct.domain.ChannelProductDomain.ChannelProductVariantGroup;
import io.products.channelProduct.domain.ChannelProductDomain;
import io.products.channelProduct.domain.ChannelProductDomain.ChannelProductAttribute;
import io.products.channelProduct.domain.ChannelProductDomain.ChannelProductOption;
import io.products.channelProduct.domain.ChannelProductDomain.ChannelProductOptionGroup;
import io.products.channelProduct.domain.ChannelProductDomain.ChannelProductState;
import io.products.channelProduct.domain.ChannelProductDomain.ChannelProductVariant;
// import io.products.product.action.ProductActionImpl;
// import io.products.product.domain.ProductDomain;
// import io.products.product.domain.ProductDomain.ProductAttribute;
import kalix.javasdk.valueentity.ValueEntityContext;
import akka.http.javadsl.Http;
import akka.http.javadsl.model.ContentTypes;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.HttpResponse;
// This class was initially generated based on the .proto definition by Kalix tooling.
// This is the implementation for the Value Entity Service described in your io/products/channel_product/api/channel_product_api.proto file.
//
// As long as this file exists it will not be overwritten: you can maintain it yourself,
// or delete it so it is regenerated as needed.
import akka.http.javadsl.model.headers.RawHeader;

public class ChannelProduct extends AbstractChannelProduct {
  private static final Logger LOG = LoggerFactory.getLogger(ChannelProduct.class);
  @SuppressWarnings("unused")
  private final String entityId;

  public ChannelProduct(ValueEntityContext context) {
    this.entityId = context.entityId();
  }

  @Override
  public ChannelProductDomain.ChannelProductState emptyState() {
    return ChannelProductDomain.ChannelProductState.getDefaultInstance();
  }

  @Override
  public Effect<Empty> createChannelProduct(ChannelProductDomain.ChannelProductState currentState,
      ChannelProductApi.ChannelProduct command) {

    ChannelProductDomain.ChannelProductState state = convertToDomain(currentState, command);

    return reject(currentState, command).orElseGet(() -> handle(state, command));
  }

  private Optional<Effect<Empty>> reject(ChannelProductState currentState,
      io.products.channelProduct.api.ChannelProductApi.ChannelProduct command) {

    if (currentState.getId().equals(command.getId())) {
      return Optional.of(effects().error("Channel Product is already exists!!!", Status.Code.NOT_FOUND));

    } else {
      return Optional.empty();
    }

  }

  private ChannelProductDomain.ChannelProductState convertToDomain(
      ChannelProductDomain.ChannelProductState currentState,
      io.products.channelProduct.api.ChannelProductApi.ChannelProduct apiChannelProduct) {

    ChannelProductDomain.ChannelProductState.Builder stateBuilder = currentState.toBuilder();

    /* ------------------------------------------ */
    // Convert Channel Attributes from Api to State
    /* ------------------------------------------ */
    List<io.products.channelProduct.api.ChannelProductApi.ChannelProductAttribute> apiChnlProdAttributeList = apiChannelProduct
        .getChannelProductAttributeList();
    List<ChannelProductAttribute> domChnlProdAttributeList = new ArrayList<>();
    for (io.products.channelProduct.api.ChannelProductApi.ChannelProductAttribute apiChnlProdAttribute : apiChnlProdAttributeList) {
      ChannelProductAttribute domChnlProdAttribute = ChannelProductAttribute.newBuilder()
          .setAttrId(apiChnlProdAttribute.getAttrId())
          .setChnlAttrName(apiChnlProdAttribute.getChnlAttrName())
          .setChnlAttrType(apiChnlProdAttribute.getChnlAttrType())
          .setValue(apiChnlProdAttribute.getValue())
          .setIsCommon(apiChnlProdAttribute.getIsCommon())
          .build();
      domChnlProdAttributeList.add(domChnlProdAttribute);
    }

    /* ------------------------------------------ */
    // Convert Channel Variants from Api to State
    /* ------------------------------------------ */
    List<io.products.channelProduct.api.ChannelProductApi.ChannelProductVariantGroup> apiChnlProdVariantGroupList = apiChannelProduct
        .getChannelProductVariantGroupList();
    List<ChannelProductVariantGroup> domChnlProdVariantGroupList = new ArrayList<>();

    for (io.products.channelProduct.api.ChannelProductApi.ChannelProductVariantGroup apiChnlProdVariantGroup : apiChnlProdVariantGroupList) {

      List<io.products.channelProduct.api.ChannelProductApi.ChannelProductVariant> apiChnlProdVariantList = apiChnlProdVariantGroup
          .getChannelProductVariantList().stream()
          .collect(Collectors.toList());
      List<ChannelProductVariant> domChnlProdVariantList = new ArrayList<>();

      for (io.products.channelProduct.api.ChannelProductApi.ChannelProductVariant apiChnlProdVariant : apiChnlProdVariantList) {
        ChannelProductVariant domChnlProdVariant = ChannelProductVariant.newBuilder()
            .setVrntId(apiChnlProdVariant.getVrntId())
            .setChnlVrntName(apiChnlProdVariant.getChnlVrntName())
            .setChnlVrntType(apiChnlProdVariant.getChnlVrntType())
            .setValue(apiChnlProdVariant.getValue())
            .build();
        domChnlProdVariantList.add(domChnlProdVariant);
      }

      ChannelProductVariantGroup domChnlProdVariantGroup = ChannelProductVariantGroup.newBuilder()
          .addAllChannelProductVariant(domChnlProdVariantList)
          .build();
      domChnlProdVariantGroupList.add(domChnlProdVariantGroup);
    }



    /* ------------------------------------------ */
    // Convert Channel Options from Api to State
    /* ------------------------------------------ */
    List<io.products.channelProduct.api.ChannelProductApi.ChannelProductOptionGroup> apiChnlProdOptionGroupList = apiChannelProduct
        .getChannelProductOptionGroupList();
    List<ChannelProductOptionGroup> domChnlProdOptionGroupList = new ArrayList<>();

    for (io.products.channelProduct.api.ChannelProductApi.ChannelProductOptionGroup apiChnlProdOptionGroup : apiChnlProdOptionGroupList) {

      List<io.products.channelProduct.api.ChannelProductApi.ChannelProductOption> apiChnlProdOptionList = apiChnlProdOptionGroup
          .getChannelProductOptionList().stream()
          .collect(Collectors.toList());
      List<ChannelProductOption> domChnlProdOptionList = new ArrayList<>();

      for (io.products.channelProduct.api.ChannelProductApi.ChannelProductOption apiChnlProdOption : apiChnlProdOptionList) {
        ChannelProductOption domChnlProdOption = ChannelProductOption.newBuilder()
            .setOptnId(apiChnlProdOption.getOptnId())
            .setChnlOptnName(apiChnlProdOption.getChnlOptnName())
            .setChnlOptnType(apiChnlProdOption.getChnlOptnType())
            .setValue(apiChnlProdOption.getValue())
            .build();
        domChnlProdOptionList.add(domChnlProdOption);
      }

      ChannelProductOptionGroup domChnlProdOptionGroup = ChannelProductOptionGroup.newBuilder()
          .addAllChannelProductOption(domChnlProdOptionList)
          .build();
      domChnlProdOptionGroupList.add(domChnlProdOptionGroup);
    }






    stateBuilder
        .setId(apiChannelProduct.getId())
        .setChannelId(apiChannelProduct.getChannelId())
        .setProductId(apiChannelProduct.getProductId())
        .clearChannelProductAttribute().addAllChannelProductAttribute(domChnlProdAttributeList)
        .clearChannelProductVariantGroup().addAllChannelProductVariantGroup(domChnlProdVariantGroupList)
        .clearChannelProductOptionGroup().addAllChannelProductOptionGroup(domChnlProdOptionGroupList);

    return stateBuilder.build();
  }

  private Effect<Empty> handle(ChannelProductDomain.ChannelProductState state,
      io.products.channelProduct.api.ChannelProductApi.ChannelProduct command) {
    return effects().updateState(state).thenReply(Empty.getDefaultInstance());
  }

}
