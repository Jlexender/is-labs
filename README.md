# is-labwork

> Using Jakarta EE (fucking superior)

## Host WildFly configuration

Port mappings:

1. HTTP: 8841
2. HTTPS: 25613
3. Mgmt HTTP: 18800
4. Mgmt HTTPS: 18803

Username: `jex`

DBMS: PostgreSQL
JNDI mapping: `java:/PostgresDS`
Driver: `org.postgresql.postgresql 42.7.8`

## Deployment

Use makefile.

* `make build` for embedding a front-end & archiving a WAR
* `make push` for deploying to WildFly on the server (change username in the makefile for your needs)



