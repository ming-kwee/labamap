package io.products.channelProduct.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.UUID;
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
import com.google.rpc.context.AttributeContext.Response;
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
import io.products.channelProduct.action.ChannelProductActionApi.ChannelMetadata;
import io.products.channelProduct.action.ChannelProductActionApi.MetadataGroup;
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
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

public class ChannelProductActionImpl extends AbstractChannelProductAction {
  private static final Logger LOG = LoggerFactory.getLogger(ChannelProductActionImpl.class);

  public ChannelProductActionImpl(ActionCreationContext creationContext) {
  }

  @Override
  public Effect<Empty> createChannelProduct(ChannelProductActionApi.ChannelProducts actChannelProducts) {


    LOG.info("CPCP");
    /* _______________________________________________ */
    // Get Metadata and Set to an List<MetadataGroup>
    /* _______________________________________________ */
    List<MetadataGroup> actChnlMetadataGroupList = actChannelProducts.getChannelProductsList().get(0)
        .getMetadataGroupsList().stream()
        .collect(Collectors.toList());
    /* _______________________________________________ */
    LOG.info("METADATA_TO_HASH" + actChnlMetadataGroupList);
    Map<String, Object> hashmapMetadata = new HashMap<>();
    for (MetadataGroup metadataGroup : actChnlMetadataGroupList) {
      for (ChannelMetadata metadata : metadataGroup.getChannelMetadataList()) {
        hashmapMetadata.put(metadata.getGrouping() + '.' + metadata.getSubGrouping() + '.' + metadata.getKey(),
            metadata);
      }
    }

    List<ChannelProduct.Builder> channelProductBuilders = new ArrayList<>();
    for (ChannelProductActionApi.ChannelProduct actChannelProduct : actChannelProducts.getChannelProductsList()) {
      channelProductBuilders.add(convert_FromAction_ToApi_ChannelProduct(actChannelProduct));
    }
    CompletableFuture<Effect<Empty>> effect;
    if ((boolean) hashmapMetadata.get("integration.body_content.in_array")) {
      /* ___________________________ */
      /* --------------------------- */
      // Create Some Channel Products
      /* ___________________________ */
      /* --------------------------- */
      effect = createSomeChannelProducts(channelProductBuilders);
    } else {
      /* ___________________________ */
      /* --------------------------- */
      // Create A Channel Product
      /* ___________________________ */
      /* --------------------------- */
      effect = createSomeChannelProducts(channelProductBuilders);
    }
    /* ------------------------------------------------------------- */
    return effects().asyncEffect(effect.exceptionally(NotEmptyAuth()));
    /* ------------------------------------------------------------- */

  }

  /*
   * --------------------- Create Some Channel Products --------------------------
   */
  private CompletableFuture<Effect<Empty>> createSomeChannelProducts(
      List<ChannelProduct.Builder> channelProductBuilders) {

    List<CompletableFuture<CompletionStage<Empty>>> futures = new ArrayList<>();
    // Start asynchronous processing for each channelProductBuilder
    for (ChannelProduct.Builder channelProductBuilder : channelProductBuilders) {
      CompletableFuture<CompletionStage<Empty>> future = CompletableFuture.supplyAsync(() -> {
        try {
          // Your asynchronous processing logic goes here
          return components().channelProduct().createChannelProduct(channelProductBuilder.build()).execute();
        } catch (Exception e) {
          LOG.info("ERROR when async processing " + e.getMessage());
          throw new RuntimeException(e); // Handle exceptions appropriately
        }
      }, Executors.newCachedThreadPool()); // Specify an executor if needed
      futures.add(future);
    }

    // Wait for all asynchronous processing to complete
    CompletableFuture<Void> allOf = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));

    // Wait for all executions to complete
    try {
      allOf.get(); // This will block until all executions are completed
    } catch (InterruptedException | ExecutionException e) {
      //  (CompletableFuture<Effect<Empty>>) effects().error(e.getMessage());
      effects().error(e.getMessage());
    }

    // Chain the next step after all asynchronous processing is complete
    // CompletableFuture<Effect<Empty>> effect = allOf.thenCompose(ignored -> {
    return allOf.thenCompose(ignored -> {

      // Process the results of the asynchronous tasks
      List<CompletionStage<Empty>> responses = futures.stream()
          .map(CompletableFuture::join) // Get the result of each CompletableFuture
          .collect(Collectors.toList());

      LOG.info("HITUNG RESPONSE" + responses.size());
      boolean anyException = responses.stream()
          .anyMatch(response -> {
            try {
              response.toCompletableFuture().join();
              return false; // No exception
            } catch (Exception e) {
              // Handle the exception as needed
              System.err.println("Exception in response: " + e.getMessage());
              return true; // Exception encountered
            }
          });

      if (anyException) {
        System.err.println("At least one response encountered an exception");
      } else {
        System.out.println("All responses completed successfully");

        LOG.info("HITUNG SERVICE " + channelProductBuilders.toArray());
        // -----------------------
        // try {
        // ChannelProductHttpResponse verifikasi = ChannelProductService
        // .createChannelProduct(channelProductBuilder.build(), hashmapMetadata);

        // if (verifikasi.getStatus() == "OK") {
        // return effects().reply(Empty.getDefaultInstance());

        // } else {

        // /* _________________________ */
        // /* ------------------------- */
        // // Delete Channel Product
        // /* _________________________ */
        // /* ------------------------- */
        // LOG.info("kesini " + channelProductBuilder.getId());
        // CompletionStage<Empty> delete_channel_product = components().channelProduct()
        // .deleteChannelProduct(DeleteChannelProductRequest.newBuilder()
        // .setId(channelProductBuilder.getId())
        // .build())
        // .execute();
        // return effects().error(verifikasi.getDescription());

        // }

        // } catch (StatusException ex) {
        // return effects().error(ex.getMessage());
        // }
        // -----------------------

      }

      // for (ChannelProduct.Builder channelProductBuilder : channelProductBuilders) {
      // System.out.println("HITUNG Success Data : " + channelProductBuilder.build());
      // }

      return CompletableFuture.completedFuture(effects().reply(Empty.getDefaultInstance()));
    });
  }

  /*
   * -----------------------------------------------------------------------------
   */
  private Function<Throwable, ? extends Effect<Empty>> NotEmptyAuth() {
    /* --- jika kesini artinya ada error saat mencreate product ---- */
    /* ------------------------------------------------------------- */
    return (e) -> effects().error(e.getMessage(), Status.Code.CANCELLED);
    /* ------------------------------------------------------------- */
  }

  /*
   * -----------------------------------------------------------------------------
   */
  private static ChannelProduct.Builder convert_FromAction_ToApi_ChannelProduct(
      ChannelProductActionApi.ChannelProduct actChannelProduct) {

    ChannelProduct.Builder channelProductBuilder = ChannelProduct.newBuilder();
    FieldDescriptor[] fields = channelProductBuilder.getDescriptorForType().getFields()
        .toArray(new FieldDescriptor[0]);

    /* _________________________ */
    /* ------------------------- */
    // Create Channel Attributes
    /* _________________________ */
    /* ------------------------- */
    List<ChannelAttribute> actChnlAttributeList = actChannelProduct.getChannelAttributesList().stream()
        .collect(Collectors.toList());
    List<ChannelProductAttribute> apiChnlProdAttributeList = new ArrayList<>();
    channelProductBuilder.setId(UUID.randomUUID().toString());
    for (ChannelAttribute actChnlAttribute : actChnlAttributeList) {
      // Create Product common fields
      if (actChnlAttribute.getIsCommon() == true) {
        for (FieldDescriptor field : fields) {
          if (field.getName().equals(actChnlAttribute.getChnlAttrName())) {
            String propName = utils.replaceAfterUnderscore(actChnlAttribute.getChnlAttrName());
            if ("productId".equals(field.getName())) {
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

    channelProductBuilder.clearChannelProductOptionGroup()
        .addAllChannelProductOptionGroup(apiChnlProdOptionGroupList);

    return channelProductBuilder;
  }

}