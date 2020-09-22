FROM openjdk:11-slim

RUN apt-get update && apt-get install --no-install-recommends -y \
    curl \
    && rm -rf /var/lib/apt/lists/*

RUN curl -sL https://deb.nodesource.com/setup_12.x | bash -

RUN apt-get update && apt-get install --no-install-recommends -y \
    nodejs \
    && rm -rf /var/lib/apt/lists/*

## Install specific node version for aws-cdk
#RUN npm install -g n && \
#    n 12.16.3

# Install aws-cdk
ENV AWS_CDK_VERSION=1.62.0
RUN npm install -g aws-cdk@${AWS_CDK_VERSION}

#ENV GRADLE_OPTS -Dorg.gradle.daemon=false

#ENV GRADLE_USER_HOME=/gradle
#
#RUN mkdir ${GRADLE_USER_HOME}
## && \
##    chmod
WORKDIR /app
