#!/bin/bash

sudo apt update && sudo apt upgrade -y
sudo apt install -y build-essential curl git software-properties-common openjdk-21-jdk nodejs npm fish

cd frontend
curl -fsSL https://bun.com/install | bash
export PATH="$HOME/.bun/bin:$PATH"

bun install
cd ..
