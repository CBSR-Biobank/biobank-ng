See https://www.danvega.dev/blog/2022/09/09/spring-security-jwt

These keys should not be in the Git repository.

### create rsa key pair

```sh
openssl genrsa -out keypair.pem 2048
```

### extract public key

```sh
openssl rsa -in keypair.pem -pubout -out public.pem
```

### create private key in PKCS#8 format

```sh
openssl pkcs8 -topk8 -inform PEM -outform PEM -nocrypt -in keypair.pem -out private.pem
```
