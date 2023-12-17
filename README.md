## CDC Project Architecture

---

![image](https://github.com/hyunwoopark0/CDC_project/assets/144861873/7923a718-8225-4bc2-b862-a01eec3cc315)

</br>
</br>

### Mysql replication setting
---

- vim 설치 </br>
$ apt-get update </br>
$ apt-get install -y vim </br>

- mysql 설정 </br>
$ vim /etc/mysql/my.cnf </br>
  log-bin=mysql-bin  </br>
  server-id=1 </br>

- 계정 생성 후 권한 적용 </br>
$ CREATE USER 'use id'@'%' IDENTIFIED BY 'use pw'; </br>
$ ALTER USER 'use id'@'%' IDENTIFIED WITH mysql_native_password BY 'use pw'; </br>
$ GRANT REPLICATION SLAVE ON *.* TO 'use id'@'%'; </br>
$ FLUSH PRIVILEGES; </br>

- slave mysql dump 파일 적용 </br>
mysqldump -u root -p 'use id' > dump.sql </br>
docker cp dump.sql mysql-slave:. </br>
mysql -u root -p 'use db' < dump.sql </br>

- master - slave 연결 </br>
CHANGE MASTER TO MASTER_HOST='mysql-master', MASTER_USER='use id', MASTER_PASSWORD='use pw', MASTER_LOG_FILE='mysql-bin.000001', MASTER_LOG_POS='use position'; </br>

</br>
</br>

### kafka connect setting 

- kafka debezium source connector </br>
```
curl --location --request POST 'http://localhost:8083/connectors' \
--header 'Content-Type: application/json' \
--data-raw '{
    "name": "mysql_cdc_source",
    "config": {
        "connector.class": "io.debezium.connector.mysql.MySqlConnector",
        "tasks.max": "1",
        "database.hostname": "mysql-master",
        "database.port": "3306",
        "database.user": "mysqluser",
        "database.password": "mysqlpw",
        "database.server.id": "10001",
        "database.server.name": "mysql01",
        "database.include.list": "cdc",
        "table.include.list": "cdc.customers", 
        "database.history.kafka.bootstrap.servers": "localhost:9092",
        "database.history.kafka.topic": "schema-changes.mysql.cdc",
        "key.converter": "org.apache.kafka.connect.json.JsonConverter",
        "value.converter": "org.apache.kafka.connect.json.JsonConverter",

        "transforms": "unwrap",
        "transforms.unwrap.type": "io.debezium.transforms.ExtractNewRecordState",
        "transforms.unwrap.drop.tombstones": "false"
    }
}'
```

- kafka jdbc sink connector </br>
```
curl --location --request POST 'http://localhost:8083/connectors' \
--header 'Content-Type: application/json' \
--data '{
    "name": "postgres-connector",
    "config": {
        "connector.class": "io.confluent.connect.jdbc.JdbcSinkConnector",
        "tasks.max": "1",
        "topics": "mysql01.cdc.customers",
        "connection.url": "jdbc:postgresql://postgres:5432/sinkdb?currentSchema=sinkdb",
        "connection.user": "postuser",
        "connection.password": "postps",
        "table.name.format": "customers",
        "insert.mode": "upsert",
        "pk.fields": "id",
        "pk.mode": "record_key",
        "delete.enabled": "true",
        "key.converter": "org.apache.kafka.connect.json.JsonConverter",
        "value.converter": "org.apache.kafka.connect.json.JsonConverter", 
        
        "transforms": "unwrap",
        "transforms.unwrap.type": "io.debezium.transforms.ExtractNewRecordState",
        "transforms.unwrap.drop.tombstones": "false"

    }
}'
```
