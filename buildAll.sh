cd Login/docker/base/
docker build -t base .
cd ../
bash build.sh login
cd ../../Tweet/docker/
bash build.sh tweet
cd ../../Newsfeed/docker/
bash build.sh feeds
cd ../../Register/docker/
bash build.sh register

