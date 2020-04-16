package com.ripple.beans;

public class SIValidatedLedger {
	
	private String validated_ledger;
	private String age;
	private String base_fee_xrp;
	private String hash;
	private String reserve_base_xrp;
	private String seq;
	
	public String getValidated_ledger() {
		return validated_ledger;
	}
	
	public void setValidated_ledger(String validated_ledger) {
		this.validated_ledger = validated_ledger;
	}
	
	public String getAge() {
		return age;
	}
	
	public void setAge(String age) {
		this.age = age;
	}
	public String getBase_fee_xrp() {
		return base_fee_xrp;
	}
	
	public void setBase_fee_xrp(String base_fee_xrp) {
		this.base_fee_xrp = base_fee_xrp;
	}
	
	public String getHash() {
		return hash;
	}
	public void setHash(String hash) {
		this.hash = hash;
	}
	
	public String getReserve_base_xrp() {
		return reserve_base_xrp;
	}
	
	public void setReserve_base_xrp(String reserve_base_xrp) {
		this.reserve_base_xrp = reserve_base_xrp;
	}
	
	public String getSeq() {
		return seq;
	}
	
	public void setSeq(String seq) {
		this.seq = seq;
	}

}
