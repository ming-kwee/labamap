package io.products.centralProduct.domain;

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
import io.products.centralProduct.action.CentralProductActionApi.CentralAttribute;
import io.products.centralProduct.action.CentralProductActionApi.CentralVariant;
import io.products.centralProduct.api.CentralProductApi;
import io.products.centralProduct.api.CentralProductApi.DeleteCentralProductRequest;
import io.products.centralProduct.domain.CentralProductDomain.CentralProductVariantGroup;
import io.products.centralProduct.domain.CentralProductDomain;
import io.products.centralProduct.domain.CentralProductDomain.CentralProductAttribute;
import io.products.centralProduct.domain.CentralProductDomain.CentralProductOption;
import io.products.centralProduct.domain.CentralProductDomain.CentralProductOptionGroup;
import io.products.centralProduct.domain.CentralProductDomain.CentralProductState;
import io.products.centralProduct.domain.CentralProductDomain.CentralProductVariant;
import kalix.javasdk.eventsourcedentity.EventSourcedEntityContext;
import akka.http.javadsl.Http;
import akka.http.javadsl.model.ContentTypes;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.HttpResponse;
// This class was initially generated based on the .proto definition by Kalix tooling.
// This is the implementation for the Value Entity Service described in your io/products/central_product/api/central_product_api.proto file.
//
// As long as this file exists it will not be overwritten: you can maintain it yourself,
// or delete it so it is regenerated as needed.
import akka.http.javadsl.model.headers.RawHeader;

public class CentralProduct extends AbstractCentralProduct {
  private static final Logger LOG = LoggerFactory.getLogger(CentralProduct.class);
  @SuppressWarnings("unused")
  private final String entityId;

  public CentralProduct(EventSourcedEntityContext context) {
    this.entityId = context.entityId();
  }

  @Override
  public CentralProductDomain.CentralProductState emptyState() {
    return CentralProductDomain.CentralProductState.getDefaultInstance();
  }

  /***********************************************************
   * CREATE CHANNEL PRODUCT & convertToDomain & handleCreation
   ***********************************************************/

  @Override
  public Effect<Empty> createCentralProduct(CentralProductDomain.CentralProductState currentState,
      CentralProductApi.CentralProduct command) {

    CentralProductDomain.CentralProductState state = convertToDomain(currentState, command);

    return reject(currentState, command).orElseGet(() -> handleCreation(state, command));
  }

  private Optional<Effect<Empty>> reject(CentralProductState currentState,
      io.products.centralProduct.api.CentralProductApi.CentralProduct command) {

    if (currentState.getId().equals(command.getId()) && currentState.getIsDeleted() == false) {
      return Optional.of(effects().error("Central Product is already exists!!!", Status.Code.NOT_FOUND));

    } else {
      return Optional.empty();
    }

  }

  private CentralProductDomain.CentralProductState convertToDomain(
      CentralProductDomain.CentralProductState currentState,
      io.products.centralProduct.api.CentralProductApi.CentralProduct apiCentralProduct) {

    CentralProductDomain.CentralProductState.Builder stateBuilder = currentState.toBuilder();

    /* ------------------------------------------ */
    // Convert Central Attributes from Api to State
    /* ------------------------------------------ */
    List<io.products.centralProduct.api.CentralProductApi.CentralProductAttribute> apiCntrlProdAttributeList = apiCentralProduct
        .getCentralProductAttributeList();
    List<CentralProductAttribute> domCntrlProdAttributeList = new ArrayList<>();
    for (io.products.centralProduct.api.CentralProductApi.CentralProductAttribute apiCntrlProdAttribute : apiCntrlProdAttributeList) {
      CentralProductAttribute domCntrlProdAttribute = CentralProductAttribute.newBuilder()
          .setAttrId(apiCntrlProdAttribute.getAttrId())
          .setCntrlAttrName(apiCntrlProdAttribute.getCntrlAttrName())
          .setCntrlAttrType(apiCntrlProdAttribute.getCntrlAttrType())
          .setValue(apiCntrlProdAttribute.getValue())
          .setIsCommon(apiCntrlProdAttribute.getIsCommon())
          .build();
      domCntrlProdAttributeList.add(domCntrlProdAttribute);
    }

    /* ------------------------------------------ */
    // Convert Central Variants from Api to State
    /* ------------------------------------------ */
    List<io.products.centralProduct.api.CentralProductApi.CentralProductVariantGroup> apiCntrlProdVariantGroupList = apiCentralProduct
        .getCentralProductVariantGroupList();
    List<CentralProductVariantGroup> domCntrlProdVariantGroupList = new ArrayList<>();

    for (io.products.centralProduct.api.CentralProductApi.CentralProductVariantGroup apiCntrlProdVariantGroup : apiCntrlProdVariantGroupList) {

      List<io.products.centralProduct.api.CentralProductApi.CentralProductVariant> apiCntrlProdVariantList = apiCntrlProdVariantGroup
          .getCentralProductVariantList().stream()
          .collect(Collectors.toList());
      List<CentralProductVariant> domCntrlProdVariantList = new ArrayList<>();

      for (io.products.centralProduct.api.CentralProductApi.CentralProductVariant apiCntrlProdVariant : apiCntrlProdVariantList) {
        CentralProductVariant domCntrlProdVariant = CentralProductVariant.newBuilder()
            .setVrntId(apiCntrlProdVariant.getVrntId())
            .setCntrlVrntName(apiCntrlProdVariant.getCntrlVrntName())
            .setCntrlVrntType(apiCntrlProdVariant.getCntrlVrntType())
            .setValue(apiCntrlProdVariant.getValue())
            .build();
        domCntrlProdVariantList.add(domCntrlProdVariant);
      }

      CentralProductVariantGroup domCntrlProdVariantGroup = CentralProductVariantGroup.newBuilder()
          .addAllCentralProductVariant(domCntrlProdVariantList)
          .build();
      domCntrlProdVariantGroupList.add(domCntrlProdVariantGroup);
    }

    /* ------------------------------------------ */
    // Convert Central Options from Api to State
    /* ------------------------------------------ */
    List<io.products.centralProduct.api.CentralProductApi.CentralProductOptionGroup> apiCntrlProdOptionGroupList = apiCentralProduct
        .getCentralProductOptionGroupList();
    List<CentralProductOptionGroup> domCntrlProdOptionGroupList = new ArrayList<>();

    for (io.products.centralProduct.api.CentralProductApi.CentralProductOptionGroup apiCntrlProdOptionGroup : apiCntrlProdOptionGroupList) {

      List<io.products.centralProduct.api.CentralProductApi.CentralProductOption> apiCntrlProdOptionList = apiCntrlProdOptionGroup
          .getCentralProductOptionList().stream()
          .collect(Collectors.toList());
      List<CentralProductOption> domCntrlProdOptionList = new ArrayList<>();

      for (io.products.centralProduct.api.CentralProductApi.CentralProductOption apiCntrlProdOption : apiCntrlProdOptionList) {
        CentralProductOption domCntrlProdOption = CentralProductOption.newBuilder()
            .setOptnId(apiCntrlProdOption.getOptnId())
            .setCntrlOptnName(apiCntrlProdOption.getCntrlOptnName())
            .setCntrlOptnType(apiCntrlProdOption.getCntrlOptnType())
            .setValue(apiCntrlProdOption.getValue())
            .build();
        domCntrlProdOptionList.add(domCntrlProdOption);
      }

      CentralProductOptionGroup domCntrlProdOptionGroup = CentralProductOptionGroup.newBuilder()
          .addAllCentralProductOption(domCntrlProdOptionList)
          .build();
      domCntrlProdOptionGroupList.add(domCntrlProdOptionGroup);
    }

    stateBuilder
        .setId(apiCentralProduct.getId())
        .setCentralId(apiCentralProduct.getCentralId())
        .setProductId(apiCentralProduct.getProductId())
        .setIsDeleted(apiCentralProduct.getIsDeleted())
        .clearCentralProductAttribute().addAllCentralProductAttribute(domCntrlProdAttributeList)
        .clearCentralProductVariantGroup().addAllCentralProductVariantGroup(domCntrlProdVariantGroupList)
        .clearCentralProductOptionGroup().addAllCentralProductOptionGroup(domCntrlProdOptionGroupList);

    return stateBuilder.build();
  }

  private Effect<Empty> handleCreation(CentralProductDomain.CentralProductState state,
      io.products.centralProduct.api.CentralProductApi.CentralProduct command) {
    CentralProductDomain.CentralProductCreated event = CentralProductDomain.CentralProductCreated.newBuilder()
        .setCentralProduct(state).build();
    return effects().emitEvent(event).thenReply(__ -> Empty.getDefaultInstance());
    // return effects().updateState(state).thenReply(Empty.getDefaultInstance());
  }

  /***********************************************
   * DELETE CHANNEL PRODUCT & handleDeletion
   ***********************************************/
  @Override
  public Effect<Empty> deleteCentralProduct(CentralProductDomain.CentralProductState currentState,
      CentralProductApi.DeleteCentralProductRequest command) {
    LOG.info("deletedelete " + currentState.getCentralId() + ", " +
        currentState.getId() + ", " + command.getId());

    return handleDeletion(currentState, command);
  }

  private Effect<Empty> handleDeletion(CentralProductDomain.CentralProductState state,
      CentralProductApi.DeleteCentralProductRequest command) {

    CentralProductDomain.CentralProductState deletedState = CentralProductDomain.CentralProductState.newBuilder()
        .setIsDeleted(true)
        .setCentralId(state.getCentralId())
        .setProductId(state.getProductId())
        .clearCentralProductVariantGroup().addAllCentralProductVariantGroup(state.getCentralProductVariantGroupList())
        .clearCentralProductAttribute().addAllCentralProductAttribute(state.getCentralProductAttributeList())
        .clearCentralProductOptionGroup().addAllCentralProductOptionGroup(state.getCentralProductOptionGroupList())
        .setId(state.getId()).build();

    CentralProductDomain.CentralProductDeleted event = CentralProductDomain.CentralProductDeleted.newBuilder()
        .setCentralProduct(deletedState).build();

    return effects().emitEvent(event).thenReply(__ -> Empty.getDefaultInstance());
    // return effects().updateState(state).thenReply(Empty.getDefaultInstance());
  }

  /************************************************
   * GET CHANNEL PRODUCT & convertToApi
   ************************************************/
  @Override
  public Effect<CentralProductApi.CentralProduct> getCentralProduct(
      CentralProductDomain.CentralProductState currentState,
      CentralProductApi.GetCentralProductRequest command) {
    if (currentState.getId().equals(command.getId())) {
      return effects().reply(convertToApi(currentState));
    } else {
      return effects().error("Central Patient " + command.getId() + " has not been created.");
    }
  }

  private CentralProductApi.CentralProduct convertToApi(CentralProductDomain.CentralProductState centralProductState) {

    CentralProductApi.CentralProduct.Builder apiCentralProduct = CentralProductApi.CentralProduct.newBuilder();

    /* ------------------------------------------ */
    // Convert Central Attributes from Domain to Api
    /* ------------------------------------------ */
    List<CentralProductAttribute> domCntrlProdAttributeList = centralProductState.getCentralProductAttributeList();

    List<io.products.centralProduct.api.CentralProductApi.CentralProductAttribute> apiCntrlProdAttributeList = new ArrayList<>();
    for (CentralProductAttribute domCntrlProdAttribute : domCntrlProdAttributeList) {
      io.products.centralProduct.api.CentralProductApi.CentralProductAttribute apiCntrlProdAttribute = io.products.centralProduct.api.CentralProductApi.CentralProductAttribute
          .newBuilder()
          .setAttrId(domCntrlProdAttribute.getAttrId())
          .setCntrlAttrName(domCntrlProdAttribute.getCntrlAttrName())
          .setCntrlAttrType(domCntrlProdAttribute.getCntrlAttrType())
          .setValue(domCntrlProdAttribute.getValue())
          .setIsCommon(domCntrlProdAttribute.getIsCommon())
          .build();
      apiCntrlProdAttributeList.add(apiCntrlProdAttribute);
    }

    /* ------------------------------------------ */
    // Convert Central Variants from Domain to Api
    /* ------------------------------------------ */
    List<CentralProductVariantGroup> domCntrlProdVariantGroupList = centralProductState
        .getCentralProductVariantGroupList();
    List<io.products.centralProduct.api.CentralProductApi.CentralProductVariantGroup> apiCntrlProdVariantGroupList = new ArrayList<>();

    for (CentralProductVariantGroup domCntrlProdVariantGroup : domCntrlProdVariantGroupList) {

      List<CentralProductVariant> domCntrlProdVariantList = domCntrlProdVariantGroup
          .getCentralProductVariantList().stream()
          .collect(Collectors.toList());
      List<io.products.centralProduct.api.CentralProductApi.CentralProductVariant> apiCntrlProdVariantList = new ArrayList<>();

      for (CentralProductVariant domCntrlProdVariant : domCntrlProdVariantList) {
        io.products.centralProduct.api.CentralProductApi.CentralProductVariant apiCntrlProdVariant = io.products.centralProduct.api.CentralProductApi.CentralProductVariant
            .newBuilder()
            .setVrntId(domCntrlProdVariant.getVrntId())
            .setCntrlVrntName(domCntrlProdVariant.getCntrlVrntName())
            .setCntrlVrntType(domCntrlProdVariant.getCntrlVrntType())
            .setValue(domCntrlProdVariant.getValue())
            .build();
        apiCntrlProdVariantList.add(apiCntrlProdVariant);
      }

      io.products.centralProduct.api.CentralProductApi.CentralProductVariantGroup apiCntrlProdVariantGroup = io.products.centralProduct.api.CentralProductApi.CentralProductVariantGroup
          .newBuilder()
          .addAllCentralProductVariant(apiCntrlProdVariantList)
          .build();
      apiCntrlProdVariantGroupList.add(apiCntrlProdVariantGroup);
    }

    /* ------------------------------------------ */
    // Convert Central Options from Api to State
    /* ------------------------------------------ */
    List<CentralProductOptionGroup> domCntrlProdOptionGroupList = centralProductState
        .getCentralProductOptionGroupList();
    List<io.products.centralProduct.api.CentralProductApi.CentralProductOptionGroup> apiCntrlProdOptionGroupList = new ArrayList<>();

    for (CentralProductOptionGroup domCntrlProdOptionGroup : domCntrlProdOptionGroupList) {

      List<CentralProductOption> domCntrlProdOptionList = domCntrlProdOptionGroup
          .getCentralProductOptionList().stream()
          .collect(Collectors.toList());
      List<io.products.centralProduct.api.CentralProductApi.CentralProductOption> apiCntrlProdOptionList = new ArrayList<>();

      for (CentralProductOption domCntrlProdOption : domCntrlProdOptionList) {
        io.products.centralProduct.api.CentralProductApi.CentralProductOption apiCntrlProdOption = io.products.centralProduct.api.CentralProductApi.CentralProductOption
            .newBuilder()
            .setOptnId(domCntrlProdOption.getOptnId())
            .setCntrlOptnName(domCntrlProdOption.getCntrlOptnName())
            .setCntrlOptnType(domCntrlProdOption.getCntrlOptnType())
            .setValue(domCntrlProdOption.getValue())
            .build();
        apiCntrlProdOptionList.add(apiCntrlProdOption);
      }

      io.products.centralProduct.api.CentralProductApi.CentralProductOptionGroup apiCntrlProdOptionGroup = io.products.centralProduct.api.CentralProductApi.CentralProductOptionGroup
          .newBuilder()
          .addAllCentralProductOption(apiCntrlProdOptionList)
          .build();
      apiCntrlProdOptionGroupList.add(apiCntrlProdOptionGroup);
    }

    apiCentralProduct
        .setId(centralProductState.getId())
        .setCentralId(centralProductState.getCentralId())
        .setProductId(centralProductState.getProductId())
        .setIsDeleted(centralProductState.getIsDeleted())
        .clearCentralProductAttribute().addAllCentralProductAttribute(apiCntrlProdAttributeList)
        .clearCentralProductVariantGroup().addAllCentralProductVariantGroup(apiCntrlProdVariantGroupList)
        .clearCentralProductOptionGroup().addAllCentralProductOptionGroup(apiCntrlProdOptionGroupList);

    return apiCentralProduct.build();

  }

  /* ___________________________ */
  /* --------------------------- */
  // event done event done event 
  /* ___________________________ */
  /* --------------------------- */

  @Override
  public CentralProductDomain.CentralProductState centralProductCreated(
      CentralProductDomain.CentralProductState currentState, CentralProductDomain.CentralProductCreated event) {

    CentralProductDomain.CentralProductState.Builder stateBuilder = currentState.toBuilder();

    stateBuilder
        .setId(event.getCentralProduct().getId())
        .setCentralId(event.getCentralProduct().getCentralId())
        .setProductId(event.getCentralProduct().getProductId())
        .setIsDeleted(event.getCentralProduct().getIsDeleted())
        .clearCentralProductAttribute()
        .addAllCentralProductAttribute(event.getCentralProduct().getCentralProductAttributeList())
        .clearCentralProductVariantGroup()
        .addAllCentralProductVariantGroup(event.getCentralProduct().getCentralProductVariantGroupList())
        .clearCentralProductOptionGroup()
        .addAllCentralProductOptionGroup(event.getCentralProduct().getCentralProductOptionGroupList());

    LOG.info("VVVC " + stateBuilder.getIsDeleted());

    return stateBuilder.build();
  }

  @Override
  public CentralProductDomain.CentralProductState centralProductDeleted(
      CentralProductDomain.CentralProductState currentState, CentralProductDomain.CentralProductDeleted event) {

    CentralProductDomain.CentralProductState.Builder stateBuilder = currentState.toBuilder();
    stateBuilder
        .setId(event.getCentralProduct().getId())
        .setCentralId(event.getCentralProduct().getCentralId())
        .setProductId(event.getCentralProduct().getProductId())
        .setIsDeleted(event.getCentralProduct().getIsDeleted())
        .clearCentralProductAttribute()
        .addAllCentralProductAttribute(event.getCentralProduct().getCentralProductAttributeList())
        .clearCentralProductVariantGroup()
        .addAllCentralProductVariantGroup(event.getCentralProduct().getCentralProductVariantGroupList())
        .clearCentralProductOptionGroup()
        .addAllCentralProductOptionGroup(event.getCentralProduct().getCentralProductOptionGroupList());

    return stateBuilder.build();
  }

}
