package io.products.centralProduct.service;

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
import io.products.centralProduct.api.CentralProductApi.CentralProduct;
import io.products.centralProduct.api.CentralProductApi.CentralProductAttribute;
import io.products.centralProduct.api.CentralProductApi.CentralProductHttpResponse;
import io.products.centralProduct.api.CentralProductApi.CentralProductOption;
import io.products.centralProduct.api.CentralProductApi.CentralProductOptionGroup;
import io.products.centralProduct.api.CentralProductApi.CentralProductVariant;
import io.products.centralProduct.api.CentralProductApi.CentralProductVariantGroup;
import io.products.shared.utils;

public class CentralProductService {
  private static final Logger LOG = LoggerFactory.getLogger(CentralProductService.class);

  
  public static CentralProductHttpResponse createCentralProduct(CentralProduct centralProduct) throws StatusException {
    ActorSystem actorSystem = ActorSystem.create("MyActorSystem");
    Http http = Http.get(actorSystem);
    LOG.info("Starting the actorSystem service");
    String postEndpoint = "https://asia-southeast2-labamap-ab9a2.cloudfunctions.net/insertDataChannel";

    String accessToken = "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";
    String requestBody = transformAttributeToJson(centralProduct);
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
        CentralProductHttpResponse cpHttpResponse = CentralProductHttpResponse.newBuilder()
            .setStatus("FAIL")
            .setDescription("Gagal simpan")
            .build();
        return cpHttpResponse;
      } else {
        CentralProductHttpResponse cpHttpResponse = CentralProductHttpResponse.newBuilder()
            .setStatus("OK")
            .setDescription("Berhasil simpan")
            .build();
        return cpHttpResponse;
      }

    } catch (Exception e) {

      CentralProductHttpResponse cpHttpResponse = CentralProductHttpResponse.newBuilder()
          .setStatus("EXCEPTION")
          .setDescription(e.getLocalizedMessage())
          .build();
      LOG.info("StatusCodes Exception LOH");
      return cpHttpResponse;
    }
  }

  private static String transformAttributeToJson(CentralProduct centralProduct) {
    ObjectMapper objectMapper = new ObjectMapper();
    try {
      // Create a map to store the attributes dynamically
      Map<String, Object> parentMap = new HashMap<>();

      // Convert attributes to a map and add to the parent map
      for (CentralProductAttribute attribute : centralProduct.getCentralProductAttributeList()) {
        utils.addAttribute_toMap(parentMap, attribute.getCntrlAttrName(), attribute.getValue(),
            attribute.getCntrlAttrType());
      }

      List<Map<String, Object>> variantsGroupList = new ArrayList<>();
      for (CentralProductVariantGroup variantGroup : centralProduct.getCentralProductVariantGroupList()) {
        Map<String, Object> groupMap = new HashMap<>();
        for (CentralProductVariant variant : variantGroup.getCentralProductVariantList()) {
          groupMap = utils.addVariantOrOption_toGroupMap(groupMap, variant.getCntrlVrntName(), variant.getValue(),
              variant.getCntrlVrntType());
        }
        variantsGroupList.add(groupMap);
      }

      List<Map<String, Object>> optionsGroupList = new ArrayList<>();
      for (CentralProductOptionGroup optionGroup : centralProduct.getCentralProductOptionGroupList()) {
        Map<String, Object> groupMap = new HashMap<>();
        for (CentralProductOption option : optionGroup.getCentralProductOptionList()) {
          groupMap = utils.addVariantOrOption_toGroupMap(groupMap, option.getCntrlOptnName(), option.getValue(),
              option.getCntrlOptnType());
        }
        optionsGroupList.add(groupMap);
      }

      Map<String, Object> result = parentMap;// new HashMap<>();
      if (variantsGroupList.size() > 0) {
        int deepLevel = 0;
        for (CentralProductVariant variant : centralProduct.getCentralProductVariantGroupList().get(0)
            .getCentralProductVariantList()) {
          if (!variant.getCntrlVrntType().trim().substring(variant.getCntrlVrntType().trim().length() - 2).equals("[]")) {
            int lengthOfArray = variant.getCntrlVrntName().split("\\.").length;
            deepLevel = lengthOfArray > 0 ? lengthOfArray - 1 : 0;
            break;
          }
        }
        result = utils.finalMergeMaps(result, utils.mergeMaps(variantsGroupList, deepLevel));
      }
      if (optionsGroupList.size() > 0) {
        int deepLevel = 0;
        for (CentralProductOption option : centralProduct.getCentralProductOptionGroupList().get(0)
            .getCentralProductOptionList()) {
          if (!option.getCntrlOptnType().trim().substring(option.getCntrlOptnType().trim().length() - 2).equals("[]")) {
            int lengthOfArray = option.getCntrlOptnName().split("\\.").length;
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
