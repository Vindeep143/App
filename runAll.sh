docker rm -f login tweet feeds register
docker run -d -P --name login login
docker run -d -P --name register register
docker run -d -P --name feeds feeds
docker run -d -P --name tweet tweet
