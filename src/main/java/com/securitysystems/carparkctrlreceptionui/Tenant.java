package com.securitysystems.carparkctrlreceptionui;

public class Tenant {
	public Integer TenantID;
	public final String Forename;
	public final String Surname;

	public Tenant(Integer TenantID, String Forename, String Surname) {
		this.TenantID = TenantID;
		this.Forename = Forename;
		this.Surname = Surname;
	}

	public String getFullName() {
		return this.Forename + " " + this.Surname;
	}
}
