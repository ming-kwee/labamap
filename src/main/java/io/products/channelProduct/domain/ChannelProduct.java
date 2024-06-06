package io.products.channelProduct.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.protobuf.Empty;
import io.grpc.Status;
import io.products.channelProduct.api.ChannelProductApi;
import io.products.channelProduct.domain.ChannelProductDomain.ChannelProductVariantGroup;
import io.products.channelProduct.domain.ChannelProductDomain.ChannelProductAttribute;
import io.products.channelProduct.domain.ChannelProductDomain.ChannelProductOption;
import io.products.channelProduct.domain.ChannelProductDomain.ChannelProductOptionGroup;
import io.products.channelProduct.domain.ChannelProductDomain.ChannelProductState;
import io.products.channelProduct.domain.ChannelProductDomain.ChannelProductVariant;
import kalix.javasdk.eventsourcedentity.EventSourcedEntityContext;

public class ChannelProduct extends AbstractChannelProduct {
    private static final Logger LOG = LoggerFactory.getLogger(ChannelProduct.class);
    @SuppressWarnings("unused")
    private final String entityId;

    public ChannelProduct(EventSourcedEntityContext context) {
        this.entityId = context.entityId();
    }

    @Override
    public ChannelProductDomain.ChannelProductState emptyState() {
        return ChannelProductDomain.ChannelProductState.getDefaultInstance();
    }

    /***********************************************************
     * CREATE CHANNEL PRODUCT & convertToDomain & handleCreation
     ***********************************************************/

    @Override
    public Effect<Empty> createChannelProduct(ChannelProductDomain.ChannelProductState currentState,
                                              ChannelProductApi.CreateChannelProductCommand command) {
        ChannelProductDomain.ChannelProductState state = convertToDomain(currentState, command);

        return reject(currentState, command).orElseGet(() -> handleCreation(state, command));
    }

    private Optional<Effect<Empty>> reject(ChannelProductState currentState,
                                           io.products.channelProduct.api.ChannelProductApi.CreateChannelProductCommand command) {

        if (currentState.getId().equals(command.getId()) && currentState.getIsDeleted() == false) {
            return Optional.of(effects().error("Channel Product is already exists!!!", Status.Code.NOT_FOUND));
        } else {
            return Optional.empty();
        }

    }

    private ChannelProductDomain.ChannelProductState convertToDomain(
            ChannelProductDomain.ChannelProductState currentState,
            io.products.channelProduct.api.ChannelProductApi.CreateChannelProductCommand apiChannelProduct) {

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
                .setSku(apiChannelProduct.getSku())
                .setStoreId(apiChannelProduct.getStoreId())
                .setChannelId(apiChannelProduct.getChannelId())
                .clearChannelProductAttribute().addAllChannelProductAttribute(domChnlProdAttributeList)
                .clearChannelProductVariantGroup().addAllChannelProductVariantGroup(domChnlProdVariantGroupList)
                .clearChannelProductOptionGroup().addAllChannelProductOptionGroup(domChnlProdOptionGroupList)
                .setIsDeleted(apiChannelProduct.getIsDeleted())
                .setEventId(apiChannelProduct.getEventId())
                .setProductId(apiChannelProduct.getProductId());
        return stateBuilder.build();
    }

    private Effect<Empty> handleCreation(ChannelProductDomain.ChannelProductState state,
                                         io.products.channelProduct.api.ChannelProductApi.CreateChannelProductCommand command) {
        ChannelProductDomain.ChannelProductCreated event = ChannelProductDomain.ChannelProductCreated.newBuilder()
                .setChannelProduct(state).build();
        return effects().emitEvent(event).thenReply(__ -> Empty.getDefaultInstance());
        // return effects().updateState(state).thenReply(Empty.getDefaultInstance());
    }

    /***********************************************************
     * UPDATE CHANNEL PRODUCT & convertToDomain & handleUpdate
     ***********************************************************/

    @Override
    public Effect<Empty> updateChannelProduct(ChannelProductDomain.ChannelProductState currentState,
                                              ChannelProductApi.UpdateChannelProductCommand command) {
        ChannelProductDomain.ChannelProductState state = convertToDomain(currentState, command);

        return reject(currentState, command).orElseGet(() -> handleUpdate(state, command));
    }

    private Optional<Effect<Empty>> reject(ChannelProductState currentState,
                                           io.products.channelProduct.api.ChannelProductApi.UpdateChannelProductCommand command) {

        if (!currentState.getId().equals(command.getId())) {
            return Optional.of(effects().error("Channel Product fail to update, id is not found!!!",
                    Status.Code.NOT_FOUND));
        } else {
            return Optional.empty();
        }

    }

    private ChannelProductDomain.ChannelProductState convertToDomain(
            ChannelProductDomain.ChannelProductState currentState,
            io.products.channelProduct.api.ChannelProductApi.UpdateChannelProductCommand apiChannelProduct) {

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
                .setSku(apiChannelProduct.getSku())
                .setStoreId(apiChannelProduct.getStoreId())
                .setChannelId(apiChannelProduct.getChannelId())
                .clearChannelProductAttribute().addAllChannelProductAttribute(domChnlProdAttributeList)
                .clearChannelProductVariantGroup().addAllChannelProductVariantGroup(domChnlProdVariantGroupList)
                .clearChannelProductOptionGroup().addAllChannelProductOptionGroup(domChnlProdOptionGroupList)
                .setIsDeleted(apiChannelProduct.getIsDeleted())
                .setEventId(apiChannelProduct.getEventId())
                .setProductId(apiChannelProduct.getProductId());
        return stateBuilder.build();
    }

    private Effect<Empty> handleUpdate(ChannelProductDomain.ChannelProductState state,
                                       io.products.channelProduct.api.ChannelProductApi.UpdateChannelProductCommand command) {

        ChannelProductDomain.ChannelProductUpdated event = ChannelProductDomain.ChannelProductUpdated.newBuilder()
                .setChannelProduct(state).build();
        return effects().emitEvent(event).thenReply(__ -> Empty.getDefaultInstance());
        // return effects().updateState(state).thenReply(Empty.getDefaultInstance());
    }

    /***********************************************
     * DELETE CHANNEL PRODUCT & handleDeletion
     ***********************************************/
    @Override
    public Effect<Empty> deleteChannelProduct(ChannelProductDomain.ChannelProductState currentState,
                                              ChannelProductApi.DeleteChannelProductCommand command) {
        return handleDeletion(currentState, command);
    }

    private Effect<Empty> handleDeletion(ChannelProductDomain.ChannelProductState state,
                                         ChannelProductApi.DeleteChannelProductCommand command) {

        ChannelProductDomain.ChannelProductState deletedState = ChannelProductDomain.ChannelProductState.newBuilder()
                .setId(state.getId())
                .setSku(state.getSku())
                .setStoreId(state.getStoreId())
                .setChannelId(state.getChannelId())
                .clearChannelProductVariantGroup().addAllChannelProductVariantGroup(state.getChannelProductVariantGroupList())
                .clearChannelProductAttribute().addAllChannelProductAttribute(state.getChannelProductAttributeList())
                .clearChannelProductOptionGroup().addAllChannelProductOptionGroup(state.getChannelProductOptionGroupList())
                .setIsDeleted(true)
                .setEventId(state.getEventId())
                .setProductId(state.getProductId()).build();

        ChannelProductDomain.ChannelProductDeleted event = ChannelProductDomain.ChannelProductDeleted.newBuilder()
                .setChannelProduct(deletedState).build();

        return effects().emitEvent(event).thenReply(__ -> Empty.getDefaultInstance());
        // return effects().updateState(state).thenReply(Empty.getDefaultInstance());
    }

    /************************************************
     * GET CHANNEL PRODUCT & convertToApi
     ************************************************/
    @Override
    public Effect<ChannelProductApi.ChannelProduct> getChannelProduct(
            ChannelProductDomain.ChannelProductState currentState,
            ChannelProductApi.GetChannelProductRequest command) {
        if (currentState.getId().equals(command.getId())) {
            return effects().reply(convertToApi(currentState));
        } else {
            return effects().error("Channel Patient " + command.getId() + " has not been created.");
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

    /* ___________________________ */
    /* --------------------------- */
    // event done event done event
    /* ___________________________ */
    /* --------------------------- */

    @Override
    public ChannelProductDomain.ChannelProductState channelProductCreated(
            ChannelProductDomain.ChannelProductState currentState, ChannelProductDomain.ChannelProductCreated event) {

        ChannelProductDomain.ChannelProductState.Builder stateBuilder = currentState.toBuilder();

        stateBuilder
                .setId(event.getChannelProduct().getId())
                .setSku(event.getChannelProduct().getSku())
                .setStoreId(event.getChannelProduct().getStoreId())
                .setChannelId(event.getChannelProduct().getChannelId())
                .clearChannelProductAttribute()
                .addAllChannelProductAttribute(event.getChannelProduct().getChannelProductAttributeList())
                .clearChannelProductVariantGroup()
                .addAllChannelProductVariantGroup(event.getChannelProduct().getChannelProductVariantGroupList())
                .clearChannelProductOptionGroup()
                .addAllChannelProductOptionGroup(event.getChannelProduct().getChannelProductOptionGroupList())
                .setIsDeleted(event.getChannelProduct().getIsDeleted())
                .setEventId(event.getChannelProduct().getEventId())
                .setProductId(event.getChannelProduct().getProductId());
        return stateBuilder.build();
    }

    @Override
    public ChannelProductDomain.ChannelProductState channelProductUpdated(
            ChannelProductDomain.ChannelProductState currentState, ChannelProductDomain.ChannelProductUpdated event) {

        ChannelProductDomain.ChannelProductState.Builder stateBuilder = currentState.toBuilder();

        stateBuilder
                .setId(event.getChannelProduct().getId())
                .setSku(event.getChannelProduct().getSku())
                .setStoreId(event.getChannelProduct().getStoreId())
                .setChannelId(event.getChannelProduct().getChannelId())
                .clearChannelProductAttribute()
                .addAllChannelProductAttribute(event.getChannelProduct().getChannelProductAttributeList())
                .clearChannelProductVariantGroup()
                .addAllChannelProductVariantGroup(event.getChannelProduct().getChannelProductVariantGroupList())
                .clearChannelProductOptionGroup()
                .addAllChannelProductOptionGroup(event.getChannelProduct().getChannelProductOptionGroupList())
                .setIsDeleted(event.getChannelProduct().getIsDeleted())
                .setEventId(event.getChannelProduct().getEventId())
                .setProductId(event.getChannelProduct().getProductId());
        return stateBuilder.build();
    }

    @Override
    public ChannelProductDomain.ChannelProductState channelProductDeleted(
            ChannelProductDomain.ChannelProductState currentState, ChannelProductDomain.ChannelProductDeleted event) {

        ChannelProductDomain.ChannelProductState.Builder stateBuilder = currentState.toBuilder();
        stateBuilder
                .setId(event.getChannelProduct().getId())
                .setSku(event.getChannelProduct().getSku())
                .setStoreId(event.getChannelProduct().getStoreId())
                .setChannelId(event.getChannelProduct().getChannelId())
                .clearChannelProductAttribute()
                .addAllChannelProductAttribute(event.getChannelProduct().getChannelProductAttributeList())
                .clearChannelProductVariantGroup()
                .addAllChannelProductVariantGroup(event.getChannelProduct().getChannelProductVariantGroupList())
                .clearChannelProductOptionGroup()
                .addAllChannelProductOptionGroup(event.getChannelProduct().getChannelProductOptionGroupList())
                .setIsDeleted(event.getChannelProduct().getIsDeleted())
                .setEventId(event.getChannelProduct().getEventId())
                .setProductId(event.getChannelProduct().getProductId());

        return stateBuilder.build();
    }

}
