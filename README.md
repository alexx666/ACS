# AlertCorrelationSystem

ACS is still in early development and lacks features like and installation mechanism and the configuration files for the tools it depends on. In other words, its still work in progress.

# Description:

A small program that listens to a NIDS (Currently supports only Suricata) and calculates in real time the network anomaly of the network traffic. ACS integrates Cxtracker and PRADS for network profiling and context, Suricata as a network intrusion detection system and Oinkmaster as a rule updating tool.

# Features:

 - Alert monitoring
 - Network profiling
 - IDS rule updating
 
# Pending:

 - 3rd party installation script. Currently ACS will not work without Suricata, Cxtracker, Oinkmaster and PRADS being correctly installed and configured.