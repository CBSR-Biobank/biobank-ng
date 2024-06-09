# Biobank NG Backend Install

## Create Non Root User

For security reasons, it is better to run the application as a non root user. To do this, create a **biobank** user account:

```sh
sudo mkdir /opt/biobank
sudo useradd --system -d /opt/biobank -s /bin/bash biobank
```

Please use `/opt/biobank` as the home directory.

You may wish to add the `biobank` user to the **sudoers** file:

```sh
sudo usermod -aG sudo biobank
```

## Install Java

1. As the **biobank** user, install SDKMAN to manage the Java version. Follow these instructions:

    * [https://sdkman.io/install](https://sdkman.io/install)

    After installation, logging out and back in, use the following commands to install Java and Maven:

    ```sh
    sdk install java 17.0.2-tem
    sdk install maven
    ```

## Install MariaDB CLI

1. Install:

    ```sh
    sudo apt install mariadb-client
    ```

## Install NodeJS

Follow these instructions to install NodeJS:

* https://github.com/nodesource/distributions?tab=readme-ov-file#installation-instructions

## Clone the Git Repository

As the **biobank** user, clone the repository:

```sh
cd /opt/biobank
git clone https://github.com/CBSR-Biobank/biobank-ng.git
```

## Build

### Backend

1. Create the following file and place it in `/opt/biobank/biobank-ng/.env`:

    ```
    MODE=DEVELOPMENT
    LOG_LEVEL=DEBUG

    DB_HOST=localhost
    DB_PORT=3306
    DB_ROOT_USER=root
    DB_ROOT_PASSWORD=root
    DB_NAME=biobank
    DB_USER=changeme
    DB_PASSWORD=changeme
    ```

    Replace all occurrences of `changeme` with your values.

1. RSA Key Pair

    For this application to run, an RSA key pair needs to be generated. Follow the instructions in the
    following file for more information: [RSA Key Pair](../src/main/resources/certs/README.md).

1. Build the application:

    ```sh
    cd /opt/biobank/biobank-ng
    mvn -Dmaven.test.skip clean package
    ```

### Frontend

1. Compile the frontend

    ```sh
    cd /opt/biobank/biobank-ng/frontend
    npm install
    npm run build
    ```

## Ubuntu Service

The application can be made into an Ubuntu service so that it starts when the computer boots.

Follow these steps:

1. Create the following file and place it in `/etc/systemd/system/biobank.service` (root access required):

    ```
    [Unit]
    Description=Biobank NG
    After=syslog.target

    [Service]
    User=biobank
    WorkingDirectory=/opt/biobank/biobank-ng
    ExecStart=/usr/bin/java -jar /opt/biobank/biobank-ng/target/biobank-0.0.1-SNAPSHOT.jar
    SuccessExitStatus=143

    [Install]
    WantedBy=multi-user.target
    ```
1. Enable the service

    ```sh
    sudo systemctl enable biobank
    ```

1. Start the service

    ```sh
    sudo systemctl start biobank
    ```
1. Reload system daemon

    ```sh
    sudo systemctl daemon-reload
    ```

1. Check the service status

    ```sh
    sudo systemctl status biobank
    ```

1. Check service logs using the following command

    ```sh
    sudo journalctl -u biobank.service --no-pager
    ```

# Install and Configure NGINX

1. Install NGINX by following these instructions: https://ubuntuhandbook.org/index.php/2024/01/install-nginx-ubuntu-2204/

1. Change the Linux user NGINX will use to run the application. Edit `/etc/nginx/nginx.conf` and change the line:

    ```
    user www-data;
    ```

      to:

    ```
    user biobank;
    ```

1. Create a temporary configuration for a server to obtain a LetsEncrypt certificate by creating a file named `/etc/nginx/sites-available/my-default` with the following:

    ```ini
     server {
        listen 80 default_server;
        root /var/www/html;
        server_name __server_name__;

        location / {
            try_files $uri $uri/ =404;
        }
    }
    ```

    Replace `__server_name__` with the DNS name for the server.

1. Enable this configuration in NGINX

    ```sh
    cd /etc/nginx/sites-enabled
    ln -s ../sites-available/my-default .
    ```

1. The check that the configuration is ok with this command:

    ```sh
    sudo nginx -t
    ```

1. Reload the NGINX configuration:

    ```sh
    sudo systemctl reload nginx
    ```

1. Install Certbot:

    ```sh
    sudo apt install certbot python3-certbot-nginx
    ```

1. Install the certificate:

    ```sh
    sudo certbot --nginx -d __server_name__
    ```

    Replace `__server_name__` with the DNS name for the server.

1. Create the biobank-ng NGINX configuration by creating a file named `/etc/nginx/sites-available/biobank-dashboard` with the following:

    ```ini
    # -*- mode: conf -*-

    server {
        listen 8443 ssl; # managed by Certbot
        server_name biobank-new.cbsr.ualberta.ca;
        root /opt/biobank/biobank-ng/frontend/dist;
        index index.html;
        error_log  /var/log/nginx/biobank-dashboard-error.log;
        access_log /var/log/nginx/biobank-dashboard-access.log;

        ssl_certificate /etc/letsencrypt/live/biobank-new.cbsr.ualberta.ca/fullchain.pem; # managed by Certbot
        ssl_certificate_key /etc/letsencrypt/live/biobank-new.cbsr.ualberta.ca/privkey.pem; # managed by Certbot
        include /etc/letsencrypt/options-ssl-nginx.conf; # managed by Certbot
        ssl_dhparam /etc/letsencrypt/ssl-dhparams.pem; # managed by Certbot

        location / {
            try_files $uri $uri/ /index.html;
            add_header Cache-Control public;
            expires 1d;
        }
        location /api {
            proxy_set_header X-Forwarded-Host \$host;
            proxy_set_header X-Forwarded-Server \$host;
            proxy_set_header X-Forwarded-For \$proxy_add_x_forwarded_for;
            proxy_pass http://localhost:9000/api;
        }
        location /site {
            proxy_set_header X-Forwarded-Host \$host;
            proxy_set_header X-Forwarded-Server \$host;
            proxy_set_header X-Forwarded-For \$proxy_add_x_forwarded_for;
            proxy_pass http://localhost:9000\$request_uri;
        }
    }

    server {
        if ($host = biobank-new.cbsr.ualberta.ca) {
            return 301 https://$host$request_uri;
        } # managed by Certbot


        listen 80 default_server;

        server_name biobank-new.cbsr.ualberta.ca;
        return 404; # managed by Certbot


    }
    ```

    Repalce `biobank-new.cbsr.ualberta.ca` with the name of the server.
