package io.products.channelProduct.service.authorization;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Base64;
import java.util.Map;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import akka.actor.ActorSystem;
import akka.http.javadsl.Http;
import akka.http.javadsl.model.HttpEntities;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.HttpResponse;
import akka.http.javadsl.model.headers.RawHeader;
import akka.protobufv3.internal.ByteString;
import akka.stream.Materializer;
import akka.stream.SystemMaterializer;
import akka.http.javadsl.model.StatusCodes;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.apache.commons.codec.digest.HmacUtils;
import org.openqa.selenium.By;

public class Oauth2_SHA256 {
  private static final Logger LOG = LoggerFactory.getLogger(Oauth2_SHA256.class);
  private static final String CLIENT_ID = "128209";
  private static final String CLIENT_SECRET = "pi6auSLgoHUncSr1NK9zLUTDFRI6YaKp";
  private static final String REDIRECT_URI = "https://asia-southeast2-labamap-ab9a2.cloudfunctions.net/insertDataChannel";

  public static HttpRequest setup_HttpRequest(String URL, String HttpMethod, Map<String, Object> hashmapMetadata)
      throws UnsupportedEncodingException {

    ActorSystem actorSystem = ActorSystem.create("MyActorSystem");
    Http http = Http.get(actorSystem);
    /* ------------------------------------------------------------------------ */
    /* -------------------- Step 1: Obtain Authorization Code ---------------- */
    /* ------------------------------------------------------------------------ */

    System.setProperty("webdriver.chrome.driver", "/usr/local/bin/chromedriver");

    // Set up ChromeOptions to disable notifications and enable headless mode
    ChromeOptions options = new ChromeOptions();
    options.addArguments("--disable-notifications");
    // options.addArguments("--headless"); // Run Chrome in headless mode (no GUI)

    // Initialize ChromeDriver with ChromeOptions
    WebDriver driver = new ChromeDriver(options);
    driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

    // Navigate to Lazada authorization URL
    String authorizationUrl = "https://auth.lazada.com/oauth/authorize?response_type=code"
        + "&client_id=" + CLIENT_ID
        + "&redirect_uri=" + REDIRECT_URI;
    driver.get(authorizationUrl);

    /* --------- BEGIN Fill in login credentials and submit the form ------- */
    WebDriverWait waitDropdown = new WebDriverWait(driver, 40);
    WebElement dropdown = waitDropdown
        .until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".next-select")));
    dropdown.click(); // Click to open the dropdown

    // waitOption.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//li[@value='id'
    // and text()='Indonesia']")));
    WebDriverWait waitOption = new WebDriverWait(driver, 40);
    WebElement optionId = waitOption
        .until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//li[@value='id' and text()='Indonesia']")));
    optionId.click();
    driver.findElement(By.id("fm-login-id")).sendKeys("syene_2002@yahoo.com");
    driver.findElement(By.id("fm-login-password")).sendKeys("Potpot99");
    driver.findElement(By.id("fm-login-submit")).click();
    /* --------- END Fill in login credentials and submit the form ------- */

    LOG.info("step#1");
    WebDriverWait wait = new WebDriverWait(driver, 20); // Wait for a maximum of 30 seconds
    // Wait until the URL contains the expected parameter (assuming the
    // authorization code is included as a parameter in the URL)
    wait.until(ExpectedConditions.urlContains("code="));
    // Extract the authorization code from the URL
    String redirectUrl = driver.getCurrentUrl();
    LOG.info("Authorization Code: " + redirectUrl);
    // Extract the authorization code from the redirect URL
    String authorizationCode = extractAuthorizationCode(redirectUrl);
    LOG.info("Authorization Code: " + authorizationCode);

    // Close the browser
    driver.quit();

    /* ------------------------------------------------------------------------ */
    /* --------- Step 2: Exchange Authorization Code for Access Token --------- */
    /* ------------------------------------------------------------------------ */
    LOG.info("step#2");
    Materializer materializer = SystemMaterializer.get(actorSystem).materializer();
    CompletionStage<String> accessTokenStage = exchangeAuthorizationCodeForAccessToken(authorizationCode, http,
        materializer);

    accessTokenStage.thenApply(accessToken -> {
      // Step 3: Use Access Token to Make Authenticated Requests
      // String resourceUrl = "https://api.lazada.com/rest";
      // getResourceWithAccessToken(resourceUrl, accessToken, http, materializer)
      // .thenAccept(response -> {
      // System.out.println("Response from Lazada API: " + response);
      // system.terminate();
      // });

      /* ------------------------------------------------------------------------ */
      /* -------- Step 3: Use Access Token to Make Authenticated Requests ------- */
      /* ------------------------------------------------------------------------ */
      LOG.info("step#3" + accessToken);
      System.out.println("accessToken Code: " + accessToken);
      System.out.println("accessToken Code: " + URL);
      return HttpRequest.POST(URL)
          .addHeader(RawHeader.create("Authorization", "Bearer " + accessToken))
          .addHeader(RawHeader.create("Content-Type", "application/json"));
    });
    return HttpRequest.POST("");
  }

  private static String buildAuthorizationUrl(String clientId, String redirectUri) throws UnsupportedEncodingException {
    String authEndpoint = "https://auth.lazada.com/oauth/authorize";
    String responseType = "code";
    String scope = "SCOPE"; // Specify the required scope(s) for your application
    String state = "STATE"; // Optional parameter for CSRF protection or session tracking

    StringBuilder urlBuilder = new StringBuilder(authEndpoint);
    urlBuilder.append("?client_id=").append(URLEncoder.encode(clientId, "UTF-8"));
    urlBuilder.append("&redirect_uri=").append(URLEncoder.encode(redirectUri, "UTF-8"));
    urlBuilder.append("&response_type=").append(responseType);
    urlBuilder.append("&scope=").append(URLEncoder.encode(scope, "UTF-8"));
    urlBuilder.append("&state=").append(URLEncoder.encode(state, "UTF-8"));

    return urlBuilder.toString();
  }

  // private static String extractAuthorizationCode(String redirectUrl) {
  // // Implement logic to extract the authorization code from the redirect URL
  // // Parse the URL and extract the query parameters
  // // Extract the value of the 'code' parameter
  // return "YOUR_AUTHORIZATION_CODE";
  // }

  private static String extractAuthorizationCode(String redirectUrl) {
    // Split the redirectUrl to extract query parameters
    String[] urlParts = redirectUrl.split("\\?");
    if (urlParts.length < 2) {
      throw new IllegalArgumentException("Invalid redirect URL: " + redirectUrl);
    }

    // Extract query parameters from the URL
    String queryString = urlParts[1];
    String[] queryParams = queryString.split("&");

    // Search for the 'code' parameter
    for (String param : queryParams) {
      String[] keyValue = param.split("=");
      if (keyValue.length == 2 && "code".equals(keyValue[0])) {
        // Return the value of the 'code' parameter
        return keyValue[1];
      }
    }

    // If 'code' parameter not found, throw an exception
    throw new IllegalArgumentException("Authorization code not found in redirect URL: " + redirectUrl);
  }

  private static CompletionStage<String> exchangeAuthorizationCodeForAccessToken(String authorizationCode, Http http,
      Materializer materializer) throws UnsupportedEncodingException {

    // Lazada API endpoint
    String lazadaUrl = "https://api.lazada.com/rest";
    // Lazada client credentials
    String appKey = CLIENT_ID;
    String appSecret = CLIENT_SECRET;

    // Lazada API request parameters
    String code = authorizationCode;

    // Construct the request URI
    String requestUriPlain = "/auth/token/create";

    // Get the current timestamp in milliseconds since the epoch
    long timestamp = Instant.now().toEpochMilli();

    // Encode the timestamp if needed
    String encodedTimestamp = URLEncoder.encode(String.valueOf(timestamp), "UTF-8");


    String requestUri = requestUriPlain + "?app_key=" + appKey
    + "&code=" + code
    + "&sign_method=sha256"        
    + "&timestamp=" + encodedTimestamp;

    String requestUriModif  = requestUri.replace("?", "")
    .replace("=", "")
    .replace("&", "");


    // Construct the request URL with parameters
    String requestUrl = "";

    String signature = ""; 
    try {
      signature = generateOAuth2Signature(requestUriModif,appSecret );
      requestUrl = lazadaUrl + requestUri
      + "&sign=" + signature;
    } catch (InvalidKeyException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (NoSuchAlgorithmException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }


        


    // Create the HTTP request
    // HttpRequest httpRequest = HttpRequest.GET(requestUrl)
    // .addHeader(RawHeader.create("app_key", appKey))
    // .addHeader(RawHeader.create("app_secret", appSecret));


    LOG.info("step requestUrl " + requestUrl) ;

    // Create the HTTP request with headers
    HttpRequest httpRequest = HttpRequest.GET(requestUrl);
        // .addHeader(RawHeader.create("app_key", appKey))
        // .addHeader(RawHeader.create("sign_method", "sha256"))
        // .addHeader(RawHeader.create("timestamp", encodedTimestamp))
        // .addHeader(RawHeader.create("sign", signature));

    // Perform the HTTP request
    // CompletionStage<HttpResponse> responseFuture =
    // http.singleRequest(httpRequest);

    // Create the HTTP request
    // HttpRequest httpRequest = HttpRequest.GET(requestUrl).withHeaders(null)
    // .addHeader(RawHeader.create("app_key", appKey))
    // .addHeader(RawHeader.create("app_secret", appSecret));
    // Create the HTTP request
    // HttpRequest httpRequest = HttpRequest.GET(requestUrl)
    // .withHeaders(
    // headers.RawHeader.create("app_key", appKey),
    // headers.RawHeader.create("app_secret", appSecret)
    // );

    LOG.info("step#2a " + httpRequest);

    return http.singleRequest(httpRequest)
        .thenCompose(response -> {
          LOG.info("step#2b " + response);
          if (response.status().equals(StatusCodes.OK)) {
            return response.entity().toStrict(10000, materializer)
                .thenApply(entity -> entity.getData().utf8String());
          } else {
            throw new RuntimeException("Failed to exchange authorization code for access token: " + response.status());
          }
        });
    // Send the HTTP request and handle the response
    // Http http = Http.get(system);
    // CompletionStage<HttpResponse> responseFuture =
    // http.singleRequest(httpRequest);

    // responseFuture.thenAccept(response -> {
    // // Handle the response
    // if (response.status().equals(StatusCodes.OK)) {
    // CompletionStage<ByteString> bodyBytes =
    // Unmarshal(response.entity()).toByteString(materializer);
    // bodyBytes.thenAccept(bytes -> {
    // String responseBody = bytes.decodeString("UTF-8");
    // System.out.println("Response body: " + responseBody);
    // });
    // } else {
    // System.out.println("Request failed with status code: " + response.status());
    // }
    // });

    // String tokenEndpoint = "https://auth.lazada.com/oauth/token";
    // String requestBody = "code=" + URLEncoder.encode(authorizationCode, "UTF-8")
    // +
    // "&grant_type=authorization_code" +
    // "&redirect_uri=" + URLEncoder.encode(REDIRECT_URI, "UTF-8") +
    // "&client_id=" + CLIENT_ID +
    // "&client_secret=" + CLIENT_SECRET;

    // LOG.info("step#2a " + tokenEndpoint);
    // HttpRequest request = HttpRequest.POST(tokenEndpoint)
    // .withEntity(ContentTypes.create(MediaTypes.APPLICATION_X_WWW_FORM_URLENCODED),
    // requestBody);

    // return http.singleRequest(request)
    // .thenCompose(response -> {
    // LOG.info("step#2b " + response);
    // if (response.status().equals(StatusCodes.OK)) {
    // return response.entity().toStrict(10000, materializer)
    // .thenApply(entity -> entity.getData().utf8String());
    // } else {
    // throw new RuntimeException("Failed to exchange authorization code for access
    // token: " + response.status());
    // }
    // });
  }

  // private static CompletionStage<String> getResourceWithAccessToken(String
  // resourceUrl, String accessToken, Http http, Materializer materializer) {
  // HttpRequest request = HttpRequest.GET(resourceUrl)
  // .addHeader(RawHeader.create("Authorization", "Bearer " + accessToken));

  // return http.singleRequest(request)
  // .thenCompose(response -> {
  // if (response.status().equals(StatusCodes.OK)) {
  // return response.entity().toStrict(10000, materializer)
  // .thenApply(entity -> entity.getData().utf8String());
  // } else {
  // throw new RuntimeException("Failed to get resource from Lazada API: " +
  // response.status());
  // }
  // });
  // }




    // Method to generate OAuth 2.0 HmacSha256 signature
    private static String generateOAuth2Signature(String uri,  String secretKey) throws NoSuchAlgorithmException, InvalidKeyException {
            // Create HMAC-SHA256 hash generator
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), "HmacSHA256");
            mac.init(secretKeySpec);

            // Generate the HMAC-SHA256 hash
            byte[] hmacSha256 = mac.doFinal(uri.getBytes());


                        // Convert byte array to a string representation (Base64)
                        String base64Hash = Base64.getEncoder().encodeToString(hmacSha256);
                        System.out.println("Base64 encoded HMAC-SHA256 hash: " + base64Hash);
            
                        // Convert the Base64 encoded hash to hexadecimal
                        String hexHash = bytesToHex(Base64.getDecoder().decode(base64Hash));
                        System.out.println("Hexadecimal HMAC-SHA256 hash: " + hexHash);
            return hexHash;

    }


        // Method to convert byte array to hexadecimal string
        private static String bytesToHex(byte[] bytes) {
          StringBuilder hexString = new StringBuilder();
          for (byte b : bytes) {
              String hex = Integer.toHexString(0xff & b);
              if (hex.length() == 1) {
                  hexString.append('0');
              }
              hexString.append(hex);
          }
          return hexString.toString();
      }
}
