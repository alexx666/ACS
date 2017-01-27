# AlertCorrelationSystem

ACS is still in early development and lacks features like and installation mechanism and the configuration files for the tools it depends on. In other words, its still work in progress.

# Description:

A small program that listens to a NIDS (Currently supports Suricata) and calculates in real time the network anomaly of the network traffic. ACS integrates Cxtracker and Prads for network profiling and context, Suricata as a network intrusion detection system and oinkmaster as a rule updating tool.

# Features:

 - (1) Alert monitorization with Suricata and Prads
 - (2) Network profiling using Cxtracker
 - (3) IDS rule updating via Oinkmaster

# Pending:

 - Installation of 3rd party software
 - Configuration