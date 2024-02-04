
sleep 1
post_channel_metadata()
{
  cat <<EOF
        {
                "channelId": "shopify",
                "key": "create_channel_product_endpoint",
                "value": "https://labamap.myshopify.com/admin/api/2023-04/products.json",
                "target": "channelProduct",
                "grouping": "integration",
                "subGrouping": "http_request"
        }
EOF
}
echo "===> POST ATTRIBUTE"
curl -i \
-H "Accept: application/json" \
-H "Content-Type:application/json" \
-X POST --data "$(post_channel_metadata)" "localhost:9000/create_metadata"



sleep 1
post_channel_metadata()
{
  cat <<EOF
        {
        "channelId": "shopify",
        "key": "create_channel_product_access_token_header_name",
        "value": "X-Shopify-Access-Token",
        "target": "channelProduct",
        "grouping": "integration",
        "subGrouping": "http_request"
        }
EOF
}
echo "===> POST ATTRIBUTE"
curl -i \
-H "Accept: application/json" \
-H "Content-Type:application/json" \
-X POST --data "$(post_channel_metadata)" "localhost:9000/create_metadata"


sleep 1
post_channel_metadata()
{
  cat <<EOF
        {
        "channelId": "shopify",
        "key": "create_channel_product_authorization",
        "value": "bearer",
        "target": "channelProduct",
        "grouping": "integration",
        "subGrouping": "security"
        }
EOF
}
echo "===> POST ATTRIBUTE"
curl -i \
-H "Accept: application/json" \
-H "Content-Type:application/json" \
-X POST --data "$(post_channel_metadata)" "localhost:9000/create_metadata"


sleep 1
post_channel_metadata()
{
  cat <<EOF
        {
        "channelId": "shopify",
        "key": "create_channel_product_token_from",
        "value": "shopify_token",
        "target": "channelProduct",
        "grouping": "integration",
        "subGrouping": "security"
        }
EOF
}
echo "===> POST ATTRIBUTE"
curl -i \
-H "Accept: application/json" \
-H "Content-Type:application/json" \
-X POST --data "$(post_channel_metadata)" "localhost:9000/create_metadata"

