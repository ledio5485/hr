#!/bin/bash

# shellcheck disable=SC2046
docker stop $(docker ps -a -q)
docker rm $(docker ps -a -q)
docker image rm $(docker images -q "challenge_*")