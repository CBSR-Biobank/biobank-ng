# Authenticate

Log in:

```
http POST :9000/api/token --auth klassen:test -v
```

The response returns an **Authorization** token. Use this token with further requests.

```
http ":9000/api/patients/2016-0120/collection-events/1" -v -A bearer --auth eyJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJzZWxmIiwic3ViIjoia2xhc3NlbiIsImV4cCI6MTcyNzAxODc4MSwiaWF0IjoxNzI3MDE1MTgxLCJzY29wZSI6IlJPTEVfQURNSU4gUk9MRV9VU0VSIn0.dUMIV9BS0Z5eNth-HfBddjbrR_7fYC-0vRECbXmOn1RfNrhd3qpGDnLQKkMfmelHXdWvArgIHeIe3DQptGfKlZ2KqLNot8RBPd7Dk6uoxbvImPXb96oW2xa60h9ZHctRWvaxpkzEshuTJ0gFeLrALitaRMkheph9CeQ_MjkvgiuBQ_6EHg8vAxv6mVY4PZAj92xdJxl5Bs2frjQ-YmEs4Smpm_Qa181TXVcaoudxHNALzVqYq4WNJt00FuxyYWBWIfdHOdVFbthI5tkykGxiiv3-qFk9YZqS7ykMMIfxPAl5R982G4akrZeb8O7IZbVjaxQg850uxgdfiXZpbOf5Bw
```

## Specimen Request

Put the following content in a file called `specimen-request.csv`:

```
2086,"2001-05-08","Plasma",2
2086,"2001-05-08","Paxgene800",2
```

Upload the CSV file to the backend:

```
http -f POST ":9000/api/specimens/request" file@./specimen-request.csv -v -A bearer --auth eyJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJzZWxmIiwic3ViIjoia2xhc3NlbiIsImV4cCI6MTcyNzAxODc4MSwiaWF0IjoxNzI3MDE1MTgxLCJzY29wZSI6IlJPTEVfQURNSU4gUk9MRV9VU0VSIn0.dUMIV9BS0Z5eNth-HfBddjbrR_7fYC-0vRECbXmOn1RfNrhd3qpGDnLQKkMfmelHXdWvArgIHeIe3DQptGfKlZ2KqLNot8RBPd7Dk6uoxbvImPXb96oW2xa60h9ZHctRWvaxpkzEshuTJ0gFeLrALitaRMkheph9CeQ_MjkvgiuBQ_6EHg8vAxv6mVY4PZAj92xdJxl5Bs2frjQ-YmEs4Smpm_Qa181TXVcaoudxHNALzVqYq4WNJt00FuxyYWBWIfdHOdVFbthI5tkykGxiiv3-qFk9YZqS7ykMMIfxPAl5R982G4akrZeb8O7IZbVjaxQg850uxgdfiXZpbOf5Bw
```
