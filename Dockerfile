FROM amazoncorretto:8
WORKDIR /tmp

COPY ../../target/univeral /tmp
RUN yum install -y unzip \
    && unzip -o 'micro-posts-*.zip' \
    && rm -f micro-posts-*.zip \
    && mv micro-posts-* micro-posts \
    && mkdir -p /var/app/play \
    && mv micro-posts/* /var/app/play/ \
    && yum clean all

WORKDIR /var/app/play

# CMD ["--cmd", "bin/micro-posts", "--cmd-opt", "-Dconfig.resource=application_production.conf"]

ENTRYPOINT bin/micro-posts -Dconfig.resource=application_production.conf
