package io.products.masterProduct.view;

import io.products.masterProduct.api.MasterProductApi;
import io.products.masterProduct.domain.MasterProductDomain;
import kalix.javasdk.view.View;
import kalix.javasdk.view.ViewContext;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.products.masterProduct.domain.MasterProductDomain.MasterProductAttribute;
import io.products.masterProduct.domain.MasterProductDomain.MasterProductOption;
import io.products.masterProduct.domain.MasterProductDomain.MasterProductOptionGroup;
import io.products.masterProduct.domain.MasterProductDomain.MasterProductVariant;
import io.products.masterProduct.domain.MasterProductDomain.MasterProductVariantGroup;

// This class was initially generated based on the .proto definition by Kalix tooling.
// This is the implementation for the View Service described in your io/products/masterProduct/view/master_product_view.proto file.
//
// As long as this file exists it will not be overwritten: you can maintain it yourself,
// or delete it so it is regenerated as needed.

public class MasterProductViewImpl extends AbstractMasterProductView {
  private static final Logger LOG = LoggerFactory.getLogger(MasterProductViewImpl.class);

  public MasterProductViewImpl(ViewContext context) {
  }

  @Override
  public MasterProductApi.MasterProduct emptyState() {
    return null;
  }

  @Override
  public View.UpdateEffect<MasterProductApi.MasterProduct> processMasterProductCreated(
      MasterProductApi.MasterProduct state,
      MasterProductDomain.MasterProductCreated masterProductCreated) {

    if (state != null) {
      return effects().ignore(); // already created
    } else {
      MasterProductApi.MasterProduct created = convertToApi(masterProductCreated.getMasterProduct());
      return effects().updateState(created);
    }
  }

  @Override
  public View.UpdateEffect<MasterProductApi.MasterProduct> processMasterProductDeleted(
      MasterProductApi.MasterProduct state,
      MasterProductDomain.MasterProductDeleted masterProductDeleted) {

    if (state == null) {
      return effects().ignore(); // state not found
    } else {
      MasterProductApi.MasterProduct deleted = convertToApi(masterProductDeleted.getMasterProduct());
      return effects().updateState(deleted);
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
        .clearMasterProductOptionGroup().addAllMasterProductOptionGroup(apiMstrProdOptionGroupList)
        .setIsDeleted(masterProductState.getIsDeleted());

    return apiMasterProduct.build();

  }

}
