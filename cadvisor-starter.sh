VERSION=v0.37.5
docker run \
  --volume=/:/rootfs:ro \
  --volume=/var/run:/var/run:ro \
  --volume=/sys:/sys:ro \
  --volume=/var/lib/docker/:/var/lib/docker:ro \
  --volume=/dev/disk/:/dev/disk:ro \
  --publish=8080:8080 \
  --detach=true \
  --name=cadvisor \
  --privileged \
  --device=/dev/kmsg \
  gcr.io/cadvisor/cadvisor:$VERSION --disable_metrics=percpu --docker_only=true
sudo ARCH=amd64 GCLOUD_STACK_ID="339885" GCLOUD_API_KEY="eyJrIjoiZjlmOGQwZWVjOGQzNmQ4M2Y1Y2RjODc3Y2NmOWVlYmY0MzI1NWFjYyIsIm4iOiJhbmF0b2xpazI1LWVhc3lzdGFydC1nY29tIiwiaWQiOjYyNDU1NX0=" GCLOUD_API_URL="https://integrations-api-eu-west.grafana.net" /bin/sh -c "$(curl -fsSL https://raw.githubusercontent.com/grafana/agent/release/production/grafanacloud-install.sh)"
