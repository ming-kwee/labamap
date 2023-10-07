
sleep 1
post_channel_attribute_mapping()
{
  cat <<EOF
{
  
    "channelAttributeMappingId": "01",
    "attributeId": "01",
    "channelId": "SHPFY",
    "channelAttributeName": "title"
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
  
    "channelAttributeMappingId": "02",
    "attributeId": "02",
    "channelId": "SHPFY",
    "channelAttributeName": "body_html"
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
  
    "channelAttributeMappingId": "03",
    "attributeId": "03",
    "channelId": "SHPFY",
    "channelAttributeName": "vendor"
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
  
    "channelAttributeMappingId": "04",
    "attributeId": "04",
    "channelId": "SHPFY",
    "channelAttributeName": "product_type"
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
  
    "channelAttributeMappingId": "05",
    "attributeId": "05",
    "channelId": "SHPFY",
    "channelAttributeName": "status"
}
EOF
}
echo "===> POST ATTRIBUTE"
curl -i \
-H "Accept: application/json" \
-H "Content-Type:application/json" \
-X POST --data "$(post_channel_attribute_mapping)" "localhost:9000/create_channel_attribute_mapping"

