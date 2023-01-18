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

# Install java 17 and gradle 7.6
RUN bash -c ". /home/gitpod/.sdkman/bin/sdkman-init.sh && \
    sdk install java 17.0.4-oracle && \
    sdk default java 17.0.4-oracle && \
    sdk install gradle 7.6 && \
    sdk default gradle 7.6"

# Install gradle 7.6

# Required for running intellij-plugin in an IDE within the vnc
RUN sudo apt install libxtst6

# Setup android sdk
ENV JAVA_HOME=/home/gitpod/.sdkman/candidates/java/current
RUN sudo mkdir /usr/local/bin/android-sdk && cd /usr/local/bin/android-sdk && \
    sudo chown root:gitpod . && sudo chmod g+rw . && \
    wget -qOlicenses.zip https://res.cloudinary.com/retech/raw/upload/v1672304116/Codestats/dev/licenses_fc6qmm.zip && \
    unzip -qq licenses.zip && \
    rm licenses.zip && \
    wget -qOcmdtools.zip https://dl.google.com/android/repository/commandlinetools-linux-9123335_latest.zip && \
    unzip -qq cmdtools.zip && \
    cd cmdline-tools && \
    mkdir latest && \
    mv bin/ lib/ NOTICE.txt source.properties latest/ && \
    cd latest/bin && \
    ./sdkmanager --install "platforms;android-33" "build-tools;33.0.1"


