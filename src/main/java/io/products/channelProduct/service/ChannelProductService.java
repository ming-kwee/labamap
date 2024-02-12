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
      String integration_Security_CreateCpTypeOfAuthorization = null;
      if (hashmapMetadata.containsKey("integration.security.create_cp_type_of_authorization")) {
        ChannelMetadata channelMetadata = (ChannelMetadata) hashmapMetadata
            .get("integration.security.create_cp_type_of_authorization");
        integration_Security_CreateCpTypeOfAuthorization = (String) channelMetadata.getValue();
      } else {
        ChannelProductHttpResponse cpHttpResponse = ChannelProductHttpResponse.newBuilder()
            .setStatus("FAIL")
            .setDescription("type of authorization has not been set")
            .build();
        throw new RuntimeException(cpHttpResponse.getDescription());
      }

      HttpResponse response = null;
      if (integration_Security_CreateCpTypeOfAuthorization.equals("bearer")) {
        LOG.info("CREATE A CHANNEL PRODUCT - BEARER");
        response = Create_CP_WithBearerToken
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

  public static ChannelProductHttpResponse createSomeChannelProductsService(List<ChannelProduct.Builder> channelProductBuilders,
      Map<String, Object> hashmapMetadata)
      throws StatusException {
    ActorSystem actorSystem = ActorSystem.create("MyActorSystem");
    Http http = Http.get(actorSystem);

    try {
      String integration_Security_CreateCpTypeOfAuthorization = null;
      if (hashmapMetadata.containsKey("integration.security.create_cp_type_of_authorization")) {
        ChannelMetadata channelMetadata = (ChannelMetadata) hashmapMetadata
            .get("integration.security.create_cp_type_of_authorization");
        integration_Security_CreateCpTypeOfAuthorization = (String) channelMetadata.getValue();
      } else {
        ChannelProductHttpResponse cpHttpResponse = ChannelProductHttpResponse.newBuilder()
            .setStatus("FAIL")
            .setDescription("type of authorization has not been set")
            .build();
        throw new RuntimeException(cpHttpResponse.getDescription());
      }


      HttpResponse response = null;
      if (integration_Security_CreateCpTypeOfAuthorization.equals("bearer")) {
        LOG.info("CREATE SOME CHANNEL PRODUCTS - BEARER");
        response = Create_CP_WithBearerToken
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
