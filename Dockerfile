# scenario-test-framework
#---------------------------------------------------------------------
# chrome/firefoxサポート
#---------------------------------------------------------------------

FROM openjdk:8-jdk

# chromeとfirefoxのインストール
RUN apt-get update -y
RUN apt-get install -y             \
    curl                           \
    xvfb                           \
    maven                          \
    git
RUN curl -o ~/google-chrome-stable_current_amd64.deb https://dl.google.com/linux/direct/google-chrome-stable_current_amd64.deb && \
    apt install -y ~/google-chrome-stable_current_amd64.deb
RUN apt install -y firefox-esr


# pomのみの状態でビルドし、ローカルリポジトリに依存ファイルダウンロード
RUN mkdir -p ~/scenario-test
COPY ./pom.xml /root/scenario-test
RUN cd ~/scenario-test && \
    mvn test

RUN apt-get clean               && \
    rm -rf /var/lib/apt/lists/*

# 仮想フレームバッファ(xvfb)起動＆テスト実行
CMD export DISPLAY=:99 && \
    ( Xvfb :99 & ) && \
    cd ~/scenario-test && \
    mvn clean test


