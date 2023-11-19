package io.products.centralProduct.action;

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
import com.google.protobuf.Empty;
import java.util.function.Function;
import akka.actor.ActorSystem;
import akka.http.javadsl.Http;
import akka.http.javadsl.model.ContentTypes;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.HttpResponse;
import akka.http.javadsl.model.headers.RawHeader;
import akka.http.scaladsl.model.StatusCodes;
import io.products.centralProduct.action.CentralProductActionApi.CentralAttribute;
import io.products.centralProduct.action.CentralProductActionApi.CentralOption;
import io.products.centralProduct.action.CentralProductActionApi.CentralVariant;
import io.products.centralProduct.action.CentralProductActionApi.OptionGroup;
import io.products.centralProduct.action.CentralProductActionApi.VariantGroup;
import io.products.centralProduct.api.CentralProductApi;
import io.products.centralProduct.api.CentralProductApi.CentralProduct;
import io.products.centralProduct.api.CentralProductApi.CentralProductAttribute;
import io.products.centralProduct.api.CentralProductApi.CentralProductHttpResponse;
import io.products.centralProduct.api.CentralProductApi.CentralProductOption;
import io.products.centralProduct.api.CentralProductApi.CentralProductOptionGroup;
import io.products.centralProduct.api.CentralProductApi.CentralProductVariant;
import io.products.centralProduct.api.CentralProductApi.CentralProductVariantGroup;
import io.products.centralProduct.api.CentralProductApi.DeleteCentralProductRequest;
import io.products.centralProduct.service.CentralProductService;
import kalix.javasdk.DeferredCall;
import kalix.javasdk.action.Action.Effect;
import kalix.javasdk.action.ActionCreationContext;
import scala.collection.View.Iterate;
import scala.util.parsing.json.JSON;
import io.products.shared.utils;

public class CentralProductActionImpl extends AbstractCentralProductAction {
  private static final Logger LOG = LoggerFactory.getLogger(CentralProductActionImpl.class);

  public CentralProductActionImpl(ActionCreationContext creationContext) {
  }

  @Override
  public Effect<Empty> createCentralProduct(CentralProductActionApi.CentralProduct actCentralProduct) {
    CentralProduct.Builder centralProductBuilder = CentralProduct.newBuilder();
    FieldDescriptor[] fields = centralProductBuilder.getDescriptorForType().getFields().toArray(new FieldDescriptor[0]);

    /* _________________________ */
    /* ------------------------- */
    // Create Central Attributes
    /* _________________________ */
    /* ------------------------- */
    List<CentralAttribute> actCntrlAttributeList = actCentralProduct.getCentralAttributesList().stream()
        .collect(Collectors.toList());
    List<CentralProductAttribute> apiCntrlProdAttributeList = new ArrayList<>();
    centralProductBuilder.setId(UUID.randomUUID().toString());
    for (CentralAttribute actCntrlAttribute : actCntrlAttributeList) {
      // Create Product common fields
      if (actCntrlAttribute.getIsCommon() == true) {
        for (FieldDescriptor field : fields) {
          if (field.getName().equals(actCntrlAttribute.getCntrlAttrName())) {
            String propName = utils.replaceAfterUnderscore(actCntrlAttribute.getCntrlAttrName());
            if ("productId".equals(field.getName())) {
              centralProductBuilder.setProductId(actCntrlAttribute.getCntrlAttrValue());
            } else if ("centralId".equals(field.getName())) {
              centralProductBuilder.setCentralId(actCntrlAttribute.getCntrlAttrValue());
            }
          }
        }
      }

      CentralProductAttribute apiCntrlProdAttribute = CentralProductAttribute.newBuilder()
          .setAttrId(actCntrlAttribute.getAttrId())
          .setCntrlAttrName(actCntrlAttribute.getCntrlAttrName())
          .setCntrlAttrType(actCntrlAttribute.getCntrlAttrType())
          .setValue(actCntrlAttribute.getCntrlAttrValue())
          .setIsCommon(actCntrlAttribute.getIsCommon())
          .build();
      apiCntrlProdAttributeList.add(apiCntrlProdAttribute);
    }
    centralProductBuilder.clearCentralProductAttribute().addAllCentralProductAttribute(apiCntrlProdAttributeList);

    /* _________________________ */
    /* ------------------------- */
    // Create Central Variants
    /* _________________________ */
    /* ------------------------- */
    List<VariantGroup> actCntrlVariantGroupList = actCentralProduct.getVariantGroupsList().stream()
        .collect(Collectors.toList());
    List<CentralProductVariantGroup> apiCntrlProdVariantGroupList = new ArrayList<>();
    for (VariantGroup actCntrlVariantGroup : actCntrlVariantGroupList) {

      List<CentralVariant> actCntrlVariantList = actCntrlVariantGroup.getCentralVariantList().stream()
          .collect(Collectors.toList());
      List<CentralProductVariant> apiCntrlProdVariantList = new ArrayList<>();
      for (CentralVariant actCntrlVariant : actCntrlVariantList) {

        CentralProductVariant apiCntrlProdVariant = CentralProductVariant.newBuilder()
            .setVrntId(actCntrlVariant.getVrntId())
            .setCntrlVrntName(actCntrlVariant.getCntrlVrntName())
            .setCntrlVrntType(actCntrlVariant.getCntrlVrntType())
            .setValue(actCntrlVariant.getCntrlVrntValue())
            .build();
        apiCntrlProdVariantList.add(apiCntrlProdVariant);

      }
      // Create a CentralProductVariantGroup and set its centralProductVariants field
      CentralProductVariantGroup apiCntrlProdVariantGroup = CentralProductVariantGroup.newBuilder()
          .addAllCentralProductVariant(apiCntrlProdVariantList)
          .build();
      apiCntrlProdVariantGroupList.add(apiCntrlProdVariantGroup);
    }

    centralProductBuilder.clearCentralProductVariantGroup()
        .addAllCentralProductVariantGroup(apiCntrlProdVariantGroupList);

    /* _________________________ */
    /* ------------------------- */
    // Create Central Options
    /* _________________________ */
    /* ------------------------- */
    List<OptionGroup> actCntrlOptionGroupList = actCentralProduct.getOptionGroupsList().stream()
        .collect(Collectors.toList());
    List<CentralProductOptionGroup> apiCntrlProdOptionGroupList = new ArrayList<>();
    for (OptionGroup actCntrlOptionGroup : actCntrlOptionGroupList) {

      List<CentralOption> actCntrlOptionList = actCntrlOptionGroup.getCentralOptionList().stream()
          .collect(Collectors.toList());
      List<CentralProductOption> apiCntrlProdOptionList = new ArrayList<>();
      for (CentralOption actCntrlOption : actCntrlOptionList) {

        CentralProductOption apiCntrlProdOption = CentralProductOption.newBuilder()
            .setOptnId(actCntrlOption.getOptnId())
            .setCntrlOptnName(actCntrlOption.getCntrlOptnName())
            .setCntrlOptnType(actCntrlOption.getCntrlOptnType())
            .setValue(actCntrlOption.getCntrlOptnValue())
            .build();
        apiCntrlProdOptionList.add(apiCntrlProdOption);

      }
      // Create a CentralProductVariantGroup and set its centralProductVariants field
      CentralProductOptionGroup apiCntrlProdOptionGroup = CentralProductOptionGroup.newBuilder()
          .addAllCentralProductOption(apiCntrlProdOptionList)
          .build();
      apiCntrlProdOptionGroupList.add(apiCntrlProdOptionGroup);
    }

    centralProductBuilder.clearCentralProductOptionGroup().addAllCentralProductOptionGroup(apiCntrlProdOptionGroupList);

    /* _________________________ */
    /* ------------------------- */
    // Create Central Product
    /* _________________________ */
    /* ------------------------- */
    CompletionStage<Empty> create_central_product = components().centralProduct()
        .createCentralProduct(centralProductBuilder.build()).execute();
    CompletionStage<Effect<Empty>> effect = create_central_product.thenApply(x -> {

      // return
      // effects().reply(CentralProductService.createCentralProduct(centralProductBuilder.build()));
      try {
        LOG.info("walau " + centralProductBuilder.getId());
        CentralProductHttpResponse verifikasi = CentralProductService
            .createCentralProduct(centralProductBuilder.build());

        if (verifikasi.getStatus() == "OK") {
          return effects().reply(Empty.getDefaultInstance());

        } else {

          /* _________________________ */
          /* ------------------------- */
          // Delete Central Product
          /* _________________________ */
          /* ------------------------- */
          LOG.info("kesini " + centralProductBuilder.getId());
          CompletionStage<Empty> delete_central_product = components().centralProduct()
              .deleteCentralProduct(DeleteCentralProductRequest.newBuilder()
                  .setId(centralProductBuilder.getId())
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