
sleep 1
post_attribute()
{
  cat <<EOF
{
  
    "attribute_id": "01",
    "attribute_name": "warna",
    "attribute_type": "text"
}
EOF
}
echo "===> POST ATTRIBUTE"
curl -i \
-H "Accept: application/json" \
-H "Content-Type:application/json" \
-X POST --data "$(post_attribute)" "localhost:9000/create_product_attribute"



sleep 1
post_attribute()
{
  cat <<EOF
{
  
    "attribute_id": "02",
    "attribute_name": "ukuran",
    "attribute_type": "text"
}
EOF
}
echo "===> POST ATTRIBUTE"
curl -i \
-H "Accept: application/json" \
-H "Content-Type:application/json" \
-X POST --data "$(post_attribute)" "localhost:9000/create_product_attribute"




sleep 1
post_attribute()
{
  cat <<EOF
{
  
    "attribute_id": "03",
    "attribute_name": "harga",
    "attribute_type": "text"
}
EOF
}
echo "===> POST ATTRIBUTE"
curl -i \
-H "Accept: application/json" \
-H "Content-Type:application/json" \
-X POST --data "$(post_attribute)" "localhost:9000/create_product_attribute"



# -----------------------------------------------------

sleep 3
post_attribute_value()
{
  cat <<EOF
{
    "value_id": "001",
    "attribute_id": "01",
    "channel_id": "z01",
    "value": "merah"
}
EOF
}
echo "===> UPDATE USER TO ADMIN"
curl -i \
-H "Accept: application/json" \
-H "Content-Type:application/json" \
-X POST --data "$(post_attribute_value)" "localhost:9000/create_product_attribute_value_impl"


sleep 3
post_attribute_value()
{
  cat <<EOF
{
    "value_id": "002",
    "attribute_id": "01",
    "channel_id": "z01",
    "value": "biru"
}
EOF
}
echo "===> UPDATE USER TO ADMIN"
curl -i \
-H "Accept: application/json" \
-H "Content-Type:application/json" \
-X POST --data "$(post_attribute_value)" "localhost:9000/create_product_attribute_value_impl"




sleep 3
post_attribute_value()
{
  cat <<EOF
{
    "value_id": "003",
    "attribute_id": "02",
    "channel_id": "z01",
    "value": "small"
}
EOF
}
echo "===> UPDATE USER TO ADMIN"
curl -i \
-H "Accept: application/json" \
-H "Content-Type:application/json" \
-X POST --data "$(post_attribute_value)" "localhost:9000/create_product_attribute_value_impl"




sleep 3
post_attribute_value()
{
  cat <<EOF
{
    "value_id": "004",
    "attribute_id": "02",
    "channel_id": "z01",
    "value": "medium"
}
EOF
}
echo "===> UPDATE USER TO ADMIN"
curl -i \
-H "Accept: application/json" \
-H "Content-Type:application/json" \
-X POST --data "$(post_attribute_value)" "localhost:9000/create_product_attribute_value_impl"




sleep 3
post_attribute_value()
{
  cat <<EOF
{
    "value_id": "005",
    "attribute_id": "02",
    "channel_id": "z01",
    "value": "large"
}
EOF
}
echo "===> UPDATE USER TO ADMIN"
curl -i \
-H "Accept: application/json" \
-H "Content-Type:application/json" \
-X POST --data "$(post_attribute_value)" "localhost:9000/create_product_attribute_value_impl"



sleep 3
post_attribute_value()
{
  cat <<EOF
{
    "value_id": "005",
    "attribute_id": "03",
    "channel_id": "z01",
    "value": "100"
}
EOF
}
echo "===> UPDATE USER TO ADMIN"
curl -i \
-H "Accept: application/json" \
-H "Content-Type:application/json" \
-X POST --data "$(post_attribute_value)" "localhost:9000/create_product_attribute_value_impl"



sleep 3
post_attribute_value()
{
  cat <<EOF
{
    "value_id": "006",
    "attribute_id": "03",
    "channel_id": "z01",
    "value": "200"
}
EOF
}
echo "===> UPDATE USER TO ADMIN"
curl -i \
-H "Accept: application/json" \
-H "Content-Type:application/json" \
-X POST --data "$(post_attribute_value)" "localhost:9000/create_product_attribute_value_impl"


sleep 3
post_attribute_value()
{
  cat <<EOF
{
    "value_id": "007",
    "attribute_id": "03",
    "channel_id": "z01",
    "value": "300"
}
EOF
}
echo "===> UPDATE USER TO ADMIN"
curl -i \
-H "Accept: application/json" \
-H "Content-Type:application/json" \
-X POST --data "$(post_attribute_value)" "localhost:9000/create_product_attribute_value_impl"