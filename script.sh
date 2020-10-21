gradle jar
for i in {1..10}
do
    chmod +x integrationTests/integTest$i.sh
    ./integrationTests/integTest$i.sh
    if (( $? != 0 ))
    then
        exit 1
    fi
done

