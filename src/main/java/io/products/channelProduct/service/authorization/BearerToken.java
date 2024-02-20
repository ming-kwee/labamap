package io.products.channelProduct.service.authorization;

import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.headers.RawHeader;
import io.products.channelProduct.action.ChannelProductActionApi.ChannelMetadata;

public class BearerToken {
  private static final Logger LOG = LoggerFactory.getLogger(BearerToken.class);

  public static HttpRequest setup_HttpRequest(String URL, String HttpMethod,  Map<String, Object> hashmapMetadata) {



    String integration_HttpRequest_CreateCpAccessTokenHeaderName = null;
    if (hashmapMetadata.containsKey("integration.http_request.create_cp_access_token_header_name")) {
      ChannelMetadata channelMetadata = (ChannelMetadata) hashmapMetadata
          .get("integration.http_request.create_cp_access_token_header_name");
      integration_HttpRequest_CreateCpAccessTokenHeaderName = (String) channelMetadata.getValue();
    }

    String accessToken = "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";
    String postEndpoint = URL;

    return HttpRequest.POST(postEndpoint)
        .addHeader(RawHeader.create(integration_HttpRequest_CreateCpAccessTokenHeaderName, accessToken))
        .addHeader(RawHeader.create("Content-Type", "application/json"));
  }


}
