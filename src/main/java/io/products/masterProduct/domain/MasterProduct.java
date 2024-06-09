package io.products.masterProduct.domain;

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
import io.products.masterProduct.action.MasterProductActionApi.MasterAttribute;
import io.products.masterProduct.action.MasterProductActionApi.MasterVariant;
import io.products.masterProduct.api.MasterProductApi;
import io.products.masterProduct.api.MasterProductApi.DeleteMasterProductRequest;
import io.products.masterProduct.domain.MasterProductDomain.MasterProductVariantGroup;
import io.products.masterProduct.domain.MasterProductDomain;
import io.products.masterProduct.domain.MasterProductDomain.MasterProductAttribute;
import io.products.masterProduct.domain.MasterProductDomain.MasterProductOption;
import io.products.masterProduct.domain.MasterProductDomain.MasterProductOptionGroup;
import io.products.masterProduct.domain.MasterProductDomain.MasterProductState;
import io.products.masterProduct.domain.MasterProductDomain.MasterProductVariant;
import kalix.javasdk.eventsourcedentity.EventSourcedEntityContext;
import akka.http.javadsl.Http;
import akka.http.javadsl.model.ContentTypes;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.HttpResponse;
// This class was initially generated based on the .proto definition by Kalix tooling.
// This is the implementation for the Value Entity Service described in your io/products/master_product/api/master_product_api.proto file.
//
// As long as this file exists it will not be overwritten: you can maintain it yourself,
// or delete it so it is regenerated as needed.
import akka.http.javadsl.model.headers.RawHeader;

public class MasterProduct extends AbstractMasterProduct {
  private static final Logger LOG = LoggerFactory.getLogger(MasterProduct.class);
  @SuppressWarnings("unused")
  private final String entityId;

  public MasterProduct(EventSourcedEntityContext context) {
    this.entityId = context.entityId();
  }

  @Override
  public MasterProductDomain.MasterProductState emptyState() {
    return MasterProductDomain.MasterProductState.getDefaultInstance();
  }

  /***********************************************************
   * CREATE CHANNEL PRODUCT & convertToDomain & handleCreation
   ***********************************************************/

  @Override
  public Effect<Empty> createMasterProduct(MasterProductDomain.MasterProductState currentState,
      MasterProductApi.MasterProduct command) {

    MasterProductDomain.MasterProductState state = convertToDomain(currentState, command);

    return reject(currentState, command).orElseGet(() -> handleCreation(state, command));
  }

  private Optional<Effect<Empty>> reject(MasterProductState currentState,
      io.products.masterProduct.api.MasterProductApi.MasterProduct command) {

    if (currentState.getId().equals(command.getId()) && currentState.getIsDeleted() == false) {
      return Optional.of(effects().error("Master Product is already exists!!!", Status.Code.NOT_FOUND));

    } else {
      return Optional.empty();
    }

  }

  private MasterProductDomain.MasterProductState convertToDomain(
      MasterProductDomain.MasterProductState currentState,
      io.products.masterProduct.api.MasterProductApi.MasterProduct apiMasterProduct) {

    MasterProductDomain.MasterProductState.Builder stateBuilder = currentState.toBuilder();

    /* ------------------------------------------ */
    // Convert Master Attributes from Api to State
    /* ------------------------------------------ */
    List<io.products.masterProduct.api.MasterProductApi.MasterProductAttribute> apiMstrProdAttributeList = apiMasterProduct
        .getMasterProductAttributeList();
    List<MasterProductAttribute> domMstrProdAttributeList = new ArrayList<>();
    for (io.products.masterProduct.api.MasterProductApi.MasterProductAttribute apiMstrProdAttribute : apiMstrProdAttributeList) {
      MasterProductAttribute domMstrProdAttribute = MasterProductAttribute.newBuilder()
          .setAttrId(apiMstrProdAttribute.getAttrId())
          .setMstrAttrName(apiMstrProdAttribute.getMstrAttrName())
          .setMstrAttrType(apiMstrProdAttribute.getMstrAttrType())
          .setValue(apiMstrProdAttribute.getValue())
          .setIsCommonField(apiMstrProdAttribute.getIsCommonField())
          .build();
      domMstrProdAttributeList.add(domMstrProdAttribute);
    }

    /* ------------------------------------------ */
    // Convert Master Variants from Api to State
    /* ------------------------------------------ */
    List<io.products.masterProduct.api.MasterProductApi.MasterProductVariantGroup> apiMstrProdVariantGroupList = apiMasterProduct
        .getMasterProductVariantGroupList();
    List<MasterProductVariantGroup> domMstrProdVariantGroupList = new ArrayList<>();

    for (io.products.masterProduct.api.MasterProductApi.MasterProductVariantGroup apiMstrProdVariantGroup : apiMstrProdVariantGroupList) {

      List<io.products.masterProduct.api.MasterProductApi.MasterProductVariant> apiMstrProdVariantList = apiMstrProdVariantGroup
          .getMasterProductVariantList().stream()
          .collect(Collectors.toList());
      List<MasterProductVariant> domMstrProdVariantList = new ArrayList<>();

      for (io.products.masterProduct.api.MasterProductApi.MasterProductVariant apiMstrProdVariant : apiMstrProdVariantList) {
        MasterProductVariant domMstrProdVariant = MasterProductVariant.newBuilder()
            .setVrntId(apiMstrProdVariant.getVrntId())
            .setMstrVrntName(apiMstrProdVariant.getMstrVrntName())
            .setMstrVrntType(apiMstrProdVariant.getMstrVrntType())
            .setValue(apiMstrProdVariant.getValue())
            .build();
        domMstrProdVariantList.add(domMstrProdVariant);
      }

      MasterProductVariantGroup domMstrProdVariantGroup = MasterProductVariantGroup.newBuilder()
          .addAllMasterProductVariant(domMstrProdVariantList)
          .build();
      domMstrProdVariantGroupList.add(domMstrProdVariantGroup);
    }

    /* ------------------------------------------ */
    // Convert Master Options from Api to State
    /* ------------------------------------------ */
    List<io.products.masterProduct.api.MasterProductApi.MasterProductOptionGroup> apiMstrProdOptionGroupList = apiMasterProduct
        .getMasterProductOptionGroupList();
    List<MasterProductOptionGroup> domMstrProdOptionGroupList = new ArrayList<>();

    for (io.products.masterProduct.api.MasterProductApi.MasterProductOptionGroup apiMstrProdOptionGroup : apiMstrProdOptionGroupList) {

      List<io.products.masterProduct.api.MasterProductApi.MasterProductOption> apiMstrProdOptionList = apiMstrProdOptionGroup
          .getMasterProductOptionList().stream()
          .collect(Collectors.toList());
      List<MasterProductOption> domMstrProdOptionList = new ArrayList<>();

      for (io.products.masterProduct.api.MasterProductApi.MasterProductOption apiMstrProdOption : apiMstrProdOptionList) {
        MasterProductOption domMstrProdOption = MasterProductOption.newBuilder()
            .setOptnId(apiMstrProdOption.getOptnId())
            .setMstrOptnName(apiMstrProdOption.getMstrOptnName())
            .setMstrOptnType(apiMstrProdOption.getMstrOptnType())
            .setValue(apiMstrProdOption.getValue())
            .build();
        domMstrProdOptionList.add(domMstrProdOption);
      }

      MasterProductOptionGroup domMstrProdOptionGroup = MasterProductOptionGroup.newBuilder()
          .addAllMasterProductOption(domMstrProdOptionList)
          .build();
      domMstrProdOptionGroupList.add(domMstrProdOptionGroup);
    }

    stateBuilder
        .setId(apiMasterProduct.getId())
        .setMasterId(apiMasterProduct.getMasterId())
        .setProductId(apiMasterProduct.getProductId())
        .setIsDeleted(apiMasterProduct.getIsDeleted())
        .clearMasterProductAttribute().addAllMasterProductAttribute(domMstrProdAttributeList)
        .clearMasterProductVariantGroup().addAllMasterProductVariantGroup(domMstrProdVariantGroupList)
        .clearMasterProductOptionGroup().addAllMasterProductOptionGroup(domMstrProdOptionGroupList);

    return stateBuilder.build();
  }

  private Effect<Empty> handleCreation(MasterProductDomain.MasterProductState state,
      io.products.masterProduct.api.MasterProductApi.MasterProduct command) {
    MasterProductDomain.MasterProductCreated event = MasterProductDomain.MasterProductCreated.newBuilder()
        .setMasterProduct(state).build();
    return effects().emitEvent(event).thenReply(__ -> Empty.getDefaultInstance());
    // return effects().updateState(state).thenReply(Empty.getDefaultInstance());
  }

  /***********************************************
   * DELETE CHANNEL PRODUCT & handleDeletion
   ***********************************************/
  @Override
  public Effect<Empty> deleteMasterProduct(MasterProductDomain.MasterProductState currentState,
      MasterProductApi.DeleteMasterProductRequest command) {
    LOG.info("deletedelete " + currentState.getMasterId() + ", " +
        currentState.getId() + ", " + command.getId());

    return handleDeletion(currentState, command);
  }

  private Effect<Empty> handleDeletion(MasterProductDomain.MasterProductState state,
      MasterProductApi.DeleteMasterProductRequest command) {

    MasterProductDomain.MasterProductState deletedState = MasterProductDomain.MasterProductState.newBuilder()
        .setIsDeleted(true)
        .setMasterId(state.getMasterId())
        .setProductId(state.getProductId())
        .clearMasterProductVariantGroup().addAllMasterProductVariantGroup(state.getMasterProductVariantGroupList())
        .clearMasterProductAttribute().addAllMasterProductAttribute(state.getMasterProductAttributeList())
        .clearMasterProductOptionGroup().addAllMasterProductOptionGroup(state.getMasterProductOptionGroupList())
        .setId(state.getId()).build();

    MasterProductDomain.MasterProductDeleted event = MasterProductDomain.MasterProductDeleted.newBuilder()
        .setMasterProduct(deletedState).build();

    return effects().emitEvent(event).thenReply(__ -> Empty.getDefaultInstance());
    // return effects().updateState(state).thenReply(Empty.getDefaultInstance());
  }

  /************************************************
   * GET CHANNEL PRODUCT & convertToApi
   ************************************************/
  @Override
  public Effect<MasterProductApi.MasterProduct> getMasterProduct(
      MasterProductDomain.MasterProductState currentState,
      MasterProductApi.GetMasterProductRequest command) {
    if (currentState.getId().equals(command.getId())) {
      return effects().reply(convertToApi(currentState));
    } else {
      return effects().error("Master Patient " + command.getId() + " has not been created.");
    }
  }

  private MasterProductApi.MasterProduct convertToApi(MasterProductDomain.MasterProductState masterProductState) {

    MasterProductApi.MasterProduct.Builder apiMasterProduct = MasterProductApi.MasterProduct.newBuilder();

    /* ------------------------------------------ */
    // Convert Master Attributes from Domain to Api
    /* ------------------------------------------ */
    List<MasterProductAttribute> domMstrProdAttributeList = masterProductState.getMasterProductAttributeList();

    List<io.products.masterProduct.api.MasterProductApi.MasterProductAttribute> apiMstrProdAttributeList = new ArrayList<>();
    for (MasterProductAttribute domMstrProdAttribute : domMstrProdAttributeList) {
      io.products.masterProduct.api.MasterProductApi.MasterProductAttribute apiMstrProdAttribute = io.products.masterProduct.api.MasterProductApi.MasterProductAttribute
          .newBuilder()
          .setAttrId(domMstrProdAttribute.getAttrId())
          .setMstrAttrName(domMstrProdAttribute.getMstrAttrName())
          .setMstrAttrType(domMstrProdAttribute.getMstrAttrType())
          .setValue(domMstrProdAttribute.getValue())
          .setIsCommonField(domMstrProdAttribute.getIsCommonField())
          .build();
      apiMstrProdAttributeList.add(apiMstrProdAttribute);
    }

    /* ------------------------------------------ */
    // Convert Master Variants from Domain to Api
    /* ------------------------------------------ */
    List<MasterProductVariantGroup> domMstrProdVariantGroupList = masterProductState
        .getMasterProductVariantGroupList();
    List<io.products.masterProduct.api.MasterProductApi.MasterProductVariantGroup> apiMstrProdVariantGroupList = new ArrayList<>();

    for (MasterProductVariantGroup domMstrProdVariantGroup : domMstrProdVariantGroupList) {

      List<MasterProductVariant> domMstrProdVariantList = domMstrProdVariantGroup
          .getMasterProductVariantList().stream()
          .collect(Collectors.toList());
      List<io.products.masterProduct.api.MasterProductApi.MasterProductVariant> apiMstrProdVariantList = new ArrayList<>();

      for (MasterProductVariant domMstrProdVariant : domMstrProdVariantList) {
        io.products.masterProduct.api.MasterProductApi.MasterProductVariant apiMstrProdVariant = io.products.masterProduct.api.MasterProductApi.MasterProductVariant
            .newBuilder()
            .setVrntId(domMstrProdVariant.getVrntId())
            .setMstrVrntName(domMstrProdVariant.getMstrVrntName())
            .setMstrVrntType(domMstrProdVariant.getMstrVrntType())
            .setValue(domMstrProdVariant.getValue())
            .build();
        apiMstrProdVariantList.add(apiMstrProdVariant);
      }

      io.products.masterProduct.api.MasterProductApi.MasterProductVariantGroup apiMstrProdVariantGroup = io.products.masterProduct.api.MasterProductApi.MasterProductVariantGroup
          .newBuilder()
          .addAllMasterProductVariant(apiMstrProdVariantList)
          .build();
      apiMstrProdVariantGroupList.add(apiMstrProdVariantGroup);
    }

    /* ------------------------------------------ */
    // Convert Master Options from Api to State
    /* ------------------------------------------ */
    List<MasterProductOptionGroup> domMstrProdOptionGroupList = masterProductState
        .getMasterProductOptionGroupList();
    List<io.products.masterProduct.api.MasterProductApi.MasterProductOptionGroup> apiMstrProdOptionGroupList = new ArrayList<>();

    for (MasterProductOptionGroup domMstrProdOptionGroup : domMstrProdOptionGroupList) {

      List<MasterProductOption> domMstrProdOptionList = domMstrProdOptionGroup
          .getMasterProductOptionList().stream()
          .collect(Collectors.toList());
      List<io.products.masterProduct.api.MasterProductApi.MasterProductOption> apiMstrProdOptionList = new ArrayList<>();

      for (MasterProductOption domMstrProdOption : domMstrProdOptionList) {
        io.products.masterProduct.api.MasterProductApi.MasterProductOption apiMstrProdOption = io.products.masterProduct.api.MasterProductApi.MasterProductOption
            .newBuilder()
            .setOptnId(domMstrProdOption.getOptnId())
            .setMstrOptnName(domMstrProdOption.getMstrOptnName())
            .setMstrOptnType(domMstrProdOption.getMstrOptnType())
            .setValue(domMstrProdOption.getValue())
            .build();
        apiMstrProdOptionList.add(apiMstrProdOption);
      }

      io.products.masterProduct.api.MasterProductApi.MasterProductOptionGroup apiMstrProdOptionGroup = io.products.masterProduct.api.MasterProductApi.MasterProductOptionGroup
          .newBuilder()
          .addAllMasterProductOption(apiMstrProdOptionList)
          .build();
      apiMstrProdOptionGroupList.add(apiMstrProdOptionGroup);
    }

    apiMasterProduct
        .setId(masterProductState.getId())
        .setMasterId(masterProductState.getMasterId())
        .setProductId(masterProductState.getProductId())
        .setIsDeleted(masterProductState.getIsDeleted())
        .clearMasterProductAttribute().addAllMasterProductAttribute(apiMstrProdAttributeList)
        .clearMasterProductVariantGroup().addAllMasterProductVariantGroup(apiMstrProdVariantGroupList)
        .clearMasterProductOptionGroup().addAllMasterProductOptionGroup(apiMstrProdOptionGroupList);

    return apiMasterProduct.build();

  }

  /* ___________________________ */
  /* --------------------------- */
  // event done event done event 
  /* ___________________________ */
  /* --------------------------- */

  @Override
  public MasterProductDomain.MasterProductState masterProductCreated(
      MasterProductDomain.MasterProductState currentState, MasterProductDomain.MasterProductCreated event) {

    MasterProductDomain.MasterProductState.Builder stateBuilder = currentState.toBuilder();

    stateBuilder
        .setId(event.getMasterProduct().getId())
        .setMasterId(event.getMasterProduct().getMasterId())
        .setProductId(event.getMasterProduct().getProductId())
        .setIsDeleted(event.getMasterProduct().getIsDeleted())
        .clearMasterProductAttribute()
        .addAllMasterProductAttribute(event.getMasterProduct().getMasterProductAttributeList())
        .clearMasterProductVariantGroup()
        .addAllMasterProductVariantGroup(event.getMasterProduct().getMasterProductVariantGroupList())
        .clearMasterProductOptionGroup()
        .addAllMasterProductOptionGroup(event.getMasterProduct().getMasterProductOptionGroupList());

    LOG.info("VVVC " + stateBuilder.getIsDeleted());

    return stateBuilder.build();
  }

  @Override
  public MasterProductDomain.MasterProductState masterProductDeleted(
      MasterProductDomain.MasterProductState currentState, MasterProductDomain.MasterProductDeleted event) {

    MasterProductDomain.MasterProductState.Builder stateBuilder = currentState.toBuilder();
    stateBuilder
        .setId(event.getMasterProduct().getId())
        .setMasterId(event.getMasterProduct().getMasterId())
        .setProductId(event.getMasterProduct().getProductId())
        .setIsDeleted(event.getMasterProduct().getIsDeleted())
        .clearMasterProductAttribute()
        .addAllMasterProductAttribute(event.getMasterProduct().getMasterProductAttributeList())
        .clearMasterProductVariantGroup()
        .addAllMasterProductVariantGroup(event.getMasterProduct().getMasterProductVariantGroupList())
        .clearMasterProductOptionGroup()
        .addAllMasterProductOptionGroup(event.getMasterProduct().getMasterProductOptionGroupList());

    return stateBuilder.build();
  }

}
