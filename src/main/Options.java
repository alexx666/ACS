package main;
//TODO usar args[0]
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
	HELP{
        @Override
        public String toString() {
            return "h - to display this message";
        }
    };
}
