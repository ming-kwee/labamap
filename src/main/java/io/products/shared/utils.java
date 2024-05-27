package io.products.shared;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.protobuf.*;
public class utils {

  private static final Logger LOG = LoggerFactory.getLogger(utils.class);

  public static void addAttribute_toMap(Map<String, Object> attrMap, String attribute, String value,
      String type) {
    String[] attributeParts = attribute.split("\\.");
    Map<String, Object> currentMap = attrMap;

    for (int i = 0; i < attributeParts.length - 1; i++) {
      currentMap.putIfAbsent(attributeParts[i], new HashMap<>());
      currentMap = (Map<String, Object>) currentMap.get(attributeParts[i]);
    }
    currentMap.put(attributeParts[attributeParts.length - 1], typeConverter(value, type));
    // LOG.info("addAttribute_toMap " + currentMap);
  }

  public static Map<String, Object> addVariantOrOption_toGroupMap(Map<String, Object> groupMap,
      String variant_or_option, String value,
      String type) {
    String[] variant_or_option_parts = variant_or_option.split("\\.");
    Map<String, Object> currentMap = groupMap;

    for (int i = 0; i < variant_or_option_parts.length - 1; i++) {
      currentMap.putIfAbsent(variant_or_option_parts[i], new HashMap<>());
      currentMap = (Map<String, Object>) currentMap.get(variant_or_option_parts[i]);
    }
    currentMap.put(variant_or_option_parts[variant_or_option_parts.length - 1], typeConverter(value, type));
    // LOG.info("addVariantOrOption_toGroupMap " + currentMap);
    return groupMap; // Return the modified groupMap
  }

  public static Map<String, Object> mergeMaps(List<Map<String, Object>> listOfMaps, int deepLevel) {
    Map<String, Object> mergedMap = new HashMap<>();
    List<Map<String, Object>> variants_or_options_merged = new ArrayList<>();

    List<String> allKeys = new ArrayList<>();
    for (Map<String, Object> map : listOfMaps) {
      collectKeys(map, allKeys);
      Map<String, Object> currentMap = map;
      for (int i = 0; i < deepLevel; i++) {
        currentMap = (Map<String, Object>) currentMap.get(allKeys.get(i));
      }
      if (currentMap instanceof Map) {
        variants_or_options_merged.add((Map<String, Object>) currentMap);
      } else if (currentMap instanceof List) {
        variants_or_options_merged.addAll((List<Map<String, Object>>) currentMap);
      }
    }
    Map<String, Object> currentMap = mergedMap;
    if (deepLevel == 0) {
      // do nothing
    } else if (deepLevel == 1) {
      currentMap.put(allKeys.get(0), variants_or_options_merged);
    } else {
      // Iterate through the keys to create nested maps
      for (int i = 0; i < deepLevel - 1; i++) {
        currentMap.put(allKeys.get(i), new HashMap<>());
        currentMap = (Map<String, Object>) currentMap.get(allKeys.get(i));
      }

      // Finally, put the data at the leaf level
      currentMap.put(allKeys.get(deepLevel - 1), variants_or_options_merged);
    }

    LOG.info("mergeMaps " + mergedMap);
    return mergedMap;
  }

  public static Map<String, Object> finalMergeMaps(Map<String, Object> map1, Map<String, Object> map2) {

    Map<String, Object> mergedMap = new HashMap<>(map1);
    for (Map.Entry<String, Object> entry : map2.entrySet()) {
      String key = entry.getKey();
      Object value = entry.getValue();

      if (mergedMap.containsKey(key)) {
        Object existingValue = mergedMap.get(key);

        if (existingValue instanceof Map && value instanceof Map) {
          // If both values are maps, recursively merge them
          Map<String, Object> mergedValue = finalMergeMaps((Map<String, Object>) existingValue,
              (Map<String, Object>) value);
          mergedMap.put(key, mergedValue);
        } else if (existingValue instanceof Map) {
          // If existing value is a map and new value is not, keep the existing map
        } else if (value instanceof Map) {
          // If new value is a map and existing value is not, replace with the new map
          mergedMap.put(key, value);
        } else {
          // If both values are not maps, replace with the new value
          mergedMap.put(key, value);
        }
      } else {
        // If the key doesn't exist in the merged map, add it
        mergedMap.put(key, value);
      }
    }

    LOG.info("finalMergeMaps " + mergedMap);
    return mergedMap;
  }

  public static String replaceAfterUnderscore(String input) {
    StringBuilder result = new StringBuilder();
    boolean convertNext = false;
    for (char c : input.toCharArray()) {
      if (c == '_') {
        convertNext = true;
      } else if (convertNext) {
        result.append(Character.toUpperCase(c));
        convertNext = false;
      } else {
        result.append(c);
      }
    }
    return result.toString();
  }





  public static Struct convertNumberFieldsToString(Map<String, Value> fieldsMap, String... fieldsToConvert) {
    Struct.Builder structBuilder = Struct.newBuilder();

    for (Map.Entry<String, Value> entry : fieldsMap.entrySet()) {
      String key = entry.getKey();
      Value value = entry.getValue();

      if (shouldConvert(key, fieldsToConvert) && value.hasNumberValue()) {
        BigDecimal numberValue = BigDecimal.valueOf(value.getNumberValue());
        structBuilder.putFields(key, Value.newBuilder().setStringValue(numberValue.toPlainString()).build());
      } else if (value.hasStructValue()) {
        structBuilder.putFields(key, Value.newBuilder().setStructValue(
            convertNumberFieldsToString(value.getStructValue().getFieldsMap(), fieldsToConvert)).build());
      } else if (value.hasListValue()) {
        ListValue.Builder listBuilder = ListValue.newBuilder();
        for (Value listItem : value.getListValue().getValuesList()) {
          if (listItem.hasStructValue()) {
            listBuilder.addValues(Value.newBuilder().setStructValue(
                convertNumberFieldsToString(listItem.getStructValue().getFieldsMap(), fieldsToConvert)).build());
          } else {
            listBuilder.addValues(listItem);
          }
        }
        structBuilder.putFields(key, Value.newBuilder().setListValue(listBuilder.build()).build());
      } else {
        structBuilder.putFields(key, value);
      }
    }

    return structBuilder.build();
  }


  
  /* ------------------------- */
  /* private supporter functions */
  /* ------------------------- */
  private static void collectKeys(Map<String, Object> map, List<String> allKeys) {
    for (Map.Entry<String, Object> entry : map.entrySet()) {
      String key = entry.getKey();
      Object value = entry.getValue();

      // Add the current key to the list
      allKeys.add(key);

      if (value instanceof Map) {
        // Recursively traverse nested maps
        collectKeys((Map<String, Object>) value, allKeys);
      }
    }
  }

  private static Object typeConverter(String value, String type) {

    // LOG.info("typeConverter.value " + value);
    // LOG.info("typeConverter.type " + type);

    if ("string".equalsIgnoreCase(type)) {
      return value;
    } else if ("int".equalsIgnoreCase(type)) {
      try {
        return Integer.parseInt(value);
      } catch (NumberFormatException e) {
        // Handle the case where the value is not a valid integer
        return 0; // You can return a default value or handle the error as needed
      }
    } else if ("boolean".equalsIgnoreCase(type)) {
      return Boolean.parseBoolean(value);
    } else if ("float".equalsIgnoreCase(type)) {
      try {
        return Float.parseFloat(value);
      } catch (NumberFormatException e) {
        // Handle the case where the value is not a valid float
        return 0.0f; // You can return a default value or handle the error as needed
      }
    } else if ("object".equalsIgnoreCase(type)) {
      // Parse the value as an Object (you might need custom logic for this)
      return parseObject(value);
    } else if ("object[]".equalsIgnoreCase(type)) {
      // Parse the value as an array of Objects (you might need custom logic for this)
      return parseObjectArray(value);
    } else if ("string[]".equalsIgnoreCase(type)) {
      // Parse the value as an array of Objects (you might need custom logic for this)
      return parseObjectArray(value);
    } else {
      // Handle unsupported types or throw an exception
      throw new IllegalArgumentException("Unsupported attribute type: " + type);
    }
  }

  // Custom method to parse an Object
  private static Object parseObject(String value) {
    // Implement your custom logic to parse the value as an Object
    // For example, you can deserialize a JSON string into an Object
    // Here's a simplified example using JSON:
    try {
      ObjectMapper objectMapper = new ObjectMapper();
      return objectMapper.readValue(value, Object.class);
    } catch (IOException e) {
      // Handle parsing errors
      return null; // Or handle the error as needed
    }
  }

  // Custom method to parse an array of Objects
  private static Object[] parseObjectArray(String value) {
    // Implement your custom logic to parse the value as an array of Objects
    // For example, you can deserialize a JSON array into an Object[] array
    // Here's a simplified example using JSON:
    try {
      ObjectMapper objectMapper = new ObjectMapper();
      return objectMapper.readValue(value, Object[].class);
    } catch (IOException e) {
      // Handle parsing errors
      return null; // Or handle the error as needed
    }
  }
  
  
  private static boolean shouldConvert(String key, String[] fieldsToConvert) {
    for (String field : fieldsToConvert) {
      if (key.equals(field)) {
        return true;
      }
    }
    return false;
  }
  /* ------------------------- */
  /* private supporter functions */
  /* ------------------------- */


}
