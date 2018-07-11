#!/bin/bash
#startup docker containers
sudo docker start 2dff3b21fb77 #gitlab
sudo docker start 4f520aa22c4c #teamcity

sudo /home/master/docker_init_production.sh
sudo /home/develop/docker_init.sh

#run application
sudo /home/develop/manually_start_application.sh
sudo /home/master/manually_start_application.sh

#run build agent
sudo -H -u production bash -c '/home/teamcity/build_agent/bin/agent.sh reboot'

#redirect production(master) to port 80
sudo iptables -t nat -A PREROUTING -p tcp --dport 80 -j REDIRECT --to-port 8000
