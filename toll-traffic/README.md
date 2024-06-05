## Instruccion para descargar cliente de MariaDB:
``
wget https://repo1.maven.org/maven2/org/mariadb/jdbc/mariadb-java-client/3.4.0/mariadb-java-client-3.4.0.jar
``

## Eventos

### Pasada por Peaje 
* Si la pasada por peaje es con tipo de pago Sucive el Modulo Sucive la registra -> Modulo de Sucive debe
  lanzar un evento con la pasada
* Si la pasada por peaje es con tipo de pago PREPaga el Modulo de Cliente la registra -> Modulo de Cliente debe
  lanzar un evento con la pasada
* Si la pasada por peaje es con tipo de pago POSTPaga el Modulo de Cliente y el Modulo de Medio de pago la registran