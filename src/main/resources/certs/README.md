See https://www.danvega.dev/blog/2022/09/09/spring-security-jwt

These keys should not be in the Git repository.

1. Create rsa key pair

    ```sh
    cd /opt/biobank/biobank-ng/src/main/resources/certs
    openssl genrsa -out keypair.pem 2048
    ```

1. Extract public key

    ```sh
    cd /opt/biobank/biobank-ng/src/main/resources/certs
    openssl rsa -in keypair.pem -pubout -out public.pem
    ```

1. Create private key in PKCS#8 format

    ```sh
    cd /opt/biobank/biobank-ng/src/main/resources/certs
    openssl pkcs8 -topk8 -inform PEM -outform PEM -nocrypt -in keypair.pem -out private.pem
    ```
