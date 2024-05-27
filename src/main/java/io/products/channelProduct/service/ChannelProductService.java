package io.products.channelProduct.service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import akka.actor.ActorSystem;
import akka.http.javadsl.Http;
import akka.http.javadsl.model.HttpResponse;
import akka.http.javadsl.unmarshalling.Unmarshaller;
import akka.stream.Materializer;
import io.grpc.StatusException;
import io.products.channelProduct.action.ChannelProductActionApi.ChannelMetadata_;
import io.products.channelProduct.api.ChannelProductApi.ChannelProductHttpResponse;
import io.products.channelProduct.api.ChannelProductApi.CreateChannelProductCommand;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Struct;
import com.google.protobuf.util.JsonFormat;

public class ChannelProductService {
  private static final Logger LOG = LoggerFactory.getLogger(ChannelProductService.class);

  @SuppressWarnings("null")
  public static ChannelProductHttpResponse createAChannelProductService(CreateChannelProductCommand channelProduct,
      Map<String, Object> hashmapMetadata)
      throws StatusException {
    ActorSystem actorSystem = ActorSystem.create("MyActorSystem");
    Http http = Http.get(actorSystem);

    try {

      /* -------- setting up single or multi execution metadata ------- */
      String integration_BodyContent_CreateCpSeparateVariantCrud = null;
      if (hashmapMetadata.containsKey("integration.body_content.create_cp_separate_variant_crud")) {
        ChannelMetadata_ channelMetadata = (ChannelMetadata_) hashmapMetadata
            .get("integration.body_content.create_cp_separate_variant_crud");
        integration_BodyContent_CreateCpSeparateVariantCrud = (String) channelMetadata.getValue();
      } else {
        integration_BodyContent_CreateCpSeparateVariantCrud = "false";
      }

      /* --- the entry to the service world based on type of authorization --- */
      HttpResponse response = null;
      CompletionStage<ChannelProductHttpResponse> responseFuture = null;
      Materializer materializer = Materializer.createMaterializer(actorSystem);
      if (integration_BodyContent_CreateCpSeparateVariantCrud.equals("true")) {
        response = Create_CP_MultiExecution
            .createAChannelProduct_WithSplitVariants(channelProduct, hashmapMetadata, http, actorSystem)
            .toCompletableFuture().get();
      } else {
        responseFuture = Create_CP_SingleExecution
            .createAChannelProduct(channelProduct, hashmapMetadata, http)
            .thenCompose(response2 -> {
              if (response2.status().isSuccess()) {
                // Extract the product ID from the response body
                return Unmarshaller.entityToString().unmarshal(response2.entity(), materializer)
                    .thenApply(responseBody -> {


                      
                              // Create a new Struct builder
        Struct.Builder structBuilder = Struct.newBuilder();
        Struct struct = null;
        try {
            // Parse the JSON data and populate the Struct builder
            JsonFormat.parser().merge(responseBody, structBuilder);

            // Build the Struct
            struct = structBuilder.build();

            // Print the Struct
            System.out.println(struct);
        } catch (Exception e) {
            e.printStackTrace();
        }

                      LOG.info("Theu" + responseBody);
                      LOG.info("Theuna " + struct);

                      return ChannelProductHttpResponse.newBuilder()
                          .setStatus("OK")
                          .setDescription("Berhasil simpan")
                          .putAllData(struct.getFieldsMap())
                          .build();
                    });
              } else {
                // Handle error response
                return CompletableFuture.completedFuture(ChannelProductHttpResponse.newBuilder()
                    .setStatus("ERROR")
                    .setDescription("Gagal simpan")
                    .build());
              }
            });
      }

      if (response != null && response.status().intValue() >= 300) {
        ChannelProductHttpResponse cpHttpResponse = ChannelProductHttpResponse.newBuilder()
            .setStatus("FAIL")
            .setDescription("Gagal simpan, (" +
                response.status().intValue() + "):" +
                response.status().defaultMessage())
            .build();

        return cpHttpResponse;
      } else {
        return responseFuture.toCompletableFuture().get();
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
      List<CreateChannelProductCommand.Builder> channelProductBuilders,
      Map<String, Object> hashmapMetadata)
      throws StatusException {
    ActorSystem actorSystem = ActorSystem.create("MyActorSystem");
    Http http = Http.get(actorSystem);

    try {
      /* -------- setting up single or multi execution metadata ------- */
      String integration_BodyContent_CreateCpSeparateVariantCrud = null;
      if (hashmapMetadata.containsKey("integration.body_content.create_cp_separate_variant_crud")) {
        ChannelMetadata_ channelMetadata = (ChannelMetadata_) hashmapMetadata
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

      if (response != null && response.status().intValue() >= 300) {
        ChannelProductHttpResponse cpHttpResponse = ChannelProductHttpResponse.newBuilder()
            .setStatus("FAIL")
            .setDescription("Gagal simpan, (" +
                response.status().intValue() + "):" +
                response.status().defaultMessage())
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
