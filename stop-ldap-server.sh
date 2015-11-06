#!/bin/bash

echo "Killing ldap processes"
ps -ef | grep ldap | grep -v grep | awk '{print $2}' | xargs kill

