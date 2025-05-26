# Use a MySQL base or build from scratch if preferred
FROM mysql:8.0-debian

# Set environment variables for MySQL
ENV MYSQL_MAJOR=8.0 \
    MYSQL_VERSION=8.0.39 \
    MYSQL_SHELL_VERSION=8.0.38 \
    GOSU_VERSION=1.17

# Install gosu and any additional tools
RUN set -eux; \
    apt-get update && \
    apt-get install -y curl gnupg lsb-release && \
    curl -Lo /usr/local/bin/gosu "https://github.com/tianon/gosu/releases/download/${GOSU_VERSION}/gosu-amd64" && \
    chmod +x /usr/local/bin/gosu && \
    gosu --version && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

# Add the entrypoint script
COPY docker-entrypoint.sh /usr/local/bin/
RUN chmod +x /usr/local/bin/docker-entrypoint.sh && ln -sf /usr/local/bin/docker-entrypoint.sh /entrypoint.sh

# Add database init files and configuration checksum
COPY configuration_checksums /configuration_checksums
COPY db-backups/*.sql.gz /docker-entrypoint-initdb.d/

# Define volumes
VOLUME ["/var/lib/mysql", "/configuration_checksums"]

# Expose MySQL ports
EXPOSE 3306 33060

# Set the entrypoint and default command
ENTRYPOINT ["docker-entrypoint.sh"]
CMD ["mysqld"]
