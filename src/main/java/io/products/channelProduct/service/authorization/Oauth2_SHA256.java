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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import akka.actor.ActorSystem;
import akka.http.javadsl.Http;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.headers.RawHeader;
import akka.stream.Materializer;
import akka.stream.SystemMaterializer;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.By;

public class Oauth2_SHA256 {
  private static final Logger LOG = LoggerFactory.getLogger(Oauth2_SHA256.class);
  private static final String CLIENT_ID = "128209";
  private static final String CLIENT_SECRET = "pi6auSLgoHUncSr1NK9zLUTDFRI6YaKp";
  private static final String REDIRECT_URI = "https://asia-southeast2-labamap-ab9a2.cloudfunctions.net/insertDataChannel";
  private static final String LAZADA_URL = "https://api.lazada.com/rest";

  public static HttpRequest setup_HttpRequest(String URL, String HttpMethod, Map<String, Object> hashmapMetadata)
      throws UnsupportedEncodingException, JsonMappingException, JsonProcessingException,
      InvalidKeyException, NoSuchAlgorithmException, InterruptedException {

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
    // Materializer materializer =
    // SystemMaterializer.get(actorSystem).materializer();
    String accessToken = exchangeAuthorizationCodeForAccessToken(authorizationCode);
    String signature = initiateGenerateSignature("", CLIENT_ID, CLIENT_SECRET, authorizationCode);

    // accessTokenStage.thenApply(accessToken -> {
    /* ------------------------------------------------------------------------ */
    /* -------- Step 3: Use Access Token to Make Authenticated Requests ------- */
    /* ------------------------------------------------------------------------ */

    /* ------------------------------------------------------------------------ */
    /* -------- Step 4: Use Access Token to Make Authenticated Requests ------- */
    /* ------------------------------------------------------------------------ */
    LOG.info("step#3" + authorizationCode);
    LOG.info("step#3a" + accessToken);
    LOG.info("step#3b" + signature);

    // createProduct(LAZADA_URL, CLIENT_ID, CLIENT_SECRET, accessToken);
    getProducts(LAZADA_URL, CLIENT_ID, CLIENT_SECRET, accessToken);


    return HttpRequest.POST(URL)
        .addHeader(RawHeader.create("Authorization", "Bearer " + accessToken))
        .addHeader(RawHeader.create("Content-Type", "application/json"));
    // });
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

  private static String exchangeAuthorizationCodeForAccessToken(String authorizationCode)
      throws UnsupportedEncodingException, JsonMappingException, JsonProcessingException {

//    LazopClient client = new LazopClient("https://api.lazada.com/rest", "128209", "pi6auSLgoHUncSr1NK9zLUTDFRI6YaKp"); // "${appkey}",
//                                                                                                                       // "${appSecret}");
//    LazopRequest request = new LazopRequest("/auth/token/create");
//    request.addApiParameter("code", authorizationCode);
//    LazopResponse response = client.execute(request);
//    ObjectMapper objectMapper = new ObjectMapper();
//    Map<String, Object> jsonMap = objectMapper.readValue(response.getBody(), Map.class);
//
//    LOG.info("step#AA " + (String) jsonMap.get("access_token"));
//    return (String) jsonMap.get("access_token");
    return null;
  }

  private static String initiateGenerateSignature(String requestUri, String appKey, String appSecret, String code)
      throws InvalidKeyException, NoSuchAlgorithmException, UnsupportedEncodingException {

    long timestamp = Instant.now().toEpochMilli();
    String encodedTimestamp = URLEncoder.encode(String.valueOf(timestamp), "UTF-8");

    String requestUrl = requestUri + "?app_key=" + appKey
        + "&code=" + code
        + "&sign_method=sha256"
        + "&timestamp=" + encodedTimestamp;

    String requestUriModif = requestUrl.replace("?", "")
        .replace("=", "")
        .replace("&", "");
    // Construct the request URL with parameters
    return generateOAuth2Signature(requestUriModif, appSecret);
  }

  // Method to generate OAuth 2.0 HmacSha256 signature
  private static String generateOAuth2Signature(String uri, String secretKey)
      throws NoSuchAlgorithmException, InvalidKeyException {
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

  // private static void createProduct(String url, String appkey, String appSecret, String accessToken)
  //     throws ApiException, InterruptedException {
  //   LazopClient client = new LazopClient(url, appkey, appSecret);
  //   LazopRequest request = new LazopRequest();
  //   request.setApiName("/product/create");
  //   request.addApiParameter("payload",
  //       "{ \t\"Request\": { \t\t\"Product\": { \t\t\t\"PrimaryCategory\": \"10002019\", \t\t\t\"Images\": { \t\t\t\t\"Image\": [\"XXX\"] \t\t\t}, \t\t\t\"Attributes\": { \t\t\t\t\"name\": \"test 2022 02\", \t\t\t\t\"description\": \"TEST\", \t\t\t\t\"brand\": \"No Brand\", \t\t\t\t\"model\": \"test\", \t\t\t\t\"waterproof\": \"Waterproof\", \t\t\t\t\"warranty_type\": \"Local seller warranty\", \t\t\t\t\"warranty\": \"1 Month\", \t\t\t\t\"short_description\": \"cm x 1efgtecm<br /><brfwefgtek\", \t\t\t\t\"Hazmat\": \"None\", \t\t\t\t\"material\": \"Leather\", \t\t\t\t\"laptop_size\": \"11 - 12 inches\", \t\t\t\t\"delivery_option_sof\": \"No\", \t\t\t\t\"name_engravement\": \"Yes\", \t\t\t\t\"gift_wrapping\": \"Yes\", \t\t\t\t\"preorder_enable\": \"Yes\", \t\t\t\t\"preorder_days\": \"25\" \t\t\t}, \t\t\t\"Skus\": { \t\t\t\t\"Sku\": [{ \t\t\t\t\t\"SellerSku\": \"test2022 02\", \t\t\t\t\t\"quantity\": \"3\", \t\t\t\t\t\"price\": \"35\", \t\t\t\t\t\"special_price\": \"33\", \t\t\t\t\t\"special_from_date\": \"2022-06-20 17:18:31\", \t\t\t\t\t\"special_to_date\": \"2025-03-15 17:18:31\", \t\t\t\t\t\"package_height\": \"10\", \t\t\t\t\t\"package_length\": \"10\", \t\t\t\t\t\"package_width\": \"10\", \t\t\t\t\t\"package_weight\": \"0.5\", \t\t\t\t\t\"package_content\": \"laptop bag\", \t\t\t\t\t\"Images\": { \t\t\t\t\t\t\"Image\": [\"XXX\"] \t\t\t\t\t} \t\t\t\t}] \t\t\t} \t\t} \t} }");

  //   LOG.info("stop 1");
  //   LazopResponse response = client.execute(request, accessToken);
  //   LOG.info("stop 2 " + response.getBody());

  //   System.out.println(response.getBody());
  //   Thread.sleep(10);
  // }

  private static void getProducts(String url, String appkey, String appSecret, String accessToken) throws InterruptedException{
  // LazopClient client = new LazopClient(url, appkey, appSecret);
//  LazopClient client = new LazopClient("https://api.lazada.com/rest", "128209", "pi6auSLgoHUncSr1NK9zLUTDFRI6YaKp"); // "${appkey}",
//
//  LazopRequest request = new LazopRequest();
//  request.setApiName("/products/get");
//  request.setHttpMethod("GET");
//  request.addApiParameter("filter", "live");
//  request.addApiParameter("update_before", "2024-03-01T00:00:00+0800");
//  request.addApiParameter("create_before", "2024-03-01T00:00:00+0800");
//  request.addApiParameter("offset", "0");
//  request.addApiParameter("create_after", "2024-01-01T00:00:00+0800");
//  request.addApiParameter("update_after", "2024-01-01T00:00:00+0800");
//  request.addApiParameter("limit", "10");
//  request.addApiParameter("options", "1");
//  // request.addApiParameter("sku_seller_list", " [\"39817:01:01\", \"Apple 6S Black\"]");
//  LOG.info("stop 1");
//  LazopResponse response = client.execute(request, accessToken);
//  LOG.info("stop 2 " + response.getBody());
//  System.out.println(response.getBody());
//  Thread.sleep(10);
  }
}
