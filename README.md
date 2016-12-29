#Order Transaction API

##To Run Locally
- install java 8, mysql, and sbt or activator
- prepare database "ecommerce_v1" or change database url in "conf/application.conf" to use custom database name
  database schema and seed will be generated automaticaly by playframework evolution db
- add environment variable PLAY_CRYPTO_SECRET, PLAY_DB_USER, and PLAY_DB_PASSWORD
- run with "sbt run" or "activator run"
- build with "sbt dist" or "activator dist"