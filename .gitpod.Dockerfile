FROM gitpod/workspace-full-vnc

USER gitpod

ENV GRADLE_USER_HOME=/workspace/.gradle/

# Install MongoDB
RUN mkdir -p /tmp/mongodb && \
    cd /tmp/mongodb && \
    wget -qOmongodb.tgz https://fastdl.mongodb.org/linux/mongodb-linux-x86_64-ubuntu2004-6.0.1.tgz && \
    tar xf mongodb.tgz && \
    cd mongodb-* && \
    sudo cp bin/* /usr/local/bin/ && \
    rm -rf /tmp/mongodb && \
    sudo mkdir -p /data/db && \
    sudo chown gitpod:gitpod -R /data/db

# Install java 17
RUN bash -c ". /home/gitpod/.sdkman/bin/sdkman-init.sh && \
    sdk install java 17.0.4-oracle && \
    sdk default java 17.0.4-oracle"

# Required for running intellij-plugin in an IDE within the vnc
RUN sudo apt install libxtst6

# Setup android sdk
RUN sudo apt update -y && sudo apt install android-sdk -y && \
    cd /usr/lib/android-sdk && \
    sudo rm -r licenses && \
    sudo wget -Olicenses.zip https://res.cloudinary.com/retech/raw/upload/v1672304116/Codestats/dev/licenses_fc6qmm.zip && \
    sudo unzip licenses.zip && \
    sudo rm licenses.zip && \
    sudo chown -R gitpod /usr/lib/android-sdk && \
    export ANDROID_HOME=/usr/lib/android-sdk