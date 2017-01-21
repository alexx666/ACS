package main;

public enum Options {
	NIDS{
        @Override
        public String toString() {
            return "1 - to start anomaly monitoring";
        }
    },
	PROFILES{
        @Override
        public String toString() {
            return "2 - to start network profiling";
        }
    },
	RULES{
        @Override
        public String toString() {
            return "3 - to update Suricata rules";
        }
    },
	VERSIONS{
        @Override
        public String toString() {
            return "v - to see the tools used";
        }
    },
	FILES{
        @Override
        public String toString() {
            return "f - to see configuration file locations";
        }
    },
	HELP{
        @Override
        public String toString() {
            return "h - to display this message";
        }
    };
}
