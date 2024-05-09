package io.products.masterProduct.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletionStage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import akka.actor.ActorSystem;
import akka.http.javadsl.Http;
import akka.http.javadsl.model.ContentTypes;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.HttpResponse;
import akka.http.javadsl.model.StatusCodes;
import akka.http.javadsl.model.headers.RawHeader;
import io.grpc.StatusException;
import io.products.masterProduct.api.MasterProductApi.MasterProduct;
import io.products.masterProduct.api.MasterProductApi.MasterProductAttribute;
import io.products.masterProduct.api.MasterProductApi.MasterProductHttpResponse;
import io.products.masterProduct.api.MasterProductApi.MasterProductOption;
import io.products.masterProduct.api.MasterProductApi.MasterProductOptionGroup;
import io.products.masterProduct.api.MasterProductApi.MasterProductVariant;
import io.products.masterProduct.api.MasterProductApi.MasterProductVariantGroup;
import io.products.shared.utils;

public class MasterProductService {
  private static final Logger LOG = LoggerFactory.getLogger(MasterProductService.class);

  
  public static MasterProductHttpResponse createMasterProduct(MasterProduct masterProduct) throws StatusException {
    ActorSystem actorSystem = ActorSystem.create("MyActorSystem");
    Http http = Http.get(actorSystem);
    LOG.info("Starting the actorSystem service");
    String postEndpoint = "https://asia-southeast2-labamap-ab9a2.cloudfunctions.net/insertDataChannel";

    String accessToken = "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";
    String requestBody = transformAttributeToJson(masterProduct);
    LOG.info("transformAttributeToJson " + requestBody);

    HttpRequest request = HttpRequest.POST(postEndpoint)
        .addHeader(RawHeader.create("X-Shopify-Access-Token", accessToken))
        .addHeader(RawHeader.create("Content-Type", "application/json"))
        .withEntity(ContentTypes.APPLICATION_JSON, requestBody);

    CompletionStage<HttpResponse> responseStage = http.singleRequest(request)
        .thenApply(response -> {
          return response;
        })
        .exceptionally(ex -> {
          return HttpResponse.create().withStatus(StatusCodes.INTERNAL_SERVER_ERROR);
        });

    try {

      HttpResponse response = responseStage.toCompletableFuture().get();

      LOG.info("LOH " + response.status().intValue());

      if (response.status().intValue() >= 400) {
        MasterProductHttpResponse cpHttpResponse = MasterProductHttpResponse.newBuilder()
            .setStatus("FAIL")
            .setDescription("Gagal simpan")
            .build();
        return cpHttpResponse;
      } else {
        MasterProductHttpResponse cpHttpResponse = MasterProductHttpResponse.newBuilder()
            .setStatus("OK")
            .setDescription("Berhasil simpan")
            .build();
        return cpHttpResponse;
      }

    } catch (Exception e) {

      MasterProductHttpResponse cpHttpResponse = MasterProductHttpResponse.newBuilder()
          .setStatus("EXCEPTION")
          .setDescription(e.getLocalizedMessage())
          .build();
      LOG.info("StatusCodes Exception LOH");
      return cpHttpResponse;
    }
  }

  private static String transformAttributeToJson(MasterProduct masterProduct) {
    ObjectMapper objectMapper = new ObjectMapper();
    try {
      // Create a map to store the attributes dynamically
      Map<String, Object> parentMap = new HashMap<>();

      // Convert attributes to a map and add to the parent map
      for (MasterProductAttribute attribute : masterProduct.getMasterProductAttributeList()) {
        utils.addAttribute_toMap(parentMap, attribute.getMstrAttrName(), attribute.getValue(),
            attribute.getMstrAttrType());
      }

      List<Map<String, Object>> variantsGroupList = new ArrayList<>();
      for (MasterProductVariantGroup variantGroup : masterProduct.getMasterProductVariantGroupList()) {
        Map<String, Object> groupMap = new HashMap<>();
        for (MasterProductVariant variant : variantGroup.getMasterProductVariantList()) {
          groupMap = utils.addVariantOrOption_toGroupMap(groupMap, variant.getMstrVrntName(), variant.getValue(),
              variant.getMstrVrntType());
        }
        variantsGroupList.add(groupMap);
      }

      List<Map<String, Object>> optionsGroupList = new ArrayList<>();
      for (MasterProductOptionGroup optionGroup : masterProduct.getMasterProductOptionGroupList()) {
        Map<String, Object> groupMap = new HashMap<>();
        for (MasterProductOption option : optionGroup.getMasterProductOptionList()) {
          groupMap = utils.addVariantOrOption_toGroupMap(groupMap, option.getMstrOptnName(), option.getValue(),
              option.getMstrOptnType());
        }
        optionsGroupList.add(groupMap);
      }

      Map<String, Object> result = parentMap;// new HashMap<>();
      if (variantsGroupList.size() > 0) {
        int deepLevel = 0;
        for (MasterProductVariant variant : masterProduct.getMasterProductVariantGroupList().get(0)
            .getMasterProductVariantList()) {
          if (!variant.getMstrVrntType().trim().substring(variant.getMstrVrntType().trim().length() - 2).equals("[]")) {
            int lengthOfArray = variant.getMstrVrntName().split("\\.").length;
            deepLevel = lengthOfArray > 0 ? lengthOfArray - 1 : 0;
            break;
          }
        }
        result = utils.finalMergeMaps(result, utils.mergeMaps(variantsGroupList, deepLevel));
      }
      if (optionsGroupList.size() > 0) {
        int deepLevel = 0;
        for (MasterProductOption option : masterProduct.getMasterProductOptionGroupList().get(0)
            .getMasterProductOptionList()) {
          if (!option.getMstrOptnType().trim().substring(option.getMstrOptnType().trim().length() - 2).equals("[]")) {
            int lengthOfArray = option.getMstrOptnName().split("\\.").length;
            deepLevel = lengthOfArray > 0 ? lengthOfArray - 1 : 0;
            break;
          }
        }
        result = utils.finalMergeMaps(result, utils.mergeMaps(optionsGroupList, deepLevel));
      }

      LOG.info("result " + result);
      String json = objectMapper.writeValueAsString(result);

      return json;
    } catch (JsonProcessingException e) {
      e.printStackTrace(); // Handle the exception as needed
      return e.getMessage();

    }
  }

}
