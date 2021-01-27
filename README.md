# Aquarius Timeseries (AQTS) Pre-Process Timeseries Data

[![Build Status](https://travis-ci.com/usgs/aqts-capture-ts-corrected.svg?branch=master)](https://travis-ci.com/usgs/aqts-capture-ts-corrected)
[![codecov](https://codecov.io/gh/usgs/aqts-capture-ts-corrected/branch/master/graph/badge.svg)](https://codecov.io/gh/usgs/aqts-capture-ts-corrected)

Split timeseries JSON data into pieces for later processing.

## Testing
This project contains JUnit tests. Maven can be used to run them (in addition to the capabilities of your IDE).

### Docker Network
A named Docker Network is needed to run the automated tests via maven. The following is a sample command for creating your own local network. In this example the name is aqts and the ip addresses will be 172.25.0.x

```.sh
docker network create --subnet=172.25.0.0/16 aqts
```

### Unit Testing
To run the unit tests of the application use:

```.sh
mvn package
```

### Database Integration Testing with Maven
To additionally start up both the transform and observation Docker databases and run the integration tests of the application use:

```.sh
mvn verify \
    -DTRANSFORM_TESTING_DATABASE_PORT=5437 \
    -DLOCAL_TRANSFORM_TESTING_DATABASE_PORT=5437 \
    -DTRANSFORM_TESTING_DATABASE_ADDRESS=localhost \
    -DTESTING_DATABASE_NETWORK=aqts \
    -DROOT_LOG_LEVEL=INFO \
```

### Database Integration Testing with an IDE
To run tests against local transform and observation Docker databases use:

Transform database:
```.sh
docker run -p 127.0.0.1:5437:5432/tcp usgswma/aqts_capture_db:ci
```

Additionally, add an application.yml configuration file at the project root (the following is an example):
```.yaml
AQTS_DATABASE_ADDRESS: localhost
AQTS_DATABASE_PORT: 5437
AQTS_DATABASE_NAME: database_name
AQTS_SCHEMA_NAME: schema_name
AQTS_SCHEMA_OWNER_USERNAME: schema_owner
AQTS_SCHEMA_OWNER_PASSWORD: changeMe
```
