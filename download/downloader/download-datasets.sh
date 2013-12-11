#!/bin/bash

rm -R ../cache

mkdir -p ../cache/dvd
mkdir -p ../cache/twitter-ratings
mkdir -p ../cache/imdb-ratings

cd ../cache/dvd

wget http://www.hometheaterinfo.com/download/DVD_Directors.zip
wget http://www.hometheaterinfo.com/download/DVD_Actors.zip
wget http://www.hometheaterinfo.com/download/dvd_csv.zip

unzip DVD_Actors.zip
unzip dvd_csv.zip
unzip DVD_Directors.zip

mv dvd_csv.txt Dvd.csv
find . -type f -not -name "*.csv" -exec rm {} \;

cd ../twitter-ratings

wget https://raw.github.com/sidooms/MovieTweetings/master/latest/movies.dat
wget https://raw.github.com/sidooms/MovieTweetings/master/latest/ratings.dat

cd ../imdb-ratings

wget http://www.imdb.com/chart/top

