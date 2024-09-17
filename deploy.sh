#VERSION=io.github.odre-framework:odre-core:0.1.2

mvn clean deploy -DskipTests
cd ./bundle
zip -r release.zip ./io
cp release.zip ../release.zip
cd ..

search_dir=./bundle/io/github/odre-framework/odre-core
clear
for file in "$search_dir"/*; do
    # Check if the file or folder name follows the number.number.number format
    if [[ $(basename "$file") =~ ^[0-9]+\.[0-9]+\.[0-9]+$ ]]; then
        version=$(basename "$file")
        echo "RELEASE NAME--> io.github.odre-framework:odre-core:$version"
    fi
done

rm -r ./bundle
