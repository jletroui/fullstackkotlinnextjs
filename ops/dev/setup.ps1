# ejson has no other installation method, apart from a docker image which is not suitable
Invoke-WebRequest -Uri "https://github.com/Shopify/ejson/releases/download/v1.5.2/ejson_1.5.2_windows_amd64.tar.gz" -OutFile ejson.tar.gz
New-Item -ItemType Directory -Force -Path .\ops\tools
tar -xvf ejson.tar.gz -C .\ops\tools
rm -Force ejson.tar.gz
echo "Installed dev tools successfully."
