package com.ripple.beans;

public class SIInfo {
	
	private String complete_ledgers;
	private String time;
	private String uptime;
	private SIValidatedLedger validated_ledger;
	
	public String getComplete_ledgers() {
		return complete_ledgers;
	}
	public void setComplete_ledgers(String complete_ledgers) {
		this.complete_ledgers = complete_ledgers;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getUptime() {
		return uptime;
	}
	public void setUptime(String uptime) {
		this.uptime = uptime;
	}
	public SIValidatedLedger getValidated_ledger() {
		return validated_ledger;
	}
	public void setValidated_ledger(SIValidatedLedger validated_ledger) {
		this.validated_ledger = validated_ledger;
	}
	
}
