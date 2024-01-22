
sleep 1
post_attribute()
{
  cat <<EOF
{
  
    "attributeId": "id",
    "attributeName": "id",
    "attributeType": "string",
    "isCommon": true,
    "group": "attribute"
}
EOF
}
echo "===> POST ATTRIBUTE"
curl -i \
-H "Accept: application/json" \
-H "Content-Type:application/json" \
-X POST --data "$(post_attribute)" "localhost:9000/create_attribute"



sleep 1
post_attribute()
{
  cat <<EOF
{
  
    "attributeId": "product_unique_id",
    "attributeName": "product_unique_id",
    "attributeType": "string",
    "isCommon": true,
    "group": "attribute"
}
EOF
}
echo "===> POST ATTRIBUTE"
curl -i \
-H "Accept: application/json" \
-H "Content-Type:application/json" \
-X POST --data "$(post_attribute)" "localhost:9000/create_attribute"




sleep 1
post_attribute()
{
  cat <<EOF
{
  
    "attributeId": "product_id",
    "attributeName": "product_id",
    "attributeType": "string",
    "isCommon": true,
    "group": "attribute"
}
EOF
}
echo "===> POST ATTRIBUTE"
curl -i \
-H "Accept: application/json" \
-H "Content-Type:application/json" \
-X POST --data "$(post_attribute)" "localhost:9000/create_attribute"






sleep 1
post_attribute()
{
  cat <<EOF
{
  
    "attributeId": "product_name",
    "attributeName": "product_name",
    "attributeType": "string",
    "isCommon": false,
    "group": "attribute"
}
EOF
}
echo "===> POST ATTRIBUTE"
curl -i \
-H "Accept: application/json" \
-H "Content-Type:application/json" \
-X POST --data "$(post_attribute)" "localhost:9000/create_attribute"





sleep 1
post_attribute()
{
  cat <<EOF
{
  
    "attributeId": "description",
    "attributeName": "description",
    "attributeType": "string",
    "isCommon": false,
    "group": "attribute"
}
EOF
}
echo "===> POST ATTRIBUTE"
curl -i \
-H "Accept: application/json" \
-H "Content-Type:application/json" \
-X POST --data "$(post_attribute)" "localhost:9000/create_attribute"



sleep 1
post_attribute()
{
  cat <<EOF
{
  
    "attributeId": "product_vendor",
    "attributeName": "product_vendor",
    "attributeType": "string",
    "isCommon": false,
    "group": "attribute"
}
EOF
}
echo "===> POST ATTRIBUTE"
curl -i \
-H "Accept: application/json" \
-H "Content-Type:application/json" \
-X POST --data "$(post_attribute)" "localhost:9000/create_attribute"


sleep 1
post_attribute()
{
  cat <<EOF
{
  
    "attributeId": "product_type",
    "attributeName": "product_type",
    "attributeType": "string",
    "isCommon": false,
    "group": "attribute"
}
EOF
}
echo "===> POST ATTRIBUTE"
curl -i \
-H "Accept: application/json" \
-H "Content-Type:application/json" \
-X POST --data "$(post_attribute)" "localhost:9000/create_attribute"


sleep 1
post_attribute()
{
  cat <<EOF
{
  
    "attributeId": "product_status",
    "attributeName": "product_status",
    "attributeType": "string",
    "isCommon": false,
    "group": "attribute"
}
EOF
}
echo "===> POST ATTRIBUTE"
curl -i \
-H "Accept: application/json" \
-H "Content-Type:application/json" \
-X POST --data "$(post_attribute)" "localhost:9000/create_attribute"


sleep 1
post_attribute()
{
  cat <<EOF
{
  
    "attributeId": "product_images",
    "attributeName": "product_images",
    "attributeType": "object[]",
    "isCommon": false,
    "group": "attribute"
}
EOF
}
echo "===> POST ATTRIBUTE"
curl -i \
-H "Accept: application/json" \
-H "Content-Type:application/json" \
-X POST --data "$(post_attribute)" "localhost:9000/create_attribute"


sleep 1
post_attribute()
{
  cat <<EOF
{
  
    "attributeId": "channel_variant_option1",
    "attributeName": "variants.channel_variant_option1",
    "attributeType": "string",
    "isCommon": false,
    "group": "variant"
}
EOF
}
echo "===> POST ATTRIBUTE"
curl -i \
-H "Accept: application/json" \
-H "Content-Type:application/json" \
-X POST --data "$(post_attribute)" "localhost:9000/create_attribute"




sleep 1
post_attribute()
{
  cat <<EOF
{
  
    "attributeId": "channel_variant_option2",
    "attributeName": "variants.channel_variant_option2",
    "attributeType": "string",
    "isCommon": false,
    "group": "variant"
}
EOF
}
echo "===> POST ATTRIBUTE"
curl -i \
-H "Accept: application/json" \
-H "Content-Type:application/json" \
-X POST --data "$(post_attribute)" "localhost:9000/create_attribute"



sleep 1
post_attribute()
{
  cat <<EOF
{
  
    "attributeId": "channel_variant_price",
    "attributeName": "variants.channel_variant_price",
    "attributeType": "string",
    "isCommon": false,
    "group": "variant"
}
EOF
}
echo "===> POST ATTRIBUTE"
curl -i \
-H "Accept: application/json" \
-H "Content-Type:application/json" \
-X POST --data "$(post_attribute)" "localhost:9000/create_attribute"




sleep 1
post_attribute()
{
  cat <<EOF
{
  
    "attributeId": "channel_variant_sku",
    "attributeName": "variants.channel_variant_sku",
    "attributeType": "string",
    "isCommon": false,
    "group": "variant"
}
EOF
}
echo "===> POST ATTRIBUTE"
curl -i \
-H "Accept: application/json" \
-H "Content-Type:application/json" \
-X POST --data "$(post_attribute)" "localhost:9000/create_attribute"




sleep 1
post_attribute()
{
  cat <<EOF
{
  
    "attributeId": "channel_variant_unique_id",
    "attributeName": "variants.channel_variant_unique_id",
    "attributeType": "string",
    "isCommon": false,
    "group": "variant"
}
EOF
}
echo "===> POST ATTRIBUTE"
curl -i \
-H "Accept: application/json" \
-H "Content-Type:application/json" \
-X POST --data "$(post_attribute)" "localhost:9000/create_attribute"



sleep 1
post_attribute()
{
  cat <<EOF
{
  
    "attributeId": "channel_variant_product_id",
    "attributeName": "variants.channel_variant_product_id",
    "attributeType": "string",
    "isCommon": false,
    "group": "variant"
}
EOF
}
echo "===> POST ATTRIBUTE"
curl -i \
-H "Accept: application/json" \
-H "Content-Type:application/json" \
-X POST --data "$(post_attribute)" "localhost:9000/create_attribute"



sleep 1
post_attribute()
{
  cat <<EOF
{
  
    "attributeId": "channel_variant_taxable",
    "attributeName": "variants.channel_variant_taxable",
    "attributeType": "boolean",
    "isCommon": false,
    "group": "variant"
}
EOF
}
echo "===> POST ATTRIBUTE"
curl -i \
-H "Accept: application/json" \
-H "Content-Type:application/json" \
-X POST --data "$(post_attribute)" "localhost:9000/create_attribute"



sleep 1
post_attribute()
{
  cat <<EOF
{
  
    "attributeId": "channel_variant_barcode",
    "attributeName": "variants.channel_variant_barcode",
    "attributeType": "string",
    "isCommon": false,
    "group": "variant"
}
EOF
}
echo "===> POST ATTRIBUTE"
curl -i \
-H "Accept: application/json" \
-H "Content-Type:application/json" \
-X POST --data "$(post_attribute)" "localhost:9000/create_attribute"




sleep 1
post_attribute()
{
  cat <<EOF
{
  
    "attributeId": "channel_variant_weight",
    "attributeName": "variants.channel_variant_weight",
    "attributeType": "string",
    "isCommon": false,
    "group": "variant"
}
EOF
}
echo "===> POST ATTRIBUTE"
curl -i \
-H "Accept: application/json" \
-H "Content-Type:application/json" \
-X POST --data "$(post_attribute)" "localhost:9000/create_attribute"




sleep 1
post_attribute()
{
  cat <<EOF
{
  
    "attributeId": "channel_variant_weight_unit",
    "attributeName": "variants.channel_variant_weight_unit",
    "attributeType": "string",
    "isCommon": false,
    "group": "variant"
}
EOF
}
echo "===> POST ATTRIBUTE"
curl -i \
-H "Accept: application/json" \
-H "Content-Type:application/json" \
-X POST --data "$(post_attribute)" "localhost:9000/create_attribute"




sleep 1
post_attribute()
{
  cat <<EOF
{
  
    "attributeId": "channel_variant_compare_at_price",
    "attributeName": "variants.channel_variant_compare_at_price",
    "attributeType": "string",
    "isCommon": false,
    "group": "variant"
}
EOF
}
echo "===> POST ATTRIBUTE"
curl -i \
-H "Accept: application/json" \
-H "Content-Type:application/json" \
-X POST --data "$(post_attribute)" "localhost:9000/create_attribute"



sleep 1
post_attribute()
{
  cat <<EOF
{
  
    "attributeId": "channel_variant_requires_shipping",
    "attributeName": "variants.channel_variant_requires_shipping",
    "attributeType": "boolean",
    "isCommon": false,
    "group": "variant"
}
EOF
}
echo "===> POST ATTRIBUTE"
curl -i \
-H "Accept: application/json" \
-H "Content-Type:application/json" \
-X POST --data "$(post_attribute)" "localhost:9000/create_attribute"



sleep 1
post_attribute()
{
  cat <<EOF
{
  
    "attributeId": "channel_variant_inventory_qty",
    "attributeName": "variants.channel_variant_inventory_qty",
    "attributeType": "boolean",
    "isCommon": false,
    "group": "variant"
}
EOF
}
echo "===> POST ATTRIBUTE"
curl -i \
-H "Accept: application/json" \
-H "Content-Type:application/json" \
-X POST --data "$(post_attribute)" "localhost:9000/create_attribute"




sleep 1
post_attribute()
{
  cat <<EOF
{
  
    "attributeId": "channel_variant_inventory_policy",
    "attributeName": "variants.channel_variant_inventory_policy",
    "attributeType": "boolean",
    "isCommon": false,
    "group": "variant"
}
EOF
}
echo "===> POST ATTRIBUTE"
curl -i \
-H "Accept: application/json" \
-H "Content-Type:application/json" \
-X POST --data "$(post_attribute)" "localhost:9000/create_attribute"




sleep 1
post_attribute()
{
  cat <<EOF
{
  
    "attributeId": "channel_variant_position",
    "attributeName": "variants.channel_variant_position",
    "attributeType": "int",
    "isCommon": false,
    "group": "variant"
}
EOF
}
echo "===> POST ATTRIBUTE"
curl -i \
-H "Accept: application/json" \
-H "Content-Type:application/json" \
-X POST --data "$(post_attribute)" "localhost:9000/create_attribute"



sleep 1
post_attribute()
{
  cat <<EOF
{
  
    "attributeId": "channel_variant_track",
    "attributeName": "variants.channel_variant_track",
    "attributeType": "string",
    "isCommon": false,
    "group": "variant"
}
EOF
}
echo "===> POST ATTRIBUTE"
curl -i \
-H "Accept: application/json" \
-H "Content-Type:application/json" \
-X POST --data "$(post_attribute)" "localhost:9000/create_attribute"



sleep 1
post_attribute()
{
  cat <<EOF
{
  
    "attributeId": "channel_option_name",
    "attributeName": "options.channel_option_name",
    "attributeType": "string",
    "isCommon": false,
    "group": "option"
}
EOF
}
echo "===> POST ATTRIBUTE"
curl -i \
-H "Accept: application/json" \
-H "Content-Type:application/json" \
-X POST --data "$(post_attribute)" "localhost:9000/create_attribute"



sleep 1
post_attribute()
{
  cat <<EOF
{
  
    "attributeId": "channel_option_values",
    "attributeName": "options.channel_option_values",
    "attributeType": "string[]",
    "isCommon": false,
    "group": "option"
}
EOF
}
echo "===> POST ATTRIBUTE"
curl -i \
-H "Accept: application/json" \
-H "Content-Type:application/json" \
-X POST --data "$(post_attribute)" "localhost:9000/create_attribute"


# -----------------------------------------------------
