# Short Description
CloudFoundry User Account and Authentication (UAA) is used as OAuth2 server in Consent2Share.

# Full Description

# Supported Tags and Respective `Dockerfile` Links

[`3.4.1`](https://github.com/bhits/uaa/blob/master/docker/Dockerfile),[`latest`](https://github.com/bhits/uaa/blob/master/docker/Dockerfile)[(3.4.1/Dockerfile)](https://github.com/bhits/uaa/blob/master/docker/Dockerfile)

For more information about this image, the source code, and its history, please see the [GitHub repository](https://github.com/bhits/uaa).

# What is UAA?

CloudFoundry User Account and Authentication (UAA) is used as OAuth2 server in Consent2Share. The UAA is a multi tenant identity management service, used in Cloud Foundry, but also available as a stand alone OAuth2 server. It's primary role is as an OAuth2 provider, issuing tokens for client applications to use when they act on behalf of Cloud Foundry users. It can also authenticate users with their Cloud Foundry credentials, and can act as an SSO service using those credentials (or others). It has endpoints for managing user accounts and for registering OAuth2 clients, as well as various other management functions.

For more information and related downloads for Consent2Share, please visit [Consent2Share](https://bhits.github.io/consent2share/).

# How to use this image


## Start a UAA instance

Be sure to familiarize yourself with the repository's [README.md](https://github.com/bhits/uaa) file before starting the instance.

`docker run  --name uaa -d bhits/uaa:latest`

*NOTE: In order for this API to fully function as a microservice in the Consent2Share application, it is required to setup the dependency microservices and support level infrastructure. Please refer to the [Consent2Share Deployment Guide]() for instructions to setup the Consent2Share infrastructure.*


## Configure

This API runs with a default configuration [uaa.yml](https://github.com/bhits/uaa/blob/master/config-template/uaa.yml) that is primarily targeted for the development environment.   

## Using a custom configuration file

To use custom `uaa.yml`, mount the file to the docker host under `/java/C2S_PROPS/uaa`.

`docker run -v "/path/on/dockerhost/uaa.yml:/java/C2S_PROPS/uaa/uaa.yml" -d bhits/uaa:latest`

## Environment Variables

When you start the UAA image, you can edit the configuration of the UAA instance by passing one or more environment variables on the command line. 

### UAA_CONFIG_PATH

This enviroment variable is used to set location of [uaa.yml](https://github.com/bhits/uaa/blob/master/config-template/uaa.yml). Default value is `/java/C2S_PROPS/uaa`. To overwrite this value, pass environment variable `CATALINA_OPTS="-DUAA_CONFIG_PATH=${path}"` 

`docker run --name uaa -v "/path/on/dockerhost/uaa.yml:/path/in/container/uaal.yml" -e CATALINA_OPTS="-DUAA_CONFIG_PATH=/path/in/container" -d bhits/uaa:latest`

### UAA_DB_HOST

This enviroment variable is used to setup database url. It will connect the database to `jdbc:mysql://${UAA_DB_HOST}:3306/uaa`. For example, if `UAA_DB_HOST=databasehost`, full database url will be `jdbc:mysql://databasehost:3306/uaa`. 

`docker run --name uaa -e UAA_DB_HOST=databasehost -d bhits/uaa:latest`

### UAA_DB_PASSWORD

This enviroment variable is allowed you using your custom password for setting up database. Default value is `admin`.

`docker run --name uaa -e UAA_DB_PASSWORD=strongpassword -d bhits/uaa:latest`

# Supported Docker versions

This image is officially supported on Docker version 1.12.1.

Support for older versions (down to 1.6) is provided on a best-effort basis.

Please see the [Docker installation documentation](https://docs.docker.com/engine/installation/) for details on how to upgrade your Docker daemon.

# License

View [license](https://github.com/bhits/uaa/blob/master/LICENSE) information for the software contained in this image.

# User Feedback

## Documentation 

Documentation for this image is stored in the [bhits/uaa](https://github.com/bhits/uaa) GitHub repository. Be sure to familiarize yourself with the repository's README.md file before attempting a pull request.

## Issues

If you have any problems with or questions about this image, please contact us through a [GitHub issue](https://github.com/bhits/uaa/issues).