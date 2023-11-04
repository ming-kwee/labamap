
sleep 1
post_attribute()
{
  cat <<EOF
{
  
    "attributeId": "01",
    "attributeName": "nama_produk",
    "attributeType": "text",
    "isCommon": true
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
  
    "attributeId": "02",
    "attributeName": "deskripsi_pendek",
    "attributeType": "text",
    "isCommon": true
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
  
    "attributeId": "03",
    "attributeName": "vendor",
    "attributeType": "text",
    "isCommon": false
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
  
    "attributeId": "04",
    "attributeName": "tipe_produk",
    "attributeType": "text",
    "isCommon": false
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
  
    "attributeId": "05",
    "attributeName": "status",
    "attributeType": "text",
    "isCommon": false
}
EOF
}
echo "===> POST ATTRIBUTE"
curl -i \
-H "Accept: application/json" \
-H "Content-Type:application/json" \
-X POST --data "$(post_attribute)" "localhost:9000/create_attribute"



# -----------------------------------------------------
