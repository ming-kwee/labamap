package io.products.channelProduct.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;
import io.grpc.Status;
import io.grpc.StatusException;
import io.grpc.StatusRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.protobuf.Descriptors.FieldDescriptor;
import com.google.protobuf.Empty;
import java.util.function.Function;
import akka.actor.ActorSystem;
import akka.http.javadsl.Http;
import akka.http.javadsl.model.ContentTypes;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.HttpResponse;
import akka.http.javadsl.model.headers.RawHeader;
import akka.http.scaladsl.model.StatusCodes;
import io.products.channelProduct.action.ChannelProductActionApi.ChannelAttribute;
import io.products.channelProduct.action.ChannelProductActionApi.ChannelOption;
import io.products.channelProduct.action.ChannelProductActionApi.ChannelVariant;
import io.products.channelProduct.action.ChannelProductActionApi.OptionGroup;
import io.products.channelProduct.action.ChannelProductActionApi.VariantGroup;
import io.products.channelProduct.api.ChannelProductApi;
import io.products.channelProduct.api.ChannelProductApi.ChannelProduct;
import io.products.channelProduct.api.ChannelProductApi.ChannelProductAttribute;
import io.products.channelProduct.api.ChannelProductApi.ChannelProductHttpResponse;
import io.products.channelProduct.api.ChannelProductApi.ChannelProductOption;
import io.products.channelProduct.api.ChannelProductApi.ChannelProductOptionGroup;
import io.products.channelProduct.api.ChannelProductApi.ChannelProductVariant;
import io.products.channelProduct.api.ChannelProductApi.ChannelProductVariantGroup;
import io.products.channelProduct.api.ChannelProductApi.DeleteChannelProductRequest;
import io.products.channelProduct.service.ChannelProductService;
import kalix.javasdk.DeferredCall;
import kalix.javasdk.action.Action.Effect;
import kalix.javasdk.action.ActionCreationContext;
import scala.collection.View.Iterate;
import scala.util.parsing.json.JSON;
import io.products.shared.utils;

public class ChannelProductActionImpl extends AbstractChannelProductAction {
  private static final Logger LOG = LoggerFactory.getLogger(ChannelProductActionImpl.class);

  public ChannelProductActionImpl(ActionCreationContext creationContext) {
  }

  @Override
  public Effect<Empty> createChannelProduct(ChannelProductActionApi.ChannelProduct actChannelProduct) {
    ChannelProduct.Builder channelProductBuilder = ChannelProduct.newBuilder();
    FieldDescriptor[] fields = channelProductBuilder.getDescriptorForType().getFields().toArray(new FieldDescriptor[0]);

    /* _________________________ */
    /* ------------------------- */
    // Create Channel Attributes
    /* _________________________ */
    /* ------------------------- */
    List<ChannelAttribute> actChnlAttributeList = actChannelProduct.getChannelAttributesList().stream()
        .collect(Collectors.toList());
    List<ChannelProductAttribute> apiChnlProdAttributeList = new ArrayList<>();
    for (ChannelAttribute actChnlAttribute : actChnlAttributeList) {

      // Create Product common fields
      if (actChnlAttribute.getIsCommon() == true) {
        for (FieldDescriptor field : fields) {
          if (field.getName().equals(actChnlAttribute.getChnlAttrName())) {
            String propName = utils.replaceAfterUnderscore(actChnlAttribute.getChnlAttrName());
            if ("id".equals(field.getName())) {
              channelProductBuilder.setId(actChnlAttribute.getChnlAttrValue());
            } else if ("productId".equals(field.getName())) {
              channelProductBuilder.setProductId(actChnlAttribute.getChnlAttrValue());
            } else if ("channelId".equals(field.getName())) {
              channelProductBuilder.setChannelId(actChnlAttribute.getChnlAttrValue());
            }
          }
        }
      }

      ChannelProductAttribute apiChnlProdAttribute = ChannelProductAttribute.newBuilder()
          .setAttrId(actChnlAttribute.getAttrId())
          .setChnlAttrName(actChnlAttribute.getChnlAttrName())
          .setChnlAttrType(actChnlAttribute.getChnlAttrType())
          .setValue(actChnlAttribute.getChnlAttrValue())
          .setIsCommon(actChnlAttribute.getIsCommon())
          .build();
      apiChnlProdAttributeList.add(apiChnlProdAttribute);
    }
    channelProductBuilder.clearChannelProductAttribute().addAllChannelProductAttribute(apiChnlProdAttributeList);

    /* _________________________ */
    /* ------------------------- */
    // Create Channel Variants
    /* _________________________ */
    /* ------------------------- */
    List<VariantGroup> actChnlVariantGroupList = actChannelProduct.getVariantGroupsList().stream()
        .collect(Collectors.toList());
    List<ChannelProductVariantGroup> apiChnlProdVariantGroupList = new ArrayList<>();
    for (VariantGroup actChnlVariantGroup : actChnlVariantGroupList) {

      List<ChannelVariant> actChnlVariantList = actChnlVariantGroup.getChannelVariantList().stream()
          .collect(Collectors.toList());
      List<ChannelProductVariant> apiChnlProdVariantList = new ArrayList<>();
      for (ChannelVariant actChnlVariant : actChnlVariantList) {

        ChannelProductVariant apiChnlProdVariant = ChannelProductVariant.newBuilder()
            .setVrntId(actChnlVariant.getVrntId())
            .setChnlVrntName(actChnlVariant.getChnlVrntName())
            .setChnlVrntType(actChnlVariant.getChnlVrntType())
            .setValue(actChnlVariant.getChnlVrntValue())
            .build();
        apiChnlProdVariantList.add(apiChnlProdVariant);

      }
      // Create a ChannelProductVariantGroup and set its channelProductVariants field
      ChannelProductVariantGroup apiChnlProdVariantGroup = ChannelProductVariantGroup.newBuilder()
          .addAllChannelProductVariant(apiChnlProdVariantList)
          .build();
      apiChnlProdVariantGroupList.add(apiChnlProdVariantGroup);
    }

    channelProductBuilder.clearChannelProductVariantGroup()
        .addAllChannelProductVariantGroup(apiChnlProdVariantGroupList);

    /* _________________________ */
    /* ------------------------- */
    // Create Channel Options
    /* _________________________ */
    /* ------------------------- */
    List<OptionGroup> actChnlOptionGroupList = actChannelProduct.getOptionGroupsList().stream()
        .collect(Collectors.toList());
    List<ChannelProductOptionGroup> apiChnlProdOptionGroupList = new ArrayList<>();
    for (OptionGroup actChnlOptionGroup : actChnlOptionGroupList) {

      List<ChannelOption> actChnlOptionList = actChnlOptionGroup.getChannelOptionList().stream()
          .collect(Collectors.toList());
      List<ChannelProductOption> apiChnlProdOptionList = new ArrayList<>();
      for (ChannelOption actChnlOption : actChnlOptionList) {

        ChannelProductOption apiChnlProdOption = ChannelProductOption.newBuilder()
            .setOptnId(actChnlOption.getOptnId())
            .setChnlOptnName(actChnlOption.getChnlOptnName())
            .setChnlOptnType(actChnlOption.getChnlOptnType())
            .setValue(actChnlOption.getChnlOptnValue())
            .build();
        apiChnlProdOptionList.add(apiChnlProdOption);

      }
      // Create a ChannelProductVariantGroup and set its channelProductVariants field
      ChannelProductOptionGroup apiChnlProdOptionGroup = ChannelProductOptionGroup.newBuilder()
          .addAllChannelProductOption(apiChnlProdOptionList)
          .build();
      apiChnlProdOptionGroupList.add(apiChnlProdOptionGroup);
    }

    channelProductBuilder.clearChannelProductOptionGroup().addAllChannelProductOptionGroup(apiChnlProdOptionGroupList);

    /* _________________________ */
    /* ------------------------- */
    // Create Channel Product
    /* _________________________ */
    /* ------------------------- */
    CompletionStage<Empty> create_channel_product = components().channelProduct()
        .createChannelProduct(channelProductBuilder.build()).execute();
    CompletionStage<Effect<Empty>> effect = create_channel_product.thenApply(x -> {

      // return
      // effects().reply(ChannelProductService.createChannelProduct(channelProductBuilder.build()));
      try {
        LOG.info("walau " + channelProductBuilder.getId());
        ChannelProductHttpResponse verifikasi = ChannelProductService
            .createChannelProduct(channelProductBuilder.build());

        if (verifikasi.getStatus() == "OK") {
          return effects().reply(Empty.getDefaultInstance());

        } else {

          /* _________________________ */
          /* ------------------------- */
          // Delete Channel Product
          /* _________________________ */
          /* ------------------------- */
          LOG.info("kesini " + channelProductBuilder.getId());
          CompletionStage<Empty> delete_channel_product = components().channelProduct()
              .deleteChannelProduct(DeleteChannelProductRequest.newBuilder()
                  .setId(channelProductBuilder.getId())
                  .build())
              .execute();
          return effects().error(verifikasi.getDescription());

        }

      } catch (StatusException ex) {
        return effects().error(ex.getMessage());

      }
    });

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