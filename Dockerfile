FROM amazoncorretto:8
WORKDIR /tmp

COPY target/universal /tmp
RUN yum install -y unzip \
    && unzip -o 'micro-posts-*.zip' \
    && rm -f micro-posts-*.zip \
    && mv micro-posts-* micro-posts \
    && mkdir -p /var/app/play \
    && mv micro-posts/* /var/app/play/ \
    && yum clean all

WORKDIR /var/app/play

ENV FLYWAY_LOCATIONS_NAMES ["common", "mysql"]
ENV JDBC_DRIVER com.mysql.cj.jdbc.Driver
ENV JDBC_URL jdbc:mysql://localhost:4306/micro_posts?autoReconnect=true&useSSL=false
ENV JDBC_USERNAME micro_posts
ENV JDBC_PASSWORD ${MICRO_POSTS_JDBC_PASSWORD}
ENV PASSWORD_SALT ${MICRO_POSTS_PASSWORD_SALT}

CMD ["-Dconfig.resource=conf/application_production.conf"]
ENTRYPOINT bin/micro-posts
