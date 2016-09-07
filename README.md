# AlertCorrelationSystem

ACS is still in early development and lacks features like and installation mechanism and the configuration files for the tools it depends on. In other words, its still work in progress.

# Description:

A small program that listens to a NIDS (like Suricata or Snort) through a unix pipe and calculates in real time the network anomaly between a previously created network profile and the current network traffic. ACS integrates Cxtracker and Prads for network profiling and contexts, Suricata as a network intrusion detection system and oinkmaster as a rule updating tool.

 - (1) Alert monitorization with Suricata and Prads
 - (2) Network profiling using Cxtracker
 - (3) IDS rule updating via Oinkmaster

# Pending:

 - Installation
 - Configuration
 - GUI
