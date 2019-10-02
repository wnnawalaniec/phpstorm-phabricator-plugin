#!/bin/bash
set -B

help() {
  echo "To run this command you have to specify 3 parameters: -n -t -u"
  echo "-n number of tasks to add"
  echo "-t token Conduit api token"
  echo "-u assigned user phid"
  printf "example: %s -n %d -t %s -u %s\n" $0 100 "api-1234" "PHID-USER-1234"
  exit 1;
}

while getopts ":n:t:u:p:h" opt; do
  case $opt in
    n) number="$OPTARG"
    ;;
    t) token="$OPTARG"
    ;;
    u) user="$OPTARG"
    ;;
    h) help
    ;;
    \?) echo "Invalid option -$OPTARG" >&2
    ;;
  esac
done

for (( i = 1; i <= $number; i++ )); do
	curl http://phabricator.local/api/project.create \
		-d api.token=$token \
		-d name=P$i \
		-d members[0]=$user
done