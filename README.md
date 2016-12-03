#Order Transaction API

This app is for Salestock Backend Technical Assessment

This app is deployed in

[http://139.59.251.54/](http://139.59.251.54/)

Api specification and usage example can be found there

##To Run Locally
- install java 8, mysql, and sbt or activator
- prepare database "ecommerce_v1" or change database url in "conf/application.conf" to use custom database name
  database schema and seed will be generated automaticaly by playframework evolution db
- add environment variable PLAY_CRYPTO_SECRET, PLAY_DB_USER, and PLAY_DB_PASSWORD
  
  example
  export PLAY_CRYPTO_SECRET="Its realy secret"
  export PLAY_DB_USER="root"
  export PLAY_DB_PASSWORD="100%%NotDefault"
  
- run with "sbt run" or "activator run"
- build with "sbt dist" or "activator dist"