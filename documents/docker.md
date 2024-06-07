# Rebuild docker image

```sh
cd /opt/biobank/biobank-thick-client
docker compose --env-file .env -f docker/compose.yaml --project-directory docker build --no-cache
docker compose --env-file .env -f docker/compose.yaml --project-directory docker run -it jboss bash
docker/copy-built-files.sh
```

```sh
ant deploy-jboss
```

```sh
docker/copy-built-files.sh
```

```sh
pv /opt/biobank/biobank_20240607.sql.gz | gzip -dc | mariadb --protocol=tcp -uroot -p biobank
```

To extract the files from the WAR file:

```
jar xf biobank.war
```
