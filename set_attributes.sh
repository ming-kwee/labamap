
sleep 1
post_attribute()
{
  cat <<EOF
{
  
    "attribute_id": "01",
    "attribute_name": "nama_produk",
    "attribute_type": "text",
    "is_common": true
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
  
    "attribute_id": "02",
    "attribute_name": "deskripsi_pendek",
    "attribute_type": "text",
    "is_common": true
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
  
    "attribute_id": "03",
    "attribute_name": "vendor",
    "attribute_type": "text",
    "is_common": false
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
  
    "attribute_id": "04",
    "attribute_name": "tipe_produk",
    "attribute_type": "text",
    "is_common": false
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
  
    "attribute_id": "05",
    "attribute_name": "status",
    "attribute_type": "text",
    "is_common": false
}
EOF
}
echo "===> POST ATTRIBUTE"
curl -i \
-H "Accept: application/json" \
-H "Content-Type:application/json" \
-X POST --data "$(post_attribute)" "localhost:9000/create_attribute"



# -----------------------------------------------------
