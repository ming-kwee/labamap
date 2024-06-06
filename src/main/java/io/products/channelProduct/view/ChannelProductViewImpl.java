package io.products.channelProduct.view;

import io.products.channelProduct.api.ChannelProductApi;
import io.products.channelProduct.domain.ChannelProductDomain;
import kalix.javasdk.view.View;
import kalix.javasdk.view.ViewContext;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.products.channelProduct.domain.ChannelProductDomain.ChannelProductAttribute;
import io.products.channelProduct.domain.ChannelProductDomain.ChannelProductOption;
import io.products.channelProduct.domain.ChannelProductDomain.ChannelProductOptionGroup;
import io.products.channelProduct.domain.ChannelProductDomain.ChannelProductVariant;
import io.products.channelProduct.domain.ChannelProductDomain.ChannelProductVariantGroup;

// This class was initially generated based on the .proto definition by Kalix tooling.
// This is the implementation for the View Service described in your io/products/channelProduct/view/channel_product_view.proto file.
//
// As long as this file exists it will not be overwritten: you can maintain it yourself,
// or delete it so it is regenerated as needed.

public class ChannelProductViewImpl extends AbstractChannelProductView {
    private static final Logger LOG = LoggerFactory.getLogger(ChannelProductViewImpl.class);

    public ChannelProductViewImpl(ViewContext context) {
    }

    @Override
    public ChannelProductApi.ChannelProduct emptyState() {
        return null;
    }

    @Override
    public View.UpdateEffect<ChannelProductApi.ChannelProduct> processChannelProductCreated(
            ChannelProductApi.ChannelProduct state,
            ChannelProductDomain.ChannelProductCreated channelProductCreated) {

        if (state != null) {
            return effects().ignore(); // already created
        } else {
            ChannelProductApi.ChannelProduct created = convertToApi(channelProductCreated.getChannelProduct());
            return effects().updateState(created);
        }
    }

    @Override
    public View.UpdateEffect<ChannelProductApi.ChannelProduct> processChannelProductUpdated(
            ChannelProductApi.ChannelProduct state,
            ChannelProductDomain.ChannelProductUpdated channelProductUpdated) {

        if (state == null) {
            return effects().ignore(); // state not found
        } else {
            ChannelProductApi.ChannelProduct updated = convertToApi(channelProductUpdated.getChannelProduct());
            return effects().updateState(updated);
        }
    }


    @Override
    public View.UpdateEffect<ChannelProductApi.ChannelProduct> processChannelProductDeleted(
            ChannelProductApi.ChannelProduct state,
            ChannelProductDomain.ChannelProductDeleted channelProductDeleted) {

        if (state == null) {
            return effects().ignore(); // state not found
        } else {
            ChannelProductApi.ChannelProduct deleted = convertToApi(channelProductDeleted.getChannelProduct());
            return effects().updateState(deleted);
        }
    }

    private ChannelProductApi.ChannelProduct convertToApi(ChannelProductDomain.ChannelProductState channelProductState) {

        ChannelProductApi.ChannelProduct.Builder apiChannelProduct = ChannelProductApi.ChannelProduct.newBuilder();

        /* ------------------------------------------ */
        // Convert Channel Attributes from Domain to Api
        /* ------------------------------------------ */
        List<ChannelProductAttribute> domChnlProdAttributeList = channelProductState.getChannelProductAttributeList();

        List<io.products.channelProduct.api.ChannelProductApi.ChannelProductAttribute> apiChnlProdAttributeList = new ArrayList<>();
        for (ChannelProductAttribute domChnlProdAttribute : domChnlProdAttributeList) {
            io.products.channelProduct.api.ChannelProductApi.ChannelProductAttribute apiChnlProdAttribute = io.products.channelProduct.api.ChannelProductApi.ChannelProductAttribute
                    .newBuilder()
                    .setAttrId(domChnlProdAttribute.getAttrId())
                    .setChnlAttrName(domChnlProdAttribute.getChnlAttrName())
                    .setChnlAttrType(domChnlProdAttribute.getChnlAttrType())
                    .setValue(domChnlProdAttribute.getValue())
                    .setIsCommon(domChnlProdAttribute.getIsCommon())
                    .build();
            apiChnlProdAttributeList.add(apiChnlProdAttribute);
        }

        /* ------------------------------------------ */
        // Convert Channel Variants from Domain to Api
        /* ------------------------------------------ */
        List<ChannelProductVariantGroup> domChnlProdVariantGroupList = channelProductState
                .getChannelProductVariantGroupList();
        List<io.products.channelProduct.api.ChannelProductApi.ChannelProductVariantGroup> apiChnlProdVariantGroupList = new ArrayList<>();

        for (ChannelProductVariantGroup domChnlProdVariantGroup : domChnlProdVariantGroupList) {

            List<ChannelProductVariant> domChnlProdVariantList = domChnlProdVariantGroup
                    .getChannelProductVariantList().stream()
                    .collect(Collectors.toList());
            List<io.products.channelProduct.api.ChannelProductApi.ChannelProductVariant> apiChnlProdVariantList = new ArrayList<>();

            for (ChannelProductVariant domChnlProdVariant : domChnlProdVariantList) {
                io.products.channelProduct.api.ChannelProductApi.ChannelProductVariant apiChnlProdVariant = io.products.channelProduct.api.ChannelProductApi.ChannelProductVariant
                        .newBuilder()
                        .setVrntId(domChnlProdVariant.getVrntId())
                        .setChnlVrntName(domChnlProdVariant.getChnlVrntName())
                        .setChnlVrntType(domChnlProdVariant.getChnlVrntType())
                        .setValue(domChnlProdVariant.getValue())
                        .build();
                apiChnlProdVariantList.add(apiChnlProdVariant);
            }

            io.products.channelProduct.api.ChannelProductApi.ChannelProductVariantGroup apiChnlProdVariantGroup = io.products.channelProduct.api.ChannelProductApi.ChannelProductVariantGroup
                    .newBuilder()
                    .addAllChannelProductVariant(apiChnlProdVariantList)
                    .build();
            apiChnlProdVariantGroupList.add(apiChnlProdVariantGroup);
        }

        /* ------------------------------------------ */
        // Convert Channel Options from Api to State
        /* ------------------------------------------ */
        List<ChannelProductOptionGroup> domChnlProdOptionGroupList = channelProductState
                .getChannelProductOptionGroupList();
        List<io.products.channelProduct.api.ChannelProductApi.ChannelProductOptionGroup> apiChnlProdOptionGroupList = new ArrayList<>();

        for (ChannelProductOptionGroup domChnlProdOptionGroup : domChnlProdOptionGroupList) {

            List<ChannelProductOption> domChnlProdOptionList = domChnlProdOptionGroup
                    .getChannelProductOptionList().stream()
                    .collect(Collectors.toList());
            List<io.products.channelProduct.api.ChannelProductApi.ChannelProductOption> apiChnlProdOptionList = new ArrayList<>();

            for (ChannelProductOption domChnlProdOption : domChnlProdOptionList) {
                io.products.channelProduct.api.ChannelProductApi.ChannelProductOption apiChnlProdOption = io.products.channelProduct.api.ChannelProductApi.ChannelProductOption
                        .newBuilder()
                        .setOptnId(domChnlProdOption.getOptnId())
                        .setChnlOptnName(domChnlProdOption.getChnlOptnName())
                        .setChnlOptnType(domChnlProdOption.getChnlOptnType())
                        .setValue(domChnlProdOption.getValue())
                        .build();
                apiChnlProdOptionList.add(apiChnlProdOption);
            }

            io.products.channelProduct.api.ChannelProductApi.ChannelProductOptionGroup apiChnlProdOptionGroup = io.products.channelProduct.api.ChannelProductApi.ChannelProductOptionGroup
                    .newBuilder()
                    .addAllChannelProductOption(apiChnlProdOptionList)
                    .build();
            apiChnlProdOptionGroupList.add(apiChnlProdOptionGroup);
        }

        apiChannelProduct
                .setId(channelProductState.getId())
                .setSku(channelProductState.getSku())
                .setStoreId(channelProductState.getStoreId())
                .setChannelId(channelProductState.getChannelId())
                .clearChannelProductAttribute().addAllChannelProductAttribute(apiChnlProdAttributeList)
                .clearChannelProductVariantGroup().addAllChannelProductVariantGroup(apiChnlProdVariantGroupList)
                .clearChannelProductOptionGroup().addAllChannelProductOptionGroup(apiChnlProdOptionGroupList)
                .setIsDeleted(channelProductState.getIsDeleted())
                .setEventId(channelProductState.getEventId())
                .setProductId(channelProductState.getProductId());

        return apiChannelProduct.build();

    }

}
