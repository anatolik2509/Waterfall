cat <<'EOF' | NAMESPACE=default /bin/sh -c 'kubectl apply -n $NAMESPACE -f -'

kind: ConfigMap
metadata:
  name: grafana-agent
apiVersion: v1
data:
  agent.yaml: |
    metrics:
      wal_directory: /var/lib/agent/wal
      global:
        scrape_interval: 60s
        external_labels:
          cluster: cloud
      configs:
      - name: integrations
        remote_write:
        - url: https://prometheus-prod-01-eu-west-0.grafana.net/api/prom/push
          basic_auth:
            username: 380074
            password: eyJrIjoiOTdmMGM2NTA3YTA4MzNhZjYyODExNTEzZWVhYTBmNjYxOTJmMTA4NSIsIm4iOiJhbmF0b2xpazI1LWVhc3lzdGFydC1wcm9tLXB1Ymxpc2hlciIsImlkIjo2MjQ1NTV9
    integrations:
      eventhandler:
        cache_path: /var/lib/agent/eventhandler.cache
        logs_instance: integrations
    logs:
      configs:
      - name: integrations
        clients:
        - url: https://logs-prod-eu-west-0.grafana.net/loki/api/v1/push
          basic_auth:
            username: 189007
            password: eyJrIjoiOTdmMGM2NTA3YTA4MzNhZjYyODExNTEzZWVhYTBmNjYxOTJmMTA4NSIsIm4iOiJhbmF0b2xpazI1LWVhc3lzdGFydC1wcm9tLXB1Ymxpc2hlciIsImlkIjo2MjQ1NTV9
          external_labels:
            cluster: cloud
            job: integrations/kubernetes/eventhandler
        positions:
          filename: /tmp/positions.yaml
        target_config:
          sync_period: 10s

EOF

MANIFEST_URL=https://raw.githubusercontent.com/grafana/agent/v0.24.0/production/kubernetes/agent-bare.yaml NAMESPACE=default /bin/sh -c "$(curl -fsSL https://raw.githubusercontent.com/grafana/agent/v0.24.0/production/kubernetes/install-bare.sh)" | kubectl apply -f -

helm repo add prometheus-community https://prometheus-community.github.io/helm-charts && helm repo update && helm install ksm prometheus-community/kube-state-metrics --set image.tag=v2.4.2 -n default

cat <<'EOF' | NAMESPACE=default /bin/sh -c 'kubectl apply -n $NAMESPACE -f -'

kind: ConfigMap
metadata:
  name: grafana-agent-logs
apiVersion: v1
data:
  agent.yaml: |
    metrics:
      wal_directory: /tmp/grafana-agent-wal
      global:
        scrape_interval: 60s
        external_labels:
          cluster: cloud
      configs:
      - name: integrations
        remote_write:
        - url: https://prometheus-prod-01-eu-west-0.grafana.net/api/prom/push
          basic_auth:
            username: 380074
            password: eyJrIjoiOTdmMGM2NTA3YTA4MzNhZjYyODExNTEzZWVhYTBmNjYxOTJmMTA4NSIsIm4iOiJhbmF0b2xpazI1LWVhc3lzdGFydC1wcm9tLXB1Ymxpc2hlciIsImlkIjo2MjQ1NTV9
    integrations:
      prometheus_remote_write:
      - url: https://prometheus-prod-01-eu-west-0.grafana.net/api/prom/push
        basic_auth:
          username: 380074
          password: eyJrIjoiOTdmMGM2NTA3YTA4MzNhZjYyODExNTEzZWVhYTBmNjYxOTJmMTA4NSIsIm4iOiJhbmF0b2xpazI1LWVhc3lzdGFydC1wcm9tLXB1Ymxpc2hlciIsImlkIjo2MjQ1NTV9


    logs:
      configs:
      - name: integrations
        clients:
        - url: https://logs-prod-eu-west-0.grafana.net/loki/api/v1/push
          basic_auth:
            username: 189007
            password: eyJrIjoiOTdmMGM2NTA3YTA4MzNhZjYyODExNTEzZWVhYTBmNjYxOTJmMTA4NSIsIm4iOiJhbmF0b2xpazI1LWVhc3lzdGFydC1wcm9tLXB1Ymxpc2hlciIsImlkIjo2MjQ1NTV9
          external_labels:
            cluster: cloud
        positions:
          filename: /tmp/positions.yaml
        target_config:
          sync_period: 10s
        scrape_configs:
        - job_name: integrations/docker
          docker_sd_configs:
            - host: unix:///var/run/docker.sock
              refresh_interval: 5s
          relabel_configs:
            - action: replace
              replacement: integrations/docker
              source_labels:
                - __meta_docker_container_id
              target_label: job
            - action: replace
              replacement: localhost:9090
              source_labels:
                - __meta_docker_container_id
              target_label: instance
            - source_labels:
                - __meta_docker_container_name
              regex: '/(.*)'
              target_label: container
            - source_labels:
                - __meta_docker_container_log_stream
              target_label: stream


EOF

MANIFEST_URL=https://raw.githubusercontent.com/grafana/agent/v0.24.0/production/kubernetes/agent-loki.yaml NAMESPACE=default /bin/sh -c "$(curl -fsSL https://raw.githubusercontent.com/grafana/agent/v0.24.0/production/kubernetes/install-bare.sh)" | kubectl apply -f -
