#!/usr/bin/sh

exec protoc ast/src/main/ast.proto --java_out=./ast/src/main/java/
