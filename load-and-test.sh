#!/bin/bash
set -e

# Load env
set -a
source <(grep -E '^[A-Z0-9_]+=' .env.testing)
set +a

# Tìm tất cả thư mục có pom.xml và chạy test
for dir in */ ; do
  if [ -f "$dir/pom.xml" ]; then
    echo "---------------------------------------------------"
    echo " Running tests for service: $dir"
    echo "---------------------------------------------------"
    mvn -f "$dir/pom.xml" clean test
  fi
done
