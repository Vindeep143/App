rm *.jar
cd ../
mvn package
cp target/*.jar docker/
cd docker
docker build -t $1 .
