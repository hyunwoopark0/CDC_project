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



