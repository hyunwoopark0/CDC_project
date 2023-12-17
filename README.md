## CDC Project Architecture

---

![image](https://github.com/hyunwoopark0/CDC_project/assets/144861873/7923a718-8225-4bc2-b862-a01eec3cc315)



### Mysql replication setting
---

vim 설치
$ apt-get update
$ apt-get install -y vim

 mysql 설정
$ vim /etc/mysql/my.cnf 
log-bin=mysql-bin  
server-id=1

- 계정 생성 후 권한 적용
$ CREATE USER 'use id'@'%' IDENTIFIED BY 'use pw';
$ ALTER USER 'use id'@'%' IDENTIFIED WITH mysql_native_password BY 'use pw';
$ GRANT REPLICATION SLAVE ON *.* TO 'use id'@'%';
$ FLUSH PRIVILEGES;

- slave mysql dump 파일 적용
mysqldump -u root -p 'use id' > dump.sql
docker cp dump.sql mysql-slave:.
mysql -u root -p 'use db' < dump.sql

- master - slave 연결
CHANGE MASTER TO MASTER_HOST='mysql-master', MASTER_USER='use id', MASTER_PASSWORD='use pw', MASTER_LOG_FILE='mysql-bin.000001', MASTER_LOG_POS='use position';



