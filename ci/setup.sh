#!/bin/bash

sudo apt update && sudo apt upgrade -y
sudo apt install -y build-essential curl git software-properties-common openjdk-21-jdk npm fish

cd frontend
curl -fsSL https://bun.com/install | bash
export PATH="$HOME/.bun/bin:$PATH"

# install node 20
curl -fsSL https://deb.nodesource.com/setup_20.x | sudo -E bash -
sudo apt-get install -y nodejs
