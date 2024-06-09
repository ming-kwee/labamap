package io.products.masterProduct.action;

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
import io.products.masterProduct.action.MasterProductActionApi.MasterAttribute;
import io.products.masterProduct.action.MasterProductActionApi.MasterOption;
import io.products.masterProduct.action.MasterProductActionApi.MasterVariant;
import io.products.masterProduct.action.MasterProductActionApi.OptionGroup;
import io.products.masterProduct.action.MasterProductActionApi.VariantGroup;
import io.products.masterProduct.api.MasterProductApi;
import io.products.masterProduct.api.MasterProductApi.MasterProduct;
import io.products.masterProduct.api.MasterProductApi.MasterProductAttribute;
import io.products.masterProduct.api.MasterProductApi.MasterProductHttpResponse;
import io.products.masterProduct.api.MasterProductApi.MasterProductOption;
import io.products.masterProduct.api.MasterProductApi.MasterProductOptionGroup;
import io.products.masterProduct.api.MasterProductApi.MasterProductVariant;
import io.products.masterProduct.api.MasterProductApi.MasterProductVariantGroup;
import io.products.masterProduct.api.MasterProductApi.DeleteMasterProductRequest;
import io.products.masterProduct.service.MasterProductService;
import kalix.javasdk.DeferredCall;
import kalix.javasdk.action.Action.Effect;
import kalix.javasdk.action.ActionCreationContext;
import scala.collection.View.Iterate;
import scala.util.parsing.json.JSON;
import io.products.shared.utils;

public class MasterProductActionImpl extends AbstractMasterProductAction {
  private static final Logger LOG = LoggerFactory.getLogger(MasterProductActionImpl.class);

  public MasterProductActionImpl(ActionCreationContext creationContext) {
  }

  @Override
  public Effect<Empty> createMasterProduct(MasterProductActionApi.MasterProduct actMasterProduct) {
    MasterProduct.Builder masterProductBuilder = MasterProduct.newBuilder();
    FieldDescriptor[] fields = masterProductBuilder.getDescriptorForType().getFields().toArray(new FieldDescriptor[0]);

    /* _________________________ */
    /* ------------------------- */
    // Create Master Attributes
    /* _________________________ */
    /* ------------------------- */
    List<MasterAttribute> actMstrAttributeList = actMasterProduct.getMasterAttributesList().stream()
        .collect(Collectors.toList());
    List<MasterProductAttribute> apiMstrProdAttributeList = new ArrayList<>();
    masterProductBuilder.setId(UUID.randomUUID().toString());
    for (MasterAttribute actMstrAttribute : actMstrAttributeList) {
      // Create Product common fields
      if (actMstrAttribute.getIsCommonField() == true) {
        for (FieldDescriptor field : fields) {
          if (field.getName().equals(actMstrAttribute.getMstrAttrName())) {
            String propName = utils.replaceAfterUnderscore(actMstrAttribute.getMstrAttrName());
            if ("productId".equals(field.getName())) {
              masterProductBuilder.setProductId(actMstrAttribute.getMstrAttrValue());
            } else if ("masterId".equals(field.getName())) {
              masterProductBuilder.setMasterId(actMstrAttribute.getMstrAttrValue());
            }
          }
        }
      }

      MasterProductAttribute apiMstrProdAttribute = MasterProductAttribute.newBuilder()
          .setAttrId(actMstrAttribute.getAttrId())
          .setMstrAttrName(actMstrAttribute.getMstrAttrName())
          .setMstrAttrType(actMstrAttribute.getMstrAttrType())
          .setValue(actMstrAttribute.getMstrAttrValue())
          .setIsCommonField(actMstrAttribute.getIsCommonField())
          .build();
      apiMstrProdAttributeList.add(apiMstrProdAttribute);
    }
    masterProductBuilder.clearMasterProductAttribute().addAllMasterProductAttribute(apiMstrProdAttributeList);

    /* _________________________ */
    /* ------------------------- */
    // Create Master Variants
    /* _________________________ */
    /* ------------------------- */
    List<VariantGroup> actMstrVariantGroupList = actMasterProduct.getVariantGroupsList().stream()
        .collect(Collectors.toList());
    List<MasterProductVariantGroup> apiMstrProdVariantGroupList = new ArrayList<>();
    for (VariantGroup actMstrVariantGroup : actMstrVariantGroupList) {

      List<MasterVariant> actMstrVariantList = actMstrVariantGroup.getMasterVariantList().stream()
          .collect(Collectors.toList());
      List<MasterProductVariant> apiMstrProdVariantList = new ArrayList<>();
      for (MasterVariant actMstrVariant : actMstrVariantList) {

        MasterProductVariant apiMstrProdVariant = MasterProductVariant.newBuilder()
            .setVrntId(actMstrVariant.getVrntId())
            .setMstrVrntName(actMstrVariant.getMstrVrntName())
            .setMstrVrntType(actMstrVariant.getMstrVrntType())
            .setValue(actMstrVariant.getMstrVrntValue())
            .build();
        apiMstrProdVariantList.add(apiMstrProdVariant);

      }
      // Create a MasterProductVariantGroup and set its masterProductVariants field
      MasterProductVariantGroup apiMstrProdVariantGroup = MasterProductVariantGroup.newBuilder()
          .addAllMasterProductVariant(apiMstrProdVariantList)
          .build();
      apiMstrProdVariantGroupList.add(apiMstrProdVariantGroup);
    }

    masterProductBuilder.clearMasterProductVariantGroup()
        .addAllMasterProductVariantGroup(apiMstrProdVariantGroupList);

    /* _________________________ */
    /* ------------------------- */
    // Create Master Options
    /* _________________________ */
    /* ------------------------- */
    List<OptionGroup> actMstrOptionGroupList = actMasterProduct.getOptionGroupsList().stream()
        .collect(Collectors.toList());
    List<MasterProductOptionGroup> apiMstrProdOptionGroupList = new ArrayList<>();
    for (OptionGroup actMstrOptionGroup : actMstrOptionGroupList) {

      List<MasterOption> actMstrOptionList = actMstrOptionGroup.getMasterOptionList().stream()
          .collect(Collectors.toList());
      List<MasterProductOption> apiMstrProdOptionList = new ArrayList<>();
      for (MasterOption actMstrOption : actMstrOptionList) {

        MasterProductOption apiMstrProdOption = MasterProductOption.newBuilder()
            .setOptnId(actMstrOption.getOptnId())
            .setMstrOptnName(actMstrOption.getMstrOptnName())
            .setMstrOptnType(actMstrOption.getMstrOptnType())
            .setValue(actMstrOption.getMstrOptnValue())
            .build();
        apiMstrProdOptionList.add(apiMstrProdOption);

      }
      // Create a MasterProductVariantGroup and set its masterProductVariants field
      MasterProductOptionGroup apiMstrProdOptionGroup = MasterProductOptionGroup.newBuilder()
          .addAllMasterProductOption(apiMstrProdOptionList)
          .build();
      apiMstrProdOptionGroupList.add(apiMstrProdOptionGroup);
    }

    masterProductBuilder.clearMasterProductOptionGroup().addAllMasterProductOptionGroup(apiMstrProdOptionGroupList);

    /* _________________________ */
    /* ------------------------- */
    // Create Master Product
    /* _________________________ */
    /* ------------------------- */
    CompletionStage<Empty> create_master_product = components().masterProduct()
        .createMasterProduct(masterProductBuilder.build()).execute();
    CompletionStage<Effect<Empty>> effect = create_master_product.thenApply(x -> {

      // return
      // effects().reply(MasterProductService.createMasterProduct(masterProductBuilder.build()));
      try {
        LOG.info("walau " + masterProductBuilder.getId());
        MasterProductHttpResponse verifikasi = MasterProductService
            .createMasterProduct(masterProductBuilder.build());

        if (verifikasi.getStatus() == "OK") {
          return effects().reply(Empty.getDefaultInstance());

        } else {

          /* _________________________ */
          /* ------------------------- */
          // Delete Master Product
          /* _________________________ */
          /* ------------------------- */
          LOG.info("kesini " + masterProductBuilder.getId());
          CompletionStage<Empty> delete_master_product = components().masterProduct()
              .deleteMasterProduct(DeleteMasterProductRequest.newBuilder()
                  .setId(masterProductBuilder.getId())
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