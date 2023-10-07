
sleep 3
post_channel_product_attribute()
{
  cat <<EOF
{
    "channel_product_attribute_id": "01",
    "product_id": "01",
    "channel_product_id": "01",
    "channel_id": "01",
    "attribute_id": "01",
    "channel_attribute_value": "mcm 508 bit"
}
EOF
}
echo "===> UPDATE USER TO ADMIN"
curl -i \
-H "Accept: application/json" \
-H "Content-Type:application/json" \
-X POST --data "$(post_channel_product_attribute)" "localhost:9000/create_channel_product_attribute"



sleep 3
post_channel_product_attribute()
{
  cat <<EOF
{
    "channel_product_attribute_id": "02",
    "product_id": "01",
    "channel_product_id": "01",
    "channel_id": "01",
    "attribute_id": "02",
    "channel_attribute_value": "magic com miyako 508 bit"
}
EOF
}
echo "===> UPDATE USER TO ADMIN"
curl -i \
-H "Accept: application/json" \
-H "Content-Type:application/json" \
-X POST --data "$(post_channel_product_attribute)" "localhost:9000/create_channel_product_attribute"



sleep 3
post_channel_product_attribute()
{
  cat <<EOF
{
    "channel_product_attribute_id": "03",
    "product_id": "01",
    "channel_product_id": "01",
    "channel_id": "01",
    "attribute_id": "03",
    "channel_attribute_value": "bhakti idola tama"
}
EOF
}
echo "===> UPDATE USER TO ADMIN"
curl -i \
-H "Accept: application/json" \
-H "Content-Type:application/json" \
-X POST --data "$(post_channel_product_attribute)" "localhost:9000/create_channel_product_attribute"



sleep 3
post_channel_product_attribute()
{
  cat <<EOF
{
    "channel_product_attribute_id": "04",
    "product_id": "01",
    "channel_product_id": "01",
    "channel_id": "01",
    "attribute_id": "04",
    "channel_attribute_value": "magic com"
}
EOF
}
echo "===> UPDATE USER TO ADMIN"
curl -i \
-H "Accept: application/json" \
-H "Content-Type:application/json" \
-X POST --data "$(post_channel_product_attribute)" "localhost:9000/create_channel_product_attribute"





sleep 3
post_channel_product_attribute()
{
  cat <<EOF
{
    "channel_product_attribute_id": "05",
    "product_id": "01",
    "channel_product_id": "01",
    "channel_id": "01",
    "attribute_id": "05",
    "channel_attribute_value": "draft"
}
EOF
}
echo "===> UPDATE USER TO ADMIN"
curl -i \
-H "Accept: application/json" \
-H "Content-Type:application/json" \
-X POST --data "$(post_channel_product_attribute)" "localhost:9000/create_channel_product_attribute"
