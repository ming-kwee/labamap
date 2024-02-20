package io.products.channelProduct.service;

import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import akka.actor.ActorSystem;
import akka.http.javadsl.Http;
import akka.http.javadsl.model.HttpResponse;
import io.grpc.StatusException;
import io.products.channelProduct.action.ChannelProductActionApi.ChannelMetadata;
import io.products.channelProduct.api.ChannelProductApi.ChannelProduct;
import io.products.channelProduct.api.ChannelProductApi.ChannelProductHttpResponse;

public class ChannelProductService {
  private static final Logger LOG = LoggerFactory.getLogger(ChannelProductService.class);

  public static ChannelProductHttpResponse createAChannelProductService(ChannelProduct channelProduct,
      Map<String, Object> hashmapMetadata)
      throws StatusException {
    ActorSystem actorSystem = ActorSystem.create("MyActorSystem");
    Http http = Http.get(actorSystem);

    try {

      /* -------- setting up single or multi execution metadata ------- */
      String integration_BodyContent_CreateCpSeparateVariantCrud = null;
      if (hashmapMetadata.containsKey("integration.body_content.create_cp_separate_variant_crud")) {
        ChannelMetadata channelMetadata = (ChannelMetadata) hashmapMetadata
            .get("integration.body_content.create_cp_separate_variant_crud");
        integration_BodyContent_CreateCpSeparateVariantCrud = (String) channelMetadata.getValue();
      } else {
        integration_BodyContent_CreateCpSeparateVariantCrud = "false";
      }

      /* --- the entry to the service world based on type of authorization --- */
      HttpResponse response = null;
      if (integration_BodyContent_CreateCpSeparateVariantCrud.equals("true")) {
        response = Create_CP_MultiExecution
            .createAChannelProduct_WithSplitVariants(channelProduct, hashmapMetadata, http, actorSystem)
            .toCompletableFuture().get();       
      } else {
        response = Create_CP_SingleExecution
            .createAChannelProduct(channelProduct, hashmapMetadata, http)
            .toCompletableFuture().get();
      
      }

      if (response != null && response.status().intValue() >= 400) {
        ChannelProductHttpResponse cpHttpResponse = ChannelProductHttpResponse.newBuilder()
            .setStatus("FAIL")
            .setDescription("Gagal simpan")
            .build();
        return cpHttpResponse;
      } else {
        ChannelProductHttpResponse cpHttpResponse = ChannelProductHttpResponse.newBuilder()
            .setStatus("OK")
            .setDescription("Berhasil simpan")
            .build();
        return cpHttpResponse;
      }

    } catch (Exception e) {

      ChannelProductHttpResponse cpHttpResponse = ChannelProductHttpResponse.newBuilder()
          .setStatus("EXCEPTION")
          .setDescription(e.getLocalizedMessage())
          .build();
      return cpHttpResponse;
    }
  }

  public static ChannelProductHttpResponse createSomeChannelProductsService(
      List<ChannelProduct.Builder> channelProductBuilders,
      Map<String, Object> hashmapMetadata)
      throws StatusException {
    ActorSystem actorSystem = ActorSystem.create("MyActorSystem");
    Http http = Http.get(actorSystem);

    try {
      // String integration_Security_CreateCpTypeOfAuthorization = null;
      // if (hashmapMetadata.containsKey("integration.security.create_cp_type_of_authorization")) {
      //   ChannelMetadata channelMetadata = (ChannelMetadata) hashmapMetadata
      //       .get("integration.security.create_cp_type_of_authorization");
      //   integration_Security_CreateCpTypeOfAuthorization = (String) channelMetadata.getValue();
      // } else {
      //   ChannelProductHttpResponse cpHttpResponse = ChannelProductHttpResponse.newBuilder()
      //       .setStatus("FAIL")
      //       .setDescription("type of authorization has not been set")
      //       .build();
      //   throw new RuntimeException(cpHttpResponse.getDescription());
      // }

      // HttpResponse response = null;

      // switch (integration_Security_CreateCpTypeOfAuthorization) {
      //   case "bearer":
      //     LOG.info("CREATE SOME CHANNEL PRODUCTS - BEARER");
      //     // response = BearerToken
      //     //     .createSomeChannelProducts(channelProductBuilders, hashmapMetadata, http)
      //     //     .toCompletableFuture().get();
      //     break;
      //   case "oauth1":
      //     LOG.info("CREATE SOME CHANNEL PRODUCTS - OAUTH1");
      //     // response = Create_CP_WithOauth1_HMACSHA1
      //     //     .createSomeChannelProducts(channelProductBuilders, hashmapMetadata, http)
      //     //     .toCompletableFuture().get();
      //     break;
      //   default:
      //     break;
      // }
      
      /* -------- setting up single or multi execution metadata ------- */
      String integration_BodyContent_CreateCpSeparateVariantCrud = null;
      if (hashmapMetadata.containsKey("integration.body_content.create_cp_separate_variant_crud")) {
        ChannelMetadata channelMetadata = (ChannelMetadata) hashmapMetadata
            .get("integration.body_content.create_cp_separate_variant_crud");
        integration_BodyContent_CreateCpSeparateVariantCrud = (String) channelMetadata.getValue();
      } else {
        integration_BodyContent_CreateCpSeparateVariantCrud = "false";
      }

      /* --- the entry to the service world based on type of authorization --- */
      HttpResponse response = null;
      if (integration_BodyContent_CreateCpSeparateVariantCrud.equals("true")) {
        response = Create_CP_MultiExecution
            .createSomeChannelProducts_WithSplitVariants(channelProductBuilders, hashmapMetadata, http)
            .toCompletableFuture().get();       
      } else {
        response = Create_CP_SingleExecution
            .createSomeChannelProducts(channelProductBuilders, hashmapMetadata, http)
            .toCompletableFuture().get();
      }

      if (response != null && response.status().intValue() >= 400) {
        ChannelProductHttpResponse cpHttpResponse = ChannelProductHttpResponse.newBuilder()
            .setStatus("FAIL")
            .setDescription("Gagal simpan")
            .build();
        return cpHttpResponse;
      } else {
        ChannelProductHttpResponse cpHttpResponse = ChannelProductHttpResponse.newBuilder()
            .setStatus("OK")
            .setDescription("Berhasil simpan")
            .build();
        return cpHttpResponse;
      }

    } catch (Exception e) {

      ChannelProductHttpResponse cpHttpResponse = ChannelProductHttpResponse.newBuilder()
          .setStatus("EXCEPTION")
          .setDescription(e.getLocalizedMessage())
          .build();
      return cpHttpResponse;
    }
  }

}
