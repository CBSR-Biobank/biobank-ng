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

To build the backend:

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

To build the frontend:

1. Compile the frontend

    ```sh
    cd /opt/biobank/biobank-ng/frontend
    npm install
    npm run build
    ```

## Ubuntu Service

The application can be made into an Ubuntu service so that it starts when the computer restarts. Follow these steps:

1. Create the following file and place it in `/etc/systemd/system/biobank.service` (root access required):

    ```
    [Unit]
    Description=Biobank NG
    After=syslog.target

    [Service]
    User=biobank
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
