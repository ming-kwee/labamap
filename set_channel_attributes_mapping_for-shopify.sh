
sleep 1
post_channel_attribute_mapping()
{
  cat <<EOF
        {
            "mappingId": 1,
            "channelId": "shopify",
            "attributeId": "product_unique_id",
            "destinationField": "id",
            "type": "string",
            "group": "attribute",
            "isCommonField": true
        }
EOF
}
echo "===> POST ATTRIBUTE"
curl -i \
-H "Accept: application/json" \
-H "Content-Type:application/json" \
-X POST --data "$(post_channel_attribute_mapping)" "localhost:9000/create_channel_attribute_mapping"


sleep 1
post_channel_attribute_mapping()
{
  cat <<EOF
        {
            "mappingId": 2,
            "channelId": "shopify",
            "attributeId": "channel_id",
            "destinationField": "channel_id",
            "type": "string",
            "group": "attribute",
            "isCommonField": true
        }
EOF
}
echo "===> POST ATTRIBUTE"
curl -i \
-H "Accept: application/json" \
-H "Content-Type:application/json" \
-X POST --data "$(post_channel_attribute_mapping)" "localhost:9000/create_channel_attribute_mapping"




sleep 1
post_channel_attribute_mapping()
{
  cat <<EOF
        {
            "mappingId": 3,
            "channelId": "shopify",
            "attributeId": "product_id",
            "destinationField": "product_id",
            "type": "string",
            "group": "attribute",
            "isCommonField": true
        }
EOF
}
echo "===> POST ATTRIBUTE"
curl -i \
-H "Accept: application/json" \
-H "Content-Type:application/json" \
-X POST --data "$(post_channel_attribute_mapping)" "localhost:9000/create_channel_attribute_mapping"




sleep 1
post_channel_attribute_mapping()
{
  cat <<EOF
        {
            "mappingId": 4,
            "channelId": "shopify",
            "attributeId": "product_name",
            "destinationField": "product.title",
            "type": "string",
            "group": "attribute",
            "isCommonField": false
        }
EOF
}
echo "===> POST ATTRIBUTE"
curl -i \
-H "Accept: application/json" \
-H "Content-Type:application/json" \
-X POST --data "$(post_channel_attribute_mapping)" "localhost:9000/create_channel_attribute_mapping"



sleep 1
post_channel_attribute_mapping()
{
  cat <<EOF
        {
            "mappingId": 5,
            "channelId": "shopify",
            "attributeId": "description",
            "destinationField": "product.body_html",
            "type": "string",
            "group": "attribute",
            "isCommonField": false
        }
EOF
}
echo "===> POST ATTRIBUTE"
curl -i \
-H "Accept: application/json" \
-H "Content-Type:application/json" \
-X POST --data "$(post_channel_attribute_mapping)" "localhost:9000/create_channel_attribute_mapping"


sleep 1
post_channel_attribute_mapping()
{
  cat <<EOF
        {
            "mappingId": 6,
            "channelId": "shopify",
            "attributeId": "product_vendor",
            "destinationField": "product.vendor",
            "type": "string",
            "group": "attribute",
            "isCommonField": false
        }
EOF
}
echo "===> POST ATTRIBUTE"
curl -i \
-H "Accept: application/json" \
-H "Content-Type:application/json" \
-X POST --data "$(post_channel_attribute_mapping)" "localhost:9000/create_channel_attribute_mapping"


sleep 1
post_channel_attribute_mapping()
{
  cat <<EOF
        {
            "mappingId": 7,
            "channelId": "shopify",
            "attributeId": "product_type",
            "destinationField": "product.product_type",
            "type": "string",
            "group": "attribute",
            "isCommonField": false
        }
EOF
}
echo "===> POST ATTRIBUTE"
curl -i \
-H "Accept: application/json" \
-H "Content-Type:application/json" \
-X POST --data "$(post_channel_attribute_mapping)" "localhost:9000/create_channel_attribute_mapping"



sleep 1
post_channel_attribute_mapping()
{
  cat <<EOF
        {
            "mappingId": 8,
            "channelId": "shopify",
            "attributeId": "product_status",
            "destinationField": "product.status",
            "type": "string",
            "group": "attribute",
            "isCommonField": false
        }
EOF
}
echo "===> POST ATTRIBUTE"
curl -i \
-H "Accept: application/json" \
-H "Content-Type:application/json" \
-X POST --data "$(post_channel_attribute_mapping)" "localhost:9000/create_channel_attribute_mapping"



sleep 1
post_channel_attribute_mapping()
{
  cat <<EOF
        {
            "mappingId": 9,
            "channelId": "shopify",
            "attributeId": "product_images",
            "destinationField": "product.images",
            "type": "object[]",
            "group": "attribute",
            "isCommonField": false
        }
EOF
}
echo "===> POST ATTRIBUTE"
curl -i \
-H "Accept: application/json" \
-H "Content-Type:application/json" \
-X POST --data "$(post_channel_attribute_mapping)" "localhost:9000/create_channel_attribute_mapping"



sleep 1
post_channel_attribute_mapping()
{
  cat <<EOF
        {
            "mappingId": 10,
            "channelId": "shopify",
            "attributeId": "channel_variant_option1",
            "destinationField": "product.variants.option1",
            "type": "string",
            "group": "variant",
            "isCommonField": false
        }
EOF
}
echo "===> POST ATTRIBUTE"
curl -i \
-H "Accept: application/json" \
-H "Content-Type:application/json" \
-X POST --data "$(post_channel_attribute_mapping)" "localhost:9000/create_channel_attribute_mapping"


sleep 1
post_channel_attribute_mapping()
{
  cat <<EOF
        {
            "mappingId": 11,
            "channelId": "shopify",
            "attributeId": "channel_variant_option2",
            "destinationField": "product.variants.option2",
            "type": "string",
            "group": "variant",
            "isCommonField": false
        }
EOF
}
echo "===> POST ATTRIBUTE"
curl -i \
-H "Accept: application/json" \
-H "Content-Type:application/json" \
-X POST --data "$(post_channel_attribute_mapping)" "localhost:9000/create_channel_attribute_mapping"



sleep 1
post_channel_attribute_mapping()
{
  cat <<EOF
        {
            "mappingId": 12,
            "channelId": "shopify",
            "attributeId": "channel_variant_price",
            "destinationField": "product.variants.price",
            "type": "string",
            "group": "variant",
            "isCommonField": false
        }
EOF
}
echo "===> POST ATTRIBUTE"
curl -i \
-H "Accept: application/json" \
-H "Content-Type:application/json" \
-X POST --data "$(post_channel_attribute_mapping)" "localhost:9000/create_channel_attribute_mapping"



sleep 1
post_channel_attribute_mapping()
{
  cat <<EOF
        {
            "mappingId": 13,
            "channelId": "shopify",
            "attributeId": "channel_variant_sku",
            "destinationField": "product.variants.sku",
            "type": "string",
            "group": "variant",
            "isCommonField": false
        }
EOF
}
echo "===> POST ATTRIBUTE"
curl -i \
-H "Accept: application/json" \
-H "Content-Type:application/json" \
-X POST --data "$(post_channel_attribute_mapping)" "localhost:9000/create_channel_attribute_mapping"



sleep 1
post_channel_attribute_mapping()
{
  cat <<EOF
        {
            "mappingId": 14,
            "channelId": "shopify",
            "attributeId": "channel_variant_unique_id",
            "destinationField": "product.variants.id",
            "type": "string",
            "group": "variant",
            "isCommonField": false
        }
EOF
}
echo "===> POST ATTRIBUTE"
curl -i \
-H "Accept: application/json" \
-H "Content-Type:application/json" \
-X POST --data "$(post_channel_attribute_mapping)" "localhost:9000/create_channel_attribute_mapping"



sleep 1
post_channel_attribute_mapping()
{
  cat <<EOF
        {
            "mappingId": 15,
            "channelId": "shopify",
            "attributeId": "channel_variant_product_id",
            "destinationField": "product.variants.product_id",
            "type": "string",
            "group": "variant",
            "isCommonField": false
        }
EOF
}
echo "===> POST ATTRIBUTE"
curl -i \
-H "Accept: application/json" \
-H "Content-Type:application/json" \
-X POST --data "$(post_channel_attribute_mapping)" "localhost:9000/create_channel_attribute_mapping"



sleep 1
post_channel_attribute_mapping()
{
  cat <<EOF
        {
            "mappingId": 16,
            "channelId": "shopify",
            "attributeId": "channel_variant_taxable",
            "destinationField": "product.variants.taxable",
            "type": "boolean",
            "group": "variant",
            "isCommonField": false
        }
EOF
}
echo "===> POST ATTRIBUTE"
curl -i \
-H "Accept: application/json" \
-H "Content-Type:application/json" \
-X POST --data "$(post_channel_attribute_mapping)" "localhost:9000/create_channel_attribute_mapping"



sleep 1
post_channel_attribute_mapping()
{
  cat <<EOF
        {
            "mappingId": 17,
            "channelId": "shopify",
            "attributeId": "channel_variant_barcode",
            "destinationField": "product.variants.barcode",
            "type": "string",
            "group": "variant",
            "isCommonField": false
        }
EOF
}
echo "===> POST ATTRIBUTE"
curl -i \
-H "Accept: application/json" \
-H "Content-Type:application/json" \
-X POST --data "$(post_channel_attribute_mapping)" "localhost:9000/create_channel_attribute_mapping"



sleep 1
post_channel_attribute_mapping()
{
  cat <<EOF
        {
            "mappingId": 18,
            "channelId": "shopify",
            "attributeId": "channel_variant_weight",
            "destinationField": "product.variants.weight",
            "type": "string",
            "group": "variant",
            "isCommonField": false
        }
EOF
}
echo "===> POST ATTRIBUTE"
curl -i \
-H "Accept: application/json" \
-H "Content-Type:application/json" \
-X POST --data "$(post_channel_attribute_mapping)" "localhost:9000/create_channel_attribute_mapping"


sleep 1
post_channel_attribute_mapping()
{
  cat <<EOF
        {
            "mappingId": 19,
            "channelId": "shopify",
            "attributeId": "channel_variant_weight_unit",
            "destinationField": "product.variants.weight_unit",
            "type": "string",
            "group": "variant",
            "isCommonField": false
        }
EOF
}
echo "===> POST ATTRIBUTE"
curl -i \
-H "Accept: application/json" \
-H "Content-Type:application/json" \
-X POST --data "$(post_channel_attribute_mapping)" "localhost:9000/create_channel_attribute_mapping"



sleep 1
post_channel_attribute_mapping()
{
  cat <<EOF
        {
            "mappingId": 20,
            "channelId": "shopify",
            "attributeId": "channel_variant_compare_at_price",
            "destinationField": "product.variants.compare_at_price",
            "type": "string",
            "group": "variant",
            "isCommonField": false
        }
EOF
}
echo "===> POST ATTRIBUTE"
curl -i \
-H "Accept: application/json" \
-H "Content-Type:application/json" \
-X POST --data "$(post_channel_attribute_mapping)" "localhost:9000/create_channel_attribute_mapping"



sleep 1
post_channel_attribute_mapping()
{
  cat <<EOF
        {
            "mappingId": 21,
            "channelId": "shopify",
            "attributeId": "channel_variant_requires_shipping",
            "destinationField": "product.variants.requires_shipping",
            "type": "boolean",
            "group": "variant",
            "isCommonField": false
        }
EOF
}
echo "===> POST ATTRIBUTE"
curl -i \
-H "Accept: application/json" \
-H "Content-Type:application/json" \
-X POST --data "$(post_channel_attribute_mapping)" "localhost:9000/create_channel_attribute_mapping"



sleep 1
post_channel_attribute_mapping()
{
  cat <<EOF
        {
            "mappingId": 22,
            "channelId": "shopify",
            "attributeId": "channel_variant_inventory_qty",
            "destinationField": "product.variants.inventory_quantity",
            "type": "string",
            "group": "variant",
            "isCommonField": false
        }
EOF
}
echo "===> POST ATTRIBUTE"
curl -i \
-H "Accept: application/json" \
-H "Content-Type:application/json" \
-X POST --data "$(post_channel_attribute_mapping)" "localhost:9000/create_channel_attribute_mapping"



sleep 1
post_channel_attribute_mapping()
{
  cat <<EOF
        {
            "mappingId": 23,
            "channelId": "shopify",
            "attributeId": "channel_variant_inventory_policy",
            "destinationField": "product.variants.inventory_policy",
            "type": "string",
            "group": "variant",
            "isCommonField": false
        }
EOF
}
echo "===> POST ATTRIBUTE"
curl -i \
-H "Accept: application/json" \
-H "Content-Type:application/json" \
-X POST --data "$(post_channel_attribute_mapping)" "localhost:9000/create_channel_attribute_mapping"



sleep 1
post_channel_attribute_mapping()
{
  cat <<EOF
        {
            "mappingId": 24,
            "channelId": "shopify",
            "attributeId": "channel_variant_position",
            "destinationField": "product.variants.position",
            "type": "int",
            "group": "variant",
            "isCommonField": false
        }
EOF
}
echo "===> POST ATTRIBUTE"
curl -i \
-H "Accept: application/json" \
-H "Content-Type:application/json" \
-X POST --data "$(post_channel_attribute_mapping)" "localhost:9000/create_channel_attribute_mapping"



sleep 1
post_channel_attribute_mapping()
{
  cat <<EOF
        {
            "mappingId": 25,
            "channelId": "shopify",
            "attributeId": "channel_variant_track",
            "destinationField": "product.variants.track",
            "type": "string",
            "group": "variant",
            "isCommonField": false
        }
EOF
}
echo "===> POST ATTRIBUTE"
curl -i \
-H "Accept: application/json" \
-H "Content-Type:application/json" \
-X POST --data "$(post_channel_attribute_mapping)" "localhost:9000/create_channel_attribute_mapping"



sleep 1
post_channel_attribute_mapping()
{
  cat <<EOF
        {
            "mappingId": 26,
            "channelId": "shopify",
            "attributeId": "channel_variant_product_images",
            "destinationField": "product.variants.images",
            "type": "object[]",
            "group": "variant",
            "isCommonField": false
        }
EOF
}
echo "===> POST ATTRIBUTE"
curl -i \
-H "Accept: application/json" \
-H "Content-Type:application/json" \
-X POST --data "$(post_channel_attribute_mapping)" "localhost:9000/create_channel_attribute_mapping"



sleep 1
post_channel_attribute_mapping()
{
  cat <<EOF
        {
            "mappingId": 27,
            "channelId": "shopify",
            "attributeId": "channel_option_name",
            "destinationField": "product.options.name",
            "type": "string",
            "group": "option",
            "isCommonField": false
        }
EOF
}
echo "===> POST ATTRIBUTE"
curl -i \
-H "Accept: application/json" \
-H "Content-Type:application/json" \
-X POST --data "$(post_channel_attribute_mapping)" "localhost:9000/create_channel_attribute_mapping"



sleep 1
post_channel_attribute_mapping()
{
  cat <<EOF
        {
            "mappingId": 28,
            "channelId": "shopify",
            "attributeId": "channel_option_values",
            "destinationField": "product.options.values",
            "type": "string[]",
            "group": "option",
            "isCommonField": false
        }
EOF
}
echo "===> POST ATTRIBUTE"
curl -i \
-H "Accept: application/json" \
-H "Content-Type:application/json" \
-X POST --data "$(post_channel_attribute_mapping)" "localhost:9000/create_channel_attribute_mapping"

