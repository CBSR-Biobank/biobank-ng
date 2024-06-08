#!/bin/bash

SCRIPT=`basename $0`

USAGE="
Usage: $SCRIPT

Resets the database.

Reads settings from the .env file if it exists.

OPTIONS
  -e PATH   Specify the path to the .env file.
  -h        Help text.
"

ENV_FILE=".env"

DUMP_FILE="biobank.sql.gz"

while getopts "d:e:h" OPTION
do
    case $OPTION in
       d)
            DUMP_FILE=$OPTARG
            ;;
       e)
            ENV_FILE=$OPTARG
            ;;
       h)
           echo "$USAGE";
           exit;;
    esac
done

# Remove the options from the positional parameters
shift $((OPTIND -1))

if [ ! -f "$ENV_FILE" ]; then
    echo "ERROR; .env file not found "
    exit 1
fi

echo "DUMP_FILE: $DUMP_FILE"
echo "ENV_FILE: $ENV_FILE"

echo "First argument: $1"
echo "Second argument: $2"
echo "Third argument: $3"
