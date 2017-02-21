#!/bin/sh

gitbook build
rm -rf ../docs
mv _book ../docs
rm ../docs/generate-gh-docs.sh
