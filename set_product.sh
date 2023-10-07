

sleep 3
post_product()
{
  cat <<EOF
{
    "id": "01",
    "nama_produk": "mcm 508 bit",
    "deskripsi_pendek": "magic com miyako 508 bit"
}
EOF
}
echo "===> UPDATE USER TO ADMIN"
curl -i \
-H "Accept: application/json" \
-H "Content-Type:application/json" \
-X POST --data "$(post_product)" "localhost:9000/create_product"



