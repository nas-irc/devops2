FROM postgres:12.7

##############
# Monitoring #
##############

# Adding Filebeat
ADD https://artifacts.elastic.co/downloads/beats/filebeat/filebeat-7.13.3-amd64.deb filebeat.deb
RUN dpkg -i filebeat.deb
RUN rm filebeat.deb

# Adding Filebeat configuration
ADD filebeat.yml /etc/filebeat/filebeat.yml
RUN chmod go-w /etc/filebeat/filebeat.yml

# Adding custom entrypoint
ADD filebeat-entrypoint.sh /entrypoint.sh
RUN chmod +x /entrypoint.sh

ENTRYPOINT ["/entrypoint.sh"]
#~ END Monitoring ~#

CMD ["postgres"]
