package io.products.centralProduct.view;

import io.products.centralProduct.api.CentralProductApi;
import io.products.centralProduct.domain.CentralProductDomain;
import kalix.javasdk.view.View;
import kalix.javasdk.view.ViewContext;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.products.centralProduct.domain.CentralProductDomain.CentralProductAttribute;
import io.products.centralProduct.domain.CentralProductDomain.CentralProductOption;
import io.products.centralProduct.domain.CentralProductDomain.CentralProductOptionGroup;
import io.products.centralProduct.domain.CentralProductDomain.CentralProductVariant;
import io.products.centralProduct.domain.CentralProductDomain.CentralProductVariantGroup;

// This class was initially generated based on the .proto definition by Kalix tooling.
// This is the implementation for the View Service described in your io/products/centralProduct/view/central_product_view.proto file.
//
// As long as this file exists it will not be overwritten: you can maintain it yourself,
// or delete it so it is regenerated as needed.

public class CentralProductViewImpl extends AbstractCentralProductView {
  private static final Logger LOG = LoggerFactory.getLogger(CentralProductViewImpl.class);

  public CentralProductViewImpl(ViewContext context) {
  }

  @Override
  public CentralProductApi.CentralProduct emptyState() {
    return null;
  }

  @Override
  public View.UpdateEffect<CentralProductApi.CentralProduct> processCentralProductCreated(
      CentralProductApi.CentralProduct state,
      CentralProductDomain.CentralProductCreated centralProductCreated) {

    if (state != null) {
      return effects().ignore(); // already created
    } else {
      CentralProductApi.CentralProduct created = convertToApi(centralProductCreated.getCentralProduct());
      return effects().updateState(created);
    }
  }

  @Override
  public View.UpdateEffect<CentralProductApi.CentralProduct> processCentralProductDeleted(
      CentralProductApi.CentralProduct state,
      CentralProductDomain.CentralProductDeleted centralProductDeleted) {

    if (state == null) {
      return effects().ignore(); // state not found
    } else {
      CentralProductApi.CentralProduct deleted = convertToApi(centralProductDeleted.getCentralProduct());
      return effects().updateState(deleted);
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
        .clearCentralProductOptionGroup().addAllCentralProductOptionGroup(apiCntrlProdOptionGroupList)
        .setIsDeleted(centralProductState.getIsDeleted());

    return apiCentralProduct.build();

  }

}
